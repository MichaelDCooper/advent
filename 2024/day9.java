import java.util.*;

public class day9 {
    public static long solve(String input) {
        List<Integer> lengths = new ArrayList<>();
        for (char c : input.toCharArray()) {
            lengths.add(Character.getNumericValue(c));
        }

        List<Integer> blocks = convertToBlocks(lengths);

        List<Integer> compactedBlocks = compactBlocksWholeFiles(blocks);

        return calculateChecksum(compactedBlocks);
    }

    private static List<Integer> convertToBlocks(List<Integer> lengths) {
        List<Integer> blocks = new ArrayList<>();
        int fileId = 0;

        for (int i = 0; i < lengths.size(); i++) {
            int length = lengths.get(i);

            if (i % 2 == 0) {
                for (int j = 0; j < length; j++) {
                    blocks.add(fileId);
                }
                fileId++;
            } else {
                for (int j = 0; j < length; j++) {
                    blocks.add(-1);
                }
            }
        }

        return blocks;
    }

    private static List<Integer> compactBlocksWholeFiles(List<Integer> blocks) {
        List<Integer> result = new ArrayList<>(blocks);

        int maxFileId = -1;
        for (int block : result) {
            maxFileId = Math.max(maxFileId, block);
        }

        for (int fileId = maxFileId; fileId >= 0; fileId--) {
            int fileStart = -1;
            int fileSize = 0;

            for (int i = 0; i < result.size(); i++) {
                if (result.get(i) == fileId) {
                    fileStart = i;
                    break;
                }
            }

            if (fileStart != -1) {
                for (int i = fileStart; i < result.size() && result.get(i) == fileId; i++) {
                    fileSize++;
                }

                int bestFreeStart = -1;
                int currentFreeStart = -1;
                int currentFreeSize = 0;

                for (int i = 0; i < fileStart; i++) {
                    if (result.get(i) == -1) {
                        if (currentFreeStart == -1) {
                            currentFreeStart = i;
                        }
                        currentFreeSize++;

                        if (currentFreeSize >= fileSize) {
                            bestFreeStart = currentFreeStart;
                            break;
                        }
                    } else {
                        currentFreeStart = -1;
                        currentFreeSize = 0;
                    }
                }

                if (bestFreeStart != -1) {
                    for (int i = 0; i < fileSize; i++) {
                        result.set(fileStart + i, -1);
                    }

                    for (int i = 0; i < fileSize; i++) {
                        result.set(bestFreeStart + i, fileId);
                    }
                }
            }
        }

        return result;
    }

    private static long calculateChecksum(List<Integer> blocks) {
        long checksum = 0;
        for (int i = 0; i < blocks.size(); i++) {
            if (blocks.get(i) != -1) {
                checksum += (long) i * blocks.get(i);
            }
        }
        return checksum;
    }

    public static void main(String[] args) {
        String input = "2333133121414131402";
        long checksum = solve(input);
        System.out.println("Filesystem checksum: " + checksum);
    }
}
