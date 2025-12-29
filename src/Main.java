import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Board board = new Board(8);

        System.out.println("--- KNIGHT A* PATHFINDING ---");

        // Start Position
        System.out.print("Enter Knight Start Column (0-7): ");
        int startCol = scanner.nextInt();
        System.out.print("Enter Knight Start Row (0-7): ");
        int startRow = scanner.nextInt();

        // Target Position
        System.out.print("Enter Coin Column (0-7): ");
        int coinCol = scanner.nextInt();
        System.out.print("Enter Coin Row (0-7): ");
        int coinRow = scanner.nextInt();

        board.setCoin(coinRow, coinCol);

        // Bombs
        System.out.print("How many bombs do you want to place? ");
        int bombCount = scanner.nextInt();

        for (int i = 0; i < bombCount; i++) {
            System.out.print("Bomb " + (i + 1) + " Column: ");
            int bCol = scanner.nextInt();
            System.out.print("Bomb " + (i + 1) + " Row: ");
            int bRow = scanner.nextInt();
            board.setBomb(bRow, bCol);
        }

        System.out.println("\nInitial Board:");
        board.printBoard(startRow, startCol);

        Node startNode = new Node(startRow, startCol, null);
        Node targetNode = new Node(coinRow, coinCol, null);

        AStarSolver solver = new AStarSolver();

        // ⏱ START TIME
        long startTime = System.currentTimeMillis();

        List<Node> path = solver.findPath(board, startNode, targetNode);

        // ⏱ END TIME
        long endTime = System.currentTimeMillis();
        long durationMs = endTime - startTime;

        if (path != null) {
            System.out.println("\n--- PATH FOUND ---");
            System.out.println("Steps: " + (path.size() - 1));

            StringBuilder pathString = new StringBuilder();

            for (int i = 0; i < path.size(); i++) {
                Node node = path.get(i);

                if ((node.row != startRow || node.col != startCol) &&
                    (node.row != coinRow || node.col != coinCol)) {
                    board.markPath(node.row, node.col);
                }

                pathString.append("[")
                          .append(node.col)
                          .append(",")
                          .append(node.row)
                          .append("]");

                if (i < path.size() - 1)
                    pathString.append(" -> ");
            }

            System.out.println("Path: " + pathString);
            System.out.println("Time: " + durationMs + " ms");

            System.out.println("\nFinal Board:");
            board.printBoard(startRow, startCol);

        } else {
            System.out.println("\nNO PATH FOUND!");
        }

        scanner.close();
    }
}
