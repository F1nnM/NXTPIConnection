#!/bin/bash/

# Check if installation is done
File=/home/pi/NXTPi/install/installscript_out.log
if grep -q Instinfo:DONE. "$File";
then
        # Output the log file
        echo -e "$(cat /home/pi/NXTPi/install/installscript_out.log)"
        echo -e "\n\n\n\n"

        # Ask for deleting the installation files
        echo -e "The Installation is completed, delete all installation Files? [y/N]: "
        read answer
        declare -l answer
        answer=$answer
        if [ $answer == "y" ] || [ $answer == "yes" ] ; then
                # Delete files
                echo "Deleting all installation files..."
                sudo rm -R -v /home/pi/NXTPi/install/*
                sudo rm -R -v /home/pi/Dexter
                sudo rm -v /home/pi/display_log
        fi
else
        # Output the log file
        echo -e "$(cat /home/pi/NXTPi/install/installscript_out.log)"
        
        # Send the current status to the User
        tail -n0 -f /home/pi/NXTPi/install/installscript_out.log
fi
