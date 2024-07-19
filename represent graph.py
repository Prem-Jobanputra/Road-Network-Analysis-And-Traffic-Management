import networkx as nx
import matplotlib.pyplot as plt
import numpy as np

filename = "citytrafficadjMatrix.txt"
with open(filename, 'r') as f:
    lines = f.readlines()
    numNodes = int(lines[0])
    cityGraph = [[int(x) for x in line.split()] for line in lines[1:]]

G = nx.Graph()

for i, line in enumerate(cityGraph):
    for j, weight in enumerate(line):
        if weight != 0:
            G.add_edge(i, j, weight=weight)

for i in range(numNodes):
    if i not in G.nodes:
        G.add_node(i)

plt.figure(figsize=(10, 8))
pos = nx.spring_layout(G, seed=42)

centroid = np.mean(np.array(list(pos.values())), axis=0)

for node in G.nodes:
    if G.degree[node] == 0:
        pos[node] = centroid + (pos[node] - centroid) * 0.1 

nx.draw_networkx_nodes(G, pos, node_size=500, node_color='lightgreen', alpha=0.8)
nx.draw_networkx_edges(G, pos, width=1.0, alpha=0.5)
nx.draw_networkx_labels(G, pos, font_size=10, font_color='black')

edge_labels = nx.get_edge_attributes(G, 'weight')
nx.draw_networkx_edge_labels(G, pos, edge_labels=edge_labels, font_color='blue', alpha=0.8, font_size=8)

plt.title('City Graph')
plt.axis('off')
plt.savefig('named_graph.png', bbox_inches='tight')
plt.show()
