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
    if [ $answer == "y" ] || [ $answer == "yes" ] ; then
        sudo mariadb -e "GRANT ALL PRIVILEGES ON *.* TO '$Username'@'localhost' WITH GRANT OPTION;"
    fi

    sudo mariadb -e "FLUSH PRIVILEGES;"
}

function delete_user() {
    echo "Enter Username to delete:"
    read Username

    sudo mariadb -e "EXEC sp_MSForEachDB 'USE [?]; IF  EXISTS (SELECT * FROM sys.schemas WHERE name = N''$Username'') DROP SCHEMA [$Username]; '"
    
    sudo mariadb -e "EXEC sp_MSForEachDB 'USE [?]; IF  EXISTS (SELECT * FROM sys.database_principals WHERE name = N''EU\USUBOLS'') DROP USER [$Username]; '"

    sudo mariadb -e "IF  EXISTS (SELECT * FROM sys.server_principals WHERE name = N'$Username') DROP LOGIN [$Username];"
}

function bye() {
    echo -e "${GREEN}Done.$NC"
    echo "Bye."
}

function main() {
    if [ ! hash mariadb 2>/dev/null ] ;
        then
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
