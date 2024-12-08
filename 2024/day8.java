import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class day8 {
    static class Point {
        final int row, col;
        
        Point(int row, int col) {
            this.row = row;
            this.col = col;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Point)) return false;
            Point point = (Point) o;
            return row == point.row && col == point.col;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }

    public static void main(String[] args) {
        String inputFilePath = "input.txt";
        try {
            List<String> inputLines = Files.readAllLines(Paths.get(inputFilePath));
            int result = solvePart1(inputLines);
            System.out.println("Part 1: " + result);

            result = solvePart2(inputLines);
            System.out.println("Part 2: " + result);

        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    private static Map<Character, List<Point>> collectAntennasByFrequency(List<String> lines) {
        Map<Character, List<Point>> antennas = new HashMap<>();
        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            for (int col = 0; col < line.length(); col++) {
                char c = line.charAt(col);
                if (Character.isLetterOrDigit(c)) {
                    antennas.computeIfAbsent(c, k -> new ArrayList<>())
                           .add(new Point(row, col));
                }
            }
        }
        return antennas;
    }

    private static Set<Point> getLinePositions(Point a, Point b, int numRows, int numCols) {
        Set<Point> positions = new HashSet<>();
        int dr = b.row - a.row;
        int dc = b.col - a.col;
        int gcd = gcd(Math.abs(dr), Math.abs(dc));
        if (gcd == 0) gcd = 1;
        
        int stepR = dr / gcd;
        int stepC = dc / gcd;
        
        for (int direction : new int[]{1, -1}) {
            int r = a.row;
            int c = a.col;
            while (r >= 0 && r < numRows && c >= 0 && c < numCols) {
                positions.add(new Point(r, c));
                r += stepR * direction;
                c += stepC * direction;
            }
        }
        return positions;
    }

    private static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    private static int solvePart1(List<String> inputLines) {
        Map<Character, List<Point>> antennas = collectAntennasByFrequency(inputLines);
        Set<Point> antinodes = new HashSet<>();
        int numRows = inputLines.size();
        int numCols = inputLines.get(0).length();

        for (List<Point> pairs : antennas.values()) {
            for (int i = 0; i < pairs.size(); i++) {
                for (int j = i + 1; j < pairs.size(); j++) {
                    Point a = pairs.get(i);
                    Point b = pairs.get(j);
                    
                    Point[] antinodePoints = {
                        new Point(2 * b.row - a.row, 2 * b.col - a.col),
                        new Point(2 * a.row - b.row, 2 * a.col - b.col)
                    };
                    
                    for (Point p : antinodePoints) {
                        if (p.row >= 0 && p.row < numRows && p.col >= 0 && p.col < numCols) {
                            antinodes.add(p);
                        }
                    }
                }
            }
        }
        return antinodes.size();
    }

    private static int solvePart2(List<String> inputLines) {
        Map<Character, List<Point>> antennas = collectAntennasByFrequency(inputLines);
        Set<Point> antinodes = new HashSet<>();
        int numRows = inputLines.size();
        int numCols = inputLines.get(0).length();

        for (List<Point> pairs : antennas.values()) {
            for (int i = 0; i < pairs.size(); i++) {
                for (int j = i + 1; j < pairs.size(); j++) {
                    antinodes.addAll(getLinePositions(pairs.get(i), pairs.get(j), numRows, numCols));
                }
            }
        }
        return antinodes.size();
    }
}
