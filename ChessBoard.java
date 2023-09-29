package org.cis1200.chess;

/*
 * CIS 120 HW09 - Chess
 * (c) University of Pennsylvania
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.imageio.ImageIO;

/**
 * This class instantiates a TicTacToe object, which is the model for the game.
 * As the user clicks the game board, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 * <p>
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * <a href="https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf">...</a>
 * <p>
 * In a Model-View-Controller framework, GameBoard stores the model as a field
 * and acts as both the controller (with a MouseListener) and the view (with
 * its paintComponent method and the status JLabel).
 */
public final class ChessBoard extends JPanel {
    private final org.cis1200.chess.Chess game; // model for the game
    private final JLabel status; // current status text
    // Game constants
    private static final int BOARD_WIDTH = 800;
    private static final int BOARD_HEIGHT = 800;
    private static int x1;
    private static int y1;

    /**
     * Initializes the game board.
     */
    ChessBoard(JLabel statusInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);
        game = new org.cis1200.chess.Chess(); // initializes model for the game
        status = statusInit; // initializes the status JLabel

        /*
         * Listens for mouse clicks. Updates the model, then updates the game
         * board based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                // updates the model given the coordinates of the mouseclick
                x1 = p.x / 100 + 1;
                y1 = 8 - p.y / 100;
                updateStatus(); // updates the status JLabel
                repaint(); // repaints the game board
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();
                // updates the model given the coordinates of the mouseclick
                game.isPlayerTurn(x1, y1, p.x / 100 + 1, 8 - p.y / 100);
                updateStatus(); // updates the status JLabel
                repaint(); // repaints the game board
            }
        });
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    void reset() {
        game.reset();
        status.setText("White's Turn");
        updateStatus();
        repaint();
        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    void undo() {
        game.undo();
        updateStatus();
        repaint();
        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        if (game.isCurrPlayer()) {
            status.setText("White's Turn");
        } else {
            status.setText("Black's Turn");
        }
        int winner = game.winner();
        if (1 == winner) {
            status.setText("White wins!!!");
        } else if (2 == winner) {
            status.setText("Black wins!!!");
        } else if (3 == winner) {
            status.setText("Draw");
        }
    }

    /**
     * Draws the game board.
     * <p>
     * There are many ways to draw a game board. This approach
     * will not be sufficient for most games because it is not
     * modular. All the logic for drawing the game board is
     * in this method, and it does not take advantage of helper
     * methods. Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draws board
        for (int i = 1; 8 >= i; i++) {
            g.drawLine(i * 100, 0, i * 100, 800);
        }
        for (int i = 1; 8 >= i; i++) {
            g.drawLine(0, i * 100, 800, i * 100);
        }

        // Draws pieces
        for (int i = 0; 8 > i; i++) {
            for (int j = 0; 8 > j; j++) {
                String state = game.prevGameState(i, 7 - j);
                if (null == state || !state.isEmpty()) {
                    // initialize piece images
                    BufferedImage piece;
                    try {
                        piece = ImageIO
                                .read(new File("files/" + state + ".png"));
                        g.drawImage(
                                piece, 10 + 100 * i, 10 + 100 * j, 90 + 100 * i, 90 + 100 * j, 0, 0,
                                piece.getWidth(), piece.getHeight(), null
                        );
                    } catch (IOException e) {
                        System.out.println("error retrieving images of chess pieces");
                    }
                }
            }
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
