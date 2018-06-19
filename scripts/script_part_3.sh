#!/bin/bash/

# Define Colors
GREEN='\033[1;32m'
NC='\033[0m'
CYAN='\033[1;36m'

echo "\n----------------------------------------------Part 3----------------------------------------------"

# Wait for Network
while ! ping -c 1 -W 1 8.8.8.8; do
    echo -e "${CYAN}Waiting for 8.8.8.8 - network interface might be down...$NC"
    sleep 1
done

# Remove this script from startup
echo -e "${CYAN}Removing this script from startup...$NC"
sudo crontab -l | grep -v "@reboot bash /home/pi/NXTPi/install/script_part_3 >> /home/pi/NXTPi/install/installscript_out.log 2>&1" | crontab -

# Create Directory for RaspAp
echo -e "${CYAN}Creating Directory for RaspAp...$NC"
sudo mkdir /var/www/html/raspap

# move RaspAp to Directory
echo -e "${CYAN}Moving RaspAp...$NC"
sudo mv /var/www/html/* /var/www/html/raspap/

# Create Directory for GumCP
echo -e "${CYAN}Creating Directory for GumCP$NC"
sudo mkdir /var/www/html/admin

# Clone GumCP
echo -e "${CYAN}Cloning GumCP...$NC"
sudo git clone https://github.com/gumslone/GumCP.git /var/www/html/admin/

# Enable password protection
echo -e "${CYAN}Updating lighttpd config for password protection...$NC"
sudo sed -i -e 's/server.modules = (/server.modules = (\n        "mod_auth",/g' /etc/lighttpd/lighttpd.conf
sudo sh -c 'echo "\n" >> /etc/lighttpd/lighttpd.conf'
sudo sh -c 'echo "auth.debug = 2" >> /etc/lighttpd/lighttpd.conf'
sudo sh -c 'echo "auth.backend = \"htpasswd\"" >> /etc/lighttpd/lighttpd.conf'
sudo sh -c 'echo "auth.backend.htpasswd.userfile = \"/etc/lighttpd/.upasswd\"" >> /etc/lighttpd/lighttpd.conf'
sudo sh -c 'echo "auth.require = ( \"/admin/\" =>" >> /etc/lighttpd/lighttpd.conf'
sudo sh -c 'echo "(" >> /etc/lighttpd/lighttpd.conf'
sudo sh -c 'echo "\"method\" => \"basic\"," >> /etc/lighttpd/lighttpd.conf'
sudo sh -c 'echo "\"realm\" => \"Password Protected Area\"," >> /etc/lighttpd/lighttpd.conf'
sudo sh -c 'echo "\"require\" => \"user=admin\"" >> /etc/lighttpd/lighttpd.conf'
sudo sh -c 'echo ")" >> /etc/lighttpd/lighttpd.conf'
sudo sh -c 'echo ")" >> /etc/lighttpd/lighttpd.conf'

# Install apache2-utils
echo -e "${CYAN}Installing apache2-utils...$NC"
sudo DEBIAN_FRONTEND=noninteractive apt-get install apache2-utils -y

# Set password for user admin
echo -e "${CYAN}Setting password for user 'admin'$NC"
sudo htpasswd -c -b /etc/lighttpd/.upasswd admin password

# Restart lighttpd
echo -e "${CYAN}Restarting lighttpd...$NC"
sudo /etc/init.d/lighttpd restart

# Install libusb-dev
echo -e "${CYAN}Installing libusb-dev...$NC"
sudo DEBIAN_FRONTEND=noninteractive apt-get install libusb-dev -y -q

# Install ant
echo -e "${CYAN}Installing Ant...$NC"
sudo DEBIAN_FRONTEND=noninteractive apt-get install ant -y -q

# Download leJos lib
echo -e "${CYAN}Downloading leJos library...$NC"
wget https://netix.dl.sourceforge.net/project/nxt.lejos.p/0.9.1beta-3/leJOS_NXJ_0.9.1beta-3.tar.gz -P /tmp/

# Unzip the leJos lib
echo -e "${CYAN}Unzipping leJos library...$NC"
sudo tar xzf /tmp/leJOS_NXJ_0.9.1beta-3.tar.gz -C /opt/

# Run ant build
echo -e "${CYAN}Running Ant build...$NC"
sudo ant -buildfile /opt/leJOS_NXJ_0.9.1beta-3/build/

# Add NXJ_HOME to PATH
echo -e "${CYAN}Adding NXJ_HOME to PATH...$NC"
sudo sh -c 'echo "NXJ_HOME=\"/opt/leJOS_NXJ_0.9.1beta-3\"" >> /home/pi/.profile'
sudo sh -c 'echo "PATH=\$NXJ_HOME/bin:\$PATH" >> /home/pi/.profile'

# Add new group 'lego'
echo -e "${CYAN}Adding new Group 'lego' and assining user 'pi' to it...$NC"
sudo groupadd lego
sudo gpasswd -a pi lego

# Create new udev rules
echo -e "${CYAN}Creating new udev rules, to allow the group 'lego' to access the NXT Brick...$NC"
sudo sh -c 'echo "# Lego NXT brick in normal mode" >> /etc/udev/rules.d/70-lego.rules'
sudo sh -c 'echo "SUBSYSTEM==\"usb\", DRIVER==\"usb\", ATTRS{idVendor}==\"0694\", ATTRS{idProduct}==\"0002\", GROUP=\"lego\", MODE=\"0660\"" >> /etc/udev/rules.d/70-lego.rules'
sudo sh -c 'echo "# Lego NXT brick in firmware update mode (Atmel SAM-BA mode)" >> /etc/udev/rules.d/70-lego.rules'
sudo sh -c 'echo "SUBSYSTEM==\"usb\", DRIVER==\"usb\", ATTRS{idVendor}==\"03eb\", ATTRS{idProduct}==\"6124\", GROUP=\"lego\", MODE=\"0660\"" >> /etc/udev/rules.d/70-lego.rules'

# Create dir for Java Script & Download Script
echo -e "${CYAN}Downloading & Installing Script for running leJos pc projects...$NC"
sudo mkdir /opt/NXTPi/
sudo wget -q https://git.io/vhBhw -O /opt/NXTPi/nxtpi
sudo chmod +x /opt/NXTPi/nxtpi
sudo sh -c 'echo -e "\n" >> /home/pi/.profile'
sudo sh -c 'echo "export PATH=\$PATH\":/opt/NXTPi\"" >> /home/pi/.profile'

# Install PHP 7.0 and mariadb
echo -e "${CYAN}Installing PHP 7.0 and mariadb...$NC"
sudo DEBIAN_FRONTEND=noninteractive apt-get install php7.0-fpm php7.0 php-ssh2 php-cgi mariadb-server mariadb-client -y -q

# Replace line in php-fpm config
echo -e "${CYAN}Uncommenting line in php config file...$NC"
sudo sed -i -e 's/;cgi.fix_pathinfo=1/cgi.fix_pathinfo=1/g' /etc/php/7.0/fpm/php.ini

# Create Backup of fastcgi-php.conf
echo -e "${CYAN}Creating Backup of fastcgi-php.conf...$NC"
sudo cp /etc/lighttpd/conf-available/15-fastcgi-php.conf /etc/lighttpd/conf-available/15-fastcgi-php.conf.bak

# Delete fastcgi-php.conf
echo -e "${CYAN}Deleting fastcgi-php.conf...$NC"
sudo rm /etc/lighttpd/conf-available/15-fastcgi-php.conf

# Write fastcgi-php.conf config file
echo -e "${CYAN}Writing new fastcgi-php.conf...$NC"
sudo sh -c 'echo "# /usr/share/doc/lighttpd-doc/fastcgi.txt.gz" >> /etc/lighttpd/conf-available/15-fastcgi-php.conf'
sudo sh -c 'echo "# http://redmine.lighttpd.net/projects/lighttpd/wiki/Docs:ConfigurationOptions#mod_fastcgi-fastcgi" >> /etc/lighttpd/conf-available/15-fastcgi-php.conf'
sudo sh -c 'echo "" >> /etc/lighttpd/conf-available/15-fastcgi-php.conf'
sudo sh -c 'echo "## Start an FastCGI server for php (needs the php7.0-cgi package)" >> /etc/lighttpd/conf-available/15-fastcgi-php.conf'
sudo sh -c 'echo "fastcgi.server += ( \".php\" =>" >> /etc/lighttpd/conf-available/15-fastcgi-php.conf'
sudo sh -c 'echo "		((" >> /etc/lighttpd/conf-available/15-fastcgi-php.conf'
sudo sh -c 'echo "                \"socket\" => \"/var/run/php/php7.0-fpm.sock\"," >> /etc/lighttpd/conf-available/15-fastcgi-php.conf'
sudo sh -c 'echo "                \"broken-scriptfilename\" => \"enable\"" >> /etc/lighttpd/conf-available/15-fastcgi-php.conf'
sudo sh -c 'echo "        ))" >> /etc/lighttpd/conf-available/15-fastcgi-php.conf'
sudo sh -c 'echo ")" >> /etc/lighttpd/conf-available/15-fastcgi-php.conf'

# Enable Lighttpd mods fastcgi and fastcgi-php
echo -e "${CYAN}Enabling Lighttpd mods...$NC"
sudo lighttpd-enable-mod fastcgi
sudo lighttpd-enable-mod fastcgi-php

# Do stuff
sudo ls -l /etc/lighttpd/conf-enabled

# Reload lighttpd
echo -e "${CYAN}Reloading lighttpd$NC"
sudo service lighttpd force-reload

# Install php packages
echo -e "${CYAN}Installing php packages...$NC"
sudo DEBIAN_FRONTEND=noninteractive apt-get install php7.0-mysql php7.0-curl php7.0-gd php7.0-intl php-pear php-imagick php7.0-imap php7.0-mcrypt php-memcache php7.0-pspell php7.0-recode php7.0-sqlite3 php7.0-tidy php7.0-xmlrpc php7.0-xsl php7.0-mbstring php-gettext php-apcu -y -q

# Reload php7.0-fpm
echo -e "${CYAN}Reloading php7.0-fpm...$NC"
sudo service php7.0-fpm reload

# Install Web Site
echo -e "${CYAN}Installing Web Site...$NC"
git clone https://github.com/MelzerFinn/Info18Web /tmp/Info18Web
sudo mv /tmp/Info18Web/info2/* /var/www/html/

# Install phpmyadmin
echo -e "${CYAN}Installing phpmyadmin...$NC"
export DEBIAN_FRONTEND=noninteractive
sudo sh -c "echo 'phpmyadmin phpmyadmin/dbconfig-install boolean true' | debconf-set-selections"
sudo sh -c "echo 'phpmyadmin phpmyadmin/reconfigure-webserver multiselect lighttpd' | debconf-set-selections"
sudo DEBIAN_FRONTEND=noninteractive apt-get install phpmyadmin -y -q

if [ $? -eq 0 ]; then
    # Installation of phpmyadmin failed
    echo -e "${CYAN}Installation of phpmyadmin failed. This may happen during the Installation of phpmyadmin.$NC"

    # Reload lighttpd
    echo -e "${CYAN}Reloading lighttpd...$NC"
    sudo /etc/init.d/lighttpd force-reload

    # Install phpmyadmin second time
    echo -e "${CYAN}Trying to Install phpmyadmin second time...$NC"
    sudo apt-get install phpmyadmin -y
fi

# Install Java
echo -e "${CYAN}Installing Java...$NC"
sudo apt-get install oracle-java8-jdk -y

# Mark Installation as Done
echo "Instinfo:DONE."

# Install BrickPi drivers
echo -e "${CYAN}Installing BrickPi drivers...$NC"
sudo curl -kL dexterindustries.com/update_brickpi_plus | bash
