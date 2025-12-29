import java.util.*;

public class GreedyBestFirstSearch {

    // Atın yapabileceği 8 olası hareket (Row değişimi, Col değişimi)
// [cite: 11] (Knight moves in L shapes)
    private static final int[][] DIRECTIONS = {{-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}, {2, -1}, {2, 1}};

    public List<Node> greedyBestFirstSearch(Board board, Node startNode, Node targetNode) {
        // Sadece hCost'a bakan PriorityQueue (Greedy Mantığı)
        PriorityQueue<Node> pq = new PriorityQueue<>((n1, n2) -> Double.compare(n1.hCost, n2.hCost));
        Set<String> visited = new HashSet<>();

        // Başlangıç ayarları
        startNode.setHeuristic(targetNode);
        pq.add(startNode);
        visited.add(startNode.row + "," + startNode.col);

        while (!pq.isEmpty()) {
            Node current = pq.poll();

            // Hedef kontrolü
            if (current.row == targetNode.row && current.col == targetNode.col) {
                return reconstructPath(current);
            }


            // Komşuları Gezme Döngüsü
            for (int[] dir : DIRECTIONS) {
                int newRow = current.row + dir[0];
                int newCol = current.col + dir[1];

                if (board.isSafe(newRow, newCol)) {

                    // 2. Daha önce ziyaret edildi mi?
                    String key = newRow + "," + newCol;
                    if (!visited.contains(key)) {
                        visited.add(key); // İşaretle

                        // 3. Yeni düğüm oluştur ve Heuristic hesapla
                        Node neighbor = new Node(newRow, newCol, current);
                        neighbor.setHeuristic(targetNode); // Greedy için kritik adım!
                        // 4. Kuyruğa ekle
                        pq.add(neighbor);
                    }
                }
            }
        }

        return null; // Yol bulunamazsa
    }

    private List<Node> reconstructPath(Node current) {
        List<Node> path = new ArrayList<>();
        while (current != null) {
            path.add(0, current);
            current = current.parent;
        }
        return path;
    }
}