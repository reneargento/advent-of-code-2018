import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by Rene Argento on 12/12/18.
 */
public class Day12 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int generationsPart1 = 20;
        int generationsPart2 = 500;
        String initialStateString = scanner.nextLine().split(" ")[2];
        StringBuilder initialState = new StringBuilder(initialStateString);
        scanner.nextLine();

        Set<Integer> changeToPlantState = new HashSet<>();
        Set<Integer> changeToNoPlantState = new HashSet<>();

        while (scanner.hasNext()) {
            String[] line = scanner.nextLine().split(" ");
            StringBuilder changeState = new StringBuilder(line[0]);
            boolean isPlant = line[2].equals("#");
            computeChangeToPlantState(changeState, changeToPlantState, changeToNoPlantState, isPlant);
        }

        System.out.println("PART 1");
        countPlants(initialState, changeToPlantState, changeToNoPlantState, generationsPart1, false);
        System.out.println();

        System.out.println("PART 2");
        countPlants(initialState, changeToPlantState, changeToNoPlantState, generationsPart2, true);
    }

    private static void computeChangeToPlantState(StringBuilder changeState, Set<Integer> changeToPlantState,
                                                  Set<Integer> changeToNoPlantState, boolean isPlant) {
        int result = getValue(changeState, 0, changeState.length());

        if (isPlant) {
            changeToPlantState.add(result);
        } else {
            changeToNoPlantState.add(result);
        }
    }

    private static int getValue(StringBuilder state, int startIndex, int endIndex) {
        int result = 0;
        int powerIndex = 1;

        for (int i = startIndex; i < endIndex; i++) {
            if (state.charAt(i) == '#') {
                result += Math.pow(10, powerIndex);
            }

            powerIndex++;
        }

        return result;
    }

    private static class CellChange {
        int index;
        boolean isPlant;

        CellChange(int index, boolean isPlant) {
            this.index = index;
            this.isPlant = isPlant;
        }
    }

    private static void countPlants(StringBuilder state, Set<Integer> changeToPlantState,
                                    Set<Integer> changeToNoPlantState, int generations, boolean part2) {
        String offset = "...";
        long lastGenerationCount = 0;

        for (int generation = 1; generation <= generations; generation++) {

            state.insert(0, offset);
            state.append(offset);

            int value = getValue(state, 0, 5);

            Set<CellChange> cellChanges = new HashSet<>();

            if (changeToPlantState.contains(value)) {
                cellChanges.add(new CellChange(2, true));
            } else if (changeToNoPlantState.contains(value)) {
                cellChanges.add(new CellChange(2, false));
            }

            for (int i = 3; i < state.length() - 2; i++) {

                if (state.charAt(i - 3) == '#') {
                    value -= 10;
                }

                value /= 10;

                if (state.charAt(i + 2) == '#') {
                    value += 100000;
                }

                if (changeToPlantState.contains(value)) {
                    cellChanges.add(new CellChange(i, true));
                } else if (changeToNoPlantState.contains(value)) {
                    cellChanges.add(new CellChange(i, false));
                }
            }

            for (CellChange cellChange : cellChanges) {
                char newChar;
                if (cellChange.isPlant) {
                    newChar = '#';
                } else {
                    newChar = '.';
                }

                state.setCharAt(cellChange.index, newChar);
            }

            if (part2) {
                long plantsCount = getPlantCount(state, offset, generation);
                System.out.println("Generation: " + generation + " Sum of pots: " + plantsCount);

                if (generation >= 1) {
                    System.out.println("Count difference: " + (plantsCount - lastGenerationCount));
                    lastGenerationCount = plantsCount;
                }
            }
        }

        if (!part2) {
            long plantsCount = getPlantCount(state, offset, generations);
            System.out.println("Sum of plants at generation 20: " + plantsCount);
        } else {
            long finalSum = 48000 + (50000000000L - 500) * 80;
            System.out.println("\nSum of plants at generation 50000000000: " + finalSum);
        }
    }

    private static long getPlantCount(StringBuilder state, String offset, int generation) {
        long plantsCount = 0;

        for (int i = 0; i < state.length(); i++) {
            if (state.charAt(i) == '#') {
                plantsCount += (i - (offset.length() * generation));
            }
        }

        return plantsCount;
    }

}