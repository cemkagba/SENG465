import java.util.*;

public class Main {

    public static void main(String[] args) {

        Board board = new Board(8);

    // 2. Setup the game (Example positions)
    // Remember: 'b' is column 1, '1' is row 0.
    int bombRow = 3, bombCol = 4; // e4
    int coinRow = 7, coinCol = 7; // h8
    int startRow = 0, startCol = 1; // b1

    board.setBomb(bombRow, bombCol);
    board.setCoin(coinRow, coinCol);

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