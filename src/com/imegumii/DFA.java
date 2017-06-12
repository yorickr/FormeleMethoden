package com.imegumii;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import javafx.util.Pair;

/**
 * Created by imegumii on 01/05/2017.
 */
public class DFA<T extends Comparable> extends Automata<T> {

    public DFA() {
        super();
        this.type = Type.DFA;
    }

    public boolean accepteer(String s) {
        // bij een DFA is er maar een beginstate, dus gebruiken we first.
        T beginState = this.beginStates.first();
        T currentState = beginState;


        for (int i = 0; i < s.length(); i++) {
            for (Transition<T> t : this.transistions) {
//                System.out.println("State is " + t.vanState);
//                System.out.println("CurrentState is " + currentState);
                if (t.vanState.equals(currentState)) {
//                    System.out.println("Char is " + s.charAt(i) + " and state is " + t);
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

    public NDFA<String> reverse()
    {
        NDFA<String> reversed = new NDFA<String>();

        for(Transition<T> t : transistions)
        {
            reversed.addTransition(new Transition<String>(t.naarState.toString(), t.symbol, t.vanState.toString()));
        }

        for(T state : eindStates) {
            reversed.defineAsStartState(state.toString());
        }

        for(T state : beginStates) {
            reversed.defineAsEndState(state.toString());
        }

        return reversed;
    }

    public DFA<String> minimaliseer() {
        return this.reverse().toDFA().reverse().toDFA();
    }

    public DFA<String> minimaliseerHopcroft() {
        HopcroftConverter converter = new HopcroftConverter((DFA<String>) this);
        return converter.minimize();
    }

    public DFA<T> ontkenning() {
        DFA<T> returnDFA = new DFA<T>();

        SortedSet<T> newEndStates = new TreeSet<>();

        for (T state : this.states) {
            if (!this.eindStates.contains(state)) {
                newEndStates.add(state);
            }
        }

        for (T newEndState : newEndStates) {
            returnDFA.defineAsEndState(newEndState);
        }

        for (T beginState : this.beginStates) {
            returnDFA.defineAsStartState(beginState);
        }

        for (Transition<T> transistion : this.transistions) {
            returnDFA.addTransition(transistion);
        }

        return returnDFA;
    }

    public DFA<String> en(DFA<T> other)
    {
        return maakTupleDFA((DFA<String>)this, (DFA<String>)other, false);
    }

    public DFA<String> of(DFA<T> other)
    {
        return maakTupleDFA((DFA<String>)this, (DFA<String>)other, true);
    }

    private DFA<String> maakTupleDFA(DFA<String> dfa1, DFA<String> dfa2, boolean of)
    {
        DFA<String> merged = new DFA<String>();

        //Create all transitions and states
        for(Transition t1 : dfa1.transistions)
        {
            for(Transition t2 : dfa2.transistions)
            {
                if(t1.symbol == t2.symbol) {
                    Transition t3 = new Transition<String>(t1.vanState + "-" + t2.vanState, t1.symbol, t1.naarState + "-" + t2.naarState);

                    if(dfa1.beginStates.contains(t1.vanState) && dfa2.beginStates.contains(t2.vanState))
                        merged.defineAsStartState((String)t3.vanState);

                    if(of)
                    {
                        if(dfa1.eindStates.contains(t1.vanState) || dfa2.eindStates.contains(t2.vanState))
                            merged.defineAsEndState((String)t3.vanState);
                    }
                    else
                    {
                        if(dfa1.eindStates.contains(t1.vanState) && dfa2.eindStates.contains(t2.vanState))
                            merged.defineAsEndState((String)t3.vanState);
                    }

                    merged.addTransition(t3);
                }
            }
        }

        //Delete wrong states
        SortedSet<String> usedStates = new TreeSet<String>();
        for(Transition t : merged.transistions)
        {
            usedStates.add((String)t.naarState);
        }

        Iterator<Transition<String>> it = merged.transistions.iterator();

        while(it.hasNext())
        {
            Transition<String> t = it.next();

            if(!usedStates.contains(t.vanState) && !merged.beginStates.contains(t.vanState))
            {
                it.remove();
                merged.eindStates.remove(t.vanState);
            }
        }

        return merged;
    }
}
