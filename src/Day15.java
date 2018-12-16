import java.util.*;

/**
 * Created by Rene Argento on 15/12/18.
 */
public class Day15 {

    private static class Unit {
        Cell cell;
        boolean isElf;
        int hitPoints;
        int round;
        boolean isAlive;

        Unit(Cell cell, boolean isElf) {
            this.cell = cell;
            this.isElf = isElf;
            hitPoints = 200;
            isAlive = true;
            round = 1;
        }
    }

    private static class Cell {
        int row;
        int column;
        int distance;

        Cell(int row, int column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public int hashCode() {
            return row * 100 + column;
        }
    }

    private static int[] neighborRows = {-1, 0, 0, 1};
    private static int[] neighborColumns = {0, -1, 1, 0};
    private final static int DEFAULT_ELF_ATTACK_POWER = 3;
    private static Comparator<Unit> unitsComparator = (unit1, unit2) -> {
        if (unit1.round != unit2.round) {
            return unit1.round - unit2.round;
        }
        if (unit1.cell.row != unit2.cell.row) {
            return unit1.cell.row - unit2.cell.row;
        }
        if (unit1.cell.column != unit2.cell.column) {
            return unit1.cell.column - unit2.cell.column;
        }
        return 0;
    };

    public static void main(String[] args) {
        int dimension = 32;
        char[][] battleground = new char[dimension][dimension];
        Unit[][] unitLocations = new Unit[dimension][dimension];

        Scanner scanner = new Scanner(System.in);
        int currentRow = 0;

        while (scanner.hasNext()) {
            String row = scanner.nextLine();

            for (int column = 0; column < row.length(); column++) {
                battleground[currentRow][column] = row.charAt(column);

                Unit newUnit = null;

                if (battleground[currentRow][column] == 'E') {
                    newUnit = new Unit(new Cell(currentRow, column), true);
                    unitLocations[currentRow][column] = newUnit;
                } else if (battleground[currentRow][column] == 'G') {
                    newUnit = new Unit(new Cell(currentRow, column), false);
                    unitLocations[currentRow][column] = newUnit;
                }
            }

            currentRow++;
        }

        System.out.println("PART 1");
        long combatOutcome = getCombatOutcome(battleground, unitLocations, DEFAULT_ELF_ATTACK_POWER, true);
        System.out.println("Combat outcome: " + combatOutcome);

        System.out.println("\nPART 2");
        long combatOutcomeWithElvesAlive = getCombatOutcomeWithElvesAlive(battleground, unitLocations);
        System.out.println("Combat outcome with elves alive: " + combatOutcomeWithElvesAlive);
    }

    private static long getCombatOutcomeWithElvesAlive(char[][] battleground, Unit[][] unitLocations) {
        int low = 4;
        int high = Integer.MAX_VALUE;
        long combatOutcomeWithElvesAlive = -1;

        while (low <= high) {
            int middle = low + (high - low) / 2;

            long combatOutcome = getCombatOutcome(battleground, unitLocations, middle, false);
            if (combatOutcome != -1) {
                combatOutcomeWithElvesAlive = combatOutcome;

                high = middle - 1;
            } else {
                low = middle + 1;
            }
        }

        return combatOutcomeWithElvesAlive;
    }

