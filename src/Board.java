public class Board {
    private final int size;
    private final int[][] grid; // 0: empty, 1: bomb, 2: coin

    public Board(int size) {
        this.size = size;
        this.grid = new int[size][size];
    }

    public void setBomb(int row, int col) {
    if (grid[row][col] == 2) {
        System.out.println("Cannot place bomb on coin!");
        return;
    }
    if (isValidCoordinate(row, col)) {
        grid[row][col] = 1;
    }
}

    public void setCoin(int row, int col) {
        if(isValidCoordinate(row, col))
        grid[row][col] = 2;
    }


    public boolean isSafe(int r, int c) {
      if(!isValidCoordinate(r, c))
          return false;
        return grid[r][c] != 1;
}


    private boolean isValidCoordinate(int r, int c) {
        return r >= 0 && r < size && c >= 0 && c < size;
    }

    public int getSize() {
        return size;
    }

    public int markPath(int r, int c) {
       if (grid[r][c] != 2 && grid[r][c] != 1)  // Coin veya Bomba değilse
            grid[r][c] = 9; // 9 numara yol olsun

        return -1; // Invalid coordinate
    }

 public void printBoard(int knightR, int knightC) {
    System.out.print("  ");
    for (int i = 1; i <= size; i++) {
        System.out.print(i + " ");
    }
    System.out.println();

    for (int r = 0; r < size; r++) {
        System.out.print((r + 1) + " ");
        for (int c = 0; c < size; c++) {
            if (r == knightR && c == knightC) {
                System.out.print("K ");
            } else if (grid[r][c] == 1) {
                System.out.print("X ");
            } else if (grid[r][c] == 2) {
                System.out.print("C ");
            } else if (grid[r][c] == 9) {
                System.out.print("J ");
            } else {
                System.out.print("- ");
            }
        }
        System.out.println();
    }
}


}
