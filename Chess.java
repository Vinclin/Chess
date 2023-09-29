package org.cis1200.chess;

import java.util.LinkedList;

public class Chess {
    static final String START_OVER = "Start Over";
    private String[][] board = {
        { "wR", "wN", "wB", "wQ", "wK", "wB", "wN", "wR" },
        { "wP", "wP", "wP", "wP", "wP", "wP", "wP", "wP" },
        { "", "", "", "", "", "", "", "" },
        { "", "", "", "", "", "", "", "" },
        { "", "", "", "", "", "", "", "" },
        { "", "", "", "", "", "", "", "" },
        { "bP", "bP", "bP", "bP", "bP", "bP", "bP", "bP" },
        { "bR", "bN", "bB", "bQ", "bK", "bB", "bN", "bR" }
    };
    private int turnCounter = 1;
    private boolean player1;
    private boolean wKCastle = true;
    private boolean wQCastle = true;
    private boolean bKCastle = true;
    private boolean bQCastle = true;
    private int enPassant;
    private LinkedList<String[][]> gameState = new LinkedList<>();

    /**
     * Constructor sets up game state.
     */
    Chess() {
        reset();
    }

    /**
     * isPlayerTurn allows players to play a turn. Returns true if the move is successful and
     * false if a player tries to play in a location that is taken or after the game has ended.
     * If the turn is successful and the game has not ended, the player is changed. If the turn
     * is unsuccessful or the game has ended, the player is not changed.
     * @param x1 - initial x position
     * @param y1 - initial y position
     * @param x2 - new x position
     * @param y2 - new y position
     * @return boolean
     */
    boolean isPlayerTurn(int x1, int y1, int x2, int y2) {
        try {
            if (isLegal(x1, y1, x2, y2)) {
                String hold = board[y2 - 1][x2 - 1];
                board[y2 - 1][x2 - 1] = board[y1 - 1][x1 - 1];
                board[y1 - 1][x1 - 1] = "";

                if (isInCheck()) {
                    board[y1 - 1][x1 - 1] = board[y2 - 1][x2 - 1];
                    board[y2 - 1][x2 - 1] = hold;
                    return false;
                } else {
                    gameState.add(cloner());
                    if (!player1) {
                        turnCounter++;
                    }
                    player1 = !player1;
                    return true;
                }
            }
            return false;
        } catch (IndexOutOfBoundsException ignored) {
        }
        return false;
    }

