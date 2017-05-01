package com.imegumii;

import com.imegumii.codevandocent.*;

import java.util.TreeSet;

public class Main {

    public static void Practicum1Opdr1() {
        TreeSet<String> stringsToParse = new TreeSet<>();
        stringsToParse.add("abbaa");
        stringsToParse.add("babaa");
        stringsToParse.add("abbaaabaab");
        stringsToParse.add("ababababababaab");

        Character [] characters = {'a', 'b'};
        DFA<String> myAutomata = new DFA<String>(characters);
        myAutomata.addTransition(new Transition<String>("q0", 'a', "q1"));

        myAutomata.addTransition(new Transition<String>("q1", 'b', "q2"));

        myAutomata.addTransition(new Transition<String>("q2", 'b', "q3"));

        myAutomata.addTransition(new Transition<String>("q3", "q0"));

        myAutomata.defineAsStartState("q0");
        myAutomata.defineAsEndState("q3");

        myAutomata.printTransitions();

        stringsToParse.forEach((s) -> {
            boolean result = myAutomata.accepteer(s);
            System.out.println("string " + s + " fully parsed, result: " + result);
        });
    }

    public static void main(String[] args) {
        System.out.println("Here my code be");
        com.imegumii.codevandocent.Automata<String> t1 = TestAutomata.getExampleSlide8Lesson2();
        t1.printTransitions();
        System.out.println("Zo moet het dus, nu onze beurt.");

        Practicum1Opdr1();

    }
}