    private static long getCombatOutcome(char[][] battleground, Unit[][] unitLocations, int elvesAttackPower,
                                         boolean verbose) {
        // Copy objects to allow binary search
        char[][] battlegroundCopy = new char[battleground.length][battleground[0].length];
        Unit[][] unitLocationsCopy = new Unit[unitLocations.length][unitLocations[0].length];
        PriorityQueue<Unit> priorityQueue = new PriorityQueue<>(unitsComparator);
        Set<Unit> units = new HashSet<>();

        for (int row = 0; row < battleground.length; row++) {
            for (int column = 0; column < battleground[0].length; column++) {
                battlegroundCopy[row][column] = battleground[row][column];
            }
        }

        for (int row = 0; row < unitLocations.length; row++) {
            for (int column = 0; column < unitLocations[0].length; column++) {
                if (unitLocations[row][column] != null) {
                    Unit currentUnit = unitLocations[row][column];
                    Cell newCell = new Cell(currentUnit.cell.row, currentUnit.cell.column);
                    Unit newUnit = new Unit(newCell, currentUnit.isElf);

                    unitLocationsCopy[row][column] = newUnit;
                    priorityQueue.offer(newUnit);
                    units.add(newUnit);
                }
            }
        }

        // Start battle
        int currentRound = 1;

        if (verbose) {
            System.out.println("Round " + currentRound);
            printBattleground(battlegroundCopy);
        }

        while (true) {
            if (priorityQueue.isEmpty()) {
                break;
            }

            Unit unit = priorityQueue.poll();

            if (!unit.isAlive) {
                continue;
            }

            if (currentRound != unit.round) {
                currentRound = unit.round;
            }

            unit.round++;

            int row = unit.cell.row;
            int column = unit.cell.column;

            Queue<Unit> enemies = new PriorityQueue<>((enemy1, enemy2) -> {
                if (enemy1.hitPoints != enemy2.hitPoints) {
                    return enemy1.hitPoints - enemy2.hitPoints;
                }
                if (enemy1.cell.row != enemy2.cell.row) {
                    return enemy1.cell.row - enemy2.cell.row;
                }
                if (enemy1.cell.column != enemy2.cell.column) {
                    return enemy1.cell.column - enemy2.cell.column;
                }
                return 0;
            });

            boolean canMove = false;
            boolean isNearEnemy = false;

            for (int k = 0; k < 4; k++) {
                int neighborRow = row + neighborRows[k];
                int neighborColumn = column + neighborColumns[k];

                if (isValidCell(neighborRow, neighborColumn, battlegroundCopy)) {
                    Unit possibleUnit = unitLocationsCopy[neighborRow][neighborColumn];

                    if (possibleUnit == null) {
                        canMove = true;
                    } else if (possibleUnit.isElf != unit.isElf) {
                        enemies.offer(possibleUnit);
                        isNearEnemy = true;
                    }
                }
            }

            if (!isNearEnemy) {
                if (canMove) {
                    Cell nextCell = bfs(battlegroundCopy, unitLocationsCopy, unit.cell, unit.isElf);

                    if (nextCell != null) {
                        unitLocationsCopy[row][column] = null;
                        unitLocationsCopy[nextCell.row][nextCell.column] = unit;
                        battlegroundCopy[row][column] = '.';
                        battlegroundCopy[nextCell.row][nextCell.column] = unit.isElf ? 'E' : 'G';

                        unit.cell.row = nextCell.row;
                        unit.cell.column = nextCell.column;
                    }
                }
            }

            // After moving, check if there is an enemy nearby
            if (!isNearEnemy) {
                for (int k = 0; k < 4; k++) {
                    int neighborRow = unit.cell.row + neighborRows[k];
                    int neighborColumn = unit.cell.column + neighborColumns[k];

                    if (isValidCell(neighborRow, neighborColumn, battlegroundCopy)) {
                        Unit possibleUnit = unitLocationsCopy[neighborRow][neighborColumn];

                        if (possibleUnit != null && possibleUnit.isElf != unit.isElf) {
                            enemies.offer(possibleUnit);
                        }
                    }
                }
            }

            if (!enemies.isEmpty()) {
                Unit topPriorityEnemy = enemies.poll();
                battle(battlegroundCopy, unitLocationsCopy, topPriorityEnemy, elvesAttackPower);

                // If it is part 2 and an elf died, the attack power is not enough
                if (elvesAttackPower > DEFAULT_ELF_ATTACK_POWER && topPriorityEnemy.isElf && !topPriorityEnemy.isAlive) {
                    return -1;
                }

                if (isBattleOver(units)) {
                    int lowestRound = getLowestRound(units);

                    if (lowestRound == currentRound) {
                        currentRound--;
                    }

                    break;
                }
            }

            priorityQueue.offer(unit);
        }

        if (verbose) {
            System.out.println("Final round: " + currentRound);
            printBattleground(battlegroundCopy);
        }

        int endBattleHitPoints = getEndBattleHitPoints(units);
        return currentRound * endBattleHitPoints;
    }

    private static int getEndBattleHitPoints(Set<Unit> units) {
        int hitPoints = 0;

        for (Unit unit : units) {
            if (unit.isAlive) {
                hitPoints += unit.hitPoints;
            }
        }
        return hitPoints;
    }

