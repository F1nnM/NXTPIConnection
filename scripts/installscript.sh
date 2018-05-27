#!/bin/bash/

# Download part 2 of this script
wget -q https://git.io/vpNyW -O /tmp/script_part_2

# Add part 2 of this script to startup
(crontab -l && echo "@reboot bash /tmp/script_part_2 >> /tmp/installscript_out.log 2>&1") | crontab -

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
