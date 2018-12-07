import java.util.*;

/**
 * Created by Rene Argento on 05/12/18.
 */
@SuppressWarnings("unchecked")
public class Day7_2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Map<String, Integer> vertexToIdMap = new HashMap<>();
        Map<Integer, String> idToVertexMap = new HashMap<>();

        List<String> input = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] values = line.split(" ");
            String vertex1 = values[1];
            String vertex2 = values[7];

            if (!vertexToIdMap.containsKey(vertex1)) {
                int vertex1Id = vertexToIdMap.size();
                vertexToIdMap.put(vertex1, vertex1Id);
                idToVertexMap.put(vertex1Id, vertex1);
            }

            if (!vertexToIdMap.containsKey(vertex2)) {
                int vertex2Id = vertexToIdMap.size();
                vertexToIdMap.put(vertex2, vertex2Id);
                idToVertexMap.put(vertex2Id, vertex2);
            }

            input.add(line);
        }

        int vertices = vertexToIdMap.size();
        List<Integer>[] adjacent = (List<Integer>[]) new ArrayList[vertices];

        for(int i = 0; i < adjacent.length; i++) {
            adjacent[i] = new ArrayList<>();
        }

        for (String line : input) {
            String[] values = line.split(" ");
            String vertex1 = values[1];
            String vertex2 = values[7];

            int vertex1Id = vertexToIdMap.get(vertex1);
            int vertex2Id = vertexToIdMap.get(vertex2);
            adjacent[vertex1Id].add(vertex2Id);
        }

        List<Integer>[] reverseGraph = invertGraphEdges(adjacent);
        Set<Integer>[] dependencies = getDependencies(vertices, reverseGraph);

        int[] topologicalSort = khanTopologicalSort(adjacent, vertices, idToVertexMap);
        int maxTime = getMaxTime(topologicalSort, idToVertexMap, dependencies);

        System.out.println("Max time spent: " + maxTime);
    }

    private static int[] khanTopologicalSort(List <Integer> adj[], int vertices, Map<Integer, String> idToVertexMap) {
        int indegree[] = new int[vertices];

        for(int i = 0; i < vertices; i++) {
            ArrayList<Integer> temp = (ArrayList<Integer>) adj[i];
            for(int node : temp) {
                indegree[node]++;
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for(int vertex = 0; vertex < vertices; vertex++) {
            if(indegree[vertex] == 0)
                queue.add(vertex);
        }

        int[] topologicalOrder = new int[vertices];
        int topologicalOrderIndex = 0;

        while(!queue.isEmpty()) {
            int currentVertex = queue.poll();
            topologicalOrder[topologicalOrderIndex++] = currentVertex;

            for(int adjacent : adj[currentVertex]) {
                indegree[adjacent]--;

                if(indegree[adjacent] == 0) {
                    queue.add(adjacent);
                }
            }
        }

        return topologicalOrder;
    }

    private static List<Integer>[] invertGraphEdges(List<Integer>[] adjacent) {
        List<Integer>[] inverseEdges = new ArrayList[adjacent.length];

        for(int i = 0; i < inverseEdges.length; i++) {
            inverseEdges[i] = new ArrayList<>();
        }

        for(int i = 0; i < adjacent.length; i++) {
            List<Integer> neighbors = adjacent[i];

            if (neighbors != null) {
                for(int neighbor : adjacent[i]) {
                    inverseEdges[neighbor].add(i);
                }
            }
        }

        return inverseEdges;
    }

    private static Set<Integer>[] getDependencies(int vertices, List<Integer>[] reverseGraph) {
        Set<Integer>[] dependencies = new HashSet[vertices];

        for (int vertex = 0; vertex < vertices; vertex++) {
            Set<Integer> vertexDependencies = new HashSet<>();
            depthFirstSearch(vertex, reverseGraph, vertexDependencies);
            dependencies[vertex] = vertexDependencies;
        }

        return dependencies;
    }

    private static void depthFirstSearch(int sourceVertex, List<Integer>[] adjacent, Set<Integer> vertexDependencies) {
        vertexDependencies.add(sourceVertex);

        if (adjacent[sourceVertex] != null) {
            for(int neighbor : adjacent[sourceVertex]) {
                depthFirstSearch(neighbor, adjacent, vertexDependencies);
            }
        }
    }

    private static int getMaxTime(int[] topologicalSort, Map<Integer, String> idToVertexMap, Set<Integer>[] dependencies) {
        int numberOfWorkers = 5;
        Queue<Integer> workers = new PriorityQueue<>();

        for (int i = 0; i < numberOfWorkers; i++) {
            workers.add(i);
        }

        Map<Integer, Integer> completionTimes = new HashMap<>();

        for (int vertexId : topologicalSort) {
            int nextWorker = workers.poll();

            for (int dependency : dependencies[vertexId]) {
                if (dependency != vertexId) {
                    int timeFinished = completionTimes.get(dependency);

                    if (timeFinished > nextWorker) {
                        nextWorker = timeFinished;
                    }
                }
            }

            String vertex = idToVertexMap.get(vertexId);
            int timeSpent = ((int) vertex.charAt(0)) - 4;
            int updatedTime = nextWorker + timeSpent;

            completionTimes.put(vertexId, updatedTime);
            workers.offer(updatedTime);
        }

        int maxTime = 0;

        for (int timeSpent : workers) {
            if (timeSpent > maxTime) {
                maxTime = timeSpent;
            }
        }

        return maxTime;
    }

}