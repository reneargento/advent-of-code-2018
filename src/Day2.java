import java.util.*;

/**
 * Created by Rene Argento on 02/12/18.
 */
public class Day2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Part 1
        int twoEqualLetterIds = 0;
        int threeEqualLetterIds = 0;
        List<String> ids = new ArrayList<>();

        while (scanner.hasNext()) {
            String id = scanner.next();
            ids.add(id);

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

        // Part 2
        String result = null;

        // Naive O(n^2 * L) solution
        for (int i = 0; i < ids.size(); i++) {
            for (int j = i + 1; j < ids.size(); j++) {
                int differentCharacters = 0;
                int firstDifferentCharIndex = 0;
                String id1 = ids.get(i);
                String id2 = ids.get(j);

                for (int c = 0; c < id1.length(); c++) {
                    if (id1.charAt(c) != id2.charAt(c)) {
                        differentCharacters++;
                    }

                    if (differentCharacters == 1 && firstDifferentCharIndex == 0) {
                        firstDifferentCharIndex = c;
                    }
                }

                if (differentCharacters == 1) {
                    result = id1.substring(0, firstDifferentCharIndex) + id1.substring(firstDifferentCharIndex + 1);
                    break;
                }
            }

            if (result != null) {
                break;
            }
        }

        System.out.println("Common letters: " + result);
    }

}
