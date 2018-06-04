#!/bin/bash/

# Define Colors
GREEN='\033[1;32m'
NC='\033[0m'
CYAN='\033[1;36m'

# Expand Filesystem
echo -e "${CYAN}Expanding Filesystem...$NC"
sudo raspi-config nonint do_expand_rootfs

# Create NXTPi Folder
echo -e "${CYAN}Creating NXTPi Folder in /home/Pi...$NC"
sudo mkdir /home/pi/NXTPi/

# Create Folder for Installation
echo -e "${CYAN}Creating Folder for installation...$NC"
sudo mkdir /home/pi/NXTPi/install/

# Download part 2 of this script
echo -e "${CYAN}Downloading second part of this script...$NC"
sudo wget -q https://git.io/vpNyW -O /home/pi/NXTPi/install/script_part_2

# Download script for displaying the output of the second part of this script
echo -e "${CYAN}Downloading Script for Displaying the log of the second script...$NC"
sudo wget -q https://git.io/vhmLc -O /home/pi/NXTPi/install/display_log

# Add part 2 of this script to startup
echo -e "${CYAN}Adding second part to startup...$NC"
(crontab -l ; echo "@reboot bash /home/pi/NXTPi/install/script_part_2 >> /home/pi/NXTPi/install/installscript_out.log 2>&1") | crontab -
echo -e "${GREEN}Succesfully added the second part of this script to startup!$NC"
echo -e "${CYAN}Active Cron Jobs:$NC"
crontab -l
echo -e "\n"

# Reboot
echo -e "${CYAN}Reboot.$NC"
sudo reboot now
