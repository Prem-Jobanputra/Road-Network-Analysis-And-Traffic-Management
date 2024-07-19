import networkx as nx
import matplotlib.pyplot as plt
import cartopy.crs as ccrs
import cartopy.feature as cfeature

# Load the matrix from the file
filename = "citytrafficadjMatrix.txt"
with open(filename, 'r') as f:
    lines = f.readlines()
    numNodes = int(lines[0])
    cityGraph = [[int(x) for x in line.split()] for line in lines[1:]]

# Define cities and their coordinates
cities = {
    "Mumbai": (19.0760, 72.8777),
    "Delhi": (28.7041, 77.1025),
    "Bangalore": (12.9716, 77.5946),
    "Kolkata": (22.5726, 88.3639),
    "Chennai": (13.0827, 80.2707),
    "Hyderabad": (17.3850, 78.4867),
    "Ahmedabad": (23.0225, 72.5714),
    "Pune": (18.5204, 73.8567),
    "Jaipur": (26.9124, 75.7873),
    "Lucknow": (26.8467, 80.9462),
    "Kanpur": (26.4499, 80.3319),
    "Nagpur": (21.1458, 79.0882),
    "Patna": (25.5941, 85.1376),
    "Indore": (22.7196, 75.8577),
    "Bhopal": (23.2599, 77.4126),
    "Visakhapatnam": (17.6868, 83.2185),
    "Ludhiana": (30.9010, 75.8573),
    "Agra": (27.1767, 78.0081),
    "Nashik": (20.0059, 73.7900),
    "Meerut": (28.9845, 77.7064),
    "Guwahati": (26.1445, 91.7362),
    "Jodhpur": (26.2389, 73.0243),
    "Srinagar": (34.0837, 74.7973),
    "Thiruvananthapuram": (8.5241, 76.9366),
    "Shimla": (31.1048, 77.1734),
    "Gandhinagar": (23.2156, 72.6369),
    "Gangtok": (27.3314, 88.6138),
    "Kohima": (25.6751, 94.1086),
    "Itanagar": (27.0844, 93.6053)
}

# Create a graph
G = nx.Graph()

# Add nodes to the graph and assign cities
for i, city in enumerate(cities):
    lat, lon = cities[city]
    G.add_node(city, pos=(lon, lat))

# Add edges to the graph
for i in range(numNodes):
    for j in range(i + 1, numNodes):
        weight = cityGraph[i][j]
        if weight != 0:
            G.add_edge(list(cities.keys())[i],
                       list(cities.keys())[j], weight=weight)

# Plot the map
plt.figure(figsize=(12,10))
ax = plt.axes(projection=ccrs.PlateCarree())
ax.set_extent([65, 100, 5, 40], crs=ccrs.PlateCarree())
# Add coastlines and features
ax.coastlines(resolution='10m', color='black', linewidth=1)
ax.add_feature(cfeature.BORDERS, linewidth=1, edgecolor='black')
ax.add_feature(cfeature.LAND, color='lightgray')
ax.add_feature(cfeature.OCEAN, color='lightblue')

# Draw the graph with smaller node size for better visibility
pos = nx.get_node_attributes(G, 'pos')
nx.draw(G, pos, with_labels=True, node_size=100, node_color='lightgreen',
        font_size=7, font_color='black')

# Add city markers
for city, (lon, lat) in cities.items():
    ax.plot(lon, lat, marker='o', markersize=4, color='red',
            transform=ccrs.PlateCarree())

# Add edge labels
edge_labels = nx.get_edge_attributes(G, 'weight')
nx.draw_networkx_edge_labels(G, pos, edge_labels=edge_labels,
        font_color='red', font_size=7, bbox=dict(facecolor='none',
                                edgecolor='none', alpha=0.7))

plt.title('City Graph on India Map')
plt.savefig('city_graph_map.png', bbox_inches='tight')
plt.show()
