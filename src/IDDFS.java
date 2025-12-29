import java.util.*;

/**
 * IDDFS (Iterative Deepening Depth First Search) Algoritması
 * DFS'in bellek verimliliği ile BFS'in optimalliğini birleştirir.
 */
public class IDDFS {
    private Board board;
    private int coinRow, coinCol;

    private int visitedNodeCount;
    private long executionTime;
    private List<Node> path;
    private boolean solutionFound;

    // Atın L şeklindeki hareketleri
    private static final int[] rowMoves = {2, 2, -2, -2, 1, 1, -1, -1};
    private static final int[] colMoves = {1, -1, 1, -1, 2, -2, 2, -2};

    public IDDFS(Board board, int coinRow, int coinCol) {
        this.board = board;
        this.coinRow = coinRow;
        this.coinCol = coinCol;

        this.visitedNodeCount = 0;
        this.path = new ArrayList<>();
        this.solutionFound = false;
    }


    public void search(int startRow, int startCol) {
        long startTime = System.nanoTime();

        int maxDepth = 0;
        int limit = board.getSize() * board.getSize(); //to prevent infinite loops

        while (maxDepth <= limit) {
            // Each time the deepness is increasing, this should reset.
            Set<String> visitedInThisIteration = new HashSet<>();

            // start the search
            Node result = depthLimitedSearch(startRow, startCol, maxDepth, visitedInThisIteration, null);

            if (result != null) {
                solutionFound = true;
                reconstructPath(result);
                break;
            }
            maxDepth++;
        }

        long endTime = System.nanoTime();
        this.executionTime = (endTime - startTime) / 1_000_000;
    }


    private Node depthLimitedSearch(int row, int col, int depthLimit, Set<String> visited, Node parent) {
        visitedNodeCount++;

        Node currentNode = new Node(row, col, parent);

        if (row == coinRow && col == coinCol) {
            return currentNode;
        }

        int currentDepth = 0;
        Node temp = parent;
        while(temp != null) { currentDepth++; temp = temp.parent; }

        if (currentDepth >= depthLimit) {
            return null;
        }

        String key = row + "," + col;
        if (visited.contains(key)) {
            return null; // There is a cycle
        }

        visited.add(key);

        for (int i = 0; i < 8; i++) {
            int newRow = row + rowMoves[i];
            int newCol = col + colMoves[i];

            if (board.isSafe(newRow, newCol)) {

                Node result = depthLimitedSearch(newRow, newCol, depthLimit, visited, currentNode);

                if (result != null) {
                    return result; // Çözüm bulundu, yukarı taşı
                }
            }
        }

        // Backtracking
        visited.remove(key);

        return null;
    }


    private void reconstructPath(Node goalNode) {
        path.clear();
        Node current = goalNode;
        while (current != null) {
            path.add(0, current);
            current = current.parent;
        }
    }


    public long getExecutionTime() {
        return executionTime;
    }

    public int getVisitedNodeCount() {
        return visitedNodeCount;
    }

    public boolean isSolutionFound() {
        return solutionFound;
    }

    public List<Node> getPath() {
        return path;
    }

    public void printResults() {
        System.out.println("\n=== IDDFS Results ===");
        System.out.println("Solution Found: " + solutionFound);
        System.out.println("Time: " + executionTime + " ms");
        System.out.println("Visited Nodes: " + visitedNodeCount);

        if (solutionFound) {
            System.out.println("Path Length: " + (path.size() - 1) + " moves"); // Number of movement excluding the start node
            System.out.print("Path: ");
            for (int i = 0; i < path.size(); i++) {
                Node node = path.get(i);
                System.out.print((char)('a' + node.col) + "" + (node.row + 1));
                if (i < path.size() - 1) {
                    System.out.print(" -> ");
                }
            }
            System.out.println();
        } else {
            System.out.println("No path found.");
        }
    }
}