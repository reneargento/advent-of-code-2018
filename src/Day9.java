import java.util.Scanner;

/**
 * Created by Rene Argento on 09/12/18.
 */
public class Day9 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String[] input = scanner.nextLine().split(" ");
        int numberOfPlayers = Integer.parseInt(input[0]);
        int lastMarble = Integer.parseInt(input[6]);
        int lastMarblePart2 = lastMarble * 100;

        long highScore1 = getHighScore(numberOfPlayers, lastMarble);
        long highScore2 = getHighScore(numberOfPlayers, lastMarblePart2);

        System.out.println("High score part 1: " + highScore1);
        System.out.println("High score part 2: " + highScore2);
    }

    private static long getHighScore(int numberOfPlayers, int lastMarble) {
        long highScore = 0;

        DoublyLinkedListCircular doublyLinkedListCircular = new DoublyLinkedListCircular();
        DoublyLinkedListCircular.DoubleNode currentMarble = doublyLinkedListCircular.insertAtTheBeginning(0);
        long[] scores = new long[numberOfPlayers];
        int playerIndex = 0;

        for (int marble = 1; marble <= lastMarble; marble++) {
            if (marble % 23 == 0) {
                scores[playerIndex] += marble;
                scores[playerIndex] += doublyLinkedListCircular.getItem(currentMarble, 7);
                currentMarble = doublyLinkedListCircular.removeAtIndexAndReturnNext(currentMarble, 7);

                if (scores[playerIndex] > highScore) {
                    highScore = scores[playerIndex];
                }
            } else {
                currentMarble = doublyLinkedListCircular.insertAtIndex(currentMarble, marble, 1);
            }

            playerIndex++;

            if (playerIndex == numberOfPlayers) {
                playerIndex = 0;
            }
        }

        return highScore;
    }

    private static class DoublyLinkedListCircular {

        public class DoubleNode {
            DoubleNode(int item, DoubleNode previous, DoubleNode next) {
                this.item = item;
                this.previous = previous;
                this.next = next;
            }

            DoubleNode() {
            }

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

        public DoubleNode insertAtIndex(DoubleNode currentNode, int item, int index) {
            DoubleNode newNode;
            DoubleNode leftNode = currentNode;

            for (int i = 0; i < index; i++) {
                leftNode = leftNode.next;
            }

            DoubleNode oldNext = leftNode.next;
            newNode = new DoubleNode(item, leftNode, oldNext);
            leftNode.next = newNode;
            oldNext.previous = newNode;

            return newNode;
        }

        public DoubleNode insertAtTheBeginning(int item) {
            DoubleNode oldFirst = first;

            first = new DoubleNode();
            first.item = item;
            first.next = oldFirst;

            if (!isEmpty()) {
                first.previous = oldFirst.previous;
                oldFirst.previous = first;
            } else {
                last = first;
                first.previous = last;
            }

            last.next = first;
            size++;

            return first;
        }

        public int getItem(DoubleNode node, int index) {
            for (int i = 0; i < index; i++) {
                node = node.previous;
            }

            return node.item;
        }

        public DoubleNode removeAtIndexAndReturnNext(DoubleNode node, int index) {
            for (int i = 0; i < index; i++) {
                node = node.previous;
            }

            DoubleNode nextNode = node.next;
            nextNode.previous = node.previous;
            nextNode.previous.next = nextNode;
            return nextNode;
        }
    }
}
