package com.imegumii;

import javafx.scene.Parent;

import java.util.*;

/**
 * Created by kenny on 2-5-2017.
 */
public class RegExp {

    public enum Operator { PLUS, STER, OF, PUNT, EEN}

    Operator operator;
    String characters;

    RegExp links;
    RegExp rechts;

    static final Comparator<String> vergelijkDmvLengte = (s1, s2) -> {
            if (s1.length() == s2.length())
            {return s1.compareTo(s2);}
            else
            {return s1.length() - s2.length();}
        };

    public RegExp()
    {
        operator = Operator.EEN;
        characters = "";
        links = null;
        rechts = null;
    }

    public RegExp(String chars){
        operator = Operator.EEN;
        characters = chars;
        links = null;
        rechts = null;
    }

    public RegExp splitOffInParenthesesGroups(String s) {
        ArrayList<String> parantheses = new ArrayList<>();

        RegExp retval = new RegExp();

        // Vind haakjessets
        System.out.println("HAKKE");
        int counter = 0;
        boolean cutOff = false;
        int lastIndex = 0;
        for (int i = 0; i < s.length(); i++) {
            char current = s.charAt(i);
            if (current == '(') {
                if (cutOff) {
//                    System.out.println("Substring van HAKKE is " + s.substring(lastIndex, i));
                    parantheses.add(s.substring(lastIndex, i));
                    lastIndex = i;
                    cutOff = false;
                }
                counter++;
            }
            if (current == ')') {
                counter--;
            }
            if (counter == 0) {
                cutOff = true;
            }
        }

//        System.out.println("Substring van HAKKE is " + s.substring(lastIndex, s.length()));
        parantheses.add(s.substring(lastIndex, s.length()));

        System.out.println("Haakjesset");
        System.out.println(parantheses);

        for (String paranthesis : parantheses) {
            findInParantheses(paranthesis, new RegExp());
        }

        return retval;
    }

    public RegExp findInParantheses(String s, RegExp startRegExp) {
        RegExp retval = startRegExp;
        System.out.println("Begin with string " + s + " in findin");

        Deque<Integer> left = new ArrayDeque<>();
        Deque<Integer> right = new ArrayDeque<>();

        ArrayList<RegExp> toDot = new ArrayList<>();
        // Vind haakjes
        for (int i = 0; i < s.length(); i++) {
            char current = s.charAt(i);

            if (current == '(') {
                left.add(i);
            }
            if (current == ')') {
                right.add(i);
            }

            // Combine the last of left with the first of right
            if (!left.isEmpty() && !right.isEmpty()) {
                int start = left.pollLast();
                int end = right.pollFirst();
                String substring = s.substring(start + 1, end);

                StringBuilder toReplace = new StringBuilder(s.substring(start, end + 1));
                StringBuilder replaceWith = new StringBuilder();
                replaceWith.append(Transition.ENDCHAR);
                String replacedString = s.replace(toReplace, replaceWith);
                // Als we een paar haakjes hebben gevonden
                retval = retval.punt(toRegExp(substring, startRegExp)); // Voeg de regex van de substring toe aan retval
                retval = retval.punt(findInParantheses(replacedString, retval)); // Haal de substring weg uit de string, vind hiervan de regex en voeg die toe aan retval
                return retval;
            }
        }


        if (!s.contains("(") && !s.contains(")")) {
            retval = retval.punt(toRegExp(s)); // Als we geen haakjes hebben, dan pakken we de huidige (sub)string en voegen we die toe aan retval
        }


        return retval;
    }

    public static RegExp toRegExp(String start) {
        return toRegExp(start, new RegExp());
    }

