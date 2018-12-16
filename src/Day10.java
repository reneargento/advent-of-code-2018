import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Rene Argento on 10/12/18.
 */
public class Day10 {

    private static class Point {
        int x;
        int y;
        int velocityX;
        int velocityY;

        Point(int x, int y, int velocityX, int velocityY) {
            this.x = x;
            this.y = y;
            this.velocityX = velocityX;
            this.velocityY = velocityY;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<Point> points = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            int x = Integer.parseInt(line.substring(10, 16).trim());
            int y = Integer.parseInt(line.substring(18, 24).trim());
            int velocityX = Integer.parseInt(line.substring(36, 38).trim());
            int velocityY = Integer.parseInt(line.substring(40, 42).trim());

            points.add(new Point(x, y, velocityX, velocityY));
        }

        movePoints(points);
    }

    private static void movePoints(List<Point> points) {
        for (int second = 1; second < 10150; second++) {
            for (Point point : points) {
                point.x = point.x + point.velocityX;
                point.y = point.y + point.velocityY;
            }

            if (second >= 10140) {
                printPoints(points, second);
                System.out.println();
                System.out.println();
            }
        }
    }

    private static void printPoints(List<Point> points, int second) {
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Point point : points) {
            if (point.x < minX) {
                minX = point.x;
            }
            if (point.y < minY) {
                minY = point.y;
            }
            if (point.x > maxX) {
                maxX = point.x;
            }
            if (point.y > maxY) {
                maxY = point.y;
            }
        }

        System.out.println("Min: " + minX + " Max: " + maxX);
        System.out.println("Id: " + second + " Width: " + (maxX - minX));

        char[][] pointsMap = new char[210][210];

        for (int i = 0; i < pointsMap.length; i++) {
            for (int j = 0; j < pointsMap[0].length; j++) {
                pointsMap[i][j] = '.';
            }
        }

        for (Point point : points) {
            pointsMap[point.x][point.y] = '#';
        }

        for (int row = 0; row < pointsMap.length; row++) {
            for (int column = 0; column < pointsMap[0].length; column++) {
                System.out.print(pointsMap[column][row] + " ");
            }
            System.out.println();
        }
    }
}
