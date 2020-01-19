
# Welcome to our upgraded Pac-man project
Moving robots along 23 different maps,
inorder to pick as many fruits as possible.

# Starting windows
Here we can choose if we want to play, or just watch the autogame.

<a href="http://www.siz.co.il/"><img src="http://up419.siz.co.il/up2/ljz5ydu2yot3.png" border="0" alt="starting window" /></a>

Choosing map:
<a href="http://www.siz.co.il/"><img src="http://up419.siz.co.il/up3/mrmmvimzjmtw.png" border="0" alt="map" /></a>


# Introduction:
Our game containes 23 difference maps, and in each map you got different number of robots, fruits and time of ending. 
# Description: 
Your characters: difference number of robot in each map.

Your goal is to eat fruits as many as possible, inorder to get highest score by eating them in limited time.

Example of the first Map:

<a href="http://www.siz.co.il/"><img src="http://up419.siz.co.il/up3/zmjjmmtwmjnk.png" border="0" alt="example" /></a>

Example of map 11:

<a href="http://www.siz.co.il/"><img src="http://up419.siz.co.il/up3/2gmtzwymqzyy.png" border="0" alt="10" /></a>

# GameClient Package:

## simplegameclient:

a very short and simple of the game without gui, using the GameServer API.


## MyGameGUI:

The Class MyGameGUI, that including the Manual gaming option

and a link to the Auto gaming option (that inside the class AutoGame).

the class using Thread, and including a constructors, and functions as startGame, run (Thread Override), updateRobots,

updateFruits, placing the robot using mouth listener in mouthPressed and action listen, scale for correcting the graph to the window, getNumOfRobots, moveRobots, AllRobotFree, initMinMax, drawFruit, drawRobots,

update(for double buffering),paint ,DrawEdges, DrawVertex, Getters and Setters.


## ClockThread: 

Writen inorder to count the time of the game and label it on the frame.


## AutoGame:

The class AutoGame is the Auto option class of the game.

the class including functions as startGame, run (Thread Override), firstplaceRobots, moveRobot and getNextNode.

this class uses MyGameGUI class as basis.

## KML_Logger:

KML is a file format used to display geographic data in an Earth browser such as Google Earth.

You can create KML files to pinpoint locations and add image overlays.

we make an option to make that file from the game, and locate the game in Google Earth.

# Elements:

## RobotInterface:

The interface RobotInterface, represent a character that init from Json file, in a String format.

the interface support at Robot list with ArrayList.


## Robot:

The class Robot represent a character of a robot.

The class implements the Interface, and realize all the functions.

in addition, Robot class have a constructor, update and draw function, getters and setters.


## FruitInterface:

The interface FruitInterface, represent an object that init from Json file, in a String format.

the interface support at Fruit list with ArrayList, and Getter for the Edge that the fruit on it.


## Fruit:

The class Fruit represent an object of a fruit(Apple or Banana).

The class implements the Interface, and realize all the functions.

in addition, Fruit class have a constructors, update and draw function, getEdge function, getters and setters.

* We use JFrame in order to implement the GUI classes.
