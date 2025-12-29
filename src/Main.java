import java.util.*;

public class Main {

    // Sonuçları karşılaştırmak için yardımcı sınıf
    static class AlgorithmResult implements Comparable<AlgorithmResult> {
        String name;
        long time;
        int stepCount;
        boolean found;

        public AlgorithmResult(String name, long time, int stepCount, boolean found) {
            this.name = name;
            this.time = time;
            this.stepCount = stepCount;
            this.found = found;
        }

        @Override
        public int compareTo(AlgorithmResult other) {
            return Long.compare(this.time, other.time);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1. Board Kurulumu
        int size = 8;
        Board board = new Board(size);

        System.out.println("\nKNIGHT PATH FINDING SIMULATION");
        System.out.println("Lütfen koordinatları 'a1', 'h8' formatında giriniz.");

        // 2. KULLANICI GİRDİLERİ

        // --- A) Başlangıç Konumu ---
        int[] startPos = getValidInput(scanner, "At Başlangıç (örn: b1): ", size);
        int startRow = startPos[0];
        int startCol = startPos[1];

        // --- B) Bomba Sayısı ve Konumları (YENİ KISIM) ---
        int bombCount = 0;
        while (true) {
            System.out.print("Kaç adet bomba koymak istiyorsunuz? (0-10 arası): ");
            try {
                String input = scanner.nextLine().trim();
                bombCount = Integer.parseInt(input);
                if (bombCount >= 0 && bombCount <= 64) { // Mantıklı bir sınır
                    break;
                }
                System.out.println("Lütfen geçerli bir sayı giriniz.");
            } catch (NumberFormatException e) {
                System.out.println("Hata: Sayısal bir değer giriniz.");
            }
        }

        for (int i = 0; i < bombCount; i++) {
            int[] bombPos = getValidInput(scanner, (i + 1) + ". Bomba Konumu (örn: e4): ", size);
            // Board sınıfındaki setBomb metodu muhtemelen grid[r][c] = -1 yapıyor.
            // Bu yüzden birden fazla kez çağırmak sorun olmaz.
            board.setBomb(bombPos[0], bombPos[1]);
        }

        // --- C) Hedef Konumu ---
        int[] coinPos = getValidInput(scanner, "Hedef (Coin) Konumu (örn: h8): ", size);
        board.setCoin(coinPos[0], coinPos[1]);

        System.out.println("\n--- OLUŞTURULAN TAHTA ---");
        // Board print edilirken bombaların hepsi 'X' veya 'B' olarak görünmeli
        board.printBoard(startRow, startCol);

        List<AlgorithmResult> results = new ArrayList<>();

        System.out.println("\n>>> ALGORİTMALAR ÇALIŞTIRILIYOR...\n");

        // ==========================================
        // 1. IDDFS (Iterative Deepening DFS)
        // ==========================================
        System.out.println("========================================");
        System.out.println("1. IDDFS ÇALIŞIYOR...");
        long startTime = System.nanoTime();

        // Not: IDDFS sınıfınızın kurucusu (constructor) tek bir bomba alıyorsa,
        // IDDFS sınıfını da board üzerinden bombaları okuyacak şekilde güncellemeniz gerekebilir.
        // Ancak genellikle algoritmalar 'board' nesnesine bakarak hareket eder.
        // Burada parametre olarak gönderilen bombPos artık sadece temsili olabilir.
        // Doğrusu IDDFS'in board.isBomb(r,c) kontrolü yapmasıdır.

        // IDDFS artık board.isSafe() metodunu kullanarak tüm bombaları kontrol eder

        IDDFS iddfs = new IDDFS(board, coinPos[0], coinPos[1]);
        iddfs.search(startRow, startCol);

        long endTime = System.nanoTime();
        // iddfs.printResults(); // Konsolu doldurmaması için kapalı
        
        boolean iddfsFound = iddfs.isSolutionFound();
        int iddfsSteps = iddfsFound ? iddfs.getPath().size() - 1 : 0;

        results.add(new AlgorithmResult("IDDFS", (endTime - startTime), iddfsSteps, iddfsFound));
        
        if (iddfsFound) {
            printPathList("IDDFS", iddfs.getPath());
        } else {
            System.out.println("IDDFS: Yol Bulunamadı.");
        }


        // ==========================================
        // 2. Greedy Best First Search
        // ==========================================
        System.out.println("\n========================================");
        System.out.println("2. GREEDY BFS ÇALIŞIYOR...");
        startTime = System.nanoTime();

        Node startNode = new Node(startRow, startCol, null);
        Node targetNode = new Node(coinPos[0], coinPos[1], null);
        GreedyBestFirstSearch greedySolver = new GreedyBestFirstSearch();
        List<Node> greedyPath = greedySolver.greedyBestFirstSearch(board, startNode, targetNode);

        endTime = System.nanoTime();
        boolean greedyFound = (greedyPath != null && !greedyPath.isEmpty());
        int greedySteps = greedyFound ? greedyPath.size() - 1 : 0;

        results.add(new AlgorithmResult("Greedy BFS", (endTime - startTime), greedySteps, greedyFound));

        if (greedyFound) {
            printPathList("Greedy BFS", greedyPath);
        } else {
            System.out.println("Greedy BFS: Yol Bulunamadı.");
        }


        // ==========================================
        // 3. BFS (Breadth-First Search)
        // ==========================================
        System.out.println("\n========================================");
        System.out.println("3. BFS ÇALIŞIYOR...");
        startTime = System.nanoTime();

        Node bfsResult = BFSSolver.solve(board, startRow, startCol, coinPos[0], coinPos[1]);

        endTime = System.nanoTime();
        boolean bfsFound = (bfsResult != null);
        int bfsSteps = bfsFound ? calculatePathLength(bfsResult) : 0;

        results.add(new AlgorithmResult("BFS", (endTime - startTime), bfsSteps, bfsFound));

        printNodePath("BFS", bfsResult);


        // ==========================================
        // 4. DFS (Depth-First Search)
        // ==========================================
        System.out.println("\n========================================");
        System.out.println("4. DFS ÇALIŞIYOR...");
        startTime = System.nanoTime();

        Node dfsResult = DFSSolver.solve(board, startRow, startCol, coinPos[0], coinPos[1]);

        endTime = System.nanoTime();
        boolean dfsFound = (dfsResult != null);
        int dfsSteps = dfsFound ? calculatePathLength(dfsResult) : 0;

        results.add(new AlgorithmResult("DFS", (endTime - startTime), dfsSteps, dfsFound));

        printNodePath("DFS", dfsResult);


        // ==========================================
        // 5. A* (A-Star) Algorithm
        // ==========================================
        System.out.println("\n========================================");
        System.out.println("5. A* ÇALIŞIYOR...");
        startTime = System.nanoTime();

        AStarSolver aStarSolver = new AStarSolver();
        Node astarStartNode = new Node(startRow, startCol, null);
        Node astarTargetNode = new Node(coinPos[0], coinPos[1], null);
        List<Node> astarPath = aStarSolver.findPath(board, astarStartNode, astarTargetNode);

        endTime = System.nanoTime();
        boolean astarFound = (astarPath != null && !astarPath.isEmpty());
        int astarSteps = astarFound ? astarPath.size() - 1 : 0;

        results.add(new AlgorithmResult("A*", (endTime - startTime), astarSteps, astarFound));

        if (astarFound) {
            printPathList("A*", astarPath);
        } else {
            System.out.println("A*: Yol Bulunamadı.");
        }


        // ==========================================
        // PERFORMANS TABLOSU
        // ==========================================
        System.out.println("\n\n###############################################");
        System.out.println("PERFORMANS KARŞILAŞTIRMASI (Hızlıdan Yavaşa)");
        System.out.println("###############################################");

        Collections.sort(results);

        System.out.printf("%-15s %-15s %-10s %-10s\n", "Algoritma", "Süre(ns)", "Adım", "Durum");
        System.out.println("-----------------------------------------------------");

        for (AlgorithmResult res : results) {
            String status = res.found ? "Buldu" : "Başarısız";
            System.out.printf("%-15s %-15d %-10d %-10s\n", res.name, res.time, res.stepCount, status);
        }

        scanner.close();
    }

