package com.imegumii;

import com.imegumii.codevandocent.*;

import java.util.TreeSet;

public class Main {

    public static void Practicum1Opdr1() {
        TreeSet<String> stringsToParse = new TreeSet<>();
        stringsToParse.add("abbaa");
        stringsToParse.add("babaa");
        stringsToParse.add("abbaaabaab");
        stringsToParse.add("abaab");
        stringsToParse.add("aaaabba");
        stringsToParse.add("baab");
        stringsToParse.add("baaba");
        stringsToParse.add("baabaabba");

        Character [] characters = {'a', 'b'};
        DFA<String> myAutomata = new DFA<String>(characters);
        // AAB
        myAutomata.addTransition(new Transition<String>("q0", 'a', "q1"));

        myAutomata.addTransition(new Transition<String>("q1", 'b', "q2"));
        myAutomata.addTransition(new Transition<String>("q1", 'a', "q0"));

        myAutomata.addTransition(new Transition<String>("q2", 'b', "q3"));
        myAutomata.addTransition(new Transition<String>("q2", 'a', "q5"));

        myAutomata.addTransition(new Transition<String>("q3", "q0"));

        // BAAB
        myAutomata.addTransition(new Transition<String>("q0", 'b', "q4"));

        myAutomata.addTransition(new Transition<String>("q4", 'a', "q5"));
        myAutomata.addTransition(new Transition<String>("q4", 'b', "q4"));

        myAutomata.addTransition(new Transition<String>("q5", 'a', "q6"));
        myAutomata.addTransition(new Transition<String>("q5", 'b', "q4"));

        myAutomata.addTransition(new Transition<String>("q6", 'a', "q4"));
        myAutomata.addTransition(new Transition<String>("q6", 'b', "q7"));

        myAutomata.addTransition(new Transition<String>("q7", 'a', "q4"));
        myAutomata.addTransition(new Transition<String>("q7", 'b', "q4"));

        myAutomata.defineAsStartState("q0");
        myAutomata.defineAsEndState("q3");
        myAutomata.defineAsEndState("q7");

        myAutomata.printTransitions();
        final long[] totalTime = {0};
        stringsToParse.forEach((s) -> {
            long before = System.currentTimeMillis();
            boolean result = myAutomata.accepteer(s);
            long after = System.currentTimeMillis();
            long delta = (after-before);
            System.out.println("string " + s + " fully parsed, result: " + result + " , it took " + delta + " ms");
            totalTime[0] += delta;
        });
        System.out.println(totalTime[0]);
    }

    public static void main(String[] args) {
//        System.out.println("Here my code be");
//        com.imegumii.codevandocent.Automata<String> t1 = TestAutomata.getExampleSlide8Lesson2();
//        t1.printTransitions();
        System.out.println("Zo moet het dus, nu onze beurt.");

        Practicum1Opdr1();

    }
}
