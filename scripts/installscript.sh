#!/bin/bash/

wget -q URL -O /tmp/script_part_2

# Add part 2 of this script to startup
sudo /tmp/script_part_2 defaults

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
