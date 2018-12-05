import java.util.*;

/**
 * Created by Rene Argento on 05/12/18.
 */
public class Day5_1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String polymer = scanner.next();

        Stack<Character> stack = new Stack<>();

        for (char unit : polymer.toCharArray()) {
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

        System.out.println("Units remaining: " + stack.size());
    }

    private static boolean areSameTypeOppositePolarities(char unit1, char unit2) {
        return Character.toLowerCase(unit1) == Character.toLowerCase(unit2)
                && unit1 != unit2;
    }

}