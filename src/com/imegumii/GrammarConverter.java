package com.imegumii;

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by imegumii on 12/06/2017.
 */
public class GrammarConverter {

    private SortedSet<String> getGrammar (NDFA<String> automata, String state) {
        SortedSet<String> canGoTo = new TreeSet<>();
        for (Character symbol : automata.symbols) {
            SortedSet<String> bereikbaarVanafViaEpsilonBereikbareState = automata.statesBereikbaarVanaf(state, symbol);
            for (String s1 : bereikbaarVanafViaEpsilonBereikbareState) {
                if (automata.eindStates.contains(s1)) {
                    canGoTo.add("" + symbol);
                } else {
                    canGoTo.add("" + symbol + s1);
                }
            }
        }
        return canGoTo;
    }

    public Grammar toGrammar(NDFA<String> automata) {
        ArrayList<Grammar> grammar = new ArrayList<>();
        Grammar retval = null;
        for (String state : automata.states) {
            SortedSet<String> canGoTo = new TreeSet<>();
            // Check where we can go from here
            for (Character symbol : automata.symbols) {
//                canGoTo.add("" + symbol + state);
                // follow epsilon
                canGoTo.addAll(getGrammar(automata, state));

                SortedSet<String> bereikbaarVanafEpsilon = automata.statesBereikbaarVanaf(state, Transition.EPSILON);
                for (String s : bereikbaarVanafEpsilon) {
                    SortedSet<String> bereikbaarVanafEpsilonViaSymbool = automata.statesBereikbaarVanaf(s, symbol);
                    for (String s1 : bereikbaarVanafEpsilonViaSymbool) {
                        canGoTo.add("" + symbol + s1);
                    }
                }
//                for (String s : bereikbaarVanafEpsilon) {
//                    canGoTo.addAll(getGrammar(automata, s));
//                }

            }
            grammar.add(new Grammar(state, canGoTo, null));
        }
//        System.out.println(grammar);
//        grammar.forEach(System.out::println);
        for (Grammar grammar1 : grammar) {
            if (grammar.size() > 1) {
                if (retval == null) {
                    retval = grammar1;
                    continue;
                } else {
                    retval = retval.link(grammar1);
                }
            } else {
                retval = grammar1;
            }
        }
        return grammar.get(0);
    }

    public class Grammar {
        Grammar below;
        String state;
        SortedSet<String> options;

        public Grammar(String state, SortedSet<String> options, Grammar below) {
            this.state = state;
            this.options = options;
            this.below = below;
        }

        @Override
        public String toString() {
            return state + " -> " + options;
        }

        public Grammar link(Grammar other) {
            this.below = other;
            return other;
        }

        public void print() {
            System.out.println(state + " -> " + options);
            if (below != null) {
                below.print();
            }
        }
    }
}
