package com.mycompany.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.awt.GridLayout;
import java.util.Random;

import org.junit.jupiter.api.Test;

class TicTacToePanelTest {

    private static void preloadPartialGame(TicTacToePanel panel) {
        Game g = panel.getGame();
        panel.getCell(0).setMarker("X");
        panel.getCell(1).setMarker("O");
        panel.getCell(2).setMarker("X");
        for (int i = 0; i < 9; i++) {
            g.board[i] = panel.getCell(i).getMarker();
        }
        g.cplayer = g.player1;
    }

    @Test
    void panelCreatesGameAndCells() {
        TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));
        panel.setTestMode(true);
        panel.getGame().setMiniMaxLogToStdout(false);
        assertNotNull(panel.getGame());
        assertEquals('X', panel.getGame().cplayer.symbol);
        for (int i = 0; i < 9; i++) {
            assertNotNull(panel.getCell(i));
        }
    }

    @Test
    void humanMoveThenAiMoveDoesNotThrow() {
        TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));
        panel.setTestMode(true);
        panel.getGame().setMiniMaxLogToStdout(false);
        panel.getGame().setTieBreakRandom(new Random(99));
        preloadPartialGame(panel);
        panel.getCell(3).doClick();
        assertEquals('X', panel.getCell(3).getMarker());
    }

    @Test
    void multipleClicksCoverAlternatingPlayers() {
        TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));
        panel.setTestMode(true);
        panel.getGame().setMiniMaxLogToStdout(false);
        panel.getGame().setTieBreakRandom(new Random(42));
        preloadPartialGame(panel);
        int[] order = {4, 5, 6, 7, 8};
        for (int idx : order) {
            if (panel.getCell(idx).isEnabled()) {
                panel.getCell(idx).doClick();
            }
        }
        assertNotNull(panel.getGame().state);
    }
}
