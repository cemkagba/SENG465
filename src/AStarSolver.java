import java.util.*;

public class AStarSolver {
    // Atın yapabileceği 8 farklı L hareketi (row, col değişimleri)
    private static final int[] dRow = {-2, -1, 1, 2, 2, 1, -1, -2};
    private static final int[] dCol = {-1, -2, -2, -1, 1, 2, 2, 1};

    public List<Node> findPath(Board board, Node startNode, Node targetNode) {
        PriorityQueue<Node> openList = new PriorityQueue<>();
        HashSet<String> pathList = new HashSet<>(); // Ziyaret edilen düğümler

        startNode.gCost = 0;
        startNode.hCost = calculateHeuristic(startNode, targetNode);
        openList.add(startNode);

        while (!openList.isEmpty()) {
            Node current = openList.poll(); // En düşük fCost'a sahip node'u al

            // Hedefe ulaştık mı?
            if (current.row == targetNode.row && current.col == targetNode.col) {
                return reconstructPath(current);
            }

            String key = current.row + "," + current.col;
            if (pathList.contains(key))
                 continue;
            pathList.add(key);

            // Atın 8 olası hareketine bak
            for (int i = 0; i < 8; i++) {
                int newRow = current.row + dRow[i];
                int newCol = current.col + dCol[i];

                if (board.isSafe(newRow, newCol)) {
                    // gCost: Başlangıçtan buraya olan mesafe (Her adım 1 birim)
                    int tempGCost = current.gCost + 1;
                    
                    Node neighbor = new Node(newRow, newCol, current);
                    neighbor.gCost = tempGCost;
                    neighbor.hCost = calculateHeuristic(neighbor, targetNode);
                    
                    if (!pathList.contains(newRow + "," + newCol)) {
                        openList.add(neighbor);
                    }
                }
            }
        }
        return null; // Yol bulunamadı
    }

    // Heuristic: Manhattan Mesafesi (Basit ve etkili)
    // At için tam doğru değildir ama hedefe yaklaştırır.
    private int calculateHeuristic(Node a, Node b) {
        return Math.abs(a.row - b.row) + Math.abs(a.col - b.col);
    }

    // Yolu geriye doğru takip etme
    private List<Node> reconstructPath(Node current) {
        List<Node> path = new ArrayList<>();
        while (current != null) {
            path.add(current);
            current = current.parent;
        }
        Collections.reverse(path); // Tersten dizildiği için düzeltiyoruz
        return path;
    }
}