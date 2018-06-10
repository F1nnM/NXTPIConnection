# NXTPIConnection 
[![NXTPiConnection Release v0.1.4](https://img.shields.io/badge/NXTPiConnection_Release-v0.1.4-blue.svg)](https://github.com/MarkusJx/NXTPIConnection/releases/tag/v0.1.4) [![NXTConn Status Debugging](https://img.shields.io/badge/NXTConn_Status-Debugging-orange.svg)](https://github.com/MarkusJx/NXTPIConnection/releases/tag/v0.1.4) [![PiConn Status Debugging](https://img.shields.io/badge/PiConn_Status-Debugging-orange.svg)](https://github.com/MarkusJx/NXTPIConnection/releases/tag/v0.1.4) 

## Requirements
* A Raspberry Pi Model 3 running [Raspbian Stretch with Desktop](https://downloads.raspberrypi.org/raspbian_latest). [(Torrent Download)](https://downloads.raspberrypi.org/raspbian_latest.torrent)

## Javadoc
* [NXTConn Javadoc](https://markusjx.github.io/NXTPIConnection/NXTConnJavadoc/)
* [PiConn Javadoc](https://markusjx.github.io/NXTPIConnection/PiConnJavadoc/)

## Useful Links
* [Website](https://markusjx.github.io/NXTPIConnection/)
* [Releases](https://github.com/MarkusJx/NXTPIConnection/releases)
* [RaspAp](https://github.com/billz/raspap-webgui)
* [GumCP](https://github.com/gumslone/GumCP)
* [leJOS NXJ](https://sourceforge.net/projects/nxt.lejos.p/files/)

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
$ bash NXTPi/install/display_log
```

## Other Stuff

To create a new MariaDB User, just type in:
```sh
$ wget -q https://git.io/vhYzC -O /tmp/mariadbtools && sudo bash /tmp/mariadbtools
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
