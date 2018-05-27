#!/bin/bash/

# Check if installation is done
File=installscript_out.log
if grep -q 1 "$File";
then
        # Output the log file
        echo -e "$(cat /home/pi/installscript_out.log)"
        
        # Delete files
        sudo rm /home/pi/display_log
        sudo rm /home/pi/installscript_out.log
else
        # Output the log file
        echo -e "$(cat /home/pi/installscript_out.log)"
        
        # Send the current status to the User
        tail -n0 -f installscript_out.log
fi
