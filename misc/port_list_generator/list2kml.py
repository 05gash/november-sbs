import simplekml
import json

with open("./locationlist.json", "r") as fi:
    js = json.loads(fi.read())
    kml = simplekml.Kml()
    for port in js['ports']:
        loc = port['location']
        kml.newpoint(name=port['name'], coords=[(loc['lng'], loc['lat'])])
    kml.save("listofports.kml")


