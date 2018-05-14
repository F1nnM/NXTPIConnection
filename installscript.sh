#!/bin/bash/

sudo apt update

sudo apt upgrade -y

sudo apt install git -y

wget -q https://git.io/voEUQ -O /tmp/raspap && sudo bash /tmp/raspap

sudo mkdir /var/www/html/raspap

sudo mv /var/www/html/* /var/www/html/raspap/

sudo mkdir /var/www/html/admin

sudo git clone https://github.com/gumslone/GumCP.git /var/www/html/admin/

sudo mv /var/www/html/admin/GumCP/* var/www/html/admin

sudo apt install php7.0-fpm php7.0 -y

sudo apt install php-ssh2 -y

sudo apt install mariadb-server mariadb-client -y

sudo sed -i -e 's/; cgi.fix_pathinfo=1/cgi.fix_pathinfo=1/g' /tmp/file.txt

sudo cp /etc/lighttpd/conf-available/15-fastcgi-php.conf /etc/lighttpd/conf-available/15-fastcgi-php.conf.bak

sudo rm /etc/lighttpd/conf-available/15-fastcgi-php.conf

sudo sh -c 'echo "# /usr/share/doc/lighttpd-doc/fastcgi.txt.gz" >> /etc/lighttpd/conf-available/15-fastcgi-php.conf'

sudo sh -c 'echo "# http://redmine.lighttpd.net/projects/lighttpd/wiki/Docs:ConfigurationOptions#mod_fastcgi-fastcgi" >> /etc/lighttpd/conf-available/15-fastcgi-php.conf'

sudo sh -c 'echo "" >> /etc/lighttpd/conf-available/15-fastcgi-php.conf'
sudo sh -c 'echo "## Start an FastCGI server for php (needs the php7.0-cgi package)" >> /etc/lighttpd/conf-available/15-fastcgi-php.conf'

sudo sh -c 'echo "fastcgi.server += ( ".php" =>" >> /etc/lighttpd/conf-available/15-fastcgi-php.conf'

sudo sh -c 'echo "        ((" >> /etc/lighttpd/conf-available/15-fastcgi-php.conf'

sudo sh -c 'echo "                "socket" => "/var/run/php/php7.0-fpm.sock"," >> /etc/lighttpd/conf-available/15-fastcgi-php.conf'

sudo sh -c 'echo "                "broken-scriptfilename" => "enable"" >> /etc/lighttpd/conf-available/15-fastcgi-php.conf'

sudo sh -c 'echo "        ))" >> /etc/lighttpd/conf-available/15-fastcgi-php.conf'

sudo sh -c 'echo ")" >> /etc/lighttpd/conf-available/15-fastcgi-php.conf'

sudo lighttpd-enable-mod fastcgi

sudo lighttpd-enable-mod fastcgi-php

sudo ls -l /etc/lighttpd/conf-enabled

sudo service lighttpd force-reload

sudo apt install php7.0-mysql php7.0-curl php7.0-gd php7.0-intl php-pear php-imagick php7.0-imap php7.0-mcrypt php-memcache  php7.0-pspell php7.0-recode php7.0-sqlite3 php7.0-tidy php7.0-xmlrpc php7.0-xsl php7.0-mbstring php-gettext -y

sudo apt install php-apcu -y

sudo service php7.0-fpm reload

sudo apt install phpmyadmin -y

sudo /etc/init.d/lighttpd force-reload

sudo apt install phpmyadmin -y

sudo apt install oracle-java8-jdk -y

sudo curl -kL dexterindustries.com/update_brickpi_plus | bash
