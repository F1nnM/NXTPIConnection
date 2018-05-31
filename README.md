# NXTPIConnection

## Requirements
* A Raspberry Pi Model 3 running [Raspbian Stretch with Desktop](https://downloads.raspberrypi.org/raspbian_latest). [(Torrent Download)](https://downloads.raspberrypi.org/raspbian_latest.torrent)

## Useful Links
* [RaspAp](https://github.com/billz/raspap-webgui)
* [GumCP](https://github.com/gumslone/GumCP)

## Installation

#### Install from shell prompt:
```sh
$ sudo wget -q https://git.io/vpNMY -O /tmp/installer && sudo bash /tmp/installer
```

The script then will automatically download the second part of the script.
The pi should reboot two times during the installation process.
After the first reboot, the output of the installation will be sent into a log file.

The Output can be seen with the display_log script:

#### To see the output of the second and third part of the script, just type (in pi's home directory):
```sh
$ bash display_log
```

## Other Stuff

To create a new MariaDB User, just type in:
```sh
$ wget -q https://git.io/vhYzC -O /tmp/mariadbtools && sudo bash /tmp/mariadbtools
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
