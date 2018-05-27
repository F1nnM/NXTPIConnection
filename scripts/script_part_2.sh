# Remove this script from startup
sudo crontab -l | grep -v "@reboot bash /tmp/script_part_2 >> /tmp/installscript_out.log 2>&1" | crontab -

# Create Directory for RaspAp
sudo mkdir /var/www/html/raspap

# move RaspAp to Directory
sudo mv /var/www/html/* /var/www/html/raspap/

# Create Directory for GumCP
sudo mkdir /var/www/html/admin

# Clone GumCP
sudo git clone https://github.com/gumslone/GumCP.git /var/www/html/admin/

# Install PHP 7.0 and mariadb
sudo apt install php7.0-fpm php7.0 php-ssh2 php-cgi mariadb-server mariadb-client -y

# Replace line in php-fpm config
sudo sed -i -e 's/;cgi.fix_pathinfo=1/cgi.fix_pathinfo=1/g' /etc/php/7.0/fpm/php.ini

# Create Backup of fastcgi-php.conf
sudo cp /etc/lighttpd/conf-available/15-fastcgi-php.conf /etc/lighttpd/conf-available/15-fastcgi-php.conf.bak

# Delete fastcgi-php.conf
sudo rm /etc/lighttpd/conf-available/15-fastcgi-php.conf

# Write fastcgi-php.conf config file
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
sudo lighttpd-enable-mod fastcgi
sudo lighttpd-enable-mod fastcgi-php

# Do stuff
sudo ls -l /etc/lighttpd/conf-enabled

# Reload lighttpd
sudo service lighttpd force-reload

# Install php packages
sudo apt install php7.0-mysql php7.0-curl php7.0-gd php7.0-intl php-pear php-imagick php7.0-imap php7.0-mcrypt php-memcache php7.0-pspell php7.0-recode php7.0-sqlite3 php7.0-tidy php7.0-xmlrpc php7.0-xsl php7.0-mbstring php-gettext php-apcu -y

# Reload php7.0-fpm
sudo service php7.0-fpm reload

# Install phpmyadmin
export DEBIAN_FRONTEND=noninteractive
sudo apt install phpmyadmin -y
sudo cp /tmp/phpmyadmin.conf /etc/dbconfig-common/phpmyadmin.conf
dpkg-reconfigure --frontend=noninteractive phpmyadmin

# Reload lighttpd
sudo /etc/init.d/lighttpd force-reload

# Install phpmyadmin second time, because first time installation always fails
sudo apt install phpmyadmin -y

# Install Java
sudo apt install oracle-java8-jdk -y

#Delete everything
sudo rm /home/pi/phpmyadmin.conf
sudo rm /home/pi/script_part_2

# Install BrickPi drivers
sudo curl -kL dexterindustries.com/update_brickpi_plus | bash
