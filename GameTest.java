package org.cis1200.chess;

import org.junit.jupiter.api.*;

/**
 * You can use this file (and others) to test your
 * implementation.
 */

class GameTest {

    @Test
    void testEnPassant() {
        Chess t = new Chess();
        t.isPlayerTurn(5, 2, 5, 4);
        t.isPlayerTurn(4, 7, 4, 5);
        t.isPlayerTurn(5, 4, 5, 5);
        t.isPlayerTurn(5, 7, 5, 6);
        Assertions.assertFalse(t.isPlayerTurn(5, 5, 4, 6), "");

        t.isPlayerTurn(4, 2, 4, 4);
        t.isPlayerTurn(6, 7, 6, 5);
        Assertions.assertTrue(t.isPlayerTurn(5, 5, 6, 6), "");
    }

    @Test
    void testIsInCheck() {
        Chess t = new Chess();
        t.isPlayerTurn(5, 2, 5, 4);
        t.isPlayerTurn(4, 7, 4, 5);
        Assertions.assertFalse(t.isInCheck(), "");

        t.isPlayerTurn(6, 1, 2, 5);
        Assertions.assertEquals(0, t.winner(), "");
        Assertions.assertTrue(t.isInCheck(), "");
    }

    @Test
    void testWhiteCheckMate() {
        Chess t = new Chess();
        t.isPlayerTurn(4, 2, 4, 3);
        t.isPlayerTurn(6, 7, 6, 6);
        t.isPlayerTurn(5, 2, 5, 4);
        t.isPlayerTurn(7, 7, 7, 5);
        Assertions.assertEquals(0, t.winner(), "");
        //checkmate
        t.isPlayerTurn(4, 1, 8, 5);
        Assertions.assertEquals(1, t.winner(), "");
    }

    @Test
    void testBlackCheckMate() {
        Chess t = new Chess();
        t.isPlayerTurn(6, 2, 6, 3);
        t.isPlayerTurn(5, 7, 5, 5);
        t.isPlayerTurn(7, 2, 7, 4);
        Assertions.assertEquals(0, t.winner(), "");
        //checkmate
        t.isPlayerTurn(4, 8, 8, 4);
        Assertions.assertEquals(2, t.winner(), "");
    }

    @Test
    void testIllegalCastleIntoCheck() {
        Chess t = new Chess();
        t.isPlayerTurn(5, 2, 5, 4);
        t.isPlayerTurn(5, 7, 5, 5);
        t.isPlayerTurn(6, 1, 3, 4);
        t.isPlayerTurn(6, 8, 3, 5);
        Assertions.assertEquals(0, t.winner(), "");

        t.isPlayerTurn(6, 2, 6, 4);
        t.isPlayerTurn(2, 8, 3, 6);
        t.isPlayerTurn(7, 1, 6, 3);
        t.isPlayerTurn(3, 6, 2, 8);
        Assertions.assertFalse(t.isPlayerTurn(5, 1, 7, 1), "");
        t.printGameState();
    }

    @Test
    void testCastle() {
        Chess t = new Chess();
        t.isPlayerTurn(5, 2, 5, 4);
        t.isPlayerTurn(5, 7, 5, 5);
        t.isPlayerTurn(6, 1, 3, 4);
        t.isPlayerTurn(6, 8, 3, 5);
        Assertions.assertEquals(0, t.winner(), "");

        t.isPlayerTurn(7, 1, 6, 3);
        t.isPlayerTurn(2, 8, 3, 6);
        t.printGameState();
        Assertions.assertTrue(t.isPlayerTurn(5, 1, 7, 1), "");
    }
}
