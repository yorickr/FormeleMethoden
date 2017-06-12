package com.imegumii;

import java.util.Arrays;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by imegumii on 18/04/2017.
 */
public class Automata <T extends Comparable> extends Importable {

    protected Set<Transition<T>> transistions;

    protected SortedSet<T> states;
    protected SortedSet<T> beginStates;
    protected SortedSet<T> eindStates;

    protected SortedSet<Character> symbols;

    public Automata() {
        super(Type.ERROR);
        states = new TreeSet<T>();
        beginStates = new TreeSet<T>();
        eindStates = new TreeSet<T>();

        transistions = new TreeSet<>();

        this.symbols = new TreeSet<>();
    }

    public void addTransition (Transition<T> t) {
        if(t.symbol != Transition.EPSILON)
            symbols.add(t.symbol);

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

    public String getTransitions()
    {
        String s = "";
        for(Transition t : transistions)
        {
            s += t.toString() + "\n";
        }

        return s;
    }

    public void print() {
        System.out.println("Transitions");
        this.printTransitions();
        System.out.println("Beginstates");
        System.out.println(beginStates);
        System.out.println("Eindstates");
        System.out.println(eindStates);
    }

    public void clearEindStates()
    {
        eindStates.clear();
    }

    public void clearBeginStates()
    {
        beginStates.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (T state : this.states) {
            sb.append(state);
        }
        return "Automata" + sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Automata))
            return false;

        if(this.type != ((Automata<T>)obj).type)
            return false;

        DFA<String> dfa1;
        DFA<String> dfa2;

        if(this.type == Type.NDFA)
        {
            dfa1 = ((NDFA<T>)this).toDFA().minimaliseerHopcroft();
            dfa2 = ((NDFA<T>) obj).toDFA().minimaliseerHopcroft();
        }
        else
        {
            dfa1 = ((DFA<String>)this).minimaliseerHopcroft();
            dfa2 = ((DFA<String>) obj).minimaliseerHopcroft();
        }

        DFA<String> result = dfa2.ontkenning().en(dfa1).minimaliseerHopcroft();

        if(result.states.size() <= 1)
            return true;

        return false;
    }
}
