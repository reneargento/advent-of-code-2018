import java.util.*;

/**
 * Created by Rene Argento on 22/12/18.
 */
public class Day22 {

    private static final int ROCKY = 0;
    private static final int WET = 1;
    private static final int NARROW = 2;

    private static final int CLIMBING_GEAR = 0;
    private static final int TORCH = 1;
    private static final int NEITHER = 2;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String[] line1 = scanner.nextLine().split(" ");
        int depth = Integer.parseInt(line1[1]);

        String[] line2 = scanner.nextLine().split(" ");
        String[] coordinates = line2[1].split(",");
        int targetRow = Integer.parseInt(coordinates[0]);
        int targetColumn = Integer.parseInt(coordinates[1]);

        int dimension = 750;
        long[][] erosionLevels = getErosionLevels(dimension, targetRow, targetColumn, depth);
        int[][] riskLevels = getRiskLevels(erosionLevels);
        long totalRiskLevel = getTotalRiskLevel(riskLevels, targetRow, targetColumn);

        System.out.println("Total risk level: " + totalRiskLevel);

        int timeToRescue = (int) getDistance(riskLevels, targetRow, targetColumn);
        System.out.println("Time to rescue: " + timeToRescue);
    }

    private static long[][] getErosionLevels(int dimension, int targetRow, int targetColumn, int depth) {
        long[][] geologicIndexes = new long[dimension][dimension];
        long[][] erosionLevels = new long[dimension][dimension];
        int mod = 20183;

        int baseCaseErosionLevel = depth % 20183;

        // Base cases
        for (int index = 1; index < geologicIndexes.length; index++) {
            geologicIndexes[index][0] = index * 16807;
            geologicIndexes[0][index] = index * 48271;

            erosionLevels[index][0] = (geologicIndexes[index][0] + depth) % mod;
            erosionLevels[0][index] = (geologicIndexes[0][index] + depth) % mod;
        }

        erosionLevels[0][0] = baseCaseErosionLevel;
        erosionLevels[targetRow][targetColumn] = baseCaseErosionLevel;

        // Get other indexes
        for (int row = 1; row < geologicIndexes.length; row++) {
            for (int column = 1; column < geologicIndexes[0].length; column++) {
                if (row == targetRow && column == targetColumn) {
                    continue;
                }

                geologicIndexes[row][column] = erosionLevels[row - 1][column] * erosionLevels[row][column - 1];
                erosionLevels[row][column] = (geologicIndexes[row][column] + depth) % 20183;
            }
        }

        return erosionLevels;
    }

    private static int[][] getRiskLevels(long[][] erosionLevels) {
        int[][] riskLevels = new int[erosionLevels.length][erosionLevels[0].length];

        for (int row = 0; row < erosionLevels.length; row++) {
            for (int column = 0; column < erosionLevels[0].length; column++) {
                riskLevels[row][column] = (int) erosionLevels[row][column] % 3;
            }
        }

        return riskLevels;
    }

    private static int getTotalRiskLevel(int[][] riskLevels, int targetRow, int targetColumn) {
        int totalRiskLevel = 0;

        for (int row = 0; row <= targetRow; row++) {
            for (int column = 0; column <= targetColumn; column++) {
                totalRiskLevel += riskLevels[row][column];
            }
        }

        return totalRiskLevel;
    }

    private static double getDistance(int[][] riskLevels, int targetRow, int targetColumn) {

        double[][][] distances = new double[riskLevels.length][riskLevels[0].length][3];

        for (int row = 0; row < riskLevels.length; row++) {
            for (int column = 0; column < riskLevels[0].length; column++) {
                for (int equipment = 0; equipment < 3; equipment++) {
                    distances[row][column][equipment] = Double.POSITIVE_INFINITY;
                }
            }
        }
        distances[0][0][TORCH] = 0;

        int[] neighborRows = {0, -1, 1, 0};
        int[] neighborColumns = {-1, 0, 0, 1};

        Queue<Cell> queue = new LinkedList<>();
        queue.offer(new Cell(0, 0, TORCH, 0));

        while (!queue.isEmpty()) {
            Cell currentCell = queue.poll();
            int row = currentCell.row;
            int column = currentCell.column;
            double distance = currentCell.distance;
            int equipment = currentCell.equipment;

            for (int neighbor = 0; neighbor < neighborRows.length; neighbor++) {
                int neighborRow = row + neighborRows[neighbor];
                int neighborColumn = column + neighborColumns[neighbor];

                if (isValidCell(neighborRow, neighborColumn, riskLevels)) {
                    double neighborDistance = 0;
                    int neighborEquipment = 0;

                    switch (riskLevels[row][column]) {
                        case ROCKY:
                            switch (riskLevels[neighborRow][neighborColumn]) {
                                case ROCKY:
                                    if (neighborRow == targetRow && neighborColumn == targetColumn) {
                                        neighborEquipment = TORCH;

                                        switch (equipment) {
                                            case TORCH:
                                                neighborDistance = 0;
                                                break;
                                            case CLIMBING_GEAR:
                                                neighborDistance = 7;
                                                break;
                                        }
                                    } else {
                                        neighborDistance = 0;
                                        neighborEquipment = equipment;
                                    }
                                    break;
                                case WET:
                                    neighborEquipment = CLIMBING_GEAR;

                                    switch (equipment) {
                                        case TORCH:
                                            neighborDistance = 7;
                                            break;
                                        case CLIMBING_GEAR:
                                            neighborDistance = 0;
                                            break;
                                    }
                                    break;
                                case NARROW:
                                    neighborEquipment = TORCH;

                                    switch (equipment) {
                                        case TORCH:
                                            neighborDistance = 0;
                                            break;
                                        case CLIMBING_GEAR:
                                            neighborDistance = 7;
                                            break;
                                    }
                                    break;
                            }
                            break;
                        case WET:
                            switch (riskLevels[neighborRow][neighborColumn]) {
                                case ROCKY:
                                    if (neighborRow == targetRow && neighborColumn == targetColumn) {
                                        continue;
                                    } else {
                                        neighborEquipment = CLIMBING_GEAR;

                                        switch (equipment) {
                                            case NEITHER:
                                                neighborDistance = 7;
                                                break;
                                            case CLIMBING_GEAR:
                                                neighborDistance = 0;
                                                break;
                                        }
                                    }
                                    break;
                                case WET:
                                    neighborDistance = 0;
                                    neighborEquipment = equipment;
                                    break;
                                case NARROW:
                                    neighborEquipment = NEITHER;

                                    switch (equipment) {
                                        case NEITHER:
                                            neighborDistance = 0;
                                            break;
                                        case CLIMBING_GEAR:
                                            neighborDistance = 7;
                                            break;
                                    }
                                    break;
                            }
                            break;
                        case NARROW:
                            switch (riskLevels[neighborRow][neighborColumn]) {
                                case ROCKY:
                                    neighborEquipment = TORCH;

                                    switch (equipment) {
                                        case TORCH:
                                            neighborDistance = 0;
                                            break;
                                        case NEITHER:
                                            neighborDistance = 7;
                                            break;
                                    }
                                    break;
                                case WET:
                                    neighborEquipment = NEITHER;

                                    switch (equipment) {
                                        case TORCH:
                                            neighborDistance = 7;
                                            break;
                                        case NEITHER:
                                            neighborDistance = 0;
                                            break;
                                    }
                                    break;
                                case NARROW:
                                    neighborDistance = 0;
                                    neighborEquipment = equipment;
                                    break;
                            }
                    }

                    // Moving cell cost
                    neighborDistance++;

                    double newDistance = distance + neighborDistance;

                    Cell neighborCell = new Cell(neighborRow, neighborColumn, neighborEquipment, newDistance);

                    if (newDistance < distances[neighborRow][neighborColumn][neighborEquipment]) {
                        queue.offer(neighborCell);
                        distances[neighborRow][neighborColumn][neighborEquipment] = newDistance;
                    }
                }
            }
        }

        double smallestDistance = Math.min(distances[targetRow][targetColumn][0], distances[targetRow][targetColumn][1]);
        smallestDistance = Math.min(smallestDistance, distances[targetRow][targetColumn][2]);

        return smallestDistance;
    }

    private static class Cell {
        int row;
        int column;
        int equipment;
        double distance;

        Cell(int row, int column, int equipment, double distance) {
            this.row = row;
            this.column = column;
            this.equipment = equipment;
            this.distance = distance;
        }
    }

    private static boolean isValidCell(int row, int column, int[][] riskLevels) {
        return row >= 0 && row < riskLevels.length && column >= 0 && column < riskLevels[0].length;
    }

}