package com.imegumii;

import java.io.*;

/**
 * Created by kenny on 8-6-2017.
 */
public class FileParser {

    public static DFA<String> readDFA(String filename)
    {
        File f = new File("input/" + filename + ".dot");

        Character[] chars = {'a', 'b'};
        DFA<String> dfa = new DFA<String>(chars);

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.startsWith("B")) {
                    String[] states = line.substring(1,line.length()).split(",");
                    for(String s : states) { dfa.defineAsStartState(s); }
                }

                else if(line.startsWith("E")) {
                    String[] states = line.substring(1,line.length()).split(",");
                    for(String s : states) { dfa.defineAsEndState(s); }
                }

                else {
                    String[] trans = line.split(">");
                    dfa.addTransition(new Transition<String>(trans[0], trans[1].toCharArray()[0], trans[2]));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dfa;
    }

    public static NDFA<String> readNDFA(String filename)
    {
        File f = new File("input/" + filename + ".dot");

        Character[] chars = {'a', 'b'};
        NDFA<String> ndfa = new NDFA<String>(chars);

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.startsWith("B")) {
                    String[] states = line.substring(1,line.length()).split(",");
                    for(String s : states) { ndfa.defineAsStartState(s); }
                }

                else if(line.startsWith("E")) {
                    String[] states = line.substring(1,line.length()).split(",");
                    for(String s : states) { ndfa.defineAsEndState(s); }
                }

                else {
                    String[] trans = line.split(">");

                    if(trans[1].equals("$")) {
                        ndfa.addTransition(new Transition<String>(trans[0], Transition.EPSILON, trans[2]));
                    }
                    else
                        ndfa.addTransition(new Transition<String>(trans[0], trans[1].toCharArray()[0], trans[2]));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ndfa;
    }

    public static RegExp readRegex(String filename)
    {
        File f = new File("input/" + filename + ".dot");

        BufferedReader br = null;
        String line = "Error";
        try {
            br = new BufferedReader(new FileReader(f));
            line = br.readLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RegExp regex = new RegExp("a"); //RegExp.parse(line);

        return regex;
    }
}
