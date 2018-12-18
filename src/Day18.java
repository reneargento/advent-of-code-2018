import java.util.*;

/**
 * Created by Rene Argento on 18/12/18.
 */
public class Day18 {

    private static class Area {
        private char[][] area;

        Area(char[][] area) {
            this.area = area;
        }

        @Override
        public int hashCode() {
            int hashcode = 0;

            for (int row = 0; row < area.length; row++) {
                for (int column = 0; column < area[0].length; column++) {
                    if (area[row][column] == '.') {
                        hashcode += ((row * area.length) + column) % 31;
                    } else if (area[row][column] == '|') {
                        hashcode += (((row * area.length) + column) * 100) % 31;
                    } else if (area[row][column] == '#') {
                        hashcode += (((row * area.length) + column) * 1000) % 31;
                    }
                }
            }

            return hashcode;
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof Area)) {
                return false;
            }

            Area otherArea = (Area) object;
            char[][] otherAreaMatrix = otherArea.area;

            if (area.length != otherAreaMatrix.length
                    || area[0].length != otherAreaMatrix[0].length) {
                return false;
            }

            for (int row = 0; row < area.length; row++) {
                for (int column = 0; column < area[0].length; column++) {
                    if (area[row][column] != otherAreaMatrix[row][column]) {
                        return false;
                    }
                }
            }

            return true;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int dimension = 50;
        int minutes1 = 10;
        int minutes2 = 1000000000;
        char[][] area = new char[dimension][dimension];

        int rowId = 0;
        while (scanner.hasNext()) {
            String row = scanner.nextLine();

            for (int column = 0; column < row.length(); column++) {
                char acre = row.charAt(column);
                area[rowId][column] = acre;
            }
            rowId++;
        }

        System.out.println("\nPART 1");
        long totalResourceValue1 = moveTime(area, minutes1);
        System.out.println("Total resource value: " + totalResourceValue1);

        System.out.println("\nPART 2");
        long totalResourceValue2 = moveTime(area, minutes2);
        System.out.println("Total resource value: " + totalResourceValue2);
    }

    private static long moveTime(char[][] area, int minutes) {
        Map<Area, Integer> timeAreaWasSeen = new HashMap<>();
        timeAreaWasSeen.put(new Area(area), 0);

        for (int minute = 1; minute <= minutes; minute++) {
            area = getNextArea(area);
            Area newArea = new Area(area);

            if (timeAreaWasSeen.containsKey(newArea)) {
                // Cycle detected
                int timeSeen = timeAreaWasSeen.get(newArea);
                int cycleLength = minute - timeSeen;

                int completeCyclesRemaining = (minutes - minute) / cycleLength;
                minute += completeCyclesRemaining * cycleLength;
            }
            timeAreaWasSeen.put(newArea, minute);
        }

        return getTotalResourceValue(area);
    }

    private static char[][] getNextArea(char[][] area) {
        int[] neighborRows = {0, -1, 1, 0, -1, -1, 1, 1};
        int[] neighborColumns = {-1, 0, 0, 1, -1, 1, -1, 1};

        char[][] nextArea = new char[area.length][area[0].length];

        for (int row = 0; row < area.length; row++) {
            for (int column = 0; column < area[0].length; column++) {

                int open = 0;
                int tree = 0;
                int lumberyard = 0;

                for (int neighbor = 0; neighbor < neighborRows.length; neighbor++) {
                    int neighborRow = row + neighborRows[neighbor];
                    int neighborColumn = column + neighborColumns[neighbor];

                    if (isValidCell(neighborRow, neighborColumn, area)) {
                        if (area[neighborRow][neighborColumn] == '.') {
                            open++;
                        } else if (area[neighborRow][neighborColumn] == '|') {
                            tree++;
                        } else if (area[neighborRow][neighborColumn] == '#') {
                            lumberyard++;
                        }
                    }
                }

                char acre = area[row][column];
                if (acre == '.') {
                    if (tree >= 3) {
                        nextArea[row][column] = '|';
                    } else {
                        nextArea[row][column] = acre;
                    }
                } else if (acre == '|') {
                    if (lumberyard >= 3) {
                        nextArea[row][column] = '#';
                    } else {
                        nextArea[row][column] = acre;
                    }
                } else {
                    if (lumberyard >= 1 && tree >= 1) {
                        nextArea[row][column] = acre;
                    } else {
                        nextArea[row][column] = '.';
                    }
                }
            }
        }

        return nextArea;
    }

    private static int getTotalResourceValue(char[][] area) {
        int woodedAcres = 0;
        int lumberyards = 0;

        for (int row = 0; row < area.length; row++) {
            for (int column = 0; column < area[0].length; column++) {
                if (area[row][column] == '|') {
                    woodedAcres++;
                } else if (area[row][column] == '#') {
                    lumberyards++;
                }
            }
        }

        return woodedAcres * lumberyards;
    }

    private static boolean isValidCell(int row, int column, char[][] area) {
        return row >= 0 && row < area.length && column >= 0 && column < area[0].length;
    }
}