    private static void battle(char[][] battleground, Unit[][] unitLocations, Unit unit, int elvesAttackPower) {
        if (!unit.isElf) {
            unit.hitPoints -= elvesAttackPower;
        } else {
            unit.hitPoints -= 3;
        }

        if (unit.hitPoints <= 0) {
            unit.isAlive = false;
            unitLocations[unit.cell.row][unit.cell.column] = null;
            battleground[unit.cell.row][unit.cell.column] = '.';
        }
    }

    private static boolean isBattleOver(Set<Unit> units) {
        boolean elvesAlive = false;
        boolean goblinsAlive = false;

        for (Unit unit : units) {
            if (unit.isAlive) {
                if (unit.isElf) {
                    elvesAlive = true;
                } else {
                    goblinsAlive = true;
                }
            }
        }

        return !elvesAlive || !goblinsAlive;
    }

    private static int getLowestRound(Set<Unit> units) {
        int lowestRound = Integer.MAX_VALUE;

        for (Unit unit : units) {
            if (unit.isAlive) {
                lowestRound = Math.min(lowestRound, unit.round);
            }
        }

        return lowestRound;
    }

    private static Cell bfs(char[][] battleground, Unit[][] unitLocations, Cell sourceCell, boolean isElf) {
        Queue<Cell> queue = new LinkedList<>();
        queue.offer(sourceCell);
        Map<Cell, Cell> parentCells = new HashMap<>();
        boolean[][] visited = new boolean[battleground.length][battleground[0].length];
        visited[sourceCell.row][sourceCell.column] = true;
        sourceCell.distance = 0;

        boolean foundEnemy = false;
        int distanceToEnemy = Integer.MAX_VALUE;
        List<Cell> nearbyEnemies = new ArrayList<>();

        while (!queue.isEmpty()) {
            Cell currentCell = queue.poll();

            int row = currentCell.row;
            int column = currentCell.column;

            Unit possibleUnit = unitLocations[row][column];
            if (possibleUnit != null && currentCell != sourceCell) {
                if (possibleUnit.isElf != isElf) {
                    if (!foundEnemy) {
                        distanceToEnemy = currentCell.distance;
                        nearbyEnemies.add(currentCell);
                        foundEnemy = true;
                    } else {
                        if (distanceToEnemy == currentCell.distance) {
                            nearbyEnemies.add(currentCell);
                        } else {
                            Cell highestPriorityCell = getHighestPriorityCell(nearbyEnemies);
                            return getNextCellToMove(sourceCell, highestPriorityCell, parentCells);
                        }
                    }
                }
                continue;
            }

            for (int k = 0; k < 4; k++) {
                int neighborRow = row + neighborRows[k];
                int neighborColumn = column + neighborColumns[k];

                if (isValidCell(neighborRow, neighborColumn, battleground)
                    && !visited[neighborRow][neighborColumn]) {
                    Cell newCell = new Cell(neighborRow, neighborColumn);
                    newCell.distance = currentCell.distance + 1;
                    queue.offer(newCell);
                    parentCells.put(newCell, currentCell);
                    visited[neighborRow][neighborColumn] = true;
                }
            }
        }

        if (!nearbyEnemies.isEmpty()) {
            Cell highestPriorityCell = getHighestPriorityCell(nearbyEnemies);
            return getNextCellToMove(sourceCell, highestPriorityCell, parentCells);
        }

        return null;
    }

    private static Cell getHighestPriorityCell(List<Cell> nearbyEnemies) {
        Queue<Cell> cells = new PriorityQueue<>((cell1, cell2) -> {
            if (cell1.row != cell2.row) {
                return cell1.row - cell2.row;
            }
            if (cell1.column != cell2.column) {
                return cell1.column - cell2.column;
            }
            return 0;
        });

        for (Cell enemy : nearbyEnemies) {
            cells.offer(enemy);
        }

        return cells.poll();
    }

    private static Cell getNextCellToMove(Cell sourceCell, Cell currentCell, Map<Cell, Cell> parentCells) {
        while (parentCells.get(currentCell) != sourceCell) {
            currentCell = parentCells.get(currentCell);
        }

        return currentCell;
    }

    private static boolean isValidCell(int row, int column, char[][] battleground) {
        return row < battleground.length
                && column < battleground[0].length
                && battleground[row][column] != '#';
    }

    private static void printBattleground(char[][] battleground) {
        for (int row = 0; row < battleground.length; row++) {
            for (int column = 0; column < battleground[0].length; column++) {
                System.out.print(battleground[row][column] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}