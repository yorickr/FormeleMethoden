package com.imegumii;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Label;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.Node;
import guru.nidi.graphviz.parse.Parser;

import java.io.*;

import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;

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

    public static void generateImage(Automata<String> a, String fileName) {
        try {
            File f = new File("images/" + a.toString() + ".dot");
            PrintWriter w = new PrintWriter(f);
            w.println(Graph.generateGraphString(a));
            w.flush();
            w.close();
            MutableGraph g = Parser.read(f);
            if (fileName != null) {
                Graphviz.fromGraph(g).width(1080).render(Format.PNG).toFile(new File("images/" + fileName + ".png"));
            } else {
                Graphviz.fromGraph(g).width(1080).render(Format.PNG).toFile(new File("images/" + a.toString() + ".png"));
            }

            if (f.delete()) {
                System.out.println("Succesfully deleted temp file");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
