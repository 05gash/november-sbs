#! /usr/bin/python
import serial
import sys

if len(sys.argv) != 2:
    print "Usage: candump.py <port>"
    sys.exit(1)

port = serial.Serial(sys.argv[1], 9600)

try:
    for i in range(100):
        print port.readline().strip()
        sys.stdout.flush()
except KeyboardInterrupt:
    pass
finally:
    port.close()
