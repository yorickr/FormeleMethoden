package com.imegumii;

import java.util.SortedSet;

/**
 * Created by imegumii on 01/05/2017.
 */
public class NDFA<T extends Comparable> extends Automata<T> {
    public NDFA(Character[] symbols) {
        super(symbols);
    }

    public NDFA(SortedSet<Character> symbols) {
        super(symbols);
    }
}
