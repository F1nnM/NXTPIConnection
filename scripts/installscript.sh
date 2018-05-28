#!/bin/bash/

# Define Colors
GREEN='\033[1;32m'
NC='\033[0m'
CYAN='\033[1;36m'

# Download part 2 of this script
echo -e "${CYAN}Downloading second part of this script...$NC"
sudo wget -q ADDR -O /home/pi/script_part_2

# Download script for displaying the output of the second part of this script
echo -e "${CYAN}Downloading Script for Displaying the log of the second script...$NC"
sudo wget -q https://git.io/vhmLc -O /home/pi/display_log

# Add part 2 of this script to startup
echo -e "${CYAN}Adding second part to startup...$NC"
(crontab -l ; echo "@reboot bash /home/pi/script_part_2 >> /home/pi/installscript_out.log 2>&1") | crontab -
echo -e "${GREEN}Succesfully added the second part of this script to startup!$NC"
echo -e "${CYAN}Active Cron Jobs:$NC"
crontab -l
echo -e "\n"

# Update
echo -e "${CYAN}Updating sources...$NC"
sudo apt update

# Upgrade
echo -e "${CYAN}Upgrading...$NC"
sudo apt upgrade -y

# Remove unnecessary Packages
echo -e "${CYAN}Running apt-autoremove...$NC"
sudo apt autoremove -y

# Install Git
echo -e "${CYAN}Installing Git...$NC"
sudo apt install git -y

# Install RaspAp
echo -e "${CYAN}Installing Raspap...$NC"
wget -q https://git.io/voEUQ -O /tmp/raspap && yes y | sudo bash /tmp/raspap

# Reboot, if RaspAp Installation Reboot not fires
echo -e "${CYAN}Reboot.$NC"
sudo reboot now
