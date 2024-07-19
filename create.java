import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

class adjMatrix {
    int adjMatrix[][];
    int numVertices;

    public adjMatrix(int numVertices) {
        this.numVertices = numVertices;
        adjMatrix = new int[numVertices][numVertices];
    }

    public void addEdge(int i, int j, int weight) {
        adjMatrix[i][j] = weight;
        adjMatrix[j][i] = weight;
    }

    public int updateTrafficFlow(int i, int j, int flow, int flag) {
        if (i < 0 || i >= numVertices || j < 0 || j >= numVertices) {
            System.out.println("\033[1;31mInvalid source or destination node.\033[0m");
            return -1;
        }

        if (flag == 1) {
            if (adjMatrix[i][j] != 0 && adjMatrix[j][i] != 0) {
                adjMatrix[i][j] += flow;
                adjMatrix[j][i] += flow;
                return 1;
            } else {
                System.out.println("\033[1;31mEdge between " + i + " and " + j + " does not exist.\033[0m");
                return -1;
            }
        } else {
            if (adjMatrix[i][j] != 0 && adjMatrix[j][i] != 0 && adjMatrix[i][j] - flow > 0) {
                adjMatrix[i][j] -= flow;
                adjMatrix[j][i] -= flow;
                return 1;
            } else {
                System.out.println("\033[1;31mThe given operation is not possible.\033[0m");
                return -1;
            }
        }
    }

