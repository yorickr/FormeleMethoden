package com.imegumii;

import java.util.SortedSet;

/**
 * Created by imegumii on 02/05/2017.
 */
public class Taal {
    private SortedSet<String> symbols;

    public Taal(SortedSet<String> sortedSet) {
        this.symbols = sortedSet;
    }

    public SortedSet<String> getSymbols() {
        return symbols;
    }

    @Override
    public String toString() {
        return symbols.toString();
    }
}
