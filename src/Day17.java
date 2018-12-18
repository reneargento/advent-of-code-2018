import java.util.*;

/**
 * Created by Rene Argento on 05/17/18.
 */
public class Day17 {

    private static class Coordinate {
        int row;
        int column;

        Coordinate(int row, int column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof Coordinate)) {
                return false;
            }
            Coordinate otherCoordinate = (Coordinate) other;
            return row == otherCoordinate.row && column == otherCoordinate.column;
        }

        @Override
        public int hashCode() {
            return row * 10000 + column;
        }
    }

    private static final int SPRING_ROW = 0;
    private static final int SPRING_COLUMN = 500;

    public static void main(String[] args) {
        int dimension = 2000;
        Scanner scanner = new Scanner(System.in);
        char[][] matrix = new char[dimension][dimension];
        int smallestRow = Integer.MAX_VALUE;
        int largestRow = 0;
        initMatrix(matrix);

        while (scanner.hasNext()) {
            String[] line = scanner.nextLine().split(" ");
            int dotsIndex = line[1].indexOf('.');

            if (line[0].startsWith("x")) {
                int column = Integer.parseInt(line[0].substring(2, line[0].length() - 1));
                int rowStart = Integer.parseInt(line[1].substring(2, dotsIndex));
                int rowEnd = Integer.parseInt(line[1].substring(dotsIndex + 2));

                for (int row = rowStart; row <= rowEnd; row++) {
                    matrix[row][column] = '#';
                }

                if (rowStart < smallestRow) {
                    smallestRow = rowStart;
                }
                if (rowEnd > largestRow) {
                    largestRow = rowEnd;
                }
            } else {
                int row = Integer.parseInt(line[0].substring(2, line[0].length() - 1));
                int columnStart = Integer.parseInt(line[1].substring(2, dotsIndex));
                int columnEnd = Integer.parseInt(line[1].substring(dotsIndex + 2));

                for (int column = columnStart; column <= columnEnd; column++) {
                    matrix[row][column] = '#';
                }

                if (row < smallestRow) {
                    smallestRow = row;
                }
                if (row > largestRow) {
                    largestRow = row;
                }
            }
        }

        matrix[SPRING_ROW][SPRING_COLUMN] = '+';
        Coordinate source = new Coordinate(0, 500);

        while (floodFill(matrix, source)) { }
        printMatrix(matrix);

        int[] waterTiles = countWaterCells(matrix, smallestRow, largestRow);
        System.out.println("Tiles water can reach: " + waterTiles[0]);
        System.out.println("Water tiles retained: " + waterTiles[1]);
    }

    private static boolean floodFill(char[][] matrix, Coordinate source) {
        Queue<Coordinate> queue = new LinkedList<>();
        queue.offer(source);
        Map<Integer, Integer> rowToLeftWall = new HashMap<>();
        Map<Integer, Integer> rowToRightWall = new HashMap<>();
        boolean filledNewSpace = false;
        Set<Integer> visited = new HashSet<>();

        while (!queue.isEmpty()) {
            Coordinate currentCoordinate = queue.poll();
            int row = currentCoordinate.row;
            int column = currentCoordinate.column;

            if (row != SPRING_ROW || column != SPRING_COLUMN) {
                if (matrix[row][column] != '|') {
                    matrix[row][column] = '|';
                    filledNewSpace = true;
                }
            }

            int lowerRow = row + 1;
            int leftColumn = column - 1;
            int rightColumn = column + 1;

            if (isValidCoordinate(lowerRow, column, matrix)) {
                char underCell = matrix[lowerRow][column];
                int lowerCellId = getCellId(lowerRow, column, matrix);

                if (underCell != '#' && underCell != '~' && !visited.contains(lowerCellId)) {
                    // Go down
                    queue.offer(new Coordinate(lowerRow, column));
                    visited.add(lowerCellId);
                } else {
                    if (underCell == '|') {
                        continue;
                    }

                    // Go left
                    if (isValidCoordinate(row, leftColumn, matrix)) {
                        char leftCell = matrix[row][leftColumn];
                        int leftCellId = getCellId(row, leftColumn, matrix);

                        if (leftCell != '#' && !visited.contains(leftCellId)) {
                            queue.offer(new Coordinate(row, leftColumn));
                            visited.add(leftCellId);
                        } else if (leftCell == '#') {
                            rowToLeftWall.put(row, column);

                            if (rowToRightWall.containsKey(row)) {
                                int rightIndex = rowToRightWall.get(row);

                                if (shouldFill(row, column, rightIndex, matrix)) {
                                    for (int index = column; index <= rightIndex; index++) {
                                        if (matrix[row][index] == '#') {
                                            break;
                                        }

                                        matrix[row][index] = '~';
                                        filledNewSpace = true;
                                    }
                                }
                            }
                        }
                    }

                    // Go right
                    if (isValidCoordinate(row, rightColumn, matrix)) {
                        char rightCell = matrix[row][rightColumn];
                        int rightCellId = getCellId(row, rightColumn, matrix);

                        if (rightCell != '#' && !visited.contains(rightCellId)) {
                            queue.offer(new Coordinate(row, rightColumn));
                            visited.add(rightCellId);
                        } else if (rightCell == '#') {
                            rowToRightWall.put(row, column);

                            if (rowToLeftWall.containsKey(row)) {
                                int leftIndex = rowToLeftWall.get(row);

                                if (shouldFill(row, leftIndex, column, matrix)) {
                                    for (int index = column; index >= leftIndex; index--) {
                                        if (matrix[row][index] == '#') {
                                            break;
                                        }

                                        matrix[row][index] = '~';
                                        filledNewSpace = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return filledNewSpace;
    }

    private static boolean shouldFill(int row, int startColumn, int endColumn, char[][] matrix) {
        int bottomRow = row + 1;
        if (!isValidCoordinate(bottomRow, startColumn, matrix)
                || !isValidCoordinate(bottomRow, endColumn, matrix)) {
            return false;
        }

        for (int index = startColumn; index <= endColumn; index++) {
            if (matrix[bottomRow][index] == '.') {
                return false;
            }
        }

        return true;
    }

    private static int[] countWaterCells(char[][] matrix, int smallestRow, int largestRow) {
        int tilesWaterCanReach = 0;
        int tilesWaterRetained = 0;

        for (int row = smallestRow; row <= largestRow; row++) {
            for (int column = 0; column < matrix[0].length; column++) {
                if (matrix[row][column] == '|') {
                    tilesWaterCanReach++;
                }

                if (matrix[row][column] == '~') {
                    tilesWaterRetained++;
                    tilesWaterCanReach++;
                }
            }
        }

        return new int[]{tilesWaterCanReach, tilesWaterRetained};
    }

    private static void initMatrix(char[][] matrix) {
        for (int row = 0; row < matrix.length; row++) {
            for (int column = 0; column < matrix[0].length; column++) {
                matrix[row][column] = '.';
            }
        }
    }

    private static void printMatrix(char[][] matrix) {
        for (int row = 0; row <= 1000; row++) {
            for (int column = 400; column < 800; column++) {
                System.out.print(matrix[row][column] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static boolean isValidCoordinate(int row, int column, char[][] matrix) {
        return row >= 0 && row < matrix.length && column >= 0 && column < matrix[0].length;
    }

    private static int getCellId(int row, int column, char[][] matrix) {
        return row * matrix[0].length + column;
    }
}