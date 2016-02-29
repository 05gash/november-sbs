# Sailing  by Sound

### Project Description: 

People with visual impairments enjoy outdoor activities as much as we all do, and local 
sailing organisations are pleased to provide opportunities for disabled and visually 
impaired people to experience sailing. One disappointing limitation is that the standard 
navigation instruments (location, windspeed, compass heading etc) all have visual readouts, 
so visually impaired people are not able to participate in this central aspect of sailing. 
For this project, we have created an embedded device that uses a Raspberry Pi to communicate 
with navigational instruments using the NMEA2K standard, and provides users with speech output 
in response to commands given via a small number of waterproof buttons on the outside of the
unit.


### Instructions On Using the Device:

##### TO START THE DEVICE ON BOAT

1. Connect the device to power
2. The device will boot automatically; and it is ready to use

##### TO START THE SIMULATION

1. Connect to RaspPi using SSH. Follow this link for complete instructions on how to connect a 
RaspPi to you computer (http://superuser.com/questions/1030331/how-to-connect-two-locally-linked-computers-to-wifi-using-ipv4-addressing)

2. Change directory to git/november-sbs
```shell
$ cd ../git/november-sbs
```
		
3. Use Maven to build
```shell
$ mvn install
```
This command performs compilation, and if the compilation is done correctly, you will see a message of success on your screen. Any error messages might be due to a compilation error or might appear if you are in the wrong directory.

4. Change directory to target
```shell
$ cd target
```

5. Launch the simulator server
```shell
$ sudo java -jar sbs-1.00-SNAPSHOT-jar-with-dependencies.jar simulator
```
This command will start the server on RaspPi. The file sbs-1.00-SNAPSHOT-jar-with-dependencies.jar will be
present in the directory after compilation.

6. Repeat steps 1-4 on client machine

7. Run the simulator client
```
$ sudo java -jar sbs-1.00-SNAPSHOT-jar-with-dependencies.jar simulator client raspberrypi.local
```

8. Now, the simulator will start. Navigate the boat with the following keys: A (turn left), D (turn right), W (speed up), D (slow down).
