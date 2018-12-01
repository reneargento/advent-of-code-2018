import java.util.Scanner;

/**
 * Created by Rene Argento on 01/12/18.
 */
public class Day1_1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long deltaFrequency = 0;

        while (scanner.hasNext()) {
            String frequency = scanner.next();

            if (frequency.equals("0")) {
                continue;
            }

            int frequencyInt = Integer.parseInt(frequency.substring(1));

            if (frequency.charAt(0) == '+') {
                deltaFrequency += frequencyInt;
            } else {
                deltaFrequency -= frequencyInt;
            }
        }

        System.out.println("Delta frequency: " + deltaFrequency);
    }

}
