import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.GridLayout;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mycompany.app.App;

class ProgramTest {

    private Game game;
    private PrintStream originalOut;
    private ByteArrayOutputStream capturedOut;

    @BeforeEach
    void setUp() {
        game = new Game();
        originalOut = System.out;
        capturedOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(capturedOut));
    }

    @AfterEach
    void restoreStdout() {
        System.setOut(originalOut);
    }

    @Test
    void appStubTest() {
        assertTrue(true);
        assertNotNull(new App());
    }

    @Test
    void programCanBeCreated() {
        assertNotNull(new Program());
    }

    @Test
    void gameStartsWithEmptyBoard() {
        assertEquals(State.PLAYING, game.state);
        assertEquals('X', game.player1.symbol);
        assertEquals('O', game.player2.symbol);
        for (int i = 0; i < 9; i++) {
            assertEquals(' ', game.board[i]);
        }
    }

    @Test
    void playerFieldsExist() {
        Player player = new Player();
        assertEquals(0, player.move);
        assertFalse(player.selected);
        assertFalse(player.win);
    }

    @Test
    void checkStateDetectsXWin() {
        game.symbol = 'X';
        char[] board = {'X', 'X', 'X', 'O', 'O', ' ', ' ', ' ', ' '};
        assertEquals(State.XWIN, game.checkState(board));
    }

    @Test
    void checkStateDetectsOWin() {
        game.symbol = 'O';
        char[] board = {'O', 'O', 'O', 'X', 'X', ' ', ' ', ' ', ' '};
        assertEquals(State.OWIN, game.checkState(board));
    }

    @Test
    void checkStateDetectsDraw() {
        game.symbol = 'X';
        char[] board = {'X', 'O', 'X', 'X', 'O', 'O', 'O', 'X', 'X'};
        assertEquals(State.DRAW, game.checkState(board));
    }

    @Test
    void checkStateDetectsPlaying() {
        game.symbol = 'X';
        char[] board = {'X', 'O', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
        assertEquals(State.PLAYING, game.checkState(board));
    }

    @Test
    void generateMovesFindsEmptyCells() {
        char[] board = {'X', ' ', 'O', ' ', ' ', 'X', 'O', ' ', 'X'};
        ArrayList<Integer> moves = new ArrayList<>();
        game.generateMoves(board, moves);
        assertEquals(Arrays.asList(1, 3, 4, 7), moves);
    }

    @Test
    void evaluatePositionForWinLossAndDraw() {
        game.symbol = 'X';
        char[] xWin = {'X', 'X', 'X', ' ', ' ', ' ', ' ', ' ', ' '};
        assertEquals(Game.INF, game.evaluatePosition(xWin, game.player1));
        assertEquals(-Game.INF, game.evaluatePosition(xWin, game.player2));

        game.symbol = 'O';
        char[] oWin = {'O', 'O', 'O', 'X', ' ', ' ', ' ', ' ', ' '};
        assertEquals(Game.INF, game.evaluatePosition(oWin, game.player2));

        game.symbol = 'X';
        char[] draw = {'X', 'O', 'X', 'X', 'O', 'O', 'O', 'X', 'X'};
        assertEquals(0, game.evaluatePosition(draw, game.player1));
    }

    @Test
    void evaluatePositionReturnsMinusOneWhilePlaying() {
        char[] board = {'X', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
        assertEquals(-1, game.evaluatePosition(board, game.player1));
    }

    @Test
    void miniMaxReturnsAllowedMove() {
        char[] board = {'X', 'O', 'X', ' ', 'O', 'X', 'O', ' ', ' '};
        int move = game.MiniMax(board, game.player2);
        assertTrue(move >= 1 && move <= 9);
        assertEquals(' ', board[move - 1]);
    }

    @Test
    void miniMaxFindsWinningMove() {
        char[] board = {'O', 'O', ' ', 'X', 'X', ' ', ' ', ' ', ' '};
        assertEquals(3, game.MiniMax(board, game.player2));
    }

    @Test
    void miniMaxBlocksThreat() {
        char[] board = {'X', 'X', ' ', ' ', 'O', ' ', ' ', ' ', ' '};
        assertEquals(3, game.MiniMax(board, game.player2));
    }

    @Test
    void minMoveOnWinningBoard() {
        game.symbol = 'X';
        char[] board = {'X', 'X', 'X', ' ', 'O', ' ', ' ', ' ', ' '};
        assertEquals(Game.INF, game.MinMove(Arrays.copyOf(board, 9), game.player1));
    }

    @Test
    void maxMoveOnWinningBoard() {
        game.symbol = 'O';
        char[] board = {'O', 'O', 'O', 'X', ' ', ' ', ' ', ' ', ' '};
        assertEquals(Game.INF, game.MaxMove(Arrays.copyOf(board, 9), game.player2));
    }

    @Test
    void utilityPrintCharBoard() {
        Utility.print(new char[] {'X', 'O', ' ', ' ', ' ', ' ', ' ', ' ', ' '});
        assertTrue(capturedOut.toString().contains("X-"));
    }

    @Test
    void utilityPrintIntBoard() {
        Utility.print(new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8});
        assertTrue(capturedOut.toString().contains("0-"));
    }

    @Test
    void utilityPrintMoveList() {
        Utility.print(new ArrayList<>(Arrays.asList(0, 4, 8)));
        assertTrue(capturedOut.toString().contains("0-"));
    }

    @Test
    void ticTacToeCellStoresMarker() {
        TicTacToeCell cell = new TicTacToeCell(5, 2, 1);
        assertEquals(5, cell.getNum());
        assertEquals(2, cell.getCol());
        assertEquals(1, cell.getRow());
        assertEquals(' ', cell.getMarker());

        cell.setMarker("X");
        assertEquals('X', cell.getMarker());
        assertEquals("X", cell.getText());
        assertFalse(cell.isEnabled());
    }

    @Test
    void ticTacToePanelCanBeCreated() {
        TicTacToePanel panel = new TicTacToePanel(new GridLayout(3, 3));
        assertNotNull(panel);
    }
}
