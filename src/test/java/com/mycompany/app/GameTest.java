package com.mycompany.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameTest {

    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
        game.setMiniMaxLogToStdout(false);
        game.setTieBreakRandom(new Random(0));
    }

    @Test
    void constructorInitializesEmptyBoardAndPlayers() {
        assertEquals(State.PLAYING, game.state);
        assertEquals('X', game.player1.symbol);
        assertEquals('O', game.player2.symbol);
        for (int i = 0; i < 9; i++) {
            assertEquals(' ', game.board[i]);
        }
    }

    @Test
    void checkStateDetectsXWinRowsColsDiags() {
        game.symbol = 'X';
        char[] top = {'X', 'X', 'X', ' ', 'O', ' ', 'O', ' ', ' '};
        assertEquals(State.XWIN, game.checkState(top));

        game.symbol = 'X';
        char[] mid = {'O', ' ', ' ', 'X', 'X', 'X', ' ', 'O', ' '};
        assertEquals(State.XWIN, game.checkState(mid));

        game.symbol = 'X';
        char[] col = {'X', 'O', ' ', 'X', ' ', 'O', 'X', ' ', ' '};
        assertEquals(State.XWIN, game.checkState(col));

        game.symbol = 'X';
        char[] d1 = {'X', 'O', ' ', ' ', 'X', 'O', ' ', ' ', 'X'};
        assertEquals(State.XWIN, game.checkState(d1));

        game.symbol = 'X';
        char[] d2 = {' ', 'O', 'X', ' ', 'X', 'O', 'X', ' ', ' '};
        assertEquals(State.XWIN, game.checkState(d2));
    }

    @Test
    void checkStateDetectsOWin() {
        game.symbol = 'O';
        char[] b = {'O', 'O', 'O', 'X', 'X', ' ', ' ', ' ', ' '};
        assertEquals(State.OWIN, game.checkState(b));
    }

    @Test
    void checkStatePlayingWhenEmptyCellsAndNoWin() {
        game.symbol = 'X';
        char[] b = {'X', 'O', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
        assertEquals(State.PLAYING, game.checkState(b));
    }

    @Test
    void checkStateDrawWhenBoardFullNoWin() {
        game.symbol = 'O';
        char[] b = {'X', 'O', 'X', 'X', 'O', 'O', 'O', 'X', 'X'};
        assertEquals(State.DRAW, game.checkState(b));
    }

    @Test
    void generateMovesListsOnlyEmptyIndices() {
        char[] b = {'X', ' ', 'O', ' ', ' ', 'X', 'O', ' ', 'X'};
        ArrayList<Integer> moves = new ArrayList<>();
        game.generateMoves(b, moves);
        assertEquals(Arrays.asList(1, 3, 4, 7), moves);
    }

    @Test
    void evaluatePositionWinLossDrawForMaximizer() {
        game.symbol = 'X';
        char[] xwin = {'X', 'X', 'X', ' ', ' ', ' ', ' ', ' ', ' '};
        assertEquals(Game.INF, game.evaluatePosition(xwin, game.player1));
        assertEquals(-Game.INF, game.evaluatePosition(xwin, game.player2));

        game.symbol = 'O';
        char[] owin = {'O', 'O', 'O', 'X', 'X', ' ', ' ', ' ', ' '};
        assertEquals(Game.INF, game.evaluatePosition(owin, game.player2));

        game.symbol = 'X';
        char[] draw = {'X', 'O', 'X', 'X', 'O', 'O', 'O', 'X', 'X'};
        assertEquals(0, game.evaluatePosition(draw, game.player1));
    }

    @Test
    void evaluatePositionReturnsMinusOneWhenInProgress() {
        char[] b = {'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
        assertEquals(-1, game.evaluatePosition(b, game.player1));
    }

    @Test
    void minimaxReturnsValidMoveOnPartialBoard() {
        game.setTieBreakRandom(new Random(12345));
        char[] b = {'X', 'O', 'X', ' ', 'O', 'X', 'O', ' ', ' '};
        System.arraycopy(b, 0, game.board, 0, 9);
        int m = game.MiniMax(game.board, game.player2);
        assertTrue(m >= 1 && m <= 9);
        assertEquals(' ', game.board[m - 1]);
    }

    @Test
    void minimaxPicksWinningMoveWhenAvailable() {
        game.setTieBreakRandom(new Random(1));
        char[] b = {'O', 'O', ' ', 'X', 'X', ' ', ' ', ' ', ' '};
        System.arraycopy(b, 0, game.board, 0, 9);
        int m = game.MiniMax(game.board, game.player2);
        assertEquals(3, m);
    }

    @Test
    void minimaxBlocksImmediateThreat() {
        game.setTieBreakRandom(new Random(2));
        char[] b = {'X', 'X', ' ', ' ', 'O', ' ', ' ', ' ', ' '};
        System.arraycopy(b, 0, game.board, 0, 9);
        int m = game.MiniMax(game.board, game.player2);
        assertEquals(3, m);
    }

    @Test
    void maxMoveAndMinMoveTerminateOnAlmostFullBoard() {
        game.setTieBreakRandom(new Random(3));
        char[] b = {'X', 'O', 'X', 'X', 'O', 'O', 'O', 'X', ' '};
        System.arraycopy(b, 0, game.board, 0, 9);
        game.symbol = 'X';
        int m = game.MiniMax(game.board, game.player1);
        assertEquals(9, m);
    }

    @Test
    void minMoveReturnsEvaluatedScoreWhenLeaf() {
        game.symbol = 'X';
        char[] b = {'X', 'X', 'X', ' ', 'O', ' ', ' ', ' ', ' '};
        int v = game.MinMove(Arrays.copyOf(b, 9), game.player1);
        assertEquals(Game.INF, v);
    }

    @Test
    void maxMoveReturnsEvaluatedScoreWhenLeaf() {
        game.symbol = 'O';
        char[] b = {'O', 'O', 'O', 'X', ' ', ' ', ' ', ' ', ' '};
        int v = game.MaxMove(Arrays.copyOf(b, 9), game.player2);
        assertEquals(Game.INF, v);
    }

    @Test
    void minMoveRecursesWhenPositionNotTerminal() {
        game.setTieBreakRandom(new Random(7));
        game.player1.symbol = 'X';
        game.player2.symbol = 'O';
        char[] b = {'X', 'O', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
        int v = game.MinMove(Arrays.copyOf(b, 9), game.player1);
        assertTrue(v >= -Game.INF && v <= Game.INF);
    }

    @Test
    void maxMoveRecursesWhenPositionNotTerminal() {
        game.setTieBreakRandom(new Random(8));
        game.player1.symbol = 'X';
        game.player2.symbol = 'O';
        char[] b = {'X', ' ', 'O', ' ', ' ', ' ', ' ', ' ', ' '};
        int v = game.MaxMove(Arrays.copyOf(b, 9), game.player1);
        assertTrue(v >= -Game.INF && v <= Game.INF);
    }
}
