import java.util.Scanner;

/**
 * Created by Rene Argento on 08/12/18.
 */
public class Day8_2 {

    private static int inputIndex = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String[] input = scanner.nextLine().split(" ");
        int rootValue = checkTree(input);

        System.out.println("Root value: " + rootValue);
    }

    private static int checkTree(String[] input) {
        int childrenNumber = Integer.parseInt(input[inputIndex++]);
        int metadataNumber = Integer.parseInt(input[inputIndex++]);
        int[] childrenValue = new int[childrenNumber];
        int value = 0;

        for (int child = 0; child < childrenNumber; child++) {
            int childValue = checkTree(input);
            childrenValue[child] = childValue;
        }

        for (int metadata = 0; metadata < metadataNumber; metadata++) {
            int meta = Integer.parseInt(input[inputIndex++]);

            if (childrenNumber == 0) {
                value += meta;
            } else {
                if (meta <= childrenNumber) {
                    value += childrenValue[meta - 1];
                }
            }
        }

        return value;
    }
}
