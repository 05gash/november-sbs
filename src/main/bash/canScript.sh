#!/bin/bash
 

case $1 in 
	-d)
		stdbuf -oL -eL python datagen.py | analyzer -json
		;;
	-b)
		actisense-serial -r /dev/ttyUSB0 | analyzer -json
		;;
	-m)
		python candump.py /dev/ttyUSB0 | stdbuf -oL -eL candump2analyzer | analyzer -json
		;;
	*)
		
		;;
esac

	
