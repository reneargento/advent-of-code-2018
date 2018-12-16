import java.util.*;

/**
 * Created by Rene Argento on 13/12/18.
 */
public class Day13 {

    private static class Cart {
        int id;
        int row;
        int column;
        char direction;
        int turns;
        int tick;

        Cart(int id, int row, int column, char direction) {
            this.row = row;
            this.column = column;
            this.direction = direction;
            this.id = id;
        }
    }

    public static void main(String[] args) {
        int dimension = 300;

        char[][] tracks = new char[dimension][dimension];
        Cart[][] carLocations = new Cart[dimension][dimension];
        Queue<Cart> priorityQueue = new PriorityQueue<>((cart1, cart2) -> {
            if (cart1.tick != cart2.tick) {
                return cart1.tick - cart2.tick;
            }

            if (cart1.row != cart2.row) {
                return cart1.row - cart2.row;
            }

            if (cart1.column != cart2.column) {
                return cart1.column - cart2.column;
            }

            return 0;
        });

        int row = 0;
        int cartId = 0;
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String currentRow = scanner.nextLine();

            for (int column = 0; column < currentRow.length(); column++) {
                tracks[row][column] = currentRow.charAt(column);

                if (tracks[row][column] == '^'
                        || tracks[row][column] == 'v'
                        || tracks[row][column] == '<'
                        || tracks[row][column] == '>') {
                    Cart newCart = new Cart(cartId, row, column, tracks[row][column]);
                    priorityQueue.offer(newCart);
                    carLocations[row][column] = newCart;
                    cartId++;
                }
            }

            row++;
        }

        simulateTrack(tracks, carLocations, priorityQueue);
    }

    private static void simulateTrack(char[][] tracks, Cart[][] carLocations, Queue<Cart> priorityQueue) {
        Set<Integer> crashedCars = new HashSet<>();

        while (true) {
            // Move cart
            Cart currentCart = priorityQueue.poll();

            if (crashedCars.contains(currentCart.id)) {
                continue;
            }

            if (priorityQueue.isEmpty()) {
                System.out.println("Last cart location: " + currentCart.column + "," + currentCart.row);
                return;
            }

            char direction = currentCart.direction;
            carLocations[currentCart.row][currentCart.column] = null;

            int nextRow = currentCart.row;
            int nextColumn = currentCart.column;

            if (direction == '^') {
                nextRow = currentCart.row - 1;
            } else if (direction == 'v') {
                nextRow = currentCart.row + 1;
            } else if (direction == '<') {
                nextColumn = currentCart.column - 1;
            } else if (direction == '>') {
                nextColumn = currentCart.column + 1;
            }

            // Crash identified
            if (carLocations[nextRow][nextColumn] != null) {
                System.out.println("Crash: " + nextColumn + "," + nextRow);
                crashedCars.add(carLocations[nextRow][nextColumn].id);
                carLocations[nextRow][nextColumn] = null;
                continue;
            }

            if (tracks[nextRow][nextColumn] == '/') {
                if (direction == '^') {
                    direction = '>';
                } else if (direction == 'v') {
                    direction = '<';
                } else if (direction == '>') {
                    direction = '^';
                } else if (direction == '<') {
                    direction = 'v';
                }
            } else if (tracks[nextRow][nextColumn] == '\\') {
                    if (direction == '^') {
                    direction = '<';
                } else if (direction == '>') {
                    direction = 'v';
                } else if (direction == '<') {
                    direction = '^';
                } else if (direction == 'v') {
                    direction = '>';
                }
            } else if (tracks[nextRow][nextColumn] == '+') {
                if (direction == '^') {
                    if (currentCart.turns % 3 == 0) {
                        direction = '<';
                    } else if (currentCart.turns % 3 == 2) {
                        direction = '>';
                    }
                } else if (direction == 'v') {
                    if (currentCart.turns % 3 == 0) {
                        direction = '>';
                    } else if (currentCart.turns % 3 == 2) {
                        direction = '<';
                    }
                } else if (direction == '>') {
                    if (currentCart.turns % 3 == 0) {
                        direction = '^';
                    } else if (currentCart.turns % 3 == 2) {
                        direction = 'v';
                    }
                } else {
                    if (currentCart.turns % 3 == 0) {
                        direction = 'v';
                    } else if (currentCart.turns % 3 == 2) {
                        direction = '^';
                    }
                }

                currentCart.turns++;
            }

            currentCart.row = nextRow;
            currentCart.column = nextColumn;
            currentCart.direction = direction;
            currentCart.tick++;
            carLocations[nextRow][nextColumn] = currentCart;

            priorityQueue.offer(currentCart);
        }
    }

}