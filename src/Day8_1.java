import java.util.Scanner;

/**
 * Created by Rene Argento on 08/12/18.
 */
public class Day8_1 {

    private static int inputIndex = 0;
    private static int metadataSum = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String[] input = scanner.nextLine().split(" ");
        checkTree(input);

        System.out.println("Metadata sum: " + metadataSum);
    }

    private static void checkTree(String[] input) {
        int childrenNumber = Integer.parseInt(input[inputIndex++]);
        int metadataNumber = Integer.parseInt(input[inputIndex++]);

        for (int child = 0; child < childrenNumber; child++) {
            checkTree(input);
        }

        for (int metadata = 0; metadata < metadataNumber; metadata++) {
            metadataSum += Integer.parseInt(input[inputIndex++]);
        }
    }
}