    // --- YARDIMCI METOTLAR ---

    private static void printNodePath(String algName, Node goal) {
        if (goal == null) {
            System.out.println(algName + " -> Yol bulunamadı!");
            return;
        }

        List<Node> path = new ArrayList<>();
        for (Node n = goal; n != null; n = n.parent) {
            path.add(n);
        }
        Collections.reverse(path);

        printFormattedPath(algName, path);
    }

    private static void printPathList(String algName, List<Node> path) {
        printFormattedPath(algName, path);
    }

    private static void printFormattedPath(String algName, List<Node> path) {
        System.out.println(algName + " Yolu (" + (path.size() - 1) + " adım):");
        for (int i = 0; i < path.size(); i++) {
            Node n = path.get(i);
            System.out.print("(" + n.row + "," + n.col + ")");
            if (i < path.size() - 1) {
                System.out.print(" -> ");
            }
            if ((i + 1) % 10 == 0) System.out.println();
        }
        System.out.println(" [HEDEF]");
    }

    private static int calculatePathLength(Node goal) {
        int count = 0;
        for (Node n = goal; n.parent != null; n = n.parent) {
            count++;
        }
        return count;
    }

    private static int[] getValidInput(Scanner scanner, String prompt, int size) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.length() < 2) {
                System.out.println("Hata: Geçersiz format. 'a1' gibi giriniz.");
                continue;
            }

            char colChar = input.charAt(0);
            String rowStr = input.substring(1);

            int col = colChar - 'a';
            int row;
            try {
                row = Integer.parseInt(rowStr) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Hata: Satır kısmı sayı olmalı.");
                continue;
            }

            if (col >= 0 && col < size && row >= 0 && row < size) {
                return new int[]{row, col};
            } else {
                System.out.println("Hata: Koordinatlar tahta sınırları dışında (a-h, 1-" + size + ").");
            }
        }
    }
}
