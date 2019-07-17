# Location-Based-Recommender-System-Application
Location based recommender system for restaurants - Mobile Application

This project is the mobile application front-end for the recommender system created at <a href="https://github.com/iantmc87/Location-Based-Recommender-System-Server">Location-
Based-Recommender-System-Server</a>. The application tracks the users location and sends the data to the server 
and then displays the recommended restaurants to the user.
<br><br>
The application has the following features:
<ul><li>View the recommended restaurants plus ratings and distance from location</li>
<li>View the location of the restaurant on a map</li>
<li>View and add reviews for the restaurant</li>
<li>Search by postcode and restaurant names</li>
<li>Change which recommender system filtering type to use</li>
<li>Choose restaurant preferences for the recommender system</li>
<li>Use a pinlock to secure the application</li></ul>

<hr>

<h3>Prerequisites</h3>
<ul><li>Android Studio installed on your computer</li>
<li>A android device or emulator running Android 6 or above(Samsung Galaxy S7 Edge was used for development and testing)</li>
<li>A Google maps API key (<a href="https://developers.google.com/maps/documentation/embed/get-api-key">Guide</a>)</li>
<li>The recommender system server side set up and running (<a href="https://github.com/iantmc87/Location-Based-Recommender-System-Server">Here</a>)</li>
</ul>

<hr>

<h3>How to Run</h3>
<ul><li>Clone or download this repository and open with Android Studio</li>
<li>create file called google_maps_api_key.xml in app/res/values and enter the following with your google maps api key</li></ul>

```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="google_maps_key" templateMergeStrategy="preserve"
        translatable="false">***ENTER YOUR GOOGLE MAPS API KEY HERE***</string>

</resources>
```

<ul><li>Open /app/res/values/servers.xml and enter your server IP address where specified</li>
<li>Run the application on your device or emulator</li>
<li>Follow the guides on the application for how to use</li>
</ul>

<hr>

<h3>Screenshots</h3>
