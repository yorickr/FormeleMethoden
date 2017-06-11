package com.imegumii;

import java.util.TreeSet;

/**
 * Created by kenny on 8-6-2017.
 */
public class Test {
    public static void test()
    {
        //        Practicum1();

//        Practicum2();

//        Practicum4();

//        Practicum5();

//        ReverseAutomata();

//        Hopcroft();

        TupleConstructie();

//        FileReadTest();
    }

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

        NDFA<String> test = ThompsonConverter.convert(expr7);

        System.out.println(Graph.generateGraphString(test));
    }

    public static void Hopcroft()
    {
        Character [] characters = {'a', 'b'};
        DFA<String> automata = new DFA<String>(characters);


        automata.addTransition(new Transition<String>("q0", 'a', "q2"));
        automata.addTransition(new Transition<String>("q0", 'b', "q3"));
        automata.addTransition(new Transition<String>("q1", 'a', "q3"));
        automata.addTransition(new Transition<String>("q1", 'b', "q2"));
        automata.addTransition(new Transition<String>("q2", 'a', "q0"));
        automata.addTransition(new Transition<String>("q2", 'b', "q4"));
        automata.addTransition(new Transition<String>("q3", 'a', "q1"));
        automata.addTransition(new Transition<String>("q3", 'b', "q5"));
        automata.addTransition(new Transition<String>("q4", 'a', "q6"));
        automata.addTransition(new Transition<String>("q4", 'b', "q5"));
        automata.addTransition(new Transition<String>("q5", 'a', "q2"));
        automata.addTransition(new Transition<String>("q5", 'b', "q0"));
        automata.addTransition(new Transition<String>("q6", 'a', "q4"));
        automata.addTransition(new Transition<String>("q6", 'b', "q0"));

        automata.defineAsStartState("q0");

        automata.defineAsEndState("q1");
        automata.defineAsEndState("q3");
        automata.defineAsEndState("q4");
        automata.defineAsEndState("q6");


        /*
        automata.addTransition(new Transition<String>("0", 'a', "0"));
        automata.addTransition(new Transition<String>("0", 'b', "1"));

        automata.addTransition(new Transition<String>("1", 'a', "2"));
        automata.addTransition(new Transition<String>("1", 'b', "1"));

        automata.addTransition(new Transition<String>("2", 'a', "0"));
        automata.addTransition(new Transition<String>("2", 'b', "3"));

        automata.addTransition(new Transition<String>("3", 'a', "4"));
        automata.addTransition(new Transition<String>("3", 'b', "1"));

        automata.addTransition(new Transition<String>("4", 'a', "5"));
        automata.addTransition(new Transition<String>("4", 'b', "3"));

        automata.addTransition(new Transition<String>("5", 'a', "0"));
        automata.addTransition(new Transition<String>("5", 'b', "3"));

        automata.defineAsStartState("0");
        automata.defineAsEndState("2");
        automata.defineAsEndState("4");
        */

        System.out.println("\nNormal automata..");
        System.out.println(Graph.generateGraphString(automata));

        HopcroftConverter convert = new HopcroftConverter(automata);
        DFA<String> minimized = convert.minimize();

        System.out.println("\nMinimized automata..");
        Graph.generateImage(minimized, null);
    }

    public static void ReverseAutomata()
    {
        Character [] characters = {'a', 'b'};
        DFA<String> myAutomata = new DFA<String>(characters);

        // AAB
        myAutomata.addTransition(new Transition<String>("0", 'a', "0"));
        myAutomata.addTransition(new Transition<String>("0", 'b', "1"));

        myAutomata.addTransition(new Transition<String>("1", 'a', "0"));
        myAutomata.addTransition(new Transition<String>("1", 'b', "2"));

        myAutomata.addTransition(new Transition<String>("2", 'a', "0"));
        myAutomata.addTransition(new Transition<String>("2", 'b', "2"));

        myAutomata.defineAsStartState("0");
        myAutomata.defineAsEndState("2");

//        System.out.println(Graph.generateGraphString(myAutomata));

//        System.out.println("\nREVERSE:");

        NDFA<String> reverseAutomata = myAutomata.reverse();

//        System.out.println(Graph.generateGraphString(reverseAutomata));

        System.out.println("ORIGINEEL");
        myAutomata.printTransitions();
        System.out.println("MET BEGIN EN EIND");
        System.out.println(myAutomata.beginStates);
        System.out.println(myAutomata.eindStates);
        System.out.println("REVERSE");
        reverseAutomata.printTransitions();
        System.out.println("MET BEGIN EN EIND");
        System.out.println(reverseAutomata.beginStates);
        System.out.println(reverseAutomata.eindStates);
    }

    public static void Practicum4() {
        Character [] characters = {'a', 'b'};
        NDFA<String> myAutomata = new NDFA<String>(characters);

//        myAutomata.addTransition(new Transition<String>("0", 'a', "1"));
//        myAutomata.addTransition(new Transition<String>("0", 'a', "2"));
//        myAutomata.addTransition(new Transition<String>("1", 'a', "1"));
//        myAutomata.addTransition(new Transition<String>("1", 'a', "2"));
//        myAutomata.addTransition(new Transition<String>("2", 'b', "1"));
//        myAutomata.addTransition(new Transition<String>("2", 'b', "3"));
//        myAutomata.addTransition(new Transition<String>("3", 'a', "2"));
//        myAutomata.addTransition(new Transition<String>("3", 'a', "1"));
//        myAutomata.defineAsStartState("0");
//        myAutomata.defineAsEndState("0");
//        myAutomata.defineAsEndState("1");

        // Test 2
//        myAutomata.addTransition(new Transition<String>("0", 'a', "1"));
//
//        myAutomata.addTransition(new Transition<String>("0", 'a', "3"));
//        myAutomata.addTransition(new Transition<String>("0", 'b', "3"));
//
//        myAutomata.addTransition(new Transition<String>("0", 'a', "2"));
//        myAutomata.addTransition(new Transition<String>("0", 'b', "2"));
//
//        myAutomata.addTransition(new Transition<String>("1", 'a', "1"));
//        myAutomata.addTransition(new Transition<String>("1", 'b', "3"));
//
//        myAutomata.addTransition(new Transition<String>("1", 'a', "2"));
//        myAutomata.addTransition(new Transition<String>("1", 'b', "2"));
//
//        myAutomata.addTransition(new Transition<String>("2", 'b', "2"));
//        myAutomata.addTransition(new Transition<String>("2", 'b', "4"));
//
//        myAutomata.addTransition(new Transition<String>("2", 'b', "3")); //ja? Ja.
//        myAutomata.addTransition(new Transition<String>("3", 'b', "2"));
//
//        myAutomata.addTransition(new Transition<String>("3", 'b', "3"));
//
//        myAutomata.addTransition(new Transition<String>("3", 'a', "4"));
//        myAutomata.addTransition(new Transition<String>("3", 'b', "4"));
//
//        myAutomata.defineAsStartState("0");
//        myAutomata.defineAsEndState("1");

        // Test 3

//        myAutomata.addTransition(new Transition<String>("1", 'a', "2"));
//        myAutomata.addTransition(new Transition<String>("1", 'b', "1"));
//
//        myAutomata.addTransition(new Transition<String>("2", 'a', "3"));
//        myAutomata.addTransition(new Transition<String>("2", 'b', "2"));
//        myAutomata.addTransition(new Transition<String>("2", Transition.EPSILON, "1"));
//
//        myAutomata.addTransition(new Transition<String>("3", 'a', "4"));
//        myAutomata.addTransition(new Transition<String>("3", 'b', "3"));
//        myAutomata.addTransition(new Transition<String>("3", Transition.EPSILON, "2"));
//
//        myAutomata.addTransition(new Transition<String>("4", 'a', "1"));
//        myAutomata.addTransition(new Transition<String>("4", 'b', "4"));
//        myAutomata.addTransition(new Transition<String>("4", Transition.EPSILON, "3"));
//
//        myAutomata.defineAsStartState("1");
//        myAutomata.defineAsEndState("2");
//        myAutomata.defineAsEndState("3");

        System.out.println("De NDFA is ");
        myAutomata.printTransitions();
        System.out.println("Met als begin en eind states");
        System.out.println(myAutomata.beginStates);
        System.out.println(myAutomata.eindStates);

        System.out.println("DFA is dan ");
        DFA<String> myDfa = myAutomata.toDFA();
        myDfa.printTransitions();
        System.out.println("Met als begin en eind states");
        System.out.println(myDfa.beginStates);
        System.out.println(myDfa.eindStates);

    }

    public static void Practicum5 () {

        Character [] characters = {'a', 'b'};
        DFA<String> myAutomata = new DFA<String>(characters);
        // Test 4 minimalisatie

        myAutomata.addTransition(new Transition<String>("0", 'a', "0"));
        myAutomata.addTransition(new Transition<String>("0", 'b', "1"));

        myAutomata.addTransition(new Transition<String>("1", 'a', "2"));
        myAutomata.addTransition(new Transition<String>("1", 'b', "1"));

        myAutomata.addTransition(new Transition<String>("2", 'a', "0"));
        myAutomata.addTransition(new Transition<String>("2", 'b', "3"));

        myAutomata.addTransition(new Transition<String>("3", 'a', "4"));
        myAutomata.addTransition(new Transition<String>("3", 'b', "1"));

        myAutomata.addTransition(new Transition<String>("4", 'a', "5"));
        myAutomata.addTransition(new Transition<String>("4", 'b', "3"));

        myAutomata.addTransition(new Transition<String>("5", 'a', "0"));
        myAutomata.addTransition(new Transition<String>("5", 'b', "3"));

        myAutomata.defineAsStartState("0");
        myAutomata.defineAsEndState("2");
        myAutomata.defineAsEndState("4");

        System.out.println("Ongeminimaliseerd krijgen we:");
        myAutomata.print();
        System.out.println("-------");
        Graph.generateImage(myAutomata, null);

        DFA<String> geminimaliseerd = myAutomata.minimaliseer();
        System.out.println("Geminimaliseerd krijgen we:");
        geminimaliseerd.print();
        System.out.println("------");
        Graph.generateImage(geminimaliseerd, null);
        geminimaliseerd.ontkenning();
    }

    public static void TupleConstructie()
    {
        Character [] characters = {'a', 'b'};
        DFA<String> aut1 = new DFA<String>(characters);

        aut1.addTransition(new Transition<String>("1", 'a', "2"));
        aut1.addTransition(new Transition<String>("1", 'b', "1"));

        aut1.addTransition(new Transition<String>("2", 'a', "1"));
        aut1.addTransition(new Transition<String>("2", 'b', "2"));


        aut1.defineAsStartState("1");
        aut1.defineAsEndState("1");

        System.out.println("\n\nA1\n--------");
        System.out.println(Graph.generateImageString(aut1));
        Graph.generateImage(aut1, "automaat1");


        DFA<String> aut2 = new DFA<String>(characters);

        aut2.addTransition(new Transition<String>("1", 'a', "1"));
        aut2.addTransition(new Transition<String>("1", 'b', "2"));

        aut2.addTransition(new Transition<String>("2", 'a', "1"));
        aut2.addTransition(new Transition<String>("2", 'b', "3"));

        aut2.addTransition(new Transition<String>("3", 'a', "1"));
        aut2.addTransition(new Transition<String>("3", 'b', "4"));

        aut2.addTransition(new Transition<String>("4", 'a', "4"));
        aut2.addTransition(new Transition<String>("4", 'b', "4"));

        aut2.defineAsStartState("1");
        aut2.defineAsEndState("1");
        aut2.defineAsEndState("2");

        System.out.println("\n\nA2\n--------");
        System.out.println(Graph.generateImageString(aut2));
        Graph.generateImage(aut2, "automaat2");

        DFA<String> en = aut1.en(aut2);

        System.out.println("\n\nEN\n--------");
        System.out.println(Graph.generateImageString(en));
        Graph.generateImage(en, "en");

        DFA<String> of = aut1.of(aut2);

        System.out.println("\n\nOF\n--------");
        System.out.println(Graph.generateImageString(of));
        Graph.generateImage(of, "of");
    }
}