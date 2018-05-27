#!/bin/bash/

# Output the log file
echo -e "$(cat /home/pi/installscript_out.log)"

# Delete files
sudo rm /home/pi/display_log.sh
sudo rm /home/pi/installscript_out.log
