#!/bin/bash/

GREEN='\033[0;32m'
NC='\033[0m'

# Download part 2 of this script
wget -q https://git.io/vpNyW -O script_part_2

#Download the phpmyadmin.conf
wget -q https://git.io/vhmUM -O phpmyadmin.conf

# Add part 2 of this script to startup
(crontab -l ; echo "@reboot bash script_part_2 >> /tmp/installscript_out.log 2>&1") | crontab -
echo -e "${GREEN}Succesfully added the second part of this script to startup!${NC}"

# Update
sudo apt update

# Upgrade
sudo apt upgrade -y

# Remove unnecessary Packages
sudo apt autoremove -y

# Install Git
sudo apt install git -y

# Install RaspAp
wget -q https://git.io/voEUQ -O /tmp/raspap && sudo bash /tmp/raspap

# Reboot
sudo reboot now
