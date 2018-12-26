import java.util.Scanner;
import java.util.Stack;

/**
 * Created by Rene Argento on 05/12/18.
 */
public class Day5 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String polymer = scanner.next();

        // Part 1
        int unitsRemaining = react(polymer, '-');
        System.out.println("Units remaining: " + unitsRemaining);

        // Part 2
        int minLength = Integer.MAX_VALUE;

        for (char unit = 'a'; unit <= 'z'; unit++) {
            int length = react(polymer, unit);

            if (length < minLength) {
                minLength = length;
            }
        }

        System.out.println("Shortest polymer length: " + minLength);
    }

    private static int react(String polymer, char unitToRemove) {
        Stack<Character> stack = new Stack<>();

        for (char unit : polymer.toCharArray()) {
            if (Character.toLowerCase(unit) == unitToRemove) {
                continue;
            }

            if (stack.isEmpty()) {
                stack.push(unit);
                continue;
            }

            char topUnit = stack.peek();

            if (areSameTypeOppositePolarities(unit, topUnit)) {
                stack.pop();
            } else {
                stack.push(unit);
            }
        }

        return stack.size();
    }

    private static boolean areSameTypeOppositePolarities(char unit1, char unit2) {
        return Character.toLowerCase(unit1) == Character.toLowerCase(unit2)
                && unit1 != unit2;
    }

}