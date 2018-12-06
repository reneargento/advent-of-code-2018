import java.util.*;

/**
 * Created by Rene Argento on 05/12/18.
 */
public class Day6_2 {

    private static class Coordinate {
        int row;
        int column;

        Coordinate(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }

    public static void main(String[] args) {
        int dimension = 400;
        Scanner scanner = new Scanner(System.in);
        int[][] matrix = new int[dimension][dimension];
        int[][] areas = new int[dimension][dimension];
        List<Coordinate> coordinates = new ArrayList<>();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = Integer.MAX_VALUE;
            }
        }

        while (scanner.hasNext()) {
            String[] line = scanner.nextLine().split(" ");
            int column = Integer.parseInt(line[0].substring(0, line[0].length() - 1));
            int row = Integer.parseInt(line[1]);
            coordinates.add(new Coordinate(row, column));
        }

        for (Coordinate source : coordinates) {
            floodFill(matrix, source, areas);
        }

        int regionSize = computeRegionSize(coordinates, areas);
        System.out.println("Region size: " + regionSize);
    }

    private static void floodFill(int[][] matrix, Coordinate source, int[][] areas) {
        Queue<Coordinate> queue = new LinkedList<>();
        queue.offer(source);
        int sourceId = getCellId(source.row, source.column, matrix[0].length);

        int[] neighborRows = {0, -1, 1, 0};
        int[] neighborColumns = {-1, 0, 0, 1};
        boolean[] visited = new boolean[areas.length * areas[0].length];

        while (!queue.isEmpty()) {
            Coordinate currentCoordinate = queue.poll();
            int row = currentCoordinate.row;
            int column = currentCoordinate.column;

            for (int k = 0; k < 4; k++) {
                int neighborRow = row + neighborRows[k];
                int neighborColumn = column + neighborColumns[k];

                if (isValidCell(neighborRow, neighborColumn, matrix)) {
                    int neighborCellId = getCellId(neighborRow, neighborColumn, matrix[0].length);

                    if (!visited[neighborCellId]) {
                        int manhattanDistance = getManhattanDistance(source, new Coordinate(neighborRow, neighborColumn));

                        if (manhattanDistance <= matrix[neighborRow][neighborColumn]) {
                            areas[neighborRow][neighborColumn] = -1;
                            queue.offer(new Coordinate(neighborRow, neighborColumn));
                        }

                        if (manhattanDistance < matrix[neighborRow][neighborColumn]) {
                            areas[neighborRow][neighborColumn] = sourceId;
                            matrix[neighborRow][neighborColumn] = manhattanDistance;
                        }

                        visited[neighborCellId] = true;
                    }
                }
            }
        }
    }

    private static int computeRegionSize(List<Coordinate> coordinates, int[][] areas) {
        int targetDistance = 10000;
        int regionSize = 0;

        for (int row = 0; row < areas.length; row++) {
            for (int column = 0; column < areas[0].length; column++) {
                int totalDistance = 0;

                for (Coordinate coordinate : coordinates) {
                    totalDistance += getManhattanDistance(coordinate, new Coordinate(row, column));
                }

                if (totalDistance < targetDistance) {
                    regionSize++;
                }
            }
        }

        return regionSize;
    }

    private static boolean isValidCell(int row, int column, int[][] matrix) {
        return row >= 0 && row < matrix.length && column >= 0 && column < matrix[0].length;
    }

    private static int getCellId(int row, int column, int maxColumn) {
        return (row * maxColumn) + column;
    }

    private static int getManhattanDistance(Coordinate coordinate1, Coordinate coordinate2) {
        return Math.abs(coordinate1.row - coordinate2.row) + Math.abs(coordinate1.column - coordinate2.column);
    }

}