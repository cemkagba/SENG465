public class Board {
    int size;
    int[][] grid;

    public Board(int size) {
        this.size = size;
        this.grid = new int[size][size];
    }

    public void setBomb(int x, int y) {
        if(isValidCoordinate(x, y))
        grid[x][y] = 1;
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
        System.out.println("  0 1 2 3 4 5 6 7"); // İndexleri sayı yaptım input kolay olsun diye
        for (int r = 0; r < size; r++) {
            System.out.print(r + " ");
            for (int c = 0; c < size; c++) {
                if (r == knightR && c == knightC) {
                    System.out.print("K "); // Knight (Başlangıç)
                } else if (grid[r][c] == 1) {
                    System.out.print("X "); // Bomb
                } else if (grid[r][c] == 2) {
                    System.out.print("C "); // Coin (Hedef)
                } else if (grid[r][c] == 9) {
                    System.out.print("J "); // Gidilen Yol
                } else {
                    System.out.print("- "); // Boş
                }
            }
            System.out.println();
        }
    }

}
