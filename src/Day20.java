import java.util.*;

/**
 * Created by Rene Argento on 20/12/18.
 */
@SuppressWarnings("unchecked")
public class Day20 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String regex = scanner.next();
        String instructions = regex.substring(0, regex.length() - 1);

        RegularExpressionMatcher regularExpressionMatcher = new RegularExpressionMatcher(instructions);
        DirectedBFS directedBFS = new DirectedBFS(regularExpressionMatcher.digraph, 0, instructions);
        int[] distances = directedBFS.distances;

        int maxDistance = 0;

        for (int i = 0; i < distances.length; i++) {
            if (distances[i] > maxDistance) {
                maxDistance = distances[i];
            }
        }

        System.out.println("\nMax distance: " + maxDistance);

        int roomsWithDistanceAtLeast1000 = 0;
        Map<Cell, Integer> distanceMap = directedBFS.distanceMap;

        for (Cell cell : distanceMap.keySet()) {
            int distance = distanceMap.get(cell);

            if (distance >= 1000) {
                roomsWithDistanceAtLeast1000++;
            }
        }

        System.out.println("Number of rooms with distance at least 1000: " + roomsWithDistanceAtLeast1000);
    }

    public static class RegularExpressionMatcher {

        private char[] regularExpression;  // Match transitions
        private Digraph digraph;           // Epsilon transitions
        private int numberOfStates;

        public RegularExpressionMatcher(String regularExpressionString) {
            // Create the nondeterministic finite automaton for the given regular expression
            Deque<Integer> operators = new ArrayDeque<>();
            regularExpression = regularExpressionString.toCharArray();
            numberOfStates = regularExpression.length;

            digraph = new Digraph(numberOfStates + 1);

            for (int i = 0; i < numberOfStates; i++) {
                if (regularExpression[i] == '(' || regularExpression[i] == '|') {
                    operators.push(i);
                } else if (regularExpression[i] == ')') {
                    handleRightParenthesis(operators, i);
                }

                digraph.addEdge(i, i + 1);
            }
        }

        private void handleRightParenthesis(Deque<Integer> operators, int index) {
            Set<Integer> orOperatorIndexes = new HashSet<>();

            while (regularExpression[operators.peek()] == '|') {
                int or = operators.pop();
                orOperatorIndexes.add(or);
            }

            int leftOperator = operators.pop();

            for (int orOperatorIndex : orOperatorIndexes) {
                digraph.addEdge(orOperatorIndex, index);
                digraph.addEdge(leftOperator, orOperatorIndex + 1);
            }
        }
    }

    public static class DirectedBFS {

        private boolean[] visited;
        private int[] distances;
        private Map<Cell, Integer> distanceMap;

        private DirectedBFS(Digraph digraph, int source, String regex) {
            visited = new boolean[digraph.vertices()];
            distances = new int[digraph.vertices];
            distanceMap = new HashMap<>();

            bfs(digraph, source, regex);

            for (int i = 0; i < regex.length(); i++) {
                System.out.printf("%3c", regex.charAt(i));
            }
            System.out.println();
            for (int i = 0; i < regex.length(); i++) {
                System.out.printf("%3d", distances[i]);
            }
        }

        private void bfs(Digraph digraph, int source, String regex) {
            Queue<Cell> parentCell = new LinkedList<>();

            Queue<Integer> queue =  new LinkedList<>();
            queue.add(source);
            distances[source] = 0;

            Cell currentCell = new Cell(0, 0);
            parentCell.offer(currentCell);
            distanceMap.put(currentCell, 0);

            while (!queue.isEmpty()) {
                int currentVertex = queue.poll();
                currentCell = parentCell.poll();

                for (int neighbor : digraph.adjacent[currentVertex]) {
                    if (!visited[neighbor]) {
                        if (neighbor >= regex.length()) {
                            break;
                        }

                        char neighborChar = regex.charAt(neighbor);

                        int nextRow = currentCell.row;
                        int nextColumn = currentCell.column;

                        if (neighborChar == 'N') {
                            nextRow = currentCell.row - 1;
                        } else if (neighborChar == 'S') {
                            nextRow = currentCell.row + 1;
                        } else if (neighborChar == 'W') {
                            nextColumn = currentCell.column - 1;
                        } else if (neighborChar == 'E') {
                            nextColumn = currentCell.column + 1;
                        }

                        Cell nextCell = new Cell(nextRow, nextColumn);

                        if (neighborChar == '('
                                || neighborChar == ')'
                                || neighborChar == '|' ) {
                            distances[neighbor] = distances[currentVertex];
                        } else if (!distanceMap.containsKey(nextCell)) {
                            distances[neighbor] = distances[currentVertex] + 1;
                        } else {
                            distances[neighbor] = distanceMap.get(nextCell);
                        }

                        distanceMap.put(nextCell, distances[neighbor]);
                        queue.add(neighbor);
                        parentCell.offer(nextCell);

                        visited[neighbor] = true;
                    }
                }
            }
        }
    }

    private static class Cell {
        int row;
        int column;

        Cell(int row, int column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public int hashCode() {
            return row * 100 + column;
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof Cell)) {
                return false;
            }

            Cell otherCell = (Cell) object;
            return row == otherCell.row && column == otherCell.column;
        }
    }

    public static class Digraph {

        private final int vertices;
        private int edges;
        private List<Integer>[] adjacent;
        private int[] indegrees;
        private int[] outdegrees;

        public Digraph(int vertices) {
            this.vertices = vertices;
            this.edges = 0;

            indegrees = new int[vertices];
            outdegrees = new int[vertices];

            adjacent = (ArrayList<Integer>[]) new ArrayList[vertices];

            for(int vertex = 0; vertex < vertices; vertex++) {
                adjacent[vertex] = new ArrayList<>();
            }
        }

        public int vertices() {
            return vertices;
        }

        public void addEdge(int vertex1, int vertex2) {
            adjacent[vertex1].add(vertex2);
            edges++;

            outdegrees[vertex1]++;
            indegrees[vertex2]++;
        }
    }

}