public class Board {
    int size;
    int[][] grid = new int[size][size];

    Board(int size) {
        this.size = size;
        this.grid = new int[size][size];
    }

    public void setBomb(int x, int y) {
        grid[x][y] = 1;
    }

    public void setCoin(int row, int col) {
        grid[row][col] = 2;
    }


    public boolean isSafe(int r, int c) {
        if (r < 0 || r >= size || c < 0 || c >= size) {
            return false;
        }
        return grid[r][c] != 1;
    }

    private boolean isValidCoordinate(int r, int c) {
        return r >= 0 && r < size && c >= 0 && c < size;
    }

    public int getSize() {
        return size;
    }

    public void printBoard(int knightR, int knightC) {
        System.out.println("  a b c d e f g h"); // Column headers
        for (int r = 0; r < size; r++) {
            System.out.print((r + 1) + " "); // Row number
            for (int c = 0; c < size; c++) {
                if (r == knightR && c == knightC) {
                    System.out.print("K "); // Knight
                } else if (grid[r][c] == 1) {
                    System.out.print("X "); // Bomb
                } else if (grid[r][c] == 2) {
                    System.out.print("C "); // Coin
                } else {
                    System.out.print(". "); // Empty
                }
            }
            System.out.println();
        }
    }


}
