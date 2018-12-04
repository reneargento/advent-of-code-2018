import java.util.*;

/**
 * Created by Rene Argento on 04/12/18.
 */
public class Day4_1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> data = new ArrayList<>();
        Map<Integer, Map<Integer, Integer>> secondsSlept = new HashMap<>();

        while (scanner.hasNext()) {
            data.add(scanner.nextLine());
        }

        Collections.sort(data);
        int guardIdMostAsleep = 0;
        int maxTimeSlept = 0;
        int currentId = 0;
        int sleepTime = 0;

        Map<Integer, Integer> totalSleepTime = new HashMap<>();

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
                    int timeSlept = timestamp - sleepTime - 1;

                    int guardCurrentSleptTime = totalSleepTime.getOrDefault(currentId, 0);
                    guardCurrentSleptTime += timeSlept;

                    totalSleepTime.put(currentId, guardCurrentSleptTime);

                    if (guardCurrentSleptTime > maxTimeSlept) {
                        maxTimeSlept = guardCurrentSleptTime;
                        guardIdMostAsleep = currentId;
                    }

                    if (!secondsSlept.containsKey(currentId)) {
                        secondsSlept.put(currentId, new HashMap<>());
                    }

                    for (int second = sleepTime; second < timestamp; second++) {
                        int totalTime = secondsSlept.get(currentId).getOrDefault(second, 0);
                        secondsSlept.get(currentId).put(second, totalTime + 1);
                    }
                }
            }
        }

        int secondMostAsleep = 0;
        int mostSecondsAsleep = 0;

        for (int second : secondsSlept.get(guardIdMostAsleep).keySet()) {
            int totalTime = secondsSlept.get(guardIdMostAsleep).get(second);

            if (totalTime > mostSecondsAsleep) {
                mostSecondsAsleep = totalTime;
                secondMostAsleep = second;
            }
        }

        System.out.println("Guard id: " + guardIdMostAsleep);
        System.out.println("Second: " + secondMostAsleep);
        System.out.println("Result: " + guardIdMostAsleep * secondMostAsleep);
    }

}