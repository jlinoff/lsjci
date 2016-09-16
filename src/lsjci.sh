#!/bin/bash
#Java="/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.101-3.b13.el7_2.x86_64/bin/java"
Java=$(which java)
Cp=$0.jar

if [ ! -f $Java ] ; then
    echo ""
    echo "ERROR: file not found: $Java."
    echo ""
    exit 1
fi

JavaVer=$($Java -version 2>&1 | grep '^java version' | awk -F'"' '{print $2}')
JavaVerOk=$(echo $JavaVer | egrep '^1.[89]|^1.[1-9][0-9]' >/dev/null && echo 1 || echo 0)
if (( $JavaVerOk == 0 )) ; then
    echo ""
    echo "ERROR: java version is $JavaVer. It must be 1.8 or later."
    echo ""
    exit 1
fi

if [ ! -f $Cp ] ; then
    echo ""
    echo "ERROR: internal error - classpath not found: $Cp."
    echo "       Please contact Joe Linoff (jlinoff@rambus.com) for help."
    echo ""
    exit 1
fi

$Java -cp $Cp ListClassInfo $*
