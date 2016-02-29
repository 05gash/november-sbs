from bs4 import BeautifulSoup
import urllib2
import sys
import codecs
sys.stdout = codecs.getwriter('utf8')(sys.stdout)

# get the list of all ports from Wikipedia
listurl = "https://en.wikipedia.org/wiki/List_of_seaports"
listpage = BeautifulSoup(urllib2.urlopen(listurl), "html.parser")

# delete the table of contents
listpage.find_all("ul")[0].extract()

# find all the remaining lists and remove any which don't contain data
lists = listpage.find_all("ul")[:-27]

# remove any nested ports
for sublist in lists:
    if len(sublist.find_all("ul")) > 0:
        for s in sublist.find_all("ul"):
            s.extract()

# rebuild the list after clearing up nested ports
lists = listpage.find_all("ul")[:-27]

# flaten the list
ports = [item for sublist in lists for item in sublist.find_all("li")]

# get the text content of each port name, and sort
portnames = ["".join(li.find_all(text=True)) for li in ports]
portnames = sorted(portnames)

print '{"ports":[' + ",".join(['"' + name.strip() + '"' for name in portnames]) + "]}"
