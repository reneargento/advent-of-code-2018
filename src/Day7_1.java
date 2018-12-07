import java.util.*;

/**
 * Created by Rene Argento on 05/12/18.
 */
@SuppressWarnings("unchecked")
public class Day7_1 {

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

        int[] topologicalSort = khanTopologicalSort(adjacent, vertices, idToVertexMap);

        for(int vertexId : topologicalSort) {
            String vertex = idToVertexMap.get(vertexId);
            System.out.print(vertex);
        }
    }

    private static int[] khanTopologicalSort(List <Integer> adj[], int vertices, Map<Integer, String> idToVertexMap) {
        int indegree[] = new int[vertices];

        for(int i = 0; i < vertices; i++) {
            ArrayList<Integer> temp = (ArrayList<Integer>) adj[i];
            for(int node : temp) {
                indegree[node]++;
            }
        }

        Queue<Integer> priorityQueue = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer vertexId1, Integer vertexId2) {
                String vertex1 = idToVertexMap.get(vertexId1);
                String vertex2 = idToVertexMap.get(vertexId2);
                return vertex1.compareTo(vertex2);
            }
        });
        for(int vertex = 0; vertex < vertices; vertex++) {
            if(indegree[vertex] == 0)
                priorityQueue.add(vertex);
        }

        int[] topologicalOrder = new int[vertices];
        int topologicalOrderIndex = 0;

        while(!priorityQueue.isEmpty()) {
            int currentVertex = priorityQueue.poll();
            topologicalOrder[topologicalOrderIndex++] = currentVertex;

            for(int adjacent : adj[currentVertex]) {
                indegree[adjacent]--;

                if(indegree[adjacent] == 0) {
                    priorityQueue.add(adjacent);
                }
            }
        }

        return topologicalOrder;
    }
}