import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Rene Argento on 23/12/18.
 */
public class Day23 {

    private static class Point {
        long x;
        long y;
        long z;
        int radius;

        Point(long x, long y, long z, int radius) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.radius = radius;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<Point> points = new ArrayList<>();

        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] sections = line.split(" ");

            int point1Start = sections[0].indexOf("<") + 1;
            int point1End = sections[0].indexOf(",");
            int point2Start = point1End + 1;
            int point2End = sections[0].indexOf(",", point2Start);
            int point3Start = point2End + 1;
            int point3End = sections[0].length() - 2;

            int x = Integer.parseInt(sections[0].substring(point1Start, point1End));
            int y = Integer.parseInt(sections[0].substring(point2Start, point2End));
            int z = Integer.parseInt(sections[0].substring(point3Start, point3End));

            int radius = Integer.parseInt(sections[1].substring(2));

            Point point = new Point(x, y, z, radius);
            points.add(point);
        }

        Point highestRadiusPoint = getHighestRadiusPoint(points);
        int pointsInRange = getPointsInRange(points, highestRadiusPoint);
        System.out.println("Points in range: " + pointsInRange);

        Point pointWithMostIntersections = getPointWithMostIntersections(points);
        long distance = getPointsDistance(pointWithMostIntersections, new Point(0, 0, 0, 0));
        System.out.println("Distance from source to point with most intersections: " + distance);
    }

    private static int getPointsInRange(List<Point> points, Point highestRadiusPoint) {
        int pointsInRange = 0;

        int highestRadius = highestRadiusPoint.radius;

        for (Point point : points) {
            if (getPointsDistance(highestRadiusPoint, point) <= highestRadius) {
                pointsInRange++;
            }
        }

        return pointsInRange;
    }

    private static Point getHighestRadiusPoint(List<Point> points) {
        int highestRadius = 0;
        Point highestRadiusPoint = null;

        for (Point point : points) {
            if (point.radius > highestRadius) {
                highestRadius = point.radius;
                highestRadiusPoint = point;
            }
        }

        return highestRadiusPoint;
    }

    private static long getPointsDistance(Point point1, Point point2) {
        return Math.abs(point1.x - point2.x) + Math.abs(point1.y - point2.y) + Math.abs(point1.z - point2.z);
    }

    private static Point getPointWithMostIntersections(List<Point> points) {

        long minX = 0;
        long minY = 0;
        long minZ = 0;
        long maxX = 0;
        long maxY = 0;
        long maxZ = 0;

        for (Point point : points) {
            if (point.x > maxX) {
                maxX = point.x;
            }
            if (point.y > maxY) {
                maxY = point.y;
            }
            if (point.z > maxZ) {
                maxZ = point.z;
            }
        }

        Point pointWithMostIntersections = null;
        Point source = new Point(0, 0, 0, 0);
        int mostIntersections = 0;
        int distance = 1;

        while (distance < maxX - minX) {
            distance *= 2;
        }

        while (distance >= 1) {
            for (long x = minX; x <= maxX; x += distance) {
                for (long y = minY; y <= maxY; y += distance) {
                    for (long z = minZ; z <= maxZ; z += distance) {
                        Point currentPoint = new Point(x, y, z, 0);
                        int intersections = 0;

                        for (Point point : points) {
                            long pointsDistance = getPointsDistance(currentPoint, point);
                            if (pointsDistance <= point.radius) {
                                intersections++;
                            }
                        }

                        if (intersections > mostIntersections || pointWithMostIntersections == null) {
                            mostIntersections = intersections;
                            pointWithMostIntersections = currentPoint;
                        } else if (intersections == mostIntersections) {
                            if (getPointsDistance(currentPoint, source) < getPointsDistance(pointWithMostIntersections, source)) {
                                pointWithMostIntersections = currentPoint;
                            }
                        }
                    }
                }
            }

            minX = pointWithMostIntersections.x - distance;
            maxX = pointWithMostIntersections.x + distance;
            minY = pointWithMostIntersections.y - distance;
            maxY = pointWithMostIntersections.y + distance;
            minZ = pointWithMostIntersections.z - distance;
            maxZ = pointWithMostIntersections.z + distance;
            distance = distance / 2;
        }

        return pointWithMostIntersections;
    }

}