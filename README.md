# SLAMS: Self Learning Automated Monitoring System
![Image of Yaktocat](https://raw.githubusercontent.com/amitmanchanda1995/SMARTHOME/master/Images/slams.png)


##### Abstract
SLAMS is a smarthome system where we try to make the place smarter. You can view all the reading anytime, check when your family members left the house. Helps you keep the house alive. 

##### Introduction
The need of today world is a home that can learn from one. One could be able to control it from anywhere, tells us about any unauthorised entry and help us tackle emergency situations. It should learn from users habits. It should be a place which understands the user and is always ready on one’s command. It should touch every aspect of human life

##### Concept
Most of the smarthome requires change in the infrastructure. Therefore it is not possible to install it in an existing building. But our product is a standalone device. It only needs is a black box to be attached on the side of the switchboard which you need to make smart. It contains the receiver and transmitter inside the box. Similar boxes have to be installed near doors for one's’ safety. The project comes under the category of IoT. It is a network of physical objects embedded with software, sensors, circuits and network connectivity.

##### About project and objectives
SLAMS stand for self learning automated monitoring system, designed and developed to help people have an easy access to their homes irrespective of the position. It has also incorporated the needs the old people who aren’t familiar with technology. It is automatic, semi automatic and manual control and monitoring of all the household appliances and residential features like security, health care, fire etc. The basic aim is to securely connect everything in home to internet.

### SLAMS
##### Self-learning
The system learns and adapts to user’s habits over time.
##### Automated
Various things and appliances in home are controlled using web portal and mobile application.
##### Monitoring
Things like doors, air quality, temperature etc. can be monitored using a system comprised of variety of sensors.

### Features:
##### Secure Login
It has a secure login page with username and password for each user to  ensures that unauthorized access does not take place and unknown person does not take control of the house. 
##### Automatic/Manual Transition
On/off switch control for those people not familiar with technologies like grandparents. For them one could just place the home back to Manual mode and use it in a traditional way.
##### Control and Secure Remote Access
The states of all appliances can be accessed and controlled remotely.
##### Door Security System
Secure mode can be turned on when the user goes out and notification is received when someone else trespasses the property in that mode.
##### Automatic Curtains
Curtains can be opened and closed using mobile app and web portal. Also the curtains will open themselves when sunlight falls on the LDR sensor attached to
window.
##### Fire Alarm
When temperature follows unusually abrupt pattern e.g. rise in temperature at high rate, then a notification is sent to the user along with the loud ringing of the buzzer.
##### Air quality sensing
The levels of many gases are analyzed against their global levels and user is notified in case of significant difference.
##### Temperature and humidity
The temperature and humidity in the house is continuously monitored and are used for Artificial Intelligence.
##### Artificial Intelligent System
The system uses machine learning to learn the habits of the users in home. After a certain time, enough learning occurs to suggest various settings to the users (e.g. speed of the fan).
##### Automated Servers
If the power disrupts, the server restarts from the same state that it was previously at. It saves the latest data it receives in the database(MySQL) and restores it when it regains power.
##### Web security
Whenever a device is connected through Wi-Fi, its mac address is noted and saved. The user can mark familiar devices and can block any mac address as needed. System shows last seen time for all devices. Online/offline status of currently connected devices is also shown.
##### SOS button
SOS button is the International Morse Code distress signal. On pressing this button, all the other users will get a notification informing about the situation of emergency. Notification is of the format: “Amit needs help at (‘latitude’, ‘longitude’)”. Modern apps such as Apple Maps and Google Maps can visually show the exact location using coordinates.
##### Modes
Modes could be selected at random to control the appliances together by a single press instead of pressing many buttons to do the stuff.

### Uniqueness
Our product helps to monitor the amount of some gases, and provide the real time data of the same. If the amount goes beyond a particular value then the user is notified with the notification. We provide modes (Morning, Night, Theater, etc) which helps to control a set of appliances together instead of pressing each button one by one. We have also incorporated fire alarm for the safety and are using Artificial Intelligence to control the speed of the fan based on temperature humidity and the past person's preferences.

### Circuit Diagram
![Circuit Diagram](https://raw.githubusercontent.com/amitmanchanda1995/SMARTHOME/master/Images/circuit.png)

#### Technical Stuff
- LM35: Used for fire alarm
- MQ135: Used for sensing concentration of CO2 gas
- LDR: Used to check the light intensity for automatic curtains
- Zigbees: Used for communications among the devices.
- Raspberry Pi: Used to set up the server and control things
- Ultrasonic Sensors: embodied as door security
- Optocouplers: provides isolation between LV and HV circuits.
- TriAC: used as switch to control AC appliances

### Questions
- Why used optocoupler and triac instead of relay ?
Ready-made AC relays are available in market. One can use those instead of using TriAC and optocoupler. Technically both are same  but  by using triac and optocoupler as relay  reduces our price of prototype significantly.