    private boolean isLegal(int x1, int y1, int x2, int y2) {
        // no piece movement
        if ("".equals(board[y1 - 1][x1 - 1])) {
            return false;
        }

        // move pieces
        if ("w".equals(board[y1 - 1][x1 - 1].substring(0, 1)) && !player1) {
            return false;
        } else if ("b".equals(board[y1 - 1][x1 - 1].substring(0, 1)) && player1) {
            return false;
        } else if (!"".equals(board[y2 - 1][x2 - 1]) && board[y1 - 1][x1 - 1].substring(0, 1)
                .equals(board[y2 - 1][x2 - 1].substring(0, 1))) {
            return false;
        }

        // jumping over pieces in column
        if (y1 == y2) {
            for (int i = Math.min(x1, x2) + 1; i < Math.max(x1, x2); i++) {
                if (!"".equals(board[y1 - 1][i - 1])) {
                    return false;
                }
            }
        }

        // jumping over pieces in row
        if (x1 == x2) {
            for (int i = Math.min(y1, y2) + 1; i < Math.max(y1, y2); i++) {
                if (!"".equals(board[i - 1][x1 - 1])) {
                    return false;
                }
            }
        }

        // jumping over other pieces in a diagonal positive slope
        if (x1 - x2 == y1 - y2) {
            var startY = Math.min(y1, y2) + 1;
            var startX = Math.min(x1, x2) + 1;
            for (; startX < Math.max(x1, x2); startX++) {
                if (!"".equals(board[startY - 1][startX - 1])) {
                    return false;
                }
                startY++;
            }
        }

        // jumping over other pieces in a diagonal negative slope
        if (x1 - x2 == y2 - y1) {
            var startY = Math.max(y1, y2) - 1;
            var startX = Math.min(x1, x2) + 1;

            for (; startX < Math.max(x1, x2); startX++) {
                if (!"".equals(board[startY - 1][startX - 1])) {
                    return false;
                }
                startY--;
            }
        }

        // piece movements
        switch (board[y1 - 1][x1 - 1].substring(1, 2)) {
            case "P" -> {
                if ((y1 >= y2 && player1) || (y1 <= y2 && !player1) || 1 < Math.abs(x1 - x2)) {
                    // pawn can't move backwards/sideways
                    return false;
                } else if (0 != enPassant && player1 && 5 == y1 && 6 == y2
                        && 1 == Math.abs(x1 - enPassant)) {
                    // white en Passant
                    board[5 - 1][enPassant - 1] = "";
                    enPassant = 0;
                    return true;
                } else if (0 != enPassant && !player1 && 4 == y1 && 3 == y2
                        && 1 == Math.abs(x1 - enPassant)) {
                    // black en Passant
                    board[4 - 1][enPassant - 1] = "";
                    enPassant = 0;
                    return true;
                } else if (!"".equals(board[y2 - 1][x2 - 1]) && x1 == x2) {
                    // pawn can't move forward to take a piece
                    return false;
                } else if (x1 == x2 && 2 == Math.abs(y1 - y2)) {
                    if (2 == y1 && player1) {
                        // white pawn moves up 2 spaces
                        enPassant = x2;
                        return true;
                    } else if (7 == y1 && !player1) {
                        // black pawn moves up 2 spaces
                        enPassant = x2;
                        return true;
                    } else {
                        return false;
                    }
                } else if (1 != Math.abs(y1 - y2)) {
                    // pawn not moving up one space
                    return false;
                } else if (1 == Math.abs(y1 - y2) && 1 == Math.abs(x1 - x2)
                        && "".equals(board[y2 - 1][x2 - 1])) {
                    // capturing
                    return false;
                } else if (1 == y2 - y1 && 8 == y2) {
                    board[y1 - 1][x1 - 1] = "wQ";
                    enPassant = 0;
                    return true;
                } else if (-1 == y2 - y1 && 1 == y2) {
                    board[y1 - 1][x1 - 1] = "bQ";
                    enPassant = 0;
                    return true;
                } else {
                    enPassant = 0;
                    return true;
                }
            }
            case "N" -> {
                if (!((2 == Math.abs(x1 - x2) && 1 == Math.abs(y1 - y2))
                        || 1 == Math.abs(x1 - x2) && 2 == Math.abs(y1 - y2))) {
                    return false;
                }
            }
            case "R" -> {
                if (x1 != x2 && y1 != y2) {
                    return false;
                } else if (wKCastle && 8 == x1 && 1 == y1) {
                    wKCastle = false;
                } else if (wQCastle && 1 == x1 && 1 == y1) {
                    wQCastle = false;
                } else if (bKCastle && 8 == x1 && 8 == y1) {
                    bKCastle = false;
                } else if (bQCastle && 1 == x1 && 8 == y1) {
                    bQCastle = false;
                }
            }
            case "B" -> {
                if (x1 - x2 != y1 - y2 && x1 - x2 != y2 - y1) {
                    return false;
                }
            }
            case "Q" -> {
                if (x1 != x2 && y1 != y2 && x1 - x2 != y1 - y2 && x1 - x2 != y2 - y1) {
                    return false;
                }
            }
            case "K" -> {
                enPassant = 0;
                if (player1 && wKCastle && 1 == y2 && 7 == x2 && !isInCheck()) {
                    board[0][6 - 1] = "wR";
                    board[0][8 - 1] = "";
                    wKCastle = false;
                    wQCastle = false;
                    return true;
                } else if (player1 && wQCastle && 1 == y2 && 3 == x2 && !isInCheck()) {
                    board[0][4 - 1] = "wR";
                    board[0][0] = " ";
                    wKCastle = false;
                    wQCastle = false;
                    return true;
                } else if (!player1 && bKCastle && 8 == y2 && 7 == x2 && !isInCheck()) {
                    board[8 - 1][6 - 1] = "bR";
                    board[8 - 1][8 - 1] = "";
                    bKCastle = false;
                    bQCastle = false;
                    return true;
                } else if (!player1 && bQCastle && 8 == y2 && 3 == x2
                        && !isInCheck()) {
                    board[8 - 1][4 - 1] = "bR";
                    board[8 - 1][0] = "";
                    bKCastle = false;
                    bQCastle = false;
                    return true;
                } else if ((2 > Math.abs(x1 - x2) && 2 > Math.abs(y1 - y2))) {
                    if (player1) {
                        wKCastle = false;
                        wQCastle = false;
                    } else {
                        bKCastle = false;
                        bQCastle = false;
                    }
                    return true;
                } else {
                    return false;
                }
            }
            default -> {
                enPassant = 0;
                return true;
            }
        }
        enPassant = 0;
        return true;
    }

