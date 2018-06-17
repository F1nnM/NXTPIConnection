#!/bin/bash/

# Define Colors
GREEN='\033[1;32m'
NC='\033[0m'
CYAN='\033[1;36m'

echo "----------------------------------------------Part 2----------------------------------------------"

# Wait for Network
while ! ping -c 1 -W 1 8.8.8.8; do
    echo -e "${CYAN}Waiting for 8.8.8.8 - network interface might be down...$NC"
    sleep 1
done

# Remove this script from startup
echo -e "${CYAN}Removing this script from startup...$NC"
sudo crontab -l | grep -v "@reboot bash /home/pi/NXTPi/install/script_part_2 >> /home/pi/NXTPi/install/installscript_out.log 2>&1" | crontab -

# Download part 3 of this script
echo -e "${CYAN}Downloading third part of this script...$NC"
sudo wget -q https://git.io/vhYnH -O /home/pi/NXTPi/install/script_part_3

# Add part 3 of this script to startup
echo -e "${CYAN}Adding third part to startup...$NC"
(crontab -l ; echo "@reboot bash /home/pi/NXTPi/install/script_part_3 >> /home/pi/NXTPi/install/installscript_out.log 2>&1") | crontab -
echo -e "${GREEN}Succesfully added the third part of this script to startup!$NC"
echo -e "${CYAN}Active Cron Jobs:$NC"
crontab -l
echo -e "\n"

# Update
echo -e "${CYAN}Updating sources...$NC"
sudo DEBIAN_FRONTEND=noninteractive apt-get update

# Upgrade
echo -e "${CYAN}Upgrading...$NC"
sudo DEBIAN_FRONTEND=noninteractive apt-get upgrade -y -q

# Remove unnecessary Packages
echo -e "${CYAN}Running apt-autoremove...$NC"
sudo DEBIAN_FRONTEND=noninteractive apt-autoremove -y -q

# Install Git
echo -e "${CYAN}Installing Git...$NC"
sudo DEBIAN_FRONTEND=noninteractive apt-get install git -y -q

# Install RaspAp
echo -e "${CYAN}Installing Raspap...$NC"
wget -q https://git.io/voEUQ -O /tmp/raspap && printf "y\ny\nn\n" | sudo bash /tmp/raspap

echo -e "------------------------------------------------------------------\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n"

# Reboot
echo -e "${CYAN}Reboot$NC"
echo "Reboot in: 3" | wall
sleep 1
echo "Reboot in: 2" | wall
sleep 1
echo "Reboot in: 1" | wall
sleep 1
sudo reboot now
