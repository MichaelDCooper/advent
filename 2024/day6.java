import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class day6 {
    private static final int[] DX = { 0, 1, 0, -1 };
    private static final int[] DY = { -1, 0, 1, 0 };

    static class Position {
        int x, y;
        int direction;

        Position(int x, int y, int direction) {
            this.x = x;
            this.y = y;
            this.direction = direction;
        }

        Position(int x, int y) {
            this(x, y, 0);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof Position))
                return false;
            Position position = (Position) o;
            return x == position.x && y == position.y && direction == position.direction;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, direction);
        }
    }

    private static boolean simulateWithObstacle(char[][] grid, int startX, int startY, int startDir,
            Position obstacle) {
        Set<Position> visitedStates = new HashSet<>();
        int currentX = startX;
        int currentY = startY;
        int direction = startDir;

        while (true) {
            Position currentState = new Position(currentX, currentY, direction);
            if (!visitedStates.add(currentState)) {
                return true;
            }

            int nextX = currentX + DX[direction];
            int nextY = currentY + DY[direction];

            if (nextY < 0 || nextY >= grid.length || nextX < 0 || nextX >= grid[0].length) {
                return false;
            }

            if (grid[nextY][nextX] == '#' || (nextX == obstacle.x && nextY == obstacle.y)) {
                direction = (direction + 1) % 4;
            } else {
                currentX = nextX;
                currentY = nextY;
            }
        }
    }

    public static void main(String[] args) {
        String inputFilePath = "input.txt";
        try {
            List<String> inputLines = Files.readAllLines(Paths.get(inputFilePath));
            int result = solvePart2(inputLines);
            System.out.println("Number of possible obstruction positions: " + result);
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    private static int solvePart2(List<String> inputLines) {
        char[][] grid = new char[inputLines.size()][inputLines.get(0).length()];
        int startX = 0, startY = 0;
        int startDirection = 0;

        for (int y = 0; y < inputLines.size(); y++) {
            String line = inputLines.get(y);
            for (int x = 0; x < line.length(); x++) {
                grid[y][x] = line.charAt(x);
                if (grid[y][x] == '^') {
                    startX = x;
                    startY = y;
                    grid[y][x] = '.';
                }
            }
        }

        int validPositions = 0;

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (grid[y][x] == '#' || (x == startX && y == startY)) {
                    continue;
                }

                Position obstacle = new Position(x, y);
                if (simulateWithObstacle(grid, startX, startY, startDirection, obstacle)) {
                    validPositions++;
                }
            }
        }

        return validPositions;
    }
}
