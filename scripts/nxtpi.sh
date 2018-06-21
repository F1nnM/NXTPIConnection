#!/bin/bash

function run() {
    filename=$2
    if [[ ${filename: -4} == ".jar" ]] ; then
        mainclassmf=$(unzip -c $filename META-INF/MANIFEST.MF | grep Main-Class)
        mainclass=$(cut -d' ' -f2 <<< $mainclassmf)
        mainclass=$(sed -e "s/[^.a-zA-ZäÄüÜöÖ0-9_$]//g" <<< $mainclass)
        echo "Path to main class: $mainclass"
        jarargs=${@#${1}}
        jarargs=${jarargs#${2}}
        sudo java -d32 -cp "$filename:/opt/leJOS_NXJ_0.9.1beta-3/lib/pc/*" $mainclass $jarargs
    else
        echo "The given file is no jar file!"
    fi
}

function getJavaFiles() {
    if [[ "$1" != */ ]] ; then
        dir="$1/"
    fi

    files=()

    for file in $dir*; do
        [ -e "$file" ] || continue
        if [[ ${file: -5} == ".java" ]] ; then
            files=(${files[@]} "$file")
        elif [[ -d $file ]] ; then
            files=(${files[@]} $(getJavaFiles $file))
        fi
    done

    echo ${files[@]}
}

function nxj() {
    if [ -f $2 ] && [ ${2: -4} == ".jar" ] ; then
            jarargs=${@#${1}}
	    jarargs=${jarargs#${2}}
            jarargs=${jarargs#${3}}
            jarargs=${jarargs#${4}}
            name=${2::-4}
            name="$name.nxj"
            nxjc -bootclasspath "$2" -cp "/opt/leJOS_NXJ_0.9.1beta-3/lib/nxt/classes.jar" $(getJavaFiles $3)
            nxjlink -cp "$2" -o "$name" $4 #$jarargs WIP
            nxjupload "$name"
    fi
}

function help() {
    echo "------------------nxtpi help------------------"
    echo "General usage: nxtpi [-jar or -nxj] <args>"
    echo "To run a leJos PC project: nxtpi -jar [path-to-jarfile] <jarargs>"
    echo "To run a leJos NXJ project: nxtpi -nxj [path-to-jarfile] [path-to-source-folder] [main-class] <java-args>"
}

function main() {
    if [[ "$1" == "-jar" ]] ; then
        run "$@"
    elif [[ "$1" == "-nxj" ]] ; then
        nxj "$@"
    elif [[ "$1" == "-help" ]] ; then
        help
    else
        echo "Illegal Arguments! Usage: nxtpi [-jar or -nxj] <args>. For further help type \"nxtpi -help\""
    fi
}

main $@
