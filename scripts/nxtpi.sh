#!/bin/bash

function main() {
    if [[ "$1" == "run" ]] ; then
        filename=$2
        if [[ ${filename: -4} == ".jar" ]] ; then
            mainclassmf=$(unzip -c $filename META-INF/MANIFEST.MF | grep Main-Class)
            mainclass=$(cut -d' ' -f2 <<< $mainclassmf)
            mainclass=$(sed -e "s/[^.a-zA-Z]//g" <<< $mainclass)
            echo "Path to main class: $mainclass"
            sudo java -d32 -cp "$filename:/opt/leJOS_NXJ_0.9.1beta-3/lib/pc/*" $mainclass
        else
            echo "The given file is no jar file!"
        fi
    else
        echo "Wrong Arguments. Usage: nxtpi run [jarfile]"
    fi
}

main "$1" "$2"