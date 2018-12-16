import java.util.*;

/**
 * Created by Rene Argento on 16/12/18.
 */
public class Day16 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int samplesThatBehaveLike3OrMoreOpcodes = 0;
        Map<Integer, Set<Integer>> opcodeToInstruction = new HashMap<>();

        for (int opcode = 0; opcode < 16; opcode++) {
            Set<Integer> instructionSet = new HashSet<>();

            for (int instruction = 0; instruction < 16; instruction++) {
                instructionSet.add(instruction);
            }

            opcodeToInstruction.put(opcode, instructionSet);
        }

        while (scanner.hasNext()) {
            String line = scanner.nextLine();

            if (line.equals("")) {
                // Part 1 input done
                scanner.nextLine();
                break;
            }

            String[] beforeRegisters = line.split(" ");
            int registerABefore = Integer.parseInt(beforeRegisters[1].substring(1, 2));
            int registerBBefore = Integer.parseInt(beforeRegisters[2].substring(0, 1));
            int registerCBefore = Integer.parseInt(beforeRegisters[3].substring(0, 1));
            int registerDBefore = Integer.parseInt(beforeRegisters[4].substring(0, 1));

            String[] instructions = scanner.nextLine().split(" ");
            int opcode = Integer.parseInt(instructions[0]);
            int input1 = Integer.parseInt(instructions[1]);
            int input2 = Integer.parseInt(instructions[2]);
            int output = Integer.parseInt(instructions[3]);

            String[] afterRegisters = scanner.nextLine().split(" ");
            int registerAAfter = Integer.parseInt(afterRegisters[2].substring(1, 2));
            int registerBAfter = Integer.parseInt(afterRegisters[3].substring(0, 1));
            int registerCAfter = Integer.parseInt(afterRegisters[4].substring(0, 1));
            int registerDAfter = Integer.parseInt(afterRegisters[5].substring(0, 1));

            int possibleOpcodesCount = mapOpcodesAndGetPossibleOpcodesCount(registerABefore, registerBBefore, registerCBefore,
                    registerDBefore, input1, input2, output, registerAAfter, registerBAfter, registerCAfter,
                    registerDAfter, opcodeToInstruction.get(opcode));
            if (possibleOpcodesCount >= 3) {
                samplesThatBehaveLike3OrMoreOpcodes++;
            }

            scanner.nextLine();
        }

        System.out.println("PART 1");
        System.out.println("Samples that behave like 3 or more opcodes: " + samplesThatBehaveLike3OrMoreOpcodes);

        mapOpcodes(opcodeToInstruction);
        int finalResult = executeInstructions(opcodeToInstruction, scanner);

        System.out.println("\nPART 2");
        System.out.println("Final register 0 result: " + finalResult);
    }

    private static int mapOpcodesAndGetPossibleOpcodesCount(int registerABefore, int registerBBefore, int registerCBefore,
                                                            int registerDBefore, int input1, int input2, int output,
                                                            int registerAAfter, int registerBAfter, int registerCAfter,
                                                            int registerDAfter, Set<Integer> possibleInstructions) {
        int possibleOpcodes = 0;
        int instructionId = 0;

        if (canBeAddr(registerABefore, registerBBefore, registerCBefore, registerDBefore, input1, input2, output,
                registerAAfter, registerBAfter, registerCAfter, registerDAfter)) {
            possibleOpcodes++;
        } else {
            possibleInstructions.remove(instructionId);
        }
        instructionId++;

        if (canBeAddi(registerABefore, registerBBefore, registerCBefore, registerDBefore, input1, input2, output,
                registerAAfter, registerBAfter, registerCAfter, registerDAfter)) {
            possibleOpcodes++;
        } else {
            possibleInstructions.remove(instructionId);
        }
        instructionId++;

        if (canBeMulr(registerABefore, registerBBefore, registerCBefore, registerDBefore, input1, input2, output,
                registerAAfter, registerBAfter, registerCAfter, registerDAfter)) {
            possibleOpcodes++;
        } else {
            possibleInstructions.remove(instructionId);
        }
        instructionId++;

        if (canBeMuli(registerABefore, registerBBefore, registerCBefore, registerDBefore, input1, input2, output,
                registerAAfter, registerBAfter, registerCAfter, registerDAfter)) {
            possibleOpcodes++;
        } else {
            possibleInstructions.remove(instructionId);
        }
        instructionId++;

        if (canBeBanr(registerABefore, registerBBefore, registerCBefore, registerDBefore, input1, input2, output,
                registerAAfter, registerBAfter, registerCAfter, registerDAfter)) {
            possibleOpcodes++;
        } else {
            possibleInstructions.remove(instructionId);
        }
        instructionId++;

        if (canBeBani(registerABefore, registerBBefore, registerCBefore, registerDBefore, input1, input2, output,
                registerAAfter, registerBAfter, registerCAfter, registerDAfter)) {
            possibleOpcodes++;
        } else {
            possibleInstructions.remove(instructionId);
        }
        instructionId++;

        if (canBeBorr(registerABefore, registerBBefore, registerCBefore, registerDBefore, input1, input2, output,
                registerAAfter, registerBAfter, registerCAfter, registerDAfter)) {
            possibleOpcodes++;
        } else {
            possibleInstructions.remove(instructionId);
        }
        instructionId++;

        if (canBeBori(registerABefore, registerBBefore, registerCBefore, registerDBefore, input1, input2, output,
                registerAAfter, registerBAfter, registerCAfter, registerDAfter)) {
            possibleOpcodes++;
        } else {
            possibleInstructions.remove(instructionId);
        }
        instructionId++;

        if (canBeSetr(registerABefore, registerBBefore, registerCBefore, registerDBefore, input1, input2, output,
                registerAAfter, registerBAfter, registerCAfter, registerDAfter)) {
            possibleOpcodes++;
        } else {
            possibleInstructions.remove(instructionId);
        }
        instructionId++;

        if (canBeSeti(input1, output, registerAAfter, registerBAfter, registerCAfter, registerDAfter)) {
            possibleOpcodes++;
        } else {
            possibleInstructions.remove(instructionId);
        }
        instructionId++;

        if (canBeGtir(registerABefore, registerBBefore, registerCBefore, registerDBefore, input1, input2, output,
                registerAAfter, registerBAfter, registerCAfter, registerDAfter)) {
            possibleOpcodes++;
        } else {
            possibleInstructions.remove(instructionId);
        }
        instructionId++;

        if (canBeGtri(registerABefore, registerBBefore, registerCBefore, registerDBefore, input1, input2, output,
                registerAAfter, registerBAfter, registerCAfter, registerDAfter)) {
            possibleOpcodes++;
        } else {
            possibleInstructions.remove(instructionId);
        }
        instructionId++;

        if (canBeGtrr(registerABefore, registerBBefore, registerCBefore, registerDBefore, input1, input2, output,
                registerAAfter, registerBAfter, registerCAfter, registerDAfter)) {
            possibleOpcodes++;
        } else {
            possibleInstructions.remove(instructionId);
        }
        instructionId++;

        if (canBeEqir(registerABefore, registerBBefore, registerCBefore, registerDBefore, input1, input2, output,
                registerAAfter, registerBAfter, registerCAfter, registerDAfter)) {
            possibleOpcodes++;
        } else {
            possibleInstructions.remove(instructionId);
        }
        instructionId++;

        if (canBeEqri(registerABefore, registerBBefore, registerCBefore, registerDBefore, input1, input2, output,
                registerAAfter, registerBAfter, registerCAfter, registerDAfter)) {
            possibleOpcodes++;
        } else {
            possibleInstructions.remove(instructionId);
        }
        instructionId++;

        if (canBeEqrr(registerABefore, registerBBefore, registerCBefore, registerDBefore, input1, input2, output,
                registerAAfter, registerBAfter, registerCAfter, registerDAfter)) {
            possibleOpcodes++;
        } else {
            possibleInstructions.remove(instructionId);
        }

        return possibleOpcodes;
    }

    private static void mapOpcodes(Map<Integer, Set<Integer>> opcodeToInstruction) {

        while (true) {
            boolean mappingComplete = true;

            // Is the mapping finished?
            for (int opcode : opcodeToInstruction.keySet()) {
                if (opcodeToInstruction.get(opcode).size() > 1) {
                    mappingComplete = false;
                    break;
                }
            }

            if (mappingComplete) {
                break;
            }

            Set<Integer> mappedInstructions = new HashSet<>();

            // Get all instructions already mapped
            for (int opcode : opcodeToInstruction.keySet()) {
                if (opcodeToInstruction.get(opcode).size() == 1) {
                    opcodeToInstruction.get(opcode).stream().mapToInt(instruction -> instruction)
                            .findFirst().ifPresent(mappedInstructions::add);
                }
            }

            // Remove already mapped instructions from other opcodes
            for (int opcode : opcodeToInstruction.keySet()) {
                Set<Integer> currentSet = opcodeToInstruction.get(opcode);

                if (currentSet.size() > 1) {
                    for (int alreadyMappedInstruction : mappedInstructions) {
                        currentSet.remove(alreadyMappedInstruction);
                    }

                    if (currentSet.size() == 1) {
                        currentSet.stream().mapToInt(instruction -> instruction)
                                .findFirst().ifPresent(mappedInstructions::add);
                    }
                }
            }
        }
    }

    private static int executeInstructions(Map<Integer, Set<Integer>> opcodeToInstruction, Scanner scanner) {
        int registerA = 0;
        int registerB = 0;
        int registerC = 0;
        int registerD = 0;

        while (scanner.hasNext()) {
            String[] instructions = scanner.nextLine().split(" ");
            int opcode = Integer.parseInt(instructions[0]);
            int input1 = Integer.parseInt(instructions[1]);
            int input2 = Integer.parseInt(instructions[2]);
            int output = Integer.parseInt(instructions[3]);

            int result = 0;

            int instruction = opcodeToInstruction.get(opcode).stream().mapToInt(instructionCode -> instructionCode)
                    .findFirst().orElse(0);
            switch (instruction) {
                case 0: result = addr(registerA, registerB, registerC, registerD, input1, input2); break;
                case 1: result = addi(registerA, registerB, registerC, registerD, input1, input2); break;
                case 2: result = mulr(registerA, registerB, registerC, registerD, input1, input2); break;
                case 3: result = muli(registerA, registerB, registerC, registerD, input1, input2); break;
                case 4: result = banr(registerA, registerB, registerC, registerD, input1, input2); break;
                case 5: result = bani(registerA, registerB, registerC, registerD, input1, input2); break;
                case 6: result = borr(registerA, registerB, registerC, registerD, input1, input2); break;
                case 7: result = bori(registerA, registerB, registerC, registerD, input1, input2); break;
                case 8: result = setr(registerA, registerB, registerC, registerD, input1); break;
                case 9: result = seti(input1); break;
                case 10: result = gtir(registerA, registerB, registerC, registerD, input1, input2); break;
                case 11: result = gtri(registerA, registerB, registerC, registerD, input1, input2); break;
                case 12: result = gtrr(registerA, registerB, registerC, registerD, input1, input2); break;
                case 13: result = eqir(registerA, registerB, registerC, registerD, input1, input2); break;
                case 14: result = eqri(registerA, registerB, registerC, registerD, input1, input2); break;
                case 15: result = eqrr(registerA, registerB, registerC, registerD, input1, input2); break;
            }

            switch (output) {
                case 0: registerA = result; break;
                case 1: registerB = result; break;
                case 2: registerC = result; break;
                case 3: registerD = result; break;
            }
        }

        return registerA;
    }

    private static int getRegisterValue(int registerA, int registerB, int registerC, int registerD, int input) {
        switch (input) {
            case 0: return registerA;
            case 1: return registerB;
            case 2: return registerC;
            case 3: return registerD;
        }
        return -1;
    }

    private static boolean canBeAddr(int registerABefore, int registerBBefore, int registerCBefore,
                                     int registerDBefore, int input1, int input2, int output,
                                     int registerAAfter, int registerBAfter, int registerCAfter, int registerDAfter) {
        int addr = addr(registerABefore, registerBBefore, registerCBefore, registerDBefore, input1, input2);
        int registerResultValue = getRegisterValue(registerAAfter, registerBAfter, registerCAfter, registerDAfter, output);
        return addr == registerResultValue;
    }
    private static int addr(int registerA, int registerB, int registerC, int registerD, int input1, int input2) {
        int register1Value = getRegisterValue(registerA, registerB, registerC, registerD, input1);
        int register2Value = getRegisterValue(registerA, registerB, registerC, registerD, input2);
        return register1Value + register2Value;
    }

    private static boolean canBeAddi(int registerABefore, int registerBBefore, int registerCBefore,
                                     int registerDBefore, int input1, int input2, int output,
                                     int registerAAfter, int registerBAfter, int registerCAfter, int registerDAfter) {
        int addi = addi(registerABefore, registerBBefore, registerCBefore, registerDBefore, input1, input2);
        int registerResultValue = getRegisterValue(registerAAfter, registerBAfter, registerCAfter, registerDAfter, output);
        return addi == registerResultValue;
    }
    private static int addi(int registerA, int registerB, int registerC, int registerD, int input1, int input2) {
        int register1Value = getRegisterValue(registerA, registerB, registerC, registerD, input1);
        return register1Value + input2;
    }

    private static boolean canBeMulr(int registerABefore, int registerBBefore, int registerCBefore,
                                     int registerDBefore, int input1, int input2, int output,
                                     int registerAAfter, int registerBAfter, int registerCAfter, int registerDAfter) {
        int mulr = mulr(registerABefore, registerBBefore, registerCBefore, registerDBefore, input1, input2);
        int registerResultValue = getRegisterValue(registerAAfter, registerBAfter, registerCAfter, registerDAfter, output);
        return mulr == registerResultValue;
    }
    private static int mulr(int registerA, int registerB, int registerC, int registerD, int input1, int input2) {
        int register1Value = getRegisterValue(registerA, registerB, registerC, registerD, input1);
        int register2Value = getRegisterValue(registerA, registerB, registerC, registerD, input2);
        return register1Value * register2Value;
    }

    private static boolean canBeMuli(int registerABefore, int registerBBefore, int registerCBefore,
                                     int registerDBefore, int input1, int input2, int output,
                                     int registerAAfter, int registerBAfter, int registerCAfter, int registerDAfter) {
        int muli = muli(registerABefore, registerBBefore, registerCBefore, registerDBefore, input1, input2);
        int registerResultValue = getRegisterValue(registerAAfter, registerBAfter, registerCAfter, registerDAfter, output);
        return muli == registerResultValue;
    }
    private static int muli(int registerA, int registerB, int registerC, int registerD, int input1, int input2) {
        int register1Value = getRegisterValue(registerA, registerB, registerC, registerD, input1);
        return register1Value * input2;
    }

    private static boolean canBeBanr(int registerABefore, int registerBBefore, int registerCBefore,
                                     int registerDBefore, int input1, int input2, int output,
                                     int registerAAfter, int registerBAfter, int registerCAfter, int registerDAfter) {
        int banr = banr(registerABefore, registerBBefore, registerCBefore, registerDBefore, input1, input2);
        int registerResultValue = getRegisterValue(registerAAfter, registerBAfter, registerCAfter, registerDAfter, output);
        return banr == registerResultValue;
    }
    private static int banr(int registerA, int registerB, int registerC, int registerD, int input1, int input2) {
        int register1Value = getRegisterValue(registerA, registerB, registerC, registerD, input1);
        int register2Value = getRegisterValue(registerA, registerB, registerC, registerD, input2);
        return register1Value & register2Value;
    }

    private static boolean canBeBani(int registerABefore, int registerBBefore, int registerCBefore,
                                     int registerDBefore, int input1, int input2, int output,
                                     int registerAAfter, int registerBAfter, int registerCAfter, int registerDAfter) {
        int bani = bani(registerABefore, registerBBefore, registerCBefore, registerDBefore, input1, input2);
        int registerResultValue = getRegisterValue(registerAAfter, registerBAfter, registerCAfter, registerDAfter, output);
        return bani == registerResultValue;
    }
    private static int bani(int registerA, int registerB, int registerC, int registerD, int input1, int input2) {
        int register1Value = getRegisterValue(registerA, registerB, registerC, registerD, input1);
        return register1Value & input2;
    }

    private static boolean canBeBorr(int registerABefore, int registerBBefore, int registerCBefore,
                                     int registerDBefore, int input1, int input2, int output,
                                     int registerAAfter, int registerBAfter, int registerCAfter, int registerDAfter) {
        int borr = borr(registerABefore, registerBBefore, registerCBefore, registerDBefore, input1, input2);
        int registerResultValue = getRegisterValue(registerAAfter, registerBAfter, registerCAfter, registerDAfter, output);
        return borr == registerResultValue;
    }
    private static int borr(int registerA, int registerB, int registerC, int registerD, int input1, int input2) {
        int register1Value = getRegisterValue(registerA, registerB, registerC, registerD, input1);
        int register2Value = getRegisterValue(registerA, registerB, registerC, registerD, input2);
        return register1Value | register2Value;
    }

    private static boolean canBeBori(int registerABefore, int registerBBefore, int registerCBefore,
                                     int registerDBefore, int input1, int input2, int output,
                                     int registerAAfter, int registerBAfter, int registerCAfter, int registerDAfter) {
        int bori = bori(registerABefore, registerBBefore, registerCBefore, registerDBefore, input1, input2);
        int registerResultValue = getRegisterValue(registerAAfter, registerBAfter, registerCAfter, registerDAfter, output);
        return bori == registerResultValue;
    }
    private static int bori(int registerA, int registerB, int registerC, int registerD, int input1, int input2) {
        int register1Value = getRegisterValue(registerA, registerB, registerC, registerD, input1);
        return register1Value | input2;
    }

    private static boolean canBeSetr(int registerABefore, int registerBBefore, int registerCBefore,
                                     int registerDBefore, int input1, int input2, int output,
                                     int registerAAfter, int registerBAfter, int registerCAfter, int registerDAfter) {
        int setr = setr(registerABefore, registerBBefore, registerCBefore, registerDBefore, input1);
        int registerResultValue = getRegisterValue(registerAAfter, registerBAfter, registerCAfter, registerDAfter, output);
        return setr == registerResultValue;
    }
    private static int setr(int registerA, int registerB, int registerC, int registerD, int input1) {
        return getRegisterValue(registerA, registerB, registerC, registerD, input1);
    }

    private static boolean canBeSeti(int input1, int output, int registerAAfter, int registerBAfter, int registerCAfter,
                                     int registerDAfter) {
        int seti = seti(input1);
        int registerResultValue = getRegisterValue(registerAAfter, registerBAfter, registerCAfter, registerDAfter, output);
        return seti == registerResultValue;
    }
    private static int seti(int input1) {
        return input1;
    }

    private static boolean canBeGtir(int registerABefore, int registerBBefore, int registerCBefore,
                                     int registerDBefore, int input1, int input2, int output,
                                     int registerAAfter, int registerBAfter, int registerCAfter, int registerDAfter) {
        int gtir = gtir(registerABefore, registerBBefore, registerCBefore, registerDBefore, input1, input2);
        int registerResultValue = getRegisterValue(registerAAfter, registerBAfter, registerCAfter, registerDAfter, output);
        return gtir == registerResultValue;
    }
    private static int gtir(int registerA, int registerB, int registerC, int registerD, int input1, int input2) {
        int register2Value = getRegisterValue(registerA, registerB, registerC, registerD, input2);
        return compareGreaterThanValues(input1, register2Value);
    }

    private static boolean canBeGtri(int registerABefore, int registerBBefore, int registerCBefore,
                                     int registerDBefore, int input1, int input2, int output,
                                     int registerAAfter, int registerBAfter, int registerCAfter, int registerDAfter) {
        int gtri = gtri(registerABefore, registerBBefore, registerCBefore, registerDBefore, input1, input2);
        int registerResultValue = getRegisterValue(registerAAfter, registerBAfter, registerCAfter, registerDAfter, output);
        return gtri == registerResultValue;
    }
    private static int gtri(int registerA, int registerB, int registerC, int registerD, int input1, int input2) {
        int register1Value = getRegisterValue(registerA, registerB, registerC, registerD, input1);
        return compareGreaterThanValues(register1Value, input2);
    }

    private static boolean canBeGtrr(int registerABefore, int registerBBefore, int registerCBefore,
                                     int registerDBefore, int input1, int input2, int output,
                                     int registerAAfter, int registerBAfter, int registerCAfter, int registerDAfter) {
        int gtrr = gtrr(registerABefore, registerBBefore, registerCBefore, registerDBefore, input1, input2);
        int registerResultValue = getRegisterValue(registerAAfter, registerBAfter, registerCAfter, registerDAfter, output);
        return gtrr == registerResultValue;
    }
    private static int gtrr(int registerA, int registerB, int registerC, int registerD,int input1, int input2) {
        int register1Value = getRegisterValue(registerA, registerB, registerC, registerD, input1);
        int register2Value = getRegisterValue(registerA, registerB, registerC, registerD, input2);
        return compareGreaterThanValues(register1Value, register2Value);
    }

    private static boolean canBeEqir(int registerABefore, int registerBBefore, int registerCBefore,
                                     int registerDBefore, int input1, int input2, int output,
                                     int registerAAfter, int registerBAfter, int registerCAfter, int registerDAfter) {
        int eqir = eqir(registerABefore, registerBBefore, registerCBefore, registerDBefore, input1, input2);
        int registerResultValue = getRegisterValue(registerAAfter, registerBAfter, registerCAfter, registerDAfter, output);
        return eqir == registerResultValue;
    }
    private static int eqir(int registerA, int registerB, int registerC, int registerD, int input1, int input2) {
        int register2Value = getRegisterValue(registerA, registerB, registerC, registerD, input2);
        return compareEqualValues(input1, register2Value);
    }

    private static boolean canBeEqri(int registerABefore, int registerBBefore, int registerCBefore,
                                     int registerDBefore, int input1, int input2, int output,
                                     int registerAAfter, int registerBAfter, int registerCAfter, int registerDAfter) {
        int eqri = eqri(registerABefore, registerBBefore, registerCBefore, registerDBefore, input1, input2);
        int registerResultValue = getRegisterValue(registerAAfter, registerBAfter, registerCAfter, registerDAfter, output);
        return eqri == registerResultValue;
    }
    private static int eqri(int registerA, int registerB, int registerC, int registerD, int input1, int input2) {
        int register1Value = getRegisterValue(registerA, registerB, registerC, registerD, input1);
        return compareEqualValues(register1Value, input2);
    }

    private static boolean canBeEqrr(int registerABefore, int registerBBefore, int registerCBefore,
                                     int registerDBefore, int input1, int input2, int output,
                                     int registerAAfter, int registerBAfter, int registerCAfter, int registerDAfter) {
        int eqrr = eqrr(registerABefore, registerBBefore, registerCBefore, registerDBefore, input1, input2);
        int registerResultValue = getRegisterValue(registerAAfter, registerBAfter, registerCAfter, registerDAfter, output);
        return eqrr == registerResultValue;
    }
    private static int eqrr(int registerA, int registerB, int registerC, int registerD, int input1, int input2) {
        int register1Value = getRegisterValue(registerA, registerB, registerC, registerD, input1);
        int register2Value = getRegisterValue(registerA, registerB, registerC, registerD, input2);
        return compareEqualValues(register1Value, register2Value);
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