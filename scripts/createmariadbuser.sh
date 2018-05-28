!/bin/bash/

# Define Colors
GREEN='\033[1;32m'
NC='\033[0m'
RED='\033[1;31m'

if ! hash mariadb 2>/dev/null; then
    echo -e "${RED}MariaDB seems to be not installed. Please install MariaDB to use this Script.$NC"
    echo "exit."
    exit 1
fi

echo "Enter Username of new User: "
read Username

echo "Enter Password: "
read Password

sudo mariadb -e "CREATE USER '$Username'@'localhost' IDENTIFIED BY '$Password';"

echo "Grant all Privileges to new User? [Y/N]: "
read answer
declare -l answer
answer=$answer
if [ $answer == "y" ] || [ $answer == "yes" ] ; then
        sudo mariadb -e "GRANT ALL PRIVILEGES ON *.* TO '$Username'@'localhost' WITH GRANT OPTION;"
fi

sudo mariadb -e "FLUSH PRIVILEGES;"

echo -e "${GREEN}Done.$NC"
echo "Bye."
