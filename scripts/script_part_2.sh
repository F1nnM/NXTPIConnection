#!/bin/bash/

GREEN='\033[1;32m'
NC='\033[0m'
CYAN='\033[1;36m'

# Wait for Network
while ! ping -c 1 -W 1 8.8.8.8; do
    echo -e "${CYAN}Waiting for 8.8.8.8 - network interface might be down...$NC"
    sleep 1
done

# Remove this script from startup
echo -e "${CYAN}Removing this script from startup...$NC"
sudo crontab -l | grep -v "@reboot bash /home/pi/script_part_2 >> /home/pi/installscript_out.log 2>&1" | crontab -

#Download the phpmyadmin.conf
echo -e "${CYAN}Downloading phpmyadmin.conf...$NC"
sudo wget -q https://git.io/vhmUM -O /tmp/phpmyadmin.conf

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

# Install phpmyadmin
echo -e "${CYAN}Installing phpmyadmin...$NC"
sudo export DEBIAN_FRONTEND=noninteractive
sudo sh -c "echo 'phpmyadmin phpmyadmin/dbconfig-install boolean true' | debconf-set-selections"
sudo sh -c "echo 'phpmyadmin phpmyadmin/reconfigure-webserver multiselect lighttpd' | debconf-set-selections"
sudo DEBIAN_FRONTEND=noninteractive apt-get install phpmyadmin -y -q

# Reload lighttpd
echo -e "${CYAN}Reloading lighttpd...$NC"
sudo /etc/init.d/lighttpd force-reload

# Install phpmyadmin second time, because first time installation always fails
echo -e "${CYAN}Trying to Install phpmyadmin second time, if first time installation fails...$NC"
sudo apt-get install phpmyadmin -y

# Install Java
echo -e "${CYAN}Installing Java...$NC"
sudo apt-get install oracle-java8-jdk -y

# Delete everything
echo -e "${CYAN}Deleting this script...$NC"
sudo rm /home/pi/script_part_2

# Mark Installation as Done
echo "Instinfo:DONE."

# Install BrickPi drivers
echo -e "${CYAN}Installing BrickPi drivers...$NC"
sudo curl -kL dexterindustries.com/update_brickpi_plus | bash
