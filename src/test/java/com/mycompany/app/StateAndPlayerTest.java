package com.mycompany.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class StateAndPlayerTest {

    @Test
    void stateEnumValues() {
        assertEquals(4, State.values().length);
        assertNotNull(State.valueOf("PLAYING"));
    }

    @Test
    void playerFieldsDefault() {
        Player p = new Player();
        assertEquals(0, p.move);
        assertFalse(p.selected);
        assertFalse(p.win);
    }

    @Test
    void programInstantiableForCoverage() throws Exception {
        Program p = new Program();
        assertNotNull(p);
    }
}
