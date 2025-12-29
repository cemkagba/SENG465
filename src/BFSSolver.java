import java.util.*;

public class BFSSolver {

    private static final int[][] MOVES = {
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
    };

    public static Node solve(Board board,
                             int sr, int sc,
                             int gr, int gc) {

        boolean[][] visited = new boolean[board.getSize()][board.getSize()];
        Queue<Node> queue = new LinkedList<>();

        queue.add(new Node(sr, sc, null));
        visited[sr][sc] = true;

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (current.row == gr && current.col == gc)
                return current;

            for (int[] m : MOVES) {
                int nr = current.row + m[0];
                int nc = current.col + m[1];

                if (board.isSafe(nr, nc) && !visited[nr][nc]) {
                    visited[nr][nc] = true;
                    queue.add(new Node(nr, nc, current));
                }
            }
        }
        return null;
    }
}
