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

        int regionSize = computeRegionSize(coordinates, areas);
        System.out.println("Region size: " + regionSize);
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

    private static int getManhattanDistance(Coordinate coordinate1, Coordinate coordinate2) {
        return Math.abs(coordinate1.row - coordinate2.row) + Math.abs(coordinate1.column - coordinate2.column);
    }

}