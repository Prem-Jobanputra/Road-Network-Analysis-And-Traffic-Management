# Road Network Analysis and Traffic Management

This application is designed to perform various analytics operations on a city road network represented as an adjacency matrix. It includes functionalities such as finding the shortest path between two cities, controlling traffic flow, determining critical paths, checking connectivity, and finding Eulerian paths/circuits.

Table of Contents
Usage
Dependencies
Functionality
Java Implementation
Python Implementation
Usage
To run the Matrix Analytics Application, follow these steps:

Clone this repository to your local machine.
Ensure you have Java and Python installed.
Compile and run the create.java file to execute the Java program.
Ensure the cityadjMatrix.txt file is in the same directory as the Java program.
Follow the on-screen instructions to interact with the program.
Dependencies
Java JDK
Python 3.x
NetworkX (Python library)
Matplotlib (Python library)
Cartopy (Python library)
You can install Python dependencies using pip:

pip install networkx matplotlib cartopy
Functionality
Shortest Path: Find the shortest path between two cities using Dijkstra's algorithm.
Flow Control: Control traffic flow by incrementing or decrementing traffic between cities.
Critical Paths: Determine critical road segments to prioritize maintenance, repair, and construction projects.
Connectivity Check: Check if the road network is connected and add routes between isolated cities if needed.
Eulerian Paths/Circuits: Find Eulerian paths or circuits in the road network.
Java Implementation
The Java implementation includes classes for handling the adjacency matrix, finding paths, and controlling traffic flow. It provides a command-line interface for user interaction.

Python Implementation
The Python implementation visualizes the city graph on an India map using NetworkX and Matplotlib. It reads the adjacency matrix from the citytrafficadjMatrix.txt file and plots the cities and edges on the map.
