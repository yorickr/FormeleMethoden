package com.imegumii;

import java.util.Arrays;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by imegumii on 18/04/2017.
 */
public class Automata <T extends Comparable> {

    private Set<Transition<T>> transistions;

    private SortedSet<T> states;
    private SortedSet<T> beginStates;
    private SortedSet<T> eindStates;

    private SortedSet<Character> symbols;

    public Automata(Character [] symbols) {
        this(new TreeSet<Character>(Arrays.asList(symbols)));
    }

    public Automata(SortedSet<Character> symbols) {
        states = new TreeSet<T>();
        beginStates = new TreeSet<T>();
        eindStates = new TreeSet<T>();

        transistions = new TreeSet<>();

        this.symbols = symbols;
    }

    public void addTransition (Transition<T> t) {
        transistions.add(t);
        states.add(t.vanState);
        states.add(t.naarState);
    }

    public void defineAsStartState(T t) {
        states.add(t);
        beginStates.add(t);
    }

    public void defineAsEndState(T t) {
        states.add(t);
        eindStates.add(t);
    }

    public void printTransitions () {
        transistions.forEach(System.out::println);
    }

    public boolean accept(String stringToSearch) {
        T currentState;
        states.forEach((s) -> {
            System.out.println("State is " + s);
        });
        beginStates.forEach(System.out::println);
        eindStates.forEach(System.out::println);

        for (T beginState : beginStates) {
            currentState = beginState;
            // process currentState.
            System.out.println(beginState);
        }
        return false;
    }
}
