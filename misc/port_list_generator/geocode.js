var sync = require('sync');
var fs = require("fs");

var GoogleMapsAPI = require("googlemaps");

var publicConfig = {
  key: 'API_KEY_HERE',
  stagger_time:       1000, // for elevationPath
  encode_polylines:   false,
  secure:             true, // use https
};
var gmAPI = new GoogleMapsAPI(publicConfig);

var portlist = require("./portlist.json");

var locations = [];

for(v in portlist['ports']){
    var name = portlist['ports'][v];
    locations.push({'name': name});
}

function geocode(loc, cb){
    var params = {
        "address":    loc.name,
        "language":   "en",
    };

    gmAPI.geocode(params, function(err, result){
        if(err){
            //console.log(err);
        }else{
            if(result.status == "OK"){
                var res = {
                    "name": loc.name,
                    "location": result.results[0].geometry.location,
                };
                cb(null, res);
                return;
            }
        }
        cb(null, {"name": loc.name, "location": {}});
    });
}

sync(function(){
    try{
        for(key in locations){
            var result = geocode.sync(null, locations[key]);
            console.log("Done " + (parseInt(key)+1) + " of " + (locations.length));
            locations[key] = result;
        }
        fs.writeFileSync("./locationlist.json", JSON.stringify({ports:locations}));
    }catch(e){
        console.error(e);
    }
});
