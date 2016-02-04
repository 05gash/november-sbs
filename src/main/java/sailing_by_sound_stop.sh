#!/bin/bash

# TODO(ml693):
# This file is created only because I was following the link
# of how to boot program on startup. I am not use
# if stop file is actually required. Need to figure that out.

pid=`ps aux | grep sailing_by_sound_start | awk '{print $2}'`
kill -9 $pid
