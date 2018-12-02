import java.util.*;

/**
 * Created by Rene Argento on 02/12/18.
 */
public class Day2_2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> ids = new ArrayList<>();

        while (scanner.hasNext()) {
            ids.add(scanner.next());
        }

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
