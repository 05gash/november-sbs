'''
Generates candump-like CAN packets for a few NMEA 2000 sensors
Designed to be fed into CANBoat for unit testing etc.
 
Jamie Wood 2016
'''

MY_ADDRESS = 35

def makeheader(pri, src, dest, pgn):
    header = (pri & 0x07) << 26 # highest 3 bits
    header += src # lowest 8 bits
    if dest == 255: #PDU2 format
        header += (pgn << 8)
    else: # PDU1 format
        header += (dest << 8)
        header += (pgn << 8)
    return header

def deg2rad(deg):
    return deg / (180.0 / 3.141592)

def makevesselheadingpacket(heading, deviation, variation):
    if heading < 0 or heading > 360:
        raise Error("Heading out of range!")
    if deviation < -180 or deviation > 180:
        raise Error("Deviation out of range!")
    if variation < -180 or variation > 180:
        raise Error("Variation out of range!")
    header = makeheader(2, MY_ADDRESS, 255, 127250)
    hea = int(deg2rad(heading) / 0.0001) # units of 0.0001 rad
    dev = int(deg2rad(deviation) / 0.0001) # units of 0.0001 rad
    var = int(deg2rad(variation) / 0.0001) # units of 0.0001 rad
    data = [
        0, # SID
        hea & 0xff, # heading, little endian
        (hea >> 8) & 0xff,
        dev & 0xff, # deviation, little endian
        (dev >> 8) & 0xff,
        var & 0xff, # variation, little endian
        (var >> 8) & 0xff,
        0x01 # 'magnetic' reference
    ]
    return header, data

def makewaterdepthpacket(depth, offset):
    if depth < 0:
        raise Error("Depth less than zero!")
    header = makeheader(2, MY_ADDRESS, 255, 128267)
    dep = int(depth / 0.01) # units of 0.01m
    off = int(offset / 0.001) # units of 0.01m
    data = [
        0, # SID
        dep & 0xff, # depth, little endian
        (dep >> 8) & 0xff,
        (dep >> 16) & 0xff,
        (dep >> 24) & 0xff,
        off & 0xff, # offset, little endian
        (off >> 8) & 0xff,
        0xff
    ]
    return header, data

def makewindpacket(speed, angle):
    if speed < 0:
        raise Error("Speed less than zero!")
    if angle < 0 or angle > 360:
        raise Error("Angle out of range!")
    header = makeheader(2, MY_ADDRESS, 255, 130306)
    spd = int(float(speed) / 0.01) # units of 0.01m/s
    angl = int(deg2rad(angle) / 0.0001) # units of 0.0001 rad
    data = [
        0, # SID
        spd & 0xff, # speed, little endian
        (spd >> 8) & 0xff,
        angl & 0xff, # angle, little endian
        (angl >> 8) & 0xff,
        0x02, # 'apparent' reference
        0xff,
        0xff
    ]
    return header, data

def makespeedpacket(speed):
    if speed < 0:
        raise Error("Speed less than zero!")
    
    header = makeheader(2, MY_ADDRESS, 255, 128259)
    spd = int(float(speed) / 0.01) # units of 0.01m/s
    data = [
        0, # SID
        spd & 0xff, # speed, little endian
        (spd >> 8) & 0xff,
        0xff,
        0xff,
        0x0, # measurement device (paddle wheel)
        0xff,
        0xff
    ]
    return header, data

def printpacket(h, d):
    print('<0x%08x> [%d] %s' % (h, len(d), ' '.join('%02x' % x for x in d)))

printpacket(*makespeedpacket(123.45))
printpacket(*makewindpacket(13.37, 20))
printpacket(*makewaterdepthpacket(123.45,-1.23))
printpacket(*makevesselheadingpacket(123.45,-1.23,-2.46))
