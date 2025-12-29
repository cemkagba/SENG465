import java.util.*;

// Helper class to store and compare algorithm results
class AlgorithmResult implements Comparable<AlgorithmResult> {
    String name;
    long time;
    boolean found;

    public AlgorithmResult(String name, long time, boolean found) {
        this.name = name;
        this.time = time;
        this.found = found;
    }

    @Override
    public int compareTo(AlgorithmResult other) {
        // Sort by time in ascending order (Fastest first)
        return Long.compare(this.time, other.time);
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1. Board Size (Default 8x8)
        int size = 8;
        Board board = new Board(size);

        System.out.println("\nKNIGHT PATH FINDING SIMULATION");
        System.out.println("\nPlease enter coordinates in 'a1', 'h8' format.");

        // 2. Get User Inputs
        // Starting Position
        int[] startPos = getValidInput(scanner, "Knight Starting Position (e.g., b1): ", size);
        int startRow = startPos[0];
        int startCol = startPos[1];

        // Bomb Position
        int[] bombPos = getValidInput(scanner, "Bomb Position (e.g., e4): ", size);
        board.setBomb(bombPos[0], bombPos[1]);

        // Coin (Goal) Position
        int[] coinPos = getValidInput(scanner, "Coin (Goal) Position (e.g., h8): ", size);
        board.setCoin(coinPos[0], coinPos[1]);

        // Display the Initial Board
        System.out.println("\nGenerated Board");
        board.printBoard(startRow, startCol);

        // List to store results for sorting
        List<AlgorithmResult> results = new ArrayList<>();

        // 3. EXECUTE ALGORITHMS

        // Run IDDFS
        System.out.println("\n>>> Running IDDFS Algorithm...");

        IDDFS iddfs = new IDDFS(board, bombPos[0], bombPos[1], coinPos[0], coinPos[1]);
        iddfs.search(startRow, startCol);
        iddfs.printResults();

        // Add to results list for later comparison
        results.add(new AlgorithmResult("IDDFS", iddfs.getExecutionTime(), iddfs.isSolutionFound()));

        // --- FUTURE ALGORITHMS (Placeholders) ---
        // When you implement BFS, DFS, A*, add them here like this:
        // BFS bfs = new BFS(board, ...);
        // bfs.search(...);
        // results.add(new AlgorithmResult("BFS", bfs.getExecutionTime(), bfs.isSolutionFound()));


        // 4. PERFORMANCE COMPARISON
        // Sort and print algorithms based on time consumption

        System.out.println("\nPERFORMANCE COMPARISON (Fastest to Slowest)");

        Collections.sort(results);

        System.out.printf("%-20s %-15s %-10s\n", "Algorithm", "Time (ms)", "Result");
        System.out.println("-----------------------------------------------");

        for (AlgorithmResult res : results) {
            String status = res.found ? "Found" : "Failed";
            System.out.printf("%-20s %-15d %-10s\n", res.name, res.time, status);
        }

        scanner.close();
    }

    /**
     * Helper method to get valid input from user and convert to coordinates.
     * e.g., "a1" -> {0, 0}, "b3" -> {2, 1}
     */
    private static int[] getValidInput(Scanner scanner, String prompt, int size) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();

            // Basic format check
            if (input.length() < 2) {
                System.out.println("Error: Invalid format. Please use format like 'a1'.");
                continue;
            }

            char colChar = input.charAt(0); // 'a', 'b', ...
            String rowStr = input.substring(1); // "1", "10", ...

            // Convert Column char to index ('a' -> 0)
            int col = colChar - 'a';

            // Convert Row string to index (User input 1 becomes index 0)
            int row;
            try {
                row = Integer.parseInt(rowStr) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Error: Row part must be a number.");
                continue;
            }

            // Boundary Check
            if (col >= 0 && col < size && row >= 0 && row < size) {
                return new int[]{row, col};
            } else {
                System.out.println("Error: Coordinates out of bounds (a-h, 1-" + size + ").");
            }
        }
    }
    // Node nesnelerini oluştur
    Node startNode = new Node(startRow, startCol, null);
    Node targetNode = new Node(coinRow, coinCol, null);

    // 3. Algoritmayı Çağır
    GreedyBestFirstSearch solver = new GreedyBestFirstSearch();

    System.out.println("Araniyor... Start: " + startRow + "," + startCol + " -> Hedef: " + coinRow + "," + coinCol);

    List<Node> path = solver.greedyBestFirstSearch(board, startNode, targetNode);

    // 4. Sonucu Gör
    if (path != null) {
        System.out.println("Yol Bulundu! Adım Sayısı: " + (path.size() - 1)); // Başlangıç düğümü hariç adım sayısı
        for (Node node : path) {
            System.out.print("(" + node.row + "," + node.col + ") -> ");
        }
        System.out.println("HEDEF");
    } else {
        System.out.println("Yol Bulunamadı!");
    }
        // BFS
        long bfsStart = System.nanoTime();
        Node bfsResult = BFSSolver.solve(board, startRow, startCol, coinRow, coinCol);
        long bfsEnd = System.nanoTime();

        System.out.println("BFS Path:");
        printPath(bfsResult);
        System.out.println("BFS Time: " + (bfsEnd - bfsStart));

        // DFS
        long dfsStart = System.nanoTime();
        Node dfsResult = DFSSolver.solve(board, startRow, startCol, coinRow, coinCol);
        long dfsEnd = System.nanoTime();

        System.out.println("\nDFS Path:");
        printPath(dfsResult);
        System.out.println("DFS Time: " + (dfsEnd - dfsStart));
    }

    private static void printPath(Node goal) {
        if (goal == null) {
            System.out.println("No solution found.");
            return;
        }

        List<Node> path = new ArrayList<>();
        for (Node n = goal; n != null; n = n.parent)
            path.add(n);

        Collections.reverse(path);

        for (Node n : path)
            System.out.print("(" + n.row + "," + n.col + ") ");

        System.out.println();
    }
}