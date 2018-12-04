import java.util.*;

/**
 * Created by Rene Argento on 04/12/18.
 */
public class Day4_2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> data = new ArrayList<>();

        while (scanner.hasNext()) {
            data.add(scanner.nextLine());
        }

        Collections.sort(data);

        Map<Integer, Map<Integer, Integer>> secondsToIds = new HashMap<>();

        for (int second = 0; second <= 59; second++) {
            secondsToIds.put(second, new HashMap<>());
        }

        int currentId = 0;
        int sleepTime = 0;
        int maxSleptId = 0;
        int maxCount = 0;
        int maxSecond = 0;

        for (String line : data) {
            String[] values = line.split(" ");

            if (values.length == 6) {
                currentId = Integer.parseInt(values[3].substring(1));
            } else {
                String minutesString = values[1].split(":")[1];
                String timestampString = minutesString.substring(0, minutesString.length() - 1);
                int timestamp = Integer.parseInt(timestampString);

                if (values[2].equals("falls")) {
                    sleepTime = timestamp;
                } else {
                    for (int second = sleepTime; second < timestamp; second++) {
                        int currentCount = secondsToIds.get(second).getOrDefault(currentId, 0);
                        int updatedCount = currentCount + 1;
                        secondsToIds.get(second).put(currentId, updatedCount);

                        if (updatedCount > maxCount) {
                            maxCount = updatedCount;
                            maxSleptId = currentId;
                            maxSecond = second;
                        }
                    }
                }
            }
        }

        System.out.println("Guard id: " + maxSleptId);
        System.out.println("Second: " + maxSecond);
        System.out.println("Result: " + maxSleptId * maxSecond);
    }

}