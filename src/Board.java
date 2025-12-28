public class Board {
    private final int size;
    private final int[][] grid; // 0: empty, 1: bomb, 2: coin

    public Board(int size) {
        this.size = size;
        this.grid = new int[size][size];
    }

    public int getSize() {
        return size;
    }

    public void setBomb(int row, int col) {
        grid[row][col] = 1;
    }

    public void setCoin(int row, int col) {
        grid[row][col] = 2;
    }

    public boolean isSafe(int row, int col) {
        return row >= 0 && row < size &&
               col >= 0 && col < size &&
               grid[row][col] != 1;
    }

    public void printBoard(int knightR, int knightC) {
        System.out.print("  ");
        for (int c = 0; c < size; c++) {
            System.out.print((char) ('a' + c) + " ");
        }
        System.out.println();

        for (int r = 0; r < size; r++) {
            System.out.print((r + 1) + " ");
            for (int c = 0; c < size; c++) {
                if (r == knightR && c == knightC) System.out.print("K ");
                else if (grid[r][c] == 1) System.out.print("X ");
                else if (grid[r][c] == 2) System.out.print("C ");
                else System.out.print(". ");
            }
            System.out.println();
        }
    }
}