    public void printMatrix() {
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                System.out.print(adjMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }


    public int dijkstra(int src, int dest) {
        int[] dist = new int[numVertices];
        boolean[] visited = new boolean[numVertices];
        int[] parent = new int[numVertices];

        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(visited, false);
        Arrays.fill(parent, -1);

        dist[src] = 0;

        for (int count = 0; count < numVertices - 1; count++) {
            int u = minDistance(dist, visited);
            visited[u] = true;

            for (int v = 0; v < numVertices; v++) {
                if (!visited[v] && adjMatrix[u][v] != 0 && dist[u] + adjMatrix[u][v] < dist[v]) {
                    dist[v] = dist[u] + adjMatrix[u][v];
                    parent[v] = u;
                }
            }
        }

        return printShortestPath(src, dest, parent, dist);
    }

    private int printShortestPath(int src, int dest, int[] parent, int[] dist) {
        if (parent[dest] == -1) {
            System.out.println("\033[1;31mSorry!!! \nThere is no path from source " + src + " to destination " + dest + "\033[0m");
            return -1;
        }

        List<Integer> path = new ArrayList<>();
        int current = dest;
        while (current != -1) {
            path.add(current);
            current = parent[current];
        }

        System.out.print("\n\033[1;36mOptimal path from " + src + " to " + dest + ":\033[0m ");
        String filename = "optimalpath.txt";
        try (FileWriter writer = new FileWriter(filename, true)) {
            for (int i = path.size() - 1; i >= 0; i--) {
                System.out.print(path.get(i));
                writer.write(String.valueOf(path.get(i)));
                if (i != 0) {
                    System.out.print(" -> ");
                    writer.write(" -> ");
                }
            }
            writer.write("\n");
            System.out.println();
            System.out.println("\033[1;36mMinimum distance from " + src + " to " + dest + ": " + dist[dest] + " kms\033[0m");
            writer.write("Minimum distance from " + src + " to " + dest + ": " + dist[dest] + " kms");
            writer.write("\n\n");
            return dist[dest];
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private int minDistance(int[] dist, boolean[] visited) {
        int min = Integer.MAX_VALUE;
        int minIndex = -1;

        for (int v = 0; v < numVertices; v++) {
            if (!visited[v] && dist[v] <= min) {
                min = dist[v];
                minIndex = v;
            }
        }

        return minIndex;
    }

    private static boolean bfs(int[][] radjMatrix, int s, int t, int[] parent) {
        boolean[] visited = new boolean[radjMatrix.length];
        Queue<Integer> q = new LinkedList<Integer>();
        q.add(s);
        visited[s] = true;
        parent[s] = -1;
        while (!q.isEmpty()) {
            int v = q.poll();
            for (int i = 0; i < radjMatrix.length; i++) {
                if (radjMatrix[v][i] > 0 && !visited[i]) {
                    q.offer(i);
                    visited[i] = true;
                    parent[i] = v;
                }
            }
        }
        return (visited[t] == true);
    }

    private static void dfs(int[][] radjMatrix, int s,
            boolean[] visited) {
        visited[s] = true;
        for (int i = 0; i < radjMatrix.length; i++) {
            if (radjMatrix[s][i] > 0 && !visited[i]) {
                dfs(radjMatrix, i, visited);
            }
        }
    }

    public void minCut(int s, int t) {
        int u, v;
        int[][] radjMatrix = new int[numVertices][numVertices];
        for (int i = 0; i < adjMatrix[0].length; i++) {
            for (int j = 0; j < adjMatrix[0].length; j++) {
                radjMatrix[i][j] = adjMatrix[i][j];
            }
        }
        int[] parent = new int[adjMatrix.length];
        while (bfs(radjMatrix, s, t, parent)) {
            int pathFlow = Integer.MAX_VALUE;
            for (v = t; v != s; v = parent[v]) {
                u = parent[v];
                pathFlow = Math.min(pathFlow, radjMatrix[u][v]);
            }
            for (v = t; v != s; v = parent[v]) {
                u = parent[v];
                pathFlow = Math.min(pathFlow, radjMatrix[u][v]);
            }

            for (v = t; v != s; v = parent[v]) {
                u = parent[v];
                radjMatrix[u][v] = radjMatrix[u][v] - pathFlow;
                radjMatrix[v][u] = radjMatrix[v][u] + pathFlow;
            }
        }
        boolean[] isVisited = new boolean[adjMatrix.length];
        dfs(radjMatrix, s, isVisited);
        int mn = 0;
        for (int i = 0; i < adjMatrix.length; i++) {
            for (int j = 0; j < adjMatrix.length; j++) {
                if (adjMatrix[i][j] > 0 && isVisited[i] && !isVisited[j]) {
                    System.out.println(i + " - " + j);
                    mn++;
                }
            }
        }
        System.out.println(
                "\033[1;36mNumber of critical road segments allows city planners to prioritize maintenance, repair, and construction projects:"
                        + mn + "\033[0m");
    }
    
    public void addIsolatedVertexWithRandomWeights() {
        this.numVertices++;
        int[][] newAdjMatrix = new int[numVertices][numVertices];
        for (int i = 0; i < adjMatrix.length; i++) {
            System.arraycopy(adjMatrix[i], 0, newAdjMatrix[i], 0, adjMatrix[i].length);
        }
        this.adjMatrix = newAdjMatrix;
    }


    private void dfs(int v, boolean[] visited) {
        visited[v] = true;
        for (int i = 0; i < numVertices; i++) {
            if (adjMatrix[v][i] != 0 && !visited[i]) {
                dfs(i, visited);
            }
        }
    }

    public boolean isConnected() {
        boolean[] visited = new boolean[numVertices];
        dfs(0, visited); 
        for (boolean v : visited) {
            if (!v) {
                return false;
            }
        }
        return true;
    }

    public void makeRouteBetweenIsolatedVertices(int routeMake, int numIsolatedVertices) {
        if (routeMake == 1) {
            for (int i = numVertices - numIsolatedVertices; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    if (i != j && adjMatrix[i][j] == 0) {
                        int weight = (int) (Math.random() * 10) + 1; 
                        adjMatrix[i][j] = weight;
                        adjMatrix[j][i] = weight;
                    }
                }
            }
            
            for (int i = 0; i < numVertices - numIsolatedVertices; i++) {
                for (int j = numVertices - numIsolatedVertices; j < numVertices; j++) {
                    if (adjMatrix[i][j] == 0) {
                        int weight = (int) (Math.random() * 10) + 1; 
                        adjMatrix[i][j] = weight;
                        adjMatrix[j][i] = weight;
                    }
                }
            }
            
            System.out.println("\033[1;32mRoutes added for all isolated vertices.\033[0m");
        } else {
            System.out.println("\033[1;31mNo routes added for isolated vertices.\033[0m");
        }
        create.storeadjMatrix(adjMatrix, "cityadjMatrix.txt");
    }

    static void dfs1(int[][] adjMatrix, int v, Set<List<Integer>> ans, List<Integer> path) {
        for (int i = 0; i < adjMatrix[v].length; i++) {
            if (adjMatrix[v][i] > 0) {
                adjMatrix[v][i]--;
                adjMatrix[i][v]--;
                dfs1(adjMatrix, i, ans, path);
            }
        }
        path.add(v);
    }

    void findEulerianPaths() {
        int n = numVertices;
        int[] degree = new int[n];
        for (int i = 0; i < n; ++i) {
            int count = 0;
            for (int j = 0; j < n; ++j)
                count += adjMatrix[i][j];
            degree[i] = count;
            if (count % 2 == 1) {
                System.out.println("\033[1;31mIt is not an Euler adjMatrix\033[0m");
                return;
            }
        }
        
        Set<List<Integer>> ans = new HashSet<>();
        for (int i = 0; i < n; ++i) {
            while (degree[i] > 0) {
                List<Integer> path = new ArrayList<>();
                dfs1(adjMatrix, i, ans, path);
                degree[i] -= 2;
            }
        }
        for (List<Integer> i : ans) {
            for (int j : i)
                System.out.print(j + " ");
            System.out.println();
        }
    }
}

public class create {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            int minVertices = 10;
            int maxVertices = 15;
            int numVertices = (int) (Math.random() * (maxVertices - minVertices + 1)) + minVertices;
            adjMatrix g = new adjMatrix(numVertices);
            double density = 0.12 + Math.random() * 0.1;
            int minEdges = (int) (0.3 * numVertices * (numVertices - 1) / 2);
            int maxEdges = (int) (0.7 * numVertices * (numVertices - 1) / 2);
            int numEdges = (int) (density * (maxEdges - minEdges) + minEdges);
            int edgeCount = 0;
            while (edgeCount < numEdges) {
                int vertex1 = (int) (Math.random() * numVertices);
                int vertex2 = (int) (Math.random() * numVertices);
                int weight = (int) (Math.random() * 10) + 1;
                if (vertex1 != vertex2 && g.adjMatrix[vertex1][vertex2] == 0) {
                    g.addEdge(vertex1, vertex2, weight);
                    edgeCount++;
                }
            }
            int numIsolatedVertices =(int) (Math.random()*5)+1; 
            System.out.println("The isolated cities are "+numIsolatedVertices);
            for (int i = 0; i < numIsolatedVertices; i++) {
                g.addIsolatedVertexWithRandomWeights();
            }
            System.out.println("Generated adjMatrix:");
            g.printMatrix();
            storeadjMatrix(g.adjMatrix, "cityadjMatrix.txt");
            initializeOptimalPathFile("optimalpath.txt");
            while (true) {
                System.out.print("\033[H\033[2J");
                System.out.flush();
                System.out.println("\n\033[1;36mWelcome to the Matrix Analytics Menu:\033[0m");
                System.out.println("\033[1;33m=====================================\033[0m");
                System.out.println("\033[1;32m1. Find the shortest path from source to destination\033[0m");
                System.out.println("\033[1;32m2. Flow Control\033[0m");
                System.out.println("\033[1;32m3. Determine the number of critical paths\033[0m");
                System.out.println("\033[1;32m4. Print the cities\033[0m");
                System.out.println("\033[1;32m5. Check connectivities of road\033[0m");
                System.out.println("\033[1;32m6. Find Eulerian Path/Circuit\033[0m");
                System.out.println("\033[1;31m7. Exit the Application\033[0m");
                System.out.println("\033[1;33m=====================================\033[0m");
                System.out.print("\033[1;34mPlease enter your choice (1, 2, 3, 4, 5, 6 or 7):\033[0m ");
                int menu = scanner.nextInt();
                if (menu == 1) {
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    System.out.println("\nDestinations are " + 0 + " - " + (numVertices - 1));
                    System.out.println("\nPlease enter the source and destination nodes:");
                    System.out.print("Source node: ");
                    int src = scanner.nextInt();
                    System.out.print("Destination node: ");
                    int dest = scanner.nextInt();
                    g.dijkstra(src, dest);
                    System.out.println("1. Continue to Menu");
                    System.out.println("2. Exit Application");
                    char o = scanner.next().charAt(0);
                    if (o == '2')
                        return;
                    else
                        continue;
                } else if (menu == 2) {
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    int tc = 1;
                    do {
                        System.out.println("\nDestinations are " + 0 + " - " + (numVertices - 1));
                        System.out.println("\nEnter the source and destinations to change traffic there:");
                        System.out.print("Source node: ");
                        int src = scanner.nextInt();
                        System.out.print("Destination node: ");
                        int dest = scanner.nextInt();
                        int flag = 0;
                        System.out.println("\nchoose \n1.Increment in traffic \n2.Decrement in traffic ");
                        System.out.print("Enter your choice: ");
                        flag = scanner.nextInt();
                        System.out.print("\nEnter traffic flow value: ");
                        int flow = scanner.nextInt();
                        int temp = g.updateTrafficFlow(src, dest, flow, flag);
                        int fp = 0;
                        if (temp == 1) {
                            System.out.println("\nDo you want to find new optimal path?[Y->1 & N->0]");
                            fp = scanner.nextInt();
                            if (fp == 1) {
                                System.out.println("--> Find new optimal path between source and destination");
                                System.out.print("Enter source node: ");
                                src = scanner.nextInt();
                                System.out.print("Enter destination node: ");
                                dest = scanner.nextInt();
                                g.dijkstra(src, dest);
                                storeadjMatrix(g.adjMatrix, "citytrafficadjMatrix.txt");
                            }
                        }
                        System.out.println("\nDo you want to update traffic?[Y->1 & N->0]");
                        tc = scanner.nextInt();
                    } while (tc != 0);
                    System.out.println("1. Continue to Menu");
                    System.out.println("2. Exit Application");
                    char o = scanner.next().charAt(0);
                    if (o == '2')
                        return;
                    else
                        continue;
                } else if (menu == 3) {
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    System.out.println("\nEnter the source and destinations to find cut set between:");
                    System.out.print("Source node: ");
                    int src = scanner.nextInt();
                    System.out.print("Destination node: ");
                    int dest = scanner.nextInt();
                    g.minCut(src, dest);
                    System.out.println("1. Continue to Menu");
                    System.out.println("2. Exit Application");
                    char o = scanner.next().charAt(0);
                    if (o == '2')
                        return;
                    else
                        continue;
                } else if (menu == 4) {
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    System.out.println("Adjacency Matrix for city routes\n");
                    g.printMatrix();
                    System.out.println("\n1. Continue to Menu");
                    System.out.println("2. Exit Application");
                    char o = scanner.next().charAt(0);
                    if (o == '2')
                        return;
                    else
                        continue;
                } 
                else if(menu == 5){
                    if (!g.isConnected()) {
                        System.out.println("The adjMatrix is not connected.");
                        System.out.println("There are "+numIsolatedVertices+" isolated vertices. It means there is no route.");
                        System.out.println("Do you want to add route to these isolated cities?[Y-1|N-0]");
                        int routeMake = scanner.nextInt();
                        g.makeRouteBetweenIsolatedVertices(routeMake,numIsolatedVertices);
                    }
                    System.out.println("\n1. Continue to Menu");
                    System.out.println("2. Exit Application");
                    char o = scanner.next().charAt(0);
                    if (o == '2')
                        return;
                    else
                        continue;
                }
                else if(menu==6){
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    g.findEulerianPaths();
                    System.out.println("1. Continue to Menu");
                    System.out.println("2. Exit Application");
                    char o = scanner.next().charAt(0);
                    if (o == '2')
                        return;
                    else
                        continue;
                }
                else if (menu == 7) {
                    System.out.println("\033[1;33mThank you for using the adjMatrix Analytics Application!\033[0m");
                    return;
                }else {
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    System.out.println("\033[1;31mInvalid choice. Please choose a valid option.\033[0m");
                    System.out.println("\033[1;34m1. Continue to Menu\033[0m");
                    System.out.println("\033[1;34m2. Exit Application\033[0m");
                    System.out.print("\033[1;34mEnter your choice (1 or 2):\033[0m ");
                    
                    char o = scanner.next().charAt(0);
                    if (o == '2')
                        return;
                    else
                        continue;
                }
            }
        }
    }

    public static void storeadjMatrix(int[][] cityadjMatrix, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            int numNodes = cityadjMatrix.length;
            writer.write(numNodes + "\n");
            for (int i = 0; i < numNodes; i++) {
                for (int j = 0; j < numNodes; j++) {
                    writer.write(cityadjMatrix[i][j] + " ");
                }
                writer.write("\n");
            }
            System.out.println("adjMatrix has been stored successfully.");
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initializeOptimalPathFile(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}