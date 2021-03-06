# Port List Generator

These scripts were used to generate a list of seaports around the world. This database is used to find the nearest port to the user on request.

### Step 1 - Generate a list of port names (ports.py)
Python script which scrapes [this wikipedia article](https://en.wikipedia.org/wiki/List_of_seaports) to create a list of all the ports mentioned on the page. Requires [BeautifulSoup4](https://pypi.python.org/pypi/beautifulsoup4/4.3.2). Outputs JSON data to stdout, for piping into a file.

```shell
$ pip install beautifulsoup4
$ python ports.py > portlist.json
```

### Step 2 - Find locations of all ports in the list (geocode.js)
NodeJS script which uses the [googlemaps node module](https://github.com/moshen/node-googlemaps/) to geocode each name in the json list. Requires a Google Maps API key (see [here](https://developers.google.com/maps/documentation/javascript/get-api-key)). Note that most ports have names which are actually cities, so the coordinates are not those of the port itself. Some ports do not have english names, and the Google maps geocoder API doesn't do automatic conversion, so some ports are not found, and have to be added manually. Within the scope of this project, these issues were deemed acceptable for a prototype.

```shell
$ npm install googlemaps sync
$ node geocode.js
```

### Step 3 (optional) - view the generated list as KML (list2kml.py)
Python script which converts the locationlist.json generated by step 2 into a .kml file which can be viewed in Google Earth or a similar viewer. Requires [simplekml](https://pypi.python.org/pypi/simplekml/).

```shell
$ pip install simplekml
$ python list2kml.py
```