import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by Rene Argento on 01/12/18.
 */
public class Day1 {

    private static final String PATH = "/Users/rene/Desktop/Algorithms/Competitions/Advent of Code/2018/";
    private static final String FILE_INPUT_PATH = PATH + "day1.txt";

    public static void main(String[] args) {
        Set<Long> frequenciesFound = new HashSet<>();
        frequenciesFound.add(0L);
        long deltaFrequency = 0;
        boolean finishedFirstRun = false;

        while (true) {
            List<String> frequencies = readFileInput(FILE_INPUT_PATH);
            boolean frequencyFound = false;

            for (String frequency : frequencies) {
                int frequencyInt = 0;

                if (!frequency.equals("0")) {
                    frequencyInt = Integer.parseInt(frequency.substring(1));
                }

                if (frequency.charAt(0) == '+') {
                    deltaFrequency += frequencyInt;
                } else {
                    deltaFrequency -= frequencyInt;
                }

                if (frequenciesFound.contains(deltaFrequency)) {
                    System.out.println("Repeated frequency: " + deltaFrequency);
                    frequencyFound = true;
                    break;
                }

                frequenciesFound.add(deltaFrequency);
            }

            if (!finishedFirstRun) {
                System.out.println("Delta frequency: " + deltaFrequency);
                finishedFirstRun = true;
            }

            if (frequencyFound) {
                break;
            }
        }
    }

    private static List<String> readFileInput(String filePath) {
        Path path = Paths.get(filePath);
        List<String> valuesList = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(path);
            valuesList.addAll(lines);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return valuesList;
    }

}
