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
            int[] registersBefore = new int[4];
            registersBefore[0] = Integer.parseInt(beforeRegisters[1].substring(1, 2));
            registersBefore[1] = Integer.parseInt(beforeRegisters[2].substring(0, 1));
            registersBefore[2] = Integer.parseInt(beforeRegisters[3].substring(0, 1));
            registersBefore[3] = Integer.parseInt(beforeRegisters[4].substring(0, 1));

            String[] instructions = scanner.nextLine().split(" ");
            int opcode = Integer.parseInt(instructions[0]);
            int input1 = Integer.parseInt(instructions[1]);
            int input2 = Integer.parseInt(instructions[2]);
            int output = Integer.parseInt(instructions[3]);

            String[] afterRegisters = scanner.nextLine().split(" ");
            int[] registersAfter = new int[4];
            registersAfter[0] = Integer.parseInt(afterRegisters[2].substring(1, 2));
            registersAfter[1] = Integer.parseInt(afterRegisters[3].substring(0, 1));
            registersAfter[2] = Integer.parseInt(afterRegisters[4].substring(0, 1));
            registersAfter[3] = Integer.parseInt(afterRegisters[5].substring(0, 1));

            int possibleOpcodesCount = mapOpcodesAndGetPossibleOpcodesCount(registersBefore, input1, input2, output,
                    registersAfter, opcodeToInstruction.get(opcode));
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

    private static int mapOpcodesAndGetPossibleOpcodesCount(int[] registersBefore, int input1, int input2, int output,
                                                            int[] registersAfter, Set<Integer> possibleInstructions) {
        int possibleOpcodes = 0;
        int instructionId = 0;

        if (canBeAddr(registersBefore, input1, input2, output, registersAfter)) {
            possibleOpcodes++;
        } else {
            possibleInstructions.remove(instructionId);
        }
        instructionId++;

        if (canBeAddi(registersBefore, input1, input2, output, registersAfter)) {
            possibleOpcodes++;
        } else {
            possibleInstructions.remove(instructionId);
        }
        instructionId++;

        if (canBeMulr(registersBefore, input1, input2, output, registersAfter)) {
            possibleOpcodes++;
        } else {
            possibleInstructions.remove(instructionId);
        }
        instructionId++;

        if (canBeMuli(registersBefore, input1, input2, output, registersAfter)) {
            possibleOpcodes++;
        } else {
            possibleInstructions.remove(instructionId);
        }
        instructionId++;

        if (canBeBanr(registersBefore, input1, input2, output, registersAfter)) {
            possibleOpcodes++;
        } else {
            possibleInstructions.remove(instructionId);
        }
        instructionId++;

        if (canBeBani(registersBefore, input1, input2, output, registersAfter)) {
            possibleOpcodes++;
        } else {
            possibleInstructions.remove(instructionId);
        }
        instructionId++;

        if (canBeBorr(registersBefore, input1, input2, output, registersAfter)) {
            possibleOpcodes++;
        } else {
            possibleInstructions.remove(instructionId);
        }
        instructionId++;

        if (canBeBori(registersBefore, input1, input2, output, registersAfter)) {
            possibleOpcodes++;
        } else {
            possibleInstructions.remove(instructionId);
        }
        instructionId++;

        if (canBeSetr(registersBefore, input1, input2, output, registersAfter)) {
            possibleOpcodes++;
        } else {
            possibleInstructions.remove(instructionId);
        }
        instructionId++;

        if (canBeSeti(input1, output, registersAfter)) {
            possibleOpcodes++;
        } else {
            possibleInstructions.remove(instructionId);
        }
        instructionId++;

        if (canBeGtir(registersBefore, input1, input2, output, registersAfter)) {
            possibleOpcodes++;
        } else {
            possibleInstructions.remove(instructionId);
        }
        instructionId++;

        if (canBeGtri(registersBefore, input1, input2, output, registersAfter)) {
            possibleOpcodes++;
        } else {
            possibleInstructions.remove(instructionId);
        }
        instructionId++;

        if (canBeGtrr(registersBefore, input1, input2, output, registersAfter)) {
            possibleOpcodes++;
        } else {
            possibleInstructions.remove(instructionId);
        }
        instructionId++;

        if (canBeEqir(registersBefore, input1, input2, output, registersAfter)) {
            possibleOpcodes++;
        } else {
            possibleInstructions.remove(instructionId);
        }
        instructionId++;

        if (canBeEqri(registersBefore, input1, input2, output, registersAfter)) {
            possibleOpcodes++;
        } else {
            possibleInstructions.remove(instructionId);
        }
        instructionId++;

        if (canBeEqrr(registersBefore, input1, input2, output, registersAfter)) {
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
        int registers[] = new int[4];

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
                case 0: result = addr(registers, input1, input2); break;
                case 1: result = addi(registers, input1, input2); break;
                case 2: result = mulr(registers, input1, input2); break;
                case 3: result = muli(registers, input1, input2); break;
                case 4: result = banr(registers, input1, input2); break;
                case 5: result = bani(registers, input1, input2); break;
                case 6: result = borr(registers, input1, input2); break;
                case 7: result = bori(registers, input1, input2); break;
                case 8: result = setr(registers, input1); break;
                case 9: result = seti(input1); break;
                case 10: result = gtir(registers, input1, input2); break;
                case 11: result = gtri(registers, input1, input2); break;
                case 12: result = gtrr(registers, input1, input2); break;
                case 13: result = eqir(registers, input1, input2); break;
                case 14: result = eqri(registers, input1, input2); break;
                case 15: result = eqrr(registers, input1, input2); break;
            }

            registers[output] = result;
        }

        return registers[0];
    }

    private static boolean canBeAddr(int[] registersBefore, int input1, int input2, int output, int[] registersAfter) {
        int addr = addr(registersBefore, input1, input2);
        return addr == registersAfter[output];
    }
    private static int addr(int[] registers, int input1, int input2) {
        return registers[input1] + registers[input2];
    }

    private static boolean canBeAddi(int[] registersBefore, int input1, int input2, int output, int[] registersAfter) {
        int addi = addi(registersBefore, input1, input2);
        return addi == registersAfter[output];
    }
    private static int addi(int[] registers, int input1, int input2) {
        return registers[input1] + input2;
    }

    private static boolean canBeMulr(int[] registersBefore, int input1, int input2, int output, int[] registersAfter) {
        int mulr = mulr(registersBefore, input1, input2);
        return mulr == registersAfter[output];
    }
    private static int mulr(int[] registers, int input1, int input2) {
        return registers[input1] * registers[input2];
    }

    private static boolean canBeMuli(int[] registersBefore, int input1, int input2, int output, int[] registersAfter) {
        int muli = muli(registersBefore, input1, input2);
        return muli == registersAfter[output];
    }
    private static int muli(int[] registers, int input1, int input2) {
        return registers[input1] * input2;
    }

    private static boolean canBeBanr(int[] registersBefore,  int input1, int input2, int output, int[] registersAfter) {
        int banr = banr(registersBefore, input1, input2);
        return banr == registersAfter[output];
    }
    private static int banr(int[] registers, int input1, int input2) {
        return registers[input1] & registers[input2];
    }

    private static boolean canBeBani(int[] registersBefore, int input1, int input2, int output, int[] registersAfter) {
        int bani = bani(registersBefore, input1, input2);
        return bani == registersAfter[output];
    }
    private static int bani(int[] registers, int input1, int input2) {
        return registers[input1] & input2;
    }

    private static boolean canBeBorr(int[] registersBefore, int input1, int input2, int output, int[] registersAfter) {
        int borr = borr(registersBefore, input1, input2);
        return borr == registersAfter[output];
    }
    private static int borr(int[] registers, int input1, int input2) {
        return registers[input1] | registers[input2];
    }

    private static boolean canBeBori(int[] registersBefore, int input1, int input2, int output, int[] registersAfter) {
        int bori = bori(registersBefore, input1, input2);
        return bori == registersAfter[output];
    }
    private static int bori(int[] registers, int input1, int input2) {
        return registers[input1] | input2;
    }

    private static boolean canBeSetr(int[] registersBefore, int input1, int input2, int output, int[] registersAfter) {
        int setr = setr(registersBefore, input1);
        return setr == registersAfter[output];
    }
    private static int setr(int[] registers, int input1) {
        return registers[input1];
    }

    private static boolean canBeSeti(int input1, int output, int[] registersAfter) {
        int seti = seti(input1);
        return seti == registersAfter[output];
    }
    private static int seti(int input1) {
        return input1;
    }

    private static boolean canBeGtir(int[] registersBefore, int input1, int input2, int output, int[] registersAfter) {
        int gtir = gtir(registersBefore, input1, input2);
        return gtir == registersAfter[output];
    }
    private static int gtir(int[] registers, int input1, int input2) {
        return compareGreaterThanValues(input1, registers[input2]);
    }

    private static boolean canBeGtri(int[] registersBefore, int input1, int input2, int output, int[] registersAfter) {
        int gtri = gtri(registersBefore, input1, input2);
        return gtri == registersAfter[output];
    }
    private static int gtri(int[] registers, int input1, int input2) {
        return compareGreaterThanValues(registers[input1], input2);
    }

    private static boolean canBeGtrr(int[] registersBefore, int input1, int input2, int output, int[] registersAfter) {
        int gtrr = gtrr(registersBefore, input1, input2);
        return gtrr == registersAfter[output];
    }
    private static int gtrr(int[] registers,int input1, int input2) {
        return compareGreaterThanValues(registers[input1], registers[input2]);
    }

    private static boolean canBeEqir(int[] registersBefore, int input1, int input2, int output, int[] registersAfter) {
        int eqir = eqir(registersBefore, input1, input2);
        return eqir == registersAfter[output];
    }
    private static int eqir(int[] registers, int input1, int input2) {
        return compareEqualValues(input1, registers[input2]);
    }

    private static boolean canBeEqri(int[] registersBefore, int input1, int input2, int output, int[] registersAfter) {
        int eqri = eqri(registersBefore, input1, input2);
        return eqri == registersAfter[output];
    }
    private static int eqri(int[] registers, int input1, int input2) {
        return compareEqualValues(registers[input1], input2);
    }

    private static boolean canBeEqrr(int[] registersBefore, int input1, int input2, int output, int[] registersAfter) {
        int eqrr = eqrr(registersBefore, input1, input2);
        return eqrr == registersAfter[output];
    }
    private static int eqrr(int[] registers, int input1, int input2) {
        return compareEqualValues(registers[input1], registers[input2]);
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