import java.util.*;

/**
 * Created by Rene Argento on 11/12/18.
 */
public class Day14 {

    public static void main(String[] args) {
        int recipes = 157901;
        System.out.println("Score: " + getScore(recipes + 10));

        String targetRecipe = "157901";
        System.out.println("Recipe number: " + getRecipe(targetRecipe));
    }

    private static String getScore(int recipes) {
        DoublyLinkedListCircular doublyLinkedListCircular = new DoublyLinkedListCircular();
        DoublyLinkedListCircular.DoubleNode elf1 = doublyLinkedListCircular.insertAtTheEnd(3);
        DoublyLinkedListCircular.DoubleNode elf2 = doublyLinkedListCircular.insertAtTheEnd(7);

        for (int recipe = 2; recipe < recipes; recipe++) {
            int elf1Score = elf1.item;
            int elf2Score = elf2.item;

            String newScore = String.valueOf(elf1Score + elf2Score);

            int newScore1 = Integer.parseInt(newScore.substring(0, 1));
            doublyLinkedListCircular.insertAtTheEnd(newScore1);

            if (newScore.length() > 1) {
                int newScore2 = Integer.parseInt(newScore.substring(1, 2));
                doublyLinkedListCircular.insertAtTheEnd(newScore2);
                recipe++;
            }

            int indexesToMoveElf1 = elf1Score + 1;
            int indexesToMoveElf2 = elf2Score + 1;

            elf1 = doublyLinkedListCircular.getNode(elf1, indexesToMoveElf1);
            elf2 = doublyLinkedListCircular.getNode(elf2, indexesToMoveElf2);
        }

        List<Integer> lastItems = doublyLinkedListCircular.getLastXItems(10);
        return getLastRecipes(lastItems);
    }

    private static long getRecipe(String targetRecipe) {
        DoublyLinkedListCircular doublyLinkedListCircular = new DoublyLinkedListCircular();
        DoublyLinkedListCircular.DoubleNode elf1 = doublyLinkedListCircular.insertAtTheEnd(3);
        DoublyLinkedListCircular.DoubleNode elf2 = doublyLinkedListCircular.insertAtTheEnd(7);
        int recipeLength = 6;

        long recipeNumber = 3;

        while (true){
            int elf1Score = elf1.item;
            int elf2Score = elf2.item;

            String newScore = String.valueOf(elf1Score + elf2Score);

            int newScore1 = Integer.parseInt(newScore.substring(0, 1));
            doublyLinkedListCircular.insertAtTheEnd(newScore1);

            if (recipeNumber > 5) {
                if (checkRecipe(doublyLinkedListCircular, recipeLength, targetRecipe)) {
                    return recipeNumber - recipeLength;
                }
            }

            if (newScore.length() > 1) {
                int newScore2 = Integer.parseInt(newScore.substring(1, 2));
                doublyLinkedListCircular.insertAtTheEnd(newScore2);
                recipeNumber++;

                if (recipeNumber > 5) {
                    if (checkRecipe(doublyLinkedListCircular, recipeLength, targetRecipe)) {
                        return recipeNumber - recipeLength;
                    }
                }
            }

            int indexesToMoveElf1 = elf1Score + 1;
            int indexesToMoveElf2 = elf2Score + 1;

            elf1 = doublyLinkedListCircular.getNode(elf1, indexesToMoveElf1);
            elf2 = doublyLinkedListCircular.getNode(elf2, indexesToMoveElf2);
            recipeNumber++;
        }
    }

    private static boolean checkRecipe(DoublyLinkedListCircular doublyLinkedListCircular, int recipeLength,
                                       String targetRecipe) {
        List<Integer> lastItems = doublyLinkedListCircular.getLastXItems(recipeLength);
        String recipes = getLastRecipes(lastItems);
        return recipes.equals(targetRecipe);
    }

    private static String getLastRecipes(List<Integer> items) {
        StringBuilder recipes = new StringBuilder();

        for (int item : items) {
            recipes.insert(0, item);
        }

        return recipes.toString();
    }

    private static class DoublyLinkedListCircular {

        public class DoubleNode {
            private int item;
            DoubleNode previous;
            DoubleNode next;
        }

        private int size;
        private DoubleNode first;
        private DoubleNode last;

        public boolean isEmpty() {
            return size == 0;
        }

        public DoubleNode insertAtTheEnd(int item) {
            DoubleNode oldLast = last;

            last = new DoubleNode();
            last.item = item;
            last.previous = oldLast;

            if (!isEmpty()) {
                last.next = oldLast.next;
                oldLast.next = last;
            } else {
                first = last;
                last.next = first;
            }

            first.previous = last;
            size++;
            return last;
        }

        public List<Integer> getLastXItems(int items) {
            if (size < items) {
                return null;
            }

            Stack<Integer> stack = new Stack<>();
            DoubleNode current = last;

            stack.push(current.item);

            int count = 0;
            while (count < items - 1) {
                current = current.previous;
                stack.push(current.item);
                count++;
            }

            return stack;
        }

        public DoubleNode getNode(DoubleNode node, int indexesToMove) {
            int moved = 0;

            while (moved < indexesToMove){
                node = node.next;
                moved++;
            }

            return node;
        }
    }

}