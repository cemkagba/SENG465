

public static void main(String[] args) {
    // 1. Create an 8x8 board
    Board board = new Board(8);

    // 2. Setup the game (Example positions)
    // Remember: 'b' is column 1, '1' is row 0.
    int bombRow = 3, bombCol = 4; // e4
    int coinRow = 7, coinCol = 7; // h8
    int startRow = 0, startCol = 1; // b1

    board.setBomb(bombRow, bombCol);
    board.setCoin(coinRow, coinCol);

    // 3. Verify it looks correct
    System.out.println("Initial Board State:");
    board.printBoard(startRow, startCol);
}