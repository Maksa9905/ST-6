package com.mycompany.app;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class UtilityTest {

    @Test
    void printCharBoardWritesToStdout() {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        PrintStream old = System.out;
        System.setOut(new PrintStream(buf));
        try {
            Utility.print(new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i'});
        } finally {
            System.setOut(old);
        }
        String s = buf.toString();
        assertTrue(s.contains("a-"));
        assertTrue(s.contains("i-"));
    }

    @Test
    void printIntBoardWritesToStdout() {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        PrintStream old = System.out;
        System.setOut(new PrintStream(buf));
        try {
            Utility.print(new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8});
        } finally {
            System.setOut(old);
        }
        assertTrue(buf.toString().contains("0-"));
    }

    @Test
    void printMoveListWritesToStdout() {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        PrintStream old = System.out;
        System.setOut(new PrintStream(buf));
        try {
            Utility.print(new ArrayList<Integer>(Arrays.asList(2, 4, 6)));
        } finally {
            System.setOut(old);
        }
        assertTrue(buf.toString().contains("2-"));
    }
}
