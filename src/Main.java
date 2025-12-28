public static void main(String[] args) {
    // 1. Board Oluştur (8x8)
    Board board = new Board(8);

    // 2. Bomba ve Hedef Ayarla (Senaryo 2: İki adımda çözüm)
    // Bombayı uzak bir yere koyuyoruz ki engel olmasın [cite: 17, 18]
    int coinRow = 1, coinCol = 5; // Hedef (3,3)
    int startRow = 2, startCol = 6; // Başlangıç (0,0)


    board.setBomb(1,3);
    board.setBomb(2,4);
    board.setBomb(5,7);
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
}