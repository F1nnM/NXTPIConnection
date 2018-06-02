# Define Colors
GREEN='\033[1;32m'
NC='\033[0m'
RED='\033[1;31m'

function create_user() {
    echo "Enter Username of new User: "
    read Username

    echo "Enter Password: "
    read Password

    sudo mariadb -e "CREATE USER '$Username'@'localhost' IDENTIFIED BY '$Password';"

    echo "Grant all Privileges to new User? [Y/N]: "
    read answer
    declare -l answer
    answer=$answer
    if [[ $answer = "y" ]] || [[ $answer = "yes" ]] ; then
        sudo mariadb -e "GRANT ALL PRIVILEGES ON *.* TO '$Username'@'localhost' WITH GRANT OPTION;"
    fi

    sudo mariadb -e "FLUSH PRIVILEGES;"
}

function delete_user() {
    echo "Enter Username to delete:"
    read Username

    sudo mariadb -e "REVOKE ALL PRIVILEGES, GRANT OPTION FROM '$Username'@'localhost';"
    
    sudo mariadb -e "DROP USER '$Username'@'localhost';"
    
    sudo mariadb -e "FLUSH PRIVILEGES;"
}

function bye() {
    echo -e "\n"
    echo -e "${GREEN}Done.$NC"
    echo "Bye."
}

function main() {

    PKG_OK=$(dpkg-query -W --showformat='${Status}\n' mariadb-server|grep "install ok installed")

    if [ "" == "$PKG_OK" ] ; then
        echo -e "${RED}MariaDB seems to be not installed. Please install MariaDB to use this Script.$NC"
        echo "exit."
        exit 1
    fi

    retry=true

    while $retry ; do
        retry=false
        echo "Select what to do:"
        echo "[1] Create User"
        echo "[2] Delete User"
        read number
        echo -e "\n"
        if [ $number == "1" ] ; then
            create_user
        elif [ $number == "2" ] ; then
            delete_user
        else
            echo -e "${RED}Illegal Arguments! Type in 1 or 2!$NC"
            echo -e "\n"
            retry=true
        fi
    done

    bye
}

main
