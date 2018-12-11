/**
 * Created by Rene Argento on 11/12/18.
 */
public class Day11 {

    public static void main(String[] args) {
        int gridSerialNumber = 7315;
        int gridSize = 301;

        long[][] powerLevels = new long[gridSize][gridSize];

        for (int row = 0; row < powerLevels.length; row++) {
            for (int column = 0; column < powerLevels[0].length; column++) {
                getPowerLevel(powerLevels, row, column, gridSerialNumber);
            }
        }

        part1(powerLevels);
        part2(powerLevels);
    }

    private static void part1(long[][] powerLevels) {
        int k = 3;
        Cell result = findMaxSumSubMatrix(powerLevels, k);

        System.out.println("PART 1");
        System.out.println("x: " + (result.row - k + 1));
        System.out.println("y: " + (result.column - k + 1));
        System.out.println();
    }

    private static void part2(long[][] powerLevels) {
        long maxPower = 0;
        Cell maxPowerCell = null;
        int maxPowerK = 0;

        for (int k = 1; k < powerLevels.length; k++) {
            Result result = getPowerAndCell(powerLevels, k);

            if (result.power > maxPower) {
                maxPower = result.power;
                maxPowerCell = result.cell;
                maxPowerK = k;
            }
        }

        System.out.println("PART 2");
        System.out.println("x: " + (maxPowerCell.row - maxPowerK + 1));
        System.out.println("y: " + (maxPowerCell.column - maxPowerK + 1));
        System.out.println("Max k: " + maxPowerK);
    }

    private static void getPowerLevel(long[][] powerLevels, int x, int y, int gridSerialNumber) {
        long rackId = x + 10;
        long value = rackId;
        value *= y;
        value += gridSerialNumber;
        value *= rackId;

        String stringValue = String.valueOf(value);
        int hundredsDigitIndex = stringValue.length() - 3;
        int hundredsDigit = Integer.parseInt(stringValue.substring(hundredsDigitIndex, hundredsDigitIndex + 1));

        powerLevels[x][y] = hundredsDigit - 5;
    }

    private static Result getPowerAndCell(long matrix[][], int k) {
        Cell topCell = findMaxSumSubMatrix(matrix, k);
        int startRow = topCell.row - k + 1;
        int startColumn = topCell.column - k + 1;

        long power = 0;

        for (int row = startRow; row < startRow + k; row++) {
            for (int column = startColumn; column < startColumn + k; column++) {
                power += matrix[row][column];
            }
        }

        return new Result(topCell, power);
    }

    private static class Cell {
        private int row;
        private int column;

        public Cell(int row, int column) {
            this.row = row;
            this.column = column;
        }
    }

    private static class Result {
        private Cell cell;
        private long power;

        public Result(Cell cell, long power) {
            this.cell = cell;
            this.power = power;
        }
    }

    public static Cell findMaxSumSubMatrix(long matrix[][], int k) {
        int rows = matrix.length;
        int columns = matrix[0].length;

        // Pre-process the input matrix such that sum[i][j] stores sum of elements in matrix from (0, 0) to (i, j)
        long[][] sum = new long[rows][columns];
        sum[0][0] = matrix[0][0];

        // Pre-process first row
        for (int j = 1; j < columns; j++) {
            sum[0][j] = matrix[0][j] + sum[0][j - 1];
        }

        // Pre-process first column
        for (int i = 1; i < rows; i++) {
            sum[i][0] = matrix[i][0] + sum[i - 1][0];
        }

        // Pre-process the rest of the matrix
        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < columns; j++) {
                sum[i][j] = matrix[i][j] + sum[i - 1][j] + sum[i][j - 1] - sum[i - 1][j - 1];
            }
        }

        long total, max = Integer.MIN_VALUE;
        Cell result = null;

        // Find maximum sum sub-matrix

        // Start from cell (k - 1, k - 1) and consider each submatrix of size k x k
        for (int i = k - 1; i < rows; i++) {
            for (int j = k - 1; j < columns; j++) {
                // Note (i, j) is bottom right corner coordinates of square sub-matrix of size k

                total = sum[i][j];

                if (i - k >= 0) {
                    total = total - sum[i - k][j];
                }

                if (j - k >= 0) {
                    total = total - sum[i][j - k];
                }

                if (i - k >= 0 && j - k >= 0) {
                    total = total + sum[i - k][j - k];
                }

                if (total > max) {
                    max = total;
                    result = new Cell(i, j);
                }
            }
        }

        // Returns coordinates of bottom right corner of sub-matrix
        return result;
    }

}
