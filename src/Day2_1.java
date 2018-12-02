import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Rene Argento on 02/12/18.
 */
public class Day2_1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int twoEqualLetterIds = 0;
        int threeEqualLetterIds = 0;

        while (scanner.hasNext()) {
            String id = scanner.next();
            Map<Character, Integer> characters = new HashMap<>();

            for (char character : id.toCharArray()) {
                if (!characters.containsKey(character)) {
                    characters.put(character, 0);
                }

                int currentCount = characters.get(character);
                characters.put(character, currentCount + 1);
            }

            boolean countedForTwoEqualLetters = false;
            boolean countedForThreeEqualLetters = false;

            for (char character : characters.keySet()) {
                int count = characters.get(character);

                if (count == 2 && !countedForTwoEqualLetters) {
                    twoEqualLetterIds++;
                    countedForTwoEqualLetters = true;
                } else if (count == 3 && !countedForThreeEqualLetters) {
                    threeEqualLetterIds++;
                    countedForThreeEqualLetters = true;
                }
            }
        }

        int checksum = twoEqualLetterIds * threeEqualLetterIds;
        System.out.println("Checksum: " + checksum);
    }

}
