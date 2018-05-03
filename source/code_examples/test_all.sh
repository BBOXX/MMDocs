#!/bin/bash

function check_sig(){
	GREEN='\033[0;32m'
	RED='\033[0;31m'
	NC='\033[0m' # No Color
if [ "$1" = "Authorization: 123:W0XOTbAobkmwBGeneeTkrGfbntQ=" ]; then
		printf "${GREEN}Success${NC}\n"
	else
		printf "${RED}Bad${NC}\n"
	fi
}

printf "Java: "
javac -classpath java/lib/commons-codec-1.11.jar java/GenerateSignature.java -d java/build
JAVA_SIGNATURE=`java -cp java/build/ GenerateSignature`
check_sig "$JAVA_SIGNATURE"

printf "Python: "
PYTHON_SIGNATURE=`python python/generate_signature.py`
check_sig "$PYTHON_SIGNATURE"

printf "Shell: "
SHELL_SIGNATURE=`shell/generate_signature.sh`
check_sig "$SHELL_SIGNATURE"