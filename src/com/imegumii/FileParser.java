package com.imegumii;

import java.io.*;
import java.nio.Buffer;

/**
 * Created by kenny on 8-6-2017.
 */
public class FileParser {

    public static Importable read(String filename)
    {
        File f = new File("input/" + filename);

        Importable imp = null;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {

            String type = br.readLine();

            switch (type)
            {
                case "DFA":
                    imp = readDFA(br);
                    break;
                case "NDFA":
                    imp = readNDFA(br);
                    break;
                case "REGEX":
                    imp = readRegex(br);
                    break;
                default:
                    break;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imp;
    }

    public static DFA<String> readDFA(BufferedReader br) throws IOException {

        Character[] chars = {'a', 'b'};
        DFA<String> dfa = new DFA<String>(chars);

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

        return dfa;
    }

    public static NDFA<String> readNDFA(BufferedReader br) throws IOException {

    Character[] chars = {'a', 'b'};
    NDFA<String> ndfa = new NDFA<String>(chars);
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

        return ndfa;
    }

    public static RegExp readRegex(BufferedReader br) throws IOException {
        String line = "Error";
        line = br.readLine();

        RegExp regex = new RegExp("a").of(new RegExp("b").ster()); //RegExp.parse(line);

        return regex;
    }
}