    /**
     * isWinner checks whether the game has reached a win condition. isWinner only looks for
     * horizontal wins.
     *
     * @return 0 if nobody has won yet, 1 if player 1 has won, and 2 if player 2 has won
     */
    boolean isInCheck() {
        int ki = 0;
        int kj = 0;
        // find king position
        for (int i = 0; 8 > i; i++) {
            for (int j = 0; 8 > j; j++) {
                if ("wK".equals(board[i][j]) && player1) {
                    ki = i;
                    kj = j;
                } else if ("bK".equals(board[i][j]) && !player1) {
                    ki = i;
                    kj = j;
                }
            }
        }
        return isInCheck(kj + 1, ki + 1);
    }

    private boolean isInCheck(int x, int y) {
        int ki = y - 1;
        int kj = x - 1;

        for (int i = 0; 8 > i; i++) {
            for (int j = 0; 8 > j; j++) {
                if (player1) {
                    player1 = false;
                    if (!"".equals(board[i][j]) && 'b' == board[i][j].charAt(0)
                            && isLegal(j + 1, i + 1, kj + 1, ki + 1)) {
                        player1 = !player1;
                        return true;
                    }
                } else {
                    player1 = true;
                    if (!"".equals(board[i][j]) && 'w' == board[i][j].charAt(0)
                            && isLegal(j + 1, i + 1, kj + 1, ki + 1)) {
                        player1 = !player1;
                        return true;
                    }
                }
                player1 = !player1;
            }
        }
        return false;
    }

    private boolean isMate() {
        if (isInCheck()) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (!"".equals(board[i][j])
                            && (("b".equals(board[i][j].substring(0, 1)) && !player1) ||
                            ("w".equals(board[i][j].substring(0, 1)) && player1))) {
                        for (int i2 = 0; i2 < 8; i2++) {
                            for (int j2 = 0; j2 < 8; j2++) {
                                if (isLegal(j + 1, i + 1, j2 + 1, i2 + 1)) {
                                    String store = board[i2][j2];
                                    board[i2][j2] = board[i][j];
                                    board[i][j] = "";

                                    if (!isInCheck()) {
                                        board[i][j] = board[i2][j2];
                                        board[i2][j2] = store;
                                        return false;
                                    }
                                    board[i][j] = board[i2][j2];
                                    board[i2][j2] = store;
                                }
                            }
                        }
                    }
                }
            }
        } else {
            return false;
        }
        return true;
    }

    int winner() {
        if (isInCheck() && isMate()) {
            if (player1) {
                return 2;
            } else {
                return 1;
            }
        }
        return 0;
    }

    /**
     * printGameState prints the current game state for debugging and test cases.
     */
    void printGameState() {
        System.out.println("\n\nTurn " + turnCounter + ":\n");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[7 - i][j].isEmpty()) {
                    System.out.print("  ");
                } else {
                    System.out.print(board[7 - i][j]);
                }
                if (7 > j) {
                    System.out.print(" | ");
                }
            }
            if (7 > i) {
                System.out.println("\n-------------------------------------------");
            }
        }
    }

    /**
     * reset (re-)sets the game state to start a new game.
     */
    void reset() {
        board = new String[][] {
            { "wR", "wN", "wB", "wQ", "wK", "wB", "wN", "wR" },
            { "wP", "wP", "wP", "wP", "wP", "wP", "wP", "wP" },
            { "", "", "", "", "", "", "", "" },
            { "", "", "", "", "", "", "", "" },
            { "", "", "", "", "", "", "", "" },
            { "", "", "", "", "", "", "", "" },
            { "bP", "bP", "bP", "bP", "bP", "bP", "bP", "bP" },
            { "bR", "bN", "bB", "bQ", "bK", "bB", "bN", "bR" }
        };
        turnCounter = 1;
        player1 = true;
        wKCastle = true;
        wQCastle = true;
        bKCastle = true;
        bQCastle = true;
        // gameOver = false;
        enPassant = 0;
        gameState = new LinkedList<>();
        System.out.println(START_OVER);
    }

    void undo() {
        if (player1) {
            if (1 < turnCounter) {
                board = gameState.get(gameState.size() - 2);
                gameState.remove(gameState.size() - 1);
                turnCounter--;
                player1 = false;
            }
        } else {
            if (1 < turnCounter) {
                board = gameState.get(gameState.size() - 2);
                gameState.remove(gameState.size() - 1);
                player1 = true;
            } else {
                reset();
            }
        }
    }

    private String[][] cloner() {
        String[][] clone = new String[8][8];
        for (int i = 0; 8 > i; i++) {
            System.arraycopy(board[i], 0, clone[i], 0, 8);
        }
        return clone;
    }

    boolean isCurrPlayer() {
        return player1;
    }

    /**
     * prevGameState is a getter for the contents of the cell specified by the method arguments.
     *
     * @param col column to retrieve
     * @param row row to retrieve
     */
    String prevGameState(int col, int row) {
        return board[row][col];
    }

    public static void main(String[] args) {
    }
}