import java.util.*;

public class DFSSolver {

    private static final int[][] MOVES = {
            {-1, -2}, {2, -1}, {-2, 1}, {-2, -1},
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
    };

    public static Node solve(Board board,
                             int sr, int sc,
                             int gr, int gc) {

        boolean[][] visited = new boolean[board.getSize()][board.getSize()];
        Stack<Node> stack = new Stack<>();

        stack.push(new Node(sr, sc, null));

        while (!stack.isEmpty()) {
            Node current = stack.pop();

            if (visited[current.row][current.col]) continue;
            visited[current.row][current.col] = true;

            if (current.row == gr && current.col == gc)
                return current;

            for (int[] m : MOVES) {
                int nr = current.row + m[0];
                int nc = current.col + m[1];

                if (board.isSafe(nr, nc) && !visited[nr][nc]) {
                    stack.push(new Node(nr, nc, current));
                }
            }
        }
        return null;
    }
}
