import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class day5 {

    public static void main(String[] args) {
        String inputFilePath = "input.txt";
        try {
            List<String> inputLines = Files.readAllLines(Paths.get(inputFilePath));
            int result = solvePart1(inputLines);
            System.out.println("Sum of middle page numbers: " + result);

            result = solvePart2(inputLines);
            System.out.println("Sum of middle page numbers after topological sort: " + result);

        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
        }
    }

    private static int solvePart1(List<String> inputLines) {
        List<String> rules = new ArrayList<>();
        List<List<Integer>> updates = new ArrayList<>();

        for (String line : inputLines) {
            if (line.contains("|")) {
                rules.add(line);
            } else if (!line.isEmpty()) {
                List<Integer> currentUpdate = Arrays.stream(line.split(","))
                        .map(String::trim)
                        .map(Integer::parseInt)
                        .toList();
                updates.add(currentUpdate);
            }
        }

        Map<Integer, Set<Integer>> mustComeBefore = new HashMap<>();
        Map<Integer, Set<Integer>> mustComeAfter = new HashMap<>();

        for (String rule : rules) {
            String[] parts = rule.split("\\|");
            int before = Integer.parseInt(parts[0]);
            int after = Integer.parseInt(parts[1]);
            mustComeBefore.computeIfAbsent(before, k -> new HashSet<>()).add(after);
            mustComeAfter.computeIfAbsent(after, k -> new HashSet<>()).add(before);
        }

        int sum = 0;
        for (List<Integer> update : updates) {
            if (isValidOrder(update, mustComeBefore, mustComeAfter)) {
                sum += update.get(update.size() / 2);
            } else {
            }
        }

        return sum;
    }

    private static boolean isValidOrder(List<Integer> pages,
            Map<Integer, Set<Integer>> mustComeBefore,
            Map<Integer, Set<Integer>> mustComeAfter) {

        for (int i = 0; i < pages.size(); i++) {
            int currentPage = pages.get(i);

            if (mustComeAfter.containsKey(currentPage)) {
                Set<Integer> required = mustComeAfter.get(currentPage);
                for (int j = i + 1; j < pages.size(); j++) {
                    if (required.contains(pages.get(j))) {
                        return false;
                    }
                }
            }

            // Check all pages that must come after this one
            if (mustComeBefore.containsKey(currentPage)) {
                Set<Integer> mustBeAfter = mustComeBefore.get(currentPage);
                for (int j = 0; j < i; j++) {
                    if (mustBeAfter.contains(pages.get(j))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static int solvePart2(List<String> inputLines) {
        List<String> rules = new ArrayList<>();
        List<List<Integer>> updates = new ArrayList<>();

        for (String line : inputLines) {
            if (line.contains("|")) {
                rules.add(line);
            } else if (!line.isEmpty()) {
                List<Integer> currentUpdate = Arrays.stream(line.split(","))
                        .map(String::trim)
                        .map(Integer::parseInt)
                        .toList();
                updates.add(currentUpdate);
            }
        }

        Map<Integer, Set<Integer>> mustComeBefore = new HashMap<>();
        Map<Integer, Set<Integer>> mustComeAfter = new HashMap<>();

        for (String rule : rules) {
            String[] parts = rule.split("\\|");
            int before = Integer.parseInt(parts[0]);
            int after = Integer.parseInt(parts[1]);
            mustComeBefore.computeIfAbsent(before, k -> new HashSet<>()).add(after);
            mustComeAfter.computeIfAbsent(after, k -> new HashSet<>()).add(before);
        }

        int sum = 0;
        for (List<Integer> update : updates) {
            if (!isValidOrder(update, mustComeBefore, mustComeAfter)) {
                List<Integer> sortedUpdate = topologicalSort(new ArrayList<>(update), mustComeBefore, mustComeAfter);
                int middleValue = sortedUpdate.get(sortedUpdate.size() / 2);
                sum += middleValue;
            }
        }

        return sum;
    }

    // Disclaimer: I needed help with this
    private static List<Integer> topologicalSort(List<Integer> pages,
            Map<Integer, Set<Integer>> mustComeBefore,
            Map<Integer, Set<Integer>> mustComeAfter) {

        Map<Integer, Set<Integer>> localGraph = new HashMap<>();
        Set<Integer> allPages = new HashSet<>(pages);

        for (int page : pages) {
            Set<Integer> dependencies = new HashSet<>();
            if (mustComeBefore.containsKey(page)) {
                dependencies.addAll(mustComeBefore.get(page));
            }
            dependencies.retainAll(allPages);
            localGraph.put(page, dependencies);
        }

        Map<Integer, Integer> inDegree = new HashMap<>();
        for (int page : pages) {
            inDegree.put(page, 0);
        }
        for (Set<Integer> deps : localGraph.values()) {
            for (int dep : deps) {
                inDegree.merge(dep, 1, Integer::sum);
            }
        }

        // Kahn's algorithm
        List<Integer> result = new ArrayList<>();
        Queue<Integer> queue = new LinkedList<>();

        for (int page : pages) {
            if (inDegree.get(page) == 0) {
                queue.add(page);
            }
        }

        while (!queue.isEmpty()) {
            int page = queue.poll();
            result.add(page);

            if (localGraph.containsKey(page)) {
                for (int dep : localGraph.get(page)) {
                    inDegree.merge(dep, -1, Integer::sum);
                    if (inDegree.get(dep) == 0) {
                        queue.add(dep);
                    }
                }
            }
        }

        return result;
    }
}
