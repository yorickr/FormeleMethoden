package com.imegumii;

/**
 * Created by kenny on 18-5-2017.
 */
public class Graph {

    public static String generateGraphString(Automata<String> a)
    {
        String text = "digraph finite_state_machine {\nrankdir=LR;\nsize=\"8,5\";\nnode [shape = doublecircle];";

        for(String s : a.eindStates){
            text += s + " ";
        }

        text += ";\nnode [shape = circle];\n";

        for(Transition<String> t : a.transistions)
        {
            String s = "";
            if(t.symbol == Transition.EPSILON)
                s = "$";
            else
                s = t.symbol + "";

            text += t.vanState + " -> " + t.naarState + " [ label = \"" + s + "\"];\n";
        }

        text += "}";

        return text;
    }
}
