# Wifi Lookup

This project takes the output from a Windows WLAN-listing, queries [wigle.net](https://wigle.net/) and generates a KML-file with the found positions of the BSSIDs from the WLAN-listing.

Usage:
```
netsh wlan show all > wlans.txt
java -jar wifi-lookup-0.0.4-SNAPSHOT-jar-with-dependencies.jar wlans.txt
```
The command `netsh wlan show all` is to be performed on a Windows-machine. The `java -jar ...` command can of course be 
executed on any operating system, but you need to be connected to the internet as the binary queries wigle.net.<br/>

Output: a KML-file to be opened in e.g. Google Earth.

## Development
### Prerequisites
To be able to query the Wigle-API, you need a Wigle-user and the user's BASE64-encoded API-token, see [src/main/resources/secret-sample.properties](src/main/resources/secret-sample.properties)

You also need the following:
```
Java version 13+
Maven version 3+
```

### Execution
The executable file is found under `target\wifi-lookup-x.y.z-SNAPSHOT-jar-with-dependencies.jar`

### Installing
To run the tests and build the project , use
```
mvn clean install 
```

### Contributing
Contributions are of course welcome. If you develop new features or fix bugs, **be sure to write tests!** Tests are based on the Spock Framework and should be self-explanatory.   

## Caveat emptor
- Date formats: not all date formats as found in the Windows WLAN-listing are supported. If not supported, the 'discoveryDate' of the WLAN is set to 'now'. May not have any practical significance
- Wigle quota. Users are limited to a small number of queries a day

## Changelog
### [0.0.4] - 2020-10-27
- update dependencies, java version 13
### [0.0.3] - 2020-01-29
- make the project not dependent on an offline library
### [0.0.2] - 2019-11-04
- rename project from wigle-scanner to wifi-lookup
### [0.0.1] - 2019-10-25
- initial release

## Support
If stuck, ask pjwalstrom@gmail.com for support.

