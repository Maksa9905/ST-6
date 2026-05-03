package com.mycompany.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class TicTacToeCellTest {

    @Test
    void initialGeometryAndMarker() {
        TicTacToeCell c = new TicTacToeCell(5, 2, 1);
        assertEquals(5, c.getNum());
        assertEquals(2, c.getCol());
        assertEquals(1, c.getRow());
        assertEquals(' ', c.getMarker());
    }

    @Test
    void setMarkerUpdatesState() {
        TicTacToeCell c = new TicTacToeCell(0, 0, 0);
        c.setMarker("X");
        assertEquals('X', c.getMarker());
        assertEquals("X", c.getText());
        assertFalse(c.isEnabled());
    }
}
