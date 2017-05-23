package com.imegumii;

import java.util.*;

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

    public SortedSet<T> statesBereikbaarVanaf(T vanaf, char s) {
        SortedSet<T> bereikbaar = new TreeSet<T>();
        Iterator<Transition<T>> it = this.transistions.iterator();

        while(it.hasNext()) {
            Transition<T> t = it.next();

            boolean isGelijk = false;
            isGelijk = s == t.symbol;

            if (isGelijk && !bereikbaar.contains(t.naarState)) {
                bereikbaar.add(t.naarState);
                bereikbaar.addAll(statesBereikbaarVanaf(t.naarState, Transition.EPSILON));
            }
        }

        return bereikbaar;
    }

    public SortedSet<T> statesBereikbaarVanafSymbool(char s) {
        SortedSet<T> bereikbareStates = new TreeSet<T>();
        Iterator<T> it = this.states.iterator();
        while (it.hasNext()) {
            T state = it.next();
            bereikbareStates.addAll(statesBereikbaarVanaf(state, s));
        }

        return bereikbareStates;
    }

    public SortedSet<T> transitie (char c) {
        SortedSet<T> bereikbareStates = statesBereikbaarVanafSymbool(c);
        if (bereikbareStates.equals(this.states)) {
            return this.states;
        } else {
            return bereikbareStates;
        }
    }

    public DFA<T> toDFA () {
        DFA<T> eindDFA = new DFA<T>(this.symbols);

        SortedSet<T> startSet = new TreeSet<T>();
        T firstState = this.beginStates.first();
        startSet.add(firstState);
        startSet.addAll(statesBereikbaarVanaf(firstState, Transition.EPSILON));
        System.out.println("Begin states zijn: ");
        startSet.forEach(System.out::println);

        for (T t : startSet) {
            eindDFA.defineAsStartState(t);
        }

        Queue<SortedSet<T>> kue = new LinkedList<>();
        kue.offer(startSet);

        while (!kue.isEmpty()) {
            SortedSet<T> set = kue.poll();

        }

        return eindDFA;
    }
}
