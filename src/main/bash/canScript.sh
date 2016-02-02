#!/bin/bash
 
stdbuf -oL -eL python ~/git/november-sbs/src/main/bash/datagen.py | analyzer -json
