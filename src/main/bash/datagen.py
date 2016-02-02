from datetime import *
import time

while True:
	d = datetime.today()
	print(d.strftime("%Y-%m-%d-%H:%M:%S.%f,2,127251,36,255,8,7d,0b,7d,02,00,ff,ff,ff") + "\n")
	print(d.strftime("%Y-%m-%d-%H:%M:%S.%f,2,127250,36,255,8,00,5a,7c,00,00,00,00,fd") + "\n")

	time.sleep(0.25)
