#!/bin/bash
 

case $1 in 
	-d)
	stdbuf -oL -eL python ~/git/november-sbs/src/main/bash/datagen.py | analyzer -json
	;;
	-c)
	
	