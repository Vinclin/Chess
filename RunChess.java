package org.cis1200.chess;

/*
 * CIS 120 HW09 - Chess
 * (c) University of Pennsylvania
 */

import java.awt.*;
import javax.swing.*;

public class RunChess implements Runnable {
    @Override
    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.
        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Chess");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel statusPanel = new JPanel();
        frame.add(statusPanel, BorderLayout.PAGE_END);
        final JLabel status = new JLabel("Initializing Game...");
        statusPanel.add(status);

        // Game board
        final org.cis1200.chess.ChessBoard board = new org.cis1200.chess.ChessBoard(status);
        frame.add(board, BorderLayout.CENTER);

        // reset button
        final JPanel resetPanel = new JPanel();
        frame.add(resetPanel, BorderLayout.PAGE_START);

        final JButton reset = new JButton(Chess.START_OVER);
        reset.addActionListener(e -> board.reset());
        resetPanel.add(reset);

        // undo button
        final JButton undo = new JButton("Undo");
        undo.addActionListener(e -> board.undo());
        resetPanel.add(undo);

        // instructions button
        final JButton instructions = new JButton("Instructions");
        final String message = """
                Hello there! Welcome to Vincent's Chess Program. Here are the basics to the
                mechanics of Chess:
                
                Chess is a two-player game played on a board with 64 squares, arranged in an 8x8
                grid. Each player starts with 16 pieces: one king, one queen, two rooks, two
                knights, two bishops, and eight pawns.
                
                The objective of the game is to checkmate your opponent's king, which means putting
                their king in a position where it is under attack (in "check") and there is no
                legal move to escape capture.
                
                Players take turns moving their pieces, with white moving first. Each piece moves
                in a specific way. The king can move one square in any direction, the queen can
                move diagonally, horizontally, or vertically any number of squares, the rook can
                move horizontally or vertically any number of squares, the bishop can move
                diagonally any number of squares, the knight moves in an L-shape (two squares in
                one direction and then one square perpendicular to that), and the pawn moves
                forward one square (or two on its first move) and captures diagonally.
                
                When a pawn reaches the opposite end of the board, it can be promoted to any other
                piece (except a king). There are a few special moves in chess, such as castling
                (moving the king two squares towards a rook and moving the rook to the other side
                of the king) and en passant (capturing a pawn that has just moved two squares
                forward from its starting position).
                
                The game ends when one player checkmates the other, resigns, or the game is
                declared a draw (such as when there is no possible way for either player to
                checkmate the other).
                
                Enjoy!""";

        instructions.addActionListener(e -> JOptionPane.showMessageDialog(new JFrame(),message));
        resetPanel.add(instructions);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // reset game
        board.reset();
    }
}