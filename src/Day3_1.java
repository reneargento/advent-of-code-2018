import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Rene Argento on 03/12/18.
 */
public class Day3_1 {

    private class FenwickTree2DRangeUpdate {
        private int[][] fenwickTree;
        private int rows;
        private int columns;

        public FenwickTree2DRangeUpdate(int rows, int columns) {
            this.rows = rows;
            this.columns = columns;

            fenwickTree = new int[rows + 1][columns + 1];
        }

        public void pointUpdate(int row, int column, int value) {
            // Add 1 for 0-based index operations
            for (int i = row + 1; i <= rows; i += i & (-i)) {
                for (int j = column + 1; j <= columns; j += j & (-j)) {
                    fenwickTree[i][j] += value;
                }
            }
        }

        public void rangeUpdate(int row1, int column1, int row2, int column2, int value) {
            int minRow = Math.min(row1, row2);
            int maxRow = Math.max(row1, row2);
            int minColumn = Math.min(column1, column2);
            int maxColumn = Math.max(column1, column2);

            //Rectangular region between lower row and left column x upper row and right column
            pointUpdate(maxRow + 1, maxColumn + 1, value);
            pointUpdate(minRow, minColumn, value);
            pointUpdate(minRow, maxColumn + 1, -value);
            pointUpdate(maxRow + 1, minColumn, -value);
        }

        private int pointQuery(int row, int column) {
            int sum = 0;
            for (int i = row; i > 0; i -= i & (-i)) {
                for (int j = column; j > 0; j -= j & (-j)) {
                    sum += fenwickTree[i][j];
                }
            }
            return sum;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int dimension = 1000;
        FenwickTree2DRangeUpdate fenwickTree2D = new Day3_1().new FenwickTree2DRangeUpdate(dimension, dimension);

        while (scanner.hasNext()) {
            String claim = scanner.nextLine();
            String[] values = claim.split(" ");
            String[] rowColumn = values[2].substring(0, values[2].length() - 1).split(",");
            String[] wideTall = values[3].split("x");

            int column = Integer.parseInt(rowColumn[0]);
            int row = Integer.parseInt(rowColumn[1]);
            int width = Integer.parseInt(wideTall[0]);
            int height = Integer.parseInt(wideTall[1]);

            fenwickTree2D.rangeUpdate(row, column, row + height - 1, column + width - 1, 1);
        }

        int overlaps = 0;

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (fenwickTree2D.pointQuery(i, j) > 1) {
                    overlaps++;
                }
            }
        }

        System.out.println("Overlaps: " + overlaps);
    }

}