    public static RegExp toRegExp(String start, RegExp startRegExp) {
        int maxOps = 200;
        System.out.println("Begin met " + start + " in toRegExp");
        System.out.println("STARTREG IS ");
        System.out.println(startRegExp.getTaal(maxOps));

        RegExp retval = new RegExp();
        ArrayList<String> strings = new ArrayList<>();
        ArrayList<RegExp> toOf = new ArrayList<>();

        StringBuilder currentString = new StringBuilder();
        for (int i = 0; i < start.length(); i++) {
            char current = start.charAt(i);

            if (current == '|') {
                // flush currentString to strings array
                strings.add(currentString.toString());
                currentString = new StringBuilder();
                continue;
            }

            currentString.append(current);
        }

        strings.add(currentString.toString());

        System.out.println("OfStrings: " + strings);

        for (String s : strings) {
            System.out.println("Parsing " + s);
            RegExp total = new RegExp();
            ArrayList<RegExp> toDot = new ArrayList<>();
            for (int i = 0; i < s.length(); i++) {
                char current = s.charAt(i);
                RegExp prev;
                switch(current) {
                    case '*':
                        prev = toDot.get(toDot.size() -1);
                        toDot.add(prev.ster());
                        toDot.remove(prev);
//                        System.out.println(prev.getTaal(5));
//                        System.out.println(prev.ster().getTaal(100));
//                        System.out.println(Main.aantalOperators(prev.ster(), 1));
                        System.out.println("STER");
                        break;
                    case '+':
                        prev = toDot.get(toDot.size() -1);
                        toDot.add(prev.plus());
                        toDot.remove(prev);
                        System.out.println("PLUS");
                        break;
                    case Transition.ENDCHAR:
                        toDot.add(startRegExp);
                        System.out.println("PREVIOUS REGEX");
                        break;
                    default:
                        toDot.add(new RegExp("" + current));
                        System.out.println("NEW CHAR");
//                        System.out.println("Adding new regExp to toDot");
                        break;
                }
//                System.out.println("-----");
            }
//            System.out.println("ToDot contains");
//            System.out.println(toDot);

            for (RegExp regExp : toDot) {
                System.out.println("Dotting " + regExp.getTaal(maxOps));
                total = total.punt(regExp);
//                System.out.println("Total is: L: (" + total.links + ") R: (" + total.rechts + ")");
            }
            System.out.println("Adding to toOf");
            System.out.println(total.getTaal(maxOps));
            toOf.add(total);
        }
        RegExp prev = null;
        for (RegExp regExp : toOf) {
            if (toOf.size() > 1) {
                if (prev == null) {
                    prev = regExp;
                    continue;
                }
                System.out.println("Offing " + prev.getTaal(maxOps));
                System.out.println("With " + regExp.getTaal(maxOps));
                retval = prev.of(regExp);
                System.out.println("Result " + retval.getTaal(maxOps));
                System.out.println("------");
                prev = retval;
            } else {
                retval = retval.punt(regExp);
            }
        }
        System.out.println("toRegExp");
        System.out.println(Main.aantalOperators(retval, 1));
        System.out.println(retval.getTaal(200));
//        Main.traverseRegExp(retval);
        System.out.println("----");

        return retval;
    }

    public RegExp removeUselessOperators(RegExp r) {
        return null;
    }

    public RegExp parseString(String s) {
        StringBuilder noSpacesString = new StringBuilder();

        for (char c : s.toCharArray()) {
            if (c != ' ') {
                noSpacesString.append(c);
            }
        }

        return splitOffInParenthesesGroups(noSpacesString.toString());

    }

    public RegExp plus()
    {
        RegExp res = new RegExp();
        res.operator = Operator.PLUS;
        res.links = this;
        return res;
    }

    public RegExp ster()
    {
        RegExp res = new RegExp();
        res.operator = Operator.STER;
        res.links = this;
        return res;
    }

    public RegExp of(RegExp other)
    {
        RegExp res = new RegExp();
        res.operator = Operator.OF;
        res.links = this;
        res.rechts = other;
        return res;
    }

    public RegExp punt(RegExp other)
    {
        RegExp res = new RegExp();
        res.operator = Operator.PUNT;
        res.links = this;
        res.rechts = other;
        return res;
    }

    public Taal getTaal(int maxOperaties)
    {
        SortedSet<String> leeg = new TreeSet<>(vergelijkDmvLengte);
        SortedSet<String> resultaat = new TreeSet<>(vergelijkDmvLengte);

        SortedSet<String> resLinks, resRechts;

        if(maxOperaties < 1) return new Taal(leeg);

        switch(operator)
        {
            case EEN:
                resultaat.add(characters);
                break;

            case OF:
                resLinks = links == null ? leeg : links.getTaal(maxOperaties - 1).getSymbols();
                resRechts = rechts == null ? leeg : rechts.getTaal(maxOperaties - 1).getSymbols();
                resultaat.addAll (resLinks);
                resultaat.addAll (resRechts);
                break;

            case PUNT:
                resLinks = links == null ? leeg : links.getTaal(maxOperaties - 1).getSymbols();
                resRechts = rechts == null ? leeg : rechts.getTaal(maxOperaties - 1).getSymbols();
                for (String s1 : resLinks) {
                    for (String s2 : resRechts) {
                        resultaat.add(s1 + s2);
                    }
                }
                break;

            case STER:
            case PLUS:
                resLinks = links == null ? leeg : links.getTaal(maxOperaties - 1).getSymbols();
                resultaat.addAll(resLinks);
                for (int i = 1; i < maxOperaties; i++)
                {
                    HashSet<String> tempTaal = new HashSet<String>(resultaat);
                    for (String s1 : resLinks)
                    {
                        for (String s2 : tempTaal)
                        {
                            resultaat.add (s1+s2);
                        }
                    }
                }
                if (this.operator  == Operator.STER)
                {
                    resultaat.add("");
                }
                break;


            default:
                System.out.println ("getTaal is nog niet gedefinieerd voor de operator: " + this.operator);
                break;

        }

        return new Taal(resultaat);
    }

    @Override
    public String toString() {
        return "Chars: " + this.characters + " en operator: " + this.operator;
    }
}
