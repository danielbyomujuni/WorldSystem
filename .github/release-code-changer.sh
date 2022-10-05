#!/bin/bash
# This is a test.sh be careful!
# Release code changer to have it easy to build
# Get config informations
apt-get install coreutils
POM-RELEASE=$(cat ./release-code-changer.yml | grep pom.release: | cut -d ':' -f2)
RELEASEVER=$(cat ./release-code-changer.yml | grep release.version: | cut -d ':' -f2)
sed -i "0,:WorldSystem/.*/:s:WorldSystem/$RELEASEVER/" ./README.md $>/dev/null 2>&1
sed -i "0,:<version>.*</version>:s:<version>$POM-RELEASE</version>" ./../pom.xml $>/dev/null 2>&1
exit 0
