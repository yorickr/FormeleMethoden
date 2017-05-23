package com.imegumii;

import com.imegumii.codevandocent.*;

import java.util.TreeSet;

public class Main {

    public static void P1Opdracht1(TreeSet<String> stringsToParse) {
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
        final long[] startTime = {System.currentTimeMillis()};
        myAutomata.geefTaalTotLengte(10).getSymbols().forEach((s) -> {
            long before = System.currentTimeMillis();
            boolean result = myAutomata.accepteer(s);
            long after = System.currentTimeMillis();
            long delta = (after-before);
            System.out.println("string " + s + " fully parsed, result: " + result + " , it took " + delta + " ms");
            startTime[0] += delta;
        });
        System.out.println((System.currentTimeMillis() - startTime[0]));
    }

    public static void Practicum1() {
        TreeSet<String> stringsToParse = new TreeSet<>();
        stringsToParse.add("abbaa");
        stringsToParse.add("babaa");
        stringsToParse.add("abbaaabaab");
        stringsToParse.add("abaab");
        stringsToParse.add("aaaabba");
        stringsToParse.add("baab");
        stringsToParse.add("baaba");
        stringsToParse.add("baabaabba");
        stringsToParse.add("aaaaabb"); // this is wrong, something is wrong


        P1Opdracht1(stringsToParse);
    }

    public static void Practicum2()
    {
        RegExp a = new RegExp("a");
        RegExp b = new RegExp("b");

        // expr1: "baa"
        RegExp expr1 = new RegExp("baa");
        // expr2: "bb"
        RegExp expr2 = new RegExp("bb");
        // expr3: "baa | baa"
        RegExp expr3 = expr1.of(expr2);

        // all: "(a|b)*"
        RegExp all = (a.of(b)).ster();

        // expr4: "(baa | baa)+"
        RegExp expr4 = expr3.plus();
        // expr5: "(baa | baa)+ (a|b)*"
        RegExp expr5 = expr4.punt(all);

        System.out.println("taal van (baa):\n" + expr1.getTaal(5));
        System.out.println("taal van (bb):\n" + expr2.getTaal(5));
        System.out.println("taal van (baa | bb):\n" + expr3.getTaal(5));

        System.out.println("taal van (a|b)*:\n" + all.getTaal(5));
        System.out.println("taal van (baa | bb)+:\n" + expr4.getTaal(5));
        System.out.println("taal van (baa | bb)+ (a|b)*:\n" + expr5.getTaal(6));

    }

    public static void Thompson() {
        RegExp expr1 = new RegExp("a");
        RegExp expr2 = new RegExp("b");
        RegExp expr3 = new RegExp("c");
        RegExp expr4 = new RegExp("d");

        RegExp expr5 = expr1.of(expr2);
        RegExp expr6 = expr3.of(expr4);

        RegExp expr7 = expr5.punt(expr6);

        RegExp expr8 = expr7.ster();

        NDFA<String> test = ThompsonConverter.Convert(expr8);

        System.out.println(Graph.generateGraphString(test));
    }

    public static void Practicum4() {
        Character [] characters = {'a', 'b'};
        NDFA<String> myAutomata = new NDFA<String>(characters);

        myAutomata.addTransition(new Transition<String>("0", 'a', "1"));
        myAutomata.addTransition(new Transition<String>("0", 'a', "2"));
        myAutomata.addTransition(new Transition<String>("1", 'a', "1"));
        myAutomata.addTransition(new Transition<String>("1", 'a', "2"));
        myAutomata.addTransition(new Transition<String>("2", 'b', "1"));
        myAutomata.addTransition(new Transition<String>("2", 'b', "3"));
        myAutomata.addTransition(new Transition<String>("3", 'a', "2"));
        myAutomata.addTransition(new Transition<String>("3", 'a', "1"));

        myAutomata.defineAsStartState("0");
        myAutomata.defineAsEndState("1");
        System.out.println("De NDFA is ");
        myAutomata.printTransitions();

        System.out.println("DFA is dan ");
        myAutomata.toDFA().printTransitions();

    }

    public static void main(String[] args) {
//        System.out.println("Here my code be");
//        com.imegumii.codevandocent.Automata<String> t1 = TestAutomata.getExampleSlide8Lesson2();
//        t1.printTransitions();

//        TestRegExp testreg = new TestRegExp();
//        testreg.testLanguage();


//        System.out.println("Zo moet het dus, nu onze beurt.");

//        Practicum1();

//        Practicum2();

        Practicum4();
    }
}
