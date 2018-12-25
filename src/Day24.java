import java.util.*;

/**
 * Created by Rene Argento on 24/12/18.
 */
public class Day24 {

    private static class Unit {
        private int count;
        private int hitPoints;
        List<String> weaknesses;
        List<String> immunities;
        private long attack;
        private String attackType;
        private int initiative;
        private boolean isInfection;
        private long effectivePower;
        private boolean isAlive;
        private int round;

        Unit(int count, int hitPoints, List<String> weaknesses, List<String> immunities, long attack, String attackType,
             int initiative, boolean isInfection) {
            this.count = count;
            this.hitPoints = hitPoints;
            this.weaknesses = weaknesses;
            this.immunities = immunities;
            this.attack = attack;
            this.attackType = attackType;
            this.initiative = initiative;
            this.isInfection = isInfection;
            effectivePower = count * attack;
            isAlive = true;
            round = 1;
        }

        @Override
        public int hashCode() {
            return (int) attack * initiative % hitPoints;
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof Unit)) {
                return false;
            }

            Unit otherUnit = (Unit) object;

            return count == otherUnit.count && hitPoints == otherUnit.hitPoints && attack == otherUnit.attack
                    && initiative == otherUnit.initiative && effectivePower == otherUnit.effectivePower
                    && isAlive == otherUnit.isAlive;
        }
    }

    public static void main(String[] args) {

        PriorityQueue<Unit> units = new PriorityQueue<>((unit1, unit2) -> {
            if (unit1.round != unit2.round) {
                return unit1.round - unit2.round;
            }
            if (unit1.effectivePower != unit2.effectivePower) {
                return (int) unit2.effectivePower - (int) unit1.effectivePower;
            }
            return unit2.initiative - unit1.initiative;
        });

        Set<Unit> unitsCopy = new HashSet<>();

        // Part 1
        getInput(units, unitsCopy);

        int round = 1;

        while (!battle(units, round++));

        int totalUnitsAlive = 0;

        for (Unit unit : units) {
            if (unit.isAlive) {
                totalUnitsAlive += unit.count;
            }
        }
        System.out.println("Winning army units: " + totalUnitsAlive);

        // Part 2
        int low = 0;
        int high = Integer.MAX_VALUE;
        int totalUnitsAliveAfterAttackBoost = 0;

        while (low <= high) {
            round = 1;
            int middle = low + (high - low) / 2;

            Set<Unit> unitSet = getUnitsWithAttackBoost(unitsCopy, middle);

            PriorityQueue<Unit> unitsPriorityQueue = new PriorityQueue<>((unit1, unit2) -> {
                if (unit1.round != unit2.round) {
                    return unit1.round - unit2.round;
                }
                if (unit1.effectivePower != unit2.effectivePower) {
                    return (int) unit2.effectivePower - (int) unit1.effectivePower;
                }
                return unit2.initiative - unit1.initiative;
            });

            for (Unit unit : unitSet) {
                unitsPriorityQueue.offer(unit);
            }

            while (!battle(unitsPriorityQueue, round++));

            totalUnitsAlive = 0;

            boolean battleWon = true;

            for (Unit unit : unitsPriorityQueue) {
                if (unit.isAlive && unit.isInfection) {
                    battleWon = false;
                }
            }

            if (battleWon) {
                for (Unit unit : unitsPriorityQueue) {
                    if (unit.isAlive && !unit.isInfection) {
                        totalUnitsAlive += unit.count;
                    }
                }
            }

            if (totalUnitsAlive > 0) {
                totalUnitsAliveAfterAttackBoost = totalUnitsAlive;
                high = middle - 1;
            } else {
                low = middle + 1;
            }
        }

        System.out.println("Total units alive after attack boost: " + totalUnitsAliveAfterAttackBoost);
    }

    private static boolean battle(PriorityQueue<Unit> units, int round) {
        boolean battleOver = true;
        Map<Unit, Unit> unitsSelected = new HashMap<>();
        Set<Unit> unitsSelectedSet = new HashSet<>();
        PriorityQueue<Unit> attackOrder = new PriorityQueue<>((unit1, unit2) -> unit2.initiative - unit1.initiative);

        while (!units.isEmpty() && units.peek().round == round) {
            Unit currentUnit = units.poll();
            if (!currentUnit.isAlive) {
                continue;
            }

            currentUnit.round++;
            String attackType = currentUnit.attackType;

            Unit selectedUnitToAttack = null;
            long damageDealt = 0;

            for (Unit unit : units) {
                long currentDamageDealt;

                if (currentUnit.isInfection != unit.isInfection && unit.isAlive) {
                    if (unitsSelectedSet.contains(unit)) {
                        continue;
                    } else if (unit.immunities.contains(attackType)) {
                        currentDamageDealt = 0;
                    } else if (unit.weaknesses.contains(attackType)) {
                        currentDamageDealt = currentUnit.effectivePower * 2;
                    } else {
                        currentDamageDealt = currentUnit.effectivePower;
                    }

                    if (currentDamageDealt == 0) {
                        continue;
                    }

                    if (currentDamageDealt > damageDealt) {
                        damageDealt = currentDamageDealt;
                        selectedUnitToAttack = unit;
                    } else if (currentDamageDealt == damageDealt) {
                        if (unit.effectivePower > selectedUnitToAttack.effectivePower
                                || (unit.effectivePower == selectedUnitToAttack.effectivePower
                                && unit.initiative > selectedUnitToAttack.initiative)) {
                            selectedUnitToAttack = unit;
                        }
                    }
                }
            }

            if (selectedUnitToAttack != null) {
                unitsSelected.put(currentUnit, selectedUnitToAttack);
                unitsSelectedSet.add(selectedUnitToAttack);
                attackOrder.offer(currentUnit);
            }
            units.offer(currentUnit);
        }

        while (!attackOrder.isEmpty()) {
            Unit currentUnit = attackOrder.poll();

            Unit selectedUnitToAttack = unitsSelected.get(currentUnit);

            if (selectedUnitToAttack != null && currentUnit.isAlive && selectedUnitToAttack.isAlive) {
                long unitsKilled = attack(currentUnit, selectedUnitToAttack);

                if (unitsKilled > 0) {
                    battleOver = false;
                }

                units.remove(selectedUnitToAttack);
                units.offer(selectedUnitToAttack);
            }
        }

        return battleOver;
    }

    private static long attack(Unit attackingUnit, Unit unit) {
        long damageDealt;

        if (unit.immunities.contains(attackingUnit.attackType)) {
            return 0;
        } else if (unit.weaknesses.contains(attackingUnit.attackType)) {
            damageDealt = attackingUnit.effectivePower * 2;
        } else {
            damageDealt = attackingUnit.effectivePower;
        }

        long unitsKilled = damageDealt / unit.hitPoints;
        unit.count -= unitsKilled;
        unit.effectivePower = unit.count * unit.attack;

        if (unit.count <= 0) {
            unit.isAlive = false;
        }

        return unitsKilled;
    }

    private static void getInput(PriorityQueue<Unit> units, Set<Unit> unitsCopy) {
        Scanner scanner = new Scanner(System.in);
        boolean isInfection = false;

        while (scanner.hasNext()) {
            String line = scanner.nextLine();

            if (line.equals("") || line.equals("Immune System:")) {
                continue;
            }

            if (line.equals("Infection:")) {
                isInfection = true;
                continue;
            }

            String[] values = line.split(" ");
            int count = Integer.parseInt(values[0]);
            int hitPoints = Integer.parseInt(values[4]);

            List<String> weaknesses = new ArrayList<>();
            List<String> immunities = new ArrayList<>();

            int weakIndex = line.indexOf("weak to ");
            if (weakIndex != -1) {
                weakIndex += 8;
                weaknesses = getValues(line, weakIndex);
            }

            int immuneIndex = line.indexOf("immune to ");
            if (immuneIndex != -1) {
                immuneIndex += 10;
                immunities = getValues(line, immuneIndex);
            }

            int attack = Integer.parseInt(values[values.length - 6]);
            String attackType = values[values.length - 5];
            int initiative = Integer.parseInt(values[values.length - 1]);

            Unit unit = new Unit(count, hitPoints, weaknesses, immunities, attack, attackType, initiative, isInfection);
            units.add(unit);

            Unit unitCopy = new Unit(count, hitPoints, weaknesses, immunities, attack, attackType, initiative, isInfection);
            unitsCopy.add(unitCopy);
        }
    }

    private static List<String> getValues(String line, int index) {
        List<String> values = new ArrayList<>();

        StringBuilder currentValue = new StringBuilder();
        while (true) {
            char indexChar = line.charAt(index++);
            if (indexChar == ';' || indexChar == ')') {
                values.add(currentValue.toString());
                break;
            } else if (indexChar == ',') {
                values.add(currentValue.toString());
                currentValue = new StringBuilder();
                index++;
            } else {
                currentValue.append(indexChar);
            }
        }

        return values;
    }

    private static Set<Unit> getUnitsWithAttackBoost(Set<Unit> units, int attackBoost) {
        Set<Unit> unitsWithAttackBoost = new HashSet<>();

        for (Unit unit : units) {
            Unit unitCopy = new Unit(unit.count, unit.hitPoints, unit.weaknesses, unit.immunities, unit.attack,
                    unit.attackType, unit.initiative, unit.isInfection);

            if (!unit.isInfection) {
                unitCopy.attack += attackBoost;
                unitCopy.effectivePower = unitCopy.count * unitCopy.attack;
            }

            unitsWithAttackBoost.add(unitCopy);
        }

        return unitsWithAttackBoost;
    }

}