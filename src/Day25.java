import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Rene Argento on 25/12/18.
 */
public class Day25 {

    private static class Coordinate {
        int w;
        int x;
        int y;
        int z;
        int id;

        Coordinate(int w, int x, int y, int z, int id) {
            this.w = w;
            this.x = x;
            this.y = y;
            this.z = z;
            this.id = id;
        }
    }

    public static class UnionFind {

        private int[] leaders;
        private int[] ranks;

        private int components;

        public UnionFind(int size) {
            leaders = new int[size];
            ranks = new int[size];
            components = size;

            for(int i = 0; i < size; i++) {
                leaders[i]  = i;
                ranks[i] = 0;
            }
        }

        public int count() {
            return components;
        }

        public boolean connected(int site1, int site2) {
            return find(site1) == find(site2);
        }

        //O(inverse Ackermann function)
        public int find(int site) {
            if (site == leaders[site]) {
                return site;
            }

            return leaders[site] = find(leaders[site]);
        }

        //O(inverse Ackermann function)
        public void union(int site1, int site2) {

            int leader1 = find(site1);
            int leader2 = find(site2);

            if (leader1 == leader2) {
                return;
            }

            if (ranks[leader1] < ranks[leader2]) {
                leaders[leader1] = leader2;
            } else if (ranks[leader2] < ranks[leader1]) {
                leaders[leader2] = leader1;
            } else {
                leaders[leader1] = leader2;
                ranks[leader2]++;
            }

            components--;
        }

    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Coordinate> coordinates = new ArrayList<>();
        int id = 0;

        while (scanner.hasNext()) {
            String[] values = scanner.nextLine().split(",");

            int w = Integer.parseInt(values[0]);
            int x = Integer.parseInt(values[1]);
            int y = Integer.parseInt(values[2]);
            int z = Integer.parseInt(values[3]);

            coordinates.add(new Coordinate(w, x, y, z, id));
            id++;
        }

        UnionFind unionFind = new UnionFind(coordinates.size());

        for (Coordinate coordinate1 : coordinates) {
            for (Coordinate coordinate2 : coordinates) {
                if (coordinate1 == coordinate2) {
                    continue;
                }

                if (manhattanDistance(coordinate1, coordinate2) <= 3) {
                    unionFind.union(coordinate1.id, coordinate2.id);
                }
            }
        }

        System.out.println("Number of constellations: " + unionFind.count());
    }

    private static int manhattanDistance(Coordinate coordinate1, Coordinate coordinate2) {
        return Math.abs(coordinate1.w - coordinate2.w) + Math.abs(coordinate1.x - coordinate2.x) +
                Math.abs(coordinate1.y - coordinate2.y) + Math.abs(coordinate1.z - coordinate2.z);
    }

}
