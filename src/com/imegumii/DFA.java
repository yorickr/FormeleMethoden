package com.imegumii;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by imegumii on 01/05/2017.
 */
public class DFA<T extends Comparable> extends Automata<T> {

    public DFA(Character[] symbols) {
        super(symbols);
    }

    public DFA(SortedSet<Character> symbols) {
        super(symbols);
    }

    public boolean accepteer(String s) {
        // bij een DFA is er maar een beginstate, dus gebruiken we first.
        T beginState = this.beginStates.first();
        T currentState = beginState;


        for (int i = 0; i < s.length(); i++) {
//            System.out.println(s.charAt(i));
            for (Transition<T> t : this.transistions) {
                if (t.vanState == currentState) {
                    // if states match, parse using symbols.
                    if (t.symbol == s.charAt(i)) {
                        // symbols match
//                        System.out.println("Symbols match for " + t + " and " + s.charAt(i) + " with currentState " + currentState);
                        currentState = t.naarState;
                        break;
                    }
                }
            }
        }
//        System.out.println("Ended on state " + currentState);
        return this.eindStates.contains(currentState);
    }

    private TreeSet<String> addSymbols(int n, TreeSet<String> tempSet) {
        TreeSet<String> tempGenerated = new TreeSet<>(tempSet);
        if (n == 0) {
            return tempGenerated;
        }

        for(String s : tempSet) {
            for(Character c : this.symbols) {
                tempGenerated.add(s + c);
            }
        }

        tempGenerated.addAll(addSymbols(n - 1, tempGenerated));

        return tempGenerated;
    }

    public Taal geefTaalTotLengte(int n) {
        TreeSet<String> taal = new TreeSet<>();
        for (Character c : this.symbols) {
            taal.add("" + c);
        }
        return new Taal(addSymbols(n - 1, taal));
    }
}
