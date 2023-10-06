/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/ClientSide/javascript.js to edit this template
 */
/* global fetch */

/* get list of general information about all nations */
function getNations(){
    var URL = "https://restcountries.com/v3.1/all";
    fetch(URL)
        .then(function(response) {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error("NETWORK RESPONSE ERROR");
            }
        })
        .then(function(data) {
            console.log('Raw Data', data);
            displayNations(data);
        })
        .catch((error) => console.error("FETCH ERROR:", error));
}

function displayNations(d) {
    var data = d;
    /* get the number of entries */
    var keyCount = Object.keys(data).length;
    
    /* reorder the objets into alphabetical order */
    data.sort(function(a, b) {
    return a.name.common.localeCompare(b.name.common);
    });
    
    var table = "<tr><th>Nation</th><th>Population</th><th>Area</th></tr>";
    for(var i = 0; i < keyCount; i++) {
        var obj = data[i];
        table = table + "<tr><td>" +
        obj.name.common +
        "</td><td>" +
        obj.population.toLocaleString() +
        "</td><td>" +
        obj.area.toLocaleString() + " km^2" + 
        "</td></tr>";
    }
    console.log('Table Data', table);
    document.getElementById("nationList").innerHTML = table;
}

/* get information about a specified nation */
var nat;
function joinSearches(){
    searchNation();
}

function searchNation(){
    var name = document.getElementById("name").value.toLowerCase();
    
    var URL = "https://restcountries.com/v3.1/name/" + name;
    fetch(URL)
        .then(function(response) {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error("NETWORK RESPONSE ERROR");6
            }
        })
        .then(function(data) {
            console.log('Raw Data', data);
            displayNationSearch(data);
        })
        .catch((error) => console.error("FETCH ERROR:", error));
}

function displayNationSearch(d) {
    var data = d;
    var obj = data[0];
    
    var coast = "";
    if (obj.landlocked === true) {coast = "<tr><td>Coastal Status</td><td>Landlocked</td></tr>"};
    
    var un = "";
    if (obj.unMember === true) {un = "Member Nation"} else {un = "non-Member State"};
    
    var independent = "";
    if (obj.independent === true) {independent = " Independent"} else {independent = " Dependent"};
    
    var currencies = "";
    for (c in obj.currencies) {
        currencies += obj.currencies[c].name + "(" + obj.currencies[c].symbol + ")<br>";
    }
    
    var languages = "";
    for (l in obj.languages) {
        languages += obj.languages[l] + "<br>";
    }
        
    var table = "<tr><th colspan='2'>" + obj.name.official + "</th></tr>" +
        "<tr><td>Population</td><td>" + obj.population.toLocaleString() + "</td></tr>" +
        "<tr><td>Demonym</td><td>" + obj.demonyms.eng.f + "</td></tr>" +
        "<tr><td>Official Languages</td><td>" + languages + "</td></tr>" +
        "<tr><td>UN Membership</td><td>" + un + "</td></tr>" +
        "<tr><td>Recognition</td><td>" + obj.status + independent + "</td></tr>" +
        "<tr><td>Currencies</td><td>" + currencies + "</td></tr>" +
        "<tr><td>Area</td><td>" + obj.area.toLocaleString() + " km^2" + "</td></tr>" +
        coast +
        "<tr><td>Location</td><td>" + obj.subregion + ", " + obj.region + "</td></tr>" +
        "<tr><td>Coordinates</td><td>" + obj.latlng[0] + "° N " + obj.latlng[1] + "° W" + "</td></tr>" +
        "<tr><td>Capital</td><td>" + obj.capital[0] + "</td></tr>" 
        ;
    nat = table;
    
    var flagURL = obj.flags.png;
    document.getElementById("flagContainer").innerHTML = '<img src="' + flagURL + '" width="100%">';
    
    /* pass a common name to the cities lister to try and avoid overlap*/
    searchCities(obj.name.common);
}

/* retireve a list of cities within a nation */
function searchCities(name){
    
    var URL = "https://public.opendatasoft.com/api/records/1.0/search/?dataset=geonames-all-cities-with-a-population-500&q=" + name + "&sort=population";
    fetch(URL)
        .then(function(response) {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error("NETWORK RESPONSE ERROR");
            }
        })
        .then(function(data) {
            console.log('Raw Data', data);
            displayCities(data);
        })
        .catch((error) => console.error("FETCH ERROR:", error));
}

function displayCities(d) {
    var data = d;
    /* get the number of entries */
    var keyCount = Object.keys(data.records).length;
    
    console.log('debug', keyCount);
    
    var table = nat + "<tr><th colspan='2'>Urban Areas</th></tr>";
    for(var i = 0; i < keyCount; i++) {
        var obj = data.records[i];
        table = table + "<tr><td colspan='2'>" +
        (i+1) + " - " + obj.fields.ascii_name +
        "</td></tr>";
    }
    console.log('Table Data', table);
    document.getElementById("nationData").innerHTML = table;
}

/* get information about a specified urban area */
function searchCity(){
    var city = document.getElementById("cityName").value.toLowerCase();
    
    var URL = "https://public.opendatasoft.com/api/records/1.0/search/?dataset=geonames-all-cities-with-a-population-500&q=" + city + "&sort=population";
    fetch(URL)
        .then(function(response) {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error("NETWORK RESPONSE ERROR");
            }
        })
        .then(function(data) {
            console.log('Raw Data', data);
            displayCitySearch(data);
        })
        .catch((error) => console.error("FETCH ERROR:", error));
}

function displayCitySearch(d) {
    var data = d;
    var obj = data.records[0];
    
    var table = "<tr><th colspan='2'>" + obj.fields.name + "</th></tr>" +
        "<tr><td>Population</td><td>" + obj.fields.population.toLocaleString() + "</td></tr>" +
        "<tr><td>Coordinates</td><td>" + obj.fields.latitude + "° N " + obj.fields.longitude + "° W" + "</td></tr>" +
        "<tr><td>Country</td><td>" + obj.fields.country + "</td></tr>"
        ;
        
    console.log('Table Data', table);
    document.getElementById("cityData").innerHTML = table;
}

/* Image Rollover */
function changeImage(){
    var image = document.getElementById('headImage');
    if (image.src.match("img/index/logo.png")) {
        image.src = "img/index/logo_hover.png";
    } else {
        image.src = "img/index/logo.png";
    }
}
