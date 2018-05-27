#!/bin/bash/

# Output the log file
echo "$(cat /home/pi/installscript_out.log)"

# Delete files
sudo rm /home/pi/display_log
sudo rm /home/pi/installscript_out.log
