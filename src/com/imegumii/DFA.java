package com.imegumii;

import java.util.Iterator;
import java.util.SortedSet;

/**
 * Created by imegumii on 01/05/2017.
 */
public class DFA<T extends Comparable> extends Automata<T> {

    public DFA(Character[] symbols) {
        super(symbols);
    }

    public DFA(SortedSet symbols) {
        super(symbols);
    }

    boolean accepteer(String s) {
        // because this is a DFA, assume first is always beginstate and endstate.
        T beginState = this.beginStates.first();
        T eindState = this.eindStates.first();
        T currentState = this.beginStates.first();
        for (int i = 0; i < s.length(); i++) {
            System.out.println(s.charAt(i));

            for (Transition<T> tr : this.transistions) {
                System.out.println(tr);
                System.out.println("Current state is : " +currentState);
                if (currentState.equals(tr.vanState)) { // this is our current state
                    System.out.println("This is our current state, checking if symbols match and advancing");
                    if (s.charAt(i) == tr.symbol) {
                        System.out.println("Theyre equal, currentState becomes naarState");
                        currentState = tr.naarState;
                        break;
                    }
                }
            }
        }
        return (currentState == eindState);
    }

    void geefTaalTotLengte(int n) {

    }
}
