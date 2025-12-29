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

    private boolean isValidCoordinate(int r, int c) {
        return r >= 0 && r < size && c >= 0 && c < size;
    }

    // YENİ EKLENEN METOT: Hücreyi temizle
    public void setEmpty(int row, int col) {
        if (isValidCoordinate(row, col)) {
            grid[row][col] = 0;
        }
    }

    public void setBomb(int row, int col) {
        // Coin üzerine bomba konulursa board bunu reddediyordu,
        // Artık FXMain tarafında kontrol edeceğiz, burası sadece emri uygulasın.
        if (isValidCoordinate(row, col)) {
            grid[row][col] = 1;
        }
    }

    public void setCoin(int row, int col) {
        if (isValidCoordinate(row, col)) {
            grid[row][col] = 2;
        }
    }

    public boolean isSafe(int row, int col) {
        return row >= 0 && row < size &&
               col >= 0 && col < size &&
               grid[row][col] != 1;
    }

    // Konsol çıktısı için (eski kodun bozulmasın diye bıraktım)
    public void printBoard(int knightR, int knightC) {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (r == knightR && c == knightC) System.out.print("K ");
                else if (grid[r][c] == 1) System.out.print("X ");
                else if (grid[r][c] == 2) System.out.print("C ");
                else System.out.print("- ");
            }
            System.out.println();
        }
    }
}