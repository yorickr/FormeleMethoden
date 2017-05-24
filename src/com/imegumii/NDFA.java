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

            if (t.vanState == vanaf) {
                boolean isGelijk = false;
                isGelijk = s == t.symbol;

                if (isGelijk && !bereikbaar.contains(t.naarState)) {
                    bereikbaar.add(t.naarState);
                    bereikbaar.addAll(statesBereikbaarVanaf(t.naarState, Transition.EPSILON));
                }
            }

        }

        return bereikbaar;
    }

    public DFA<String> toDFA () {
        DFA<String> eindDFA = new DFA<>(this.symbols);

        SortedSet<T> startSet = new TreeSet<T>();
        T firstState = this.beginStates.first();
        startSet.add(firstState);
        startSet.addAll(statesBereikbaarVanaf(firstState, Transition.EPSILON));
//        System.out.println("Begin states zijn: ");
//        startSet.forEach(System.out::println);

        HashMap<SortedSet<T>, Integer> totaalEindStates = new HashMap<>();

        totaalEindStates.put(startSet, 0);

        Queue<SortedSet<T>> kue = new LinkedList<>();
        kue.offer(startSet); // add beginstate(s)

//        System.out.println("Start ------------");

        int stateCounter = 1;

        SortedSet<Transition<String>> trans = new TreeSet<>();

        while (!kue.isEmpty()) {
            SortedSet<T> set = kue.poll();
            // we beginnen bij de begin states

            // kijk welke states je kunt bereiken vanaf deze state.
            for (Character symbol : this.symbols) {
                SortedSet<T> totaalTeBereiken = new TreeSet<>();
                boolean isEindState = false;
                boolean isBeginState = stateCounter == 1;
                for (T t : set) {
                    // kijk wat er te bereiken is vanaf t
                    SortedSet<T> bereikbareStates = statesBereikbaarVanaf(t, symbol);
//                    System.out.println("Vanaf " + t + " kan ik bij " + bereikbareStates + " komen via " + symbol);
                    totaalTeBereiken.addAll(bereikbareStates);
                    if (this.eindStates.contains(t)) {
                        isEindState = true;
                    }
                }

                if (!totaalEindStates.containsKey(totaalTeBereiken)) {
                    kue.offer(totaalTeBereiken);
                    totaalEindStates.put(totaalTeBereiken, stateCounter);
                    stateCounter++;
                }

                // add transitions
//                System.out.println("Vanaf " + set + " kan ik dus bij " + totaalTeBereiken + " komen via " + symbol);
                eindDFA.addTransition(new Transition<>(totaalEindStates.get(set).toString(), symbol, totaalEindStates.get(totaalTeBereiken).toString()));
                if (isBeginState) {
                    // beginstate
                    eindDFA.defineAsStartState(totaalEindStates.get(set).toString());
                }
                if (isEindState) {
//                    System.out.println("Eind state is ");
                    eindDFA.defineAsEndState(totaalEindStates.get(set).toString());
                }

            }

        }

//        System.out.println("----end");

//        System.out.println("In totaal krijgen we " + totaalEindStates);
//        eindDFA.transistions.forEach((t) -> {
////            System.out.println(t.vanState);
//            System.out.println("(" + getByValue(totaalEindStates, t.vanState) + ", " + t.symbol + ")-->" + getByValue(totaalEindStates, t.naarState));
//
//        });


//        trans.forEach(System.out::println);
//        for (Transition<String> t : trans) {
//            eindDFA.addTransition(t);
//        }
        return eindDFA;
    }

//    public SortedSet<T> getByValue (HashMap<SortedSet<T>, Integer> map, String value) {
//        for (Map.Entry<SortedSet<T>, Integer> e : map.entrySet()) {
//            if (value.equals(e.getValue().toString())) {
//                return e.getKey();
//            }
//        }
//
//        return null;
//    }

}
