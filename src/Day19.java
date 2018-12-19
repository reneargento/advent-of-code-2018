import java.util.*;

/**
 * Created by Rene Argento on 19/12/18.
 */
public class Day19 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int instructionPointer = Integer.parseInt(scanner.nextLine().split(" ")[1]);
        List<String> instructions = new ArrayList<>();

        while (scanner.hasNext()) {
            instructions.add(scanner.nextLine());
        }

        int finalValueInRegister0Part1 = executeInstructions(instructions, instructionPointer, 0);

        System.out.println("PART 1");
        System.out.println("Final value in register 0: " + finalValueInRegister0Part1);


//        int finalValueInRegister0Part2 = executeInstructions(instructions, instructionPointer, 1);
        long finalValueInRegister0Part2 = sumFactors(10551288);

        System.out.println("\nPART 2");
        System.out.println("Final value in register 0: " + finalValueInRegister0Part2);
    }

    private static int executeInstructions(List<String> instructions, int instructionPointer, int initialRegister0Value) {
        int[] registers = new int[6];
        registers[0] = initialRegister0Value;

        int nextInstructionId = 0;

        while (true) {
            registers[instructionPointer] = nextInstructionId;

            String[] instructionInformation = instructions.get(nextInstructionId).split(" ");
            String instruction = instructionInformation[0];
            int input1 = Integer.parseInt(instructionInformation[1]);
            int input2 = Integer.parseInt(instructionInformation[2]);
            int output = Integer.parseInt(instructionInformation[3]);

            int result = 0;

            switch (instruction) {
                case "addr": result = registers[input1] + registers[input2]; break;
                case "addi": result = registers[input1] + input2; break;
                case "mulr": result = registers[input1] * registers[input2]; break;
                case "muli": result = registers[input1] * input2; break;
                case "banr": result = registers[input1] & registers[input2]; break;
                case "bani": result = registers[input1] & input2; break;
                case "borr": result = registers[input1] | registers[input2]; break;
                case "bori": result = registers[input1] | input2; break;
                case "setr": result = registers[input1]; break;
                case "seti": result = input1; break;
                case "gtir": result = compareGreaterThanValues(input1, registers[input2]); break;
                case "gtri": result = compareGreaterThanValues(registers[input1], input2); break;
                case "gtrr": result = compareGreaterThanValues(registers[input1], registers[input2]); break;
                case "eqir": result = compareEqualValues(input1, registers[input2]); break;
                case "eqri": result = compareEqualValues(registers[input1], input2); break;
                case "eqrr": result = compareEqualValues(registers[input1], registers[input2]); break;
            }

            registers[output] = result;

            printRegisterValues(registers[0], registers[1], registers[2], registers[3], registers[4],
                    registers[5]);

            nextInstructionId = registers[instructionPointer];
            nextInstructionId++;

            if (nextInstructionId >= instructions.size()) {
                break;
            }
        }

        return registers[0];
    }

    private static long sumFactors(long number) {
        long sum = 0;

        for (int i = 1; i <= number; i++) {
            if (number % i == 0) {
                sum += i;
            }
        }

        return sum;
    }

    private static void printRegisterValues(int registerA, int registerB, int registerC, int registerD, int registerE,
                                            int registerF) {
        System.out.printf("%d, %d, %d, %d, %d, %d\n", registerA, registerB, registerC, registerD, registerE, registerF);
    }

    private static int compareGreaterThanValues(int value1, int value2) {
        if (value1 > value2) {
            return 1;
        } else {
            return 0;
        }
    }

    private static int compareEqualValues(int value1, int value2) {
        if (value1 == value2) {
            return 1;
        } else {
            return 0;
        }
    }

}