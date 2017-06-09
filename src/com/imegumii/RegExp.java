package com.imegumii;

import javafx.scene.Parent;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;

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
//        for (int i = 0; i < s.length(); i++) {
//            char current = s.charAt(i);
//            if (current == '(') {
//                if (cutOff) {
////                    System.out.println("Substring van HAKKE is " + s.substring(lastIndex, i));
//                    parantheses.add(s.substring(lastIndex, i));
//                    lastIndex = i;
//                    cutOff = false;
//                }
//                counter++;
//            }
//            if (current == ')') {
//                counter--;
//            }
//            if (counter == 0) {
//                cutOff = true;
//            }
//        }

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
        ArrayList<RegExp> toOf = new ArrayList<>();

        ArrayList<String> strings = new ArrayList<>();
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

    public RegExp parseString(String s) {
        StringBuilder noSpacesString = new StringBuilder();

        for (char c : s.toCharArray()) {
            if (c != ' ') {
                noSpacesString.append(c);
            }
        }


        return naarRegExp(noSpacesString.toString());
    }

    public boolean hasEvenAmountOfParantheses(String s) {
        int counter = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                counter++;
            }
            if (c == ')') {
                counter--;
            }
        }

        return counter == 0;
    }

    public RegExpGroup splitsOpBasisVanHaakjes(String s) {
        System.out.println("SplitsOpBasisVanHaakjes de string " + s);
        ArrayList<String> haakjesList = new ArrayList<>();
        ArrayList<RegExpGroup> regExpGroup = new ArrayList<>();
        int counter = 0;
        boolean cutOff = false;
        int lastIndex = 0;

        int leftLocation = -1;
        int rightLocation = -1;
        for (int i = 0; i < s.length(); i++) {
            char current = s.charAt(i);
            // We can look for |'s if we only have root parantheses
            if (current == '(') {
                if (cutOff) {
                    String sub = s.substring(lastIndex, i);
                    haakjesList.add(sub);

                    if (leftLocation != -1 && rightLocation != -1) {
                        regExpGroup.add(new RegExpGroup(Operator.PUNT,
                                new RegExpGroup(Operator.PUNT, s.substring(leftLocation + 1, rightLocation)),
                                new RegExpGroup(Operator.PUNT, s.substring(rightLocation + 1, i))
                        ));
                    } else {
                        regExpGroup.add(new RegExpGroup(Operator.PUNT,
                                new RegExpGroup(Operator.PUNT, s.substring(lastIndex, i)),
                                null
                        ));
                    }

                    lastIndex = i;
                    cutOff = false;
                    leftLocation = -1;
                    rightLocation = -1;
                }
                if (counter == 0) {
                    leftLocation = i;
                }
                counter++;
            }
            if (current == ')') {
                counter--;
                if (counter == 0) {
                    rightLocation = i;
                }
            }
            if (counter == 0) {
                cutOff = true;
            }
        }

        haakjesList.add(s.substring(lastIndex, s.length()));
        if (leftLocation != -1 && rightLocation != -1) {
            regExpGroup.add(new RegExpGroup(Operator.PUNT,
                    new RegExpGroup(Operator.PUNT, s.substring(leftLocation + 1, rightLocation)),
                    new RegExpGroup(Operator.PUNT, s.substring(rightLocation + 1, s.length())
                    )));
        } else {
            regExpGroup.add(new RegExpGroup(Operator.PUNT,
                    new RegExpGroup(Operator.PUNT, s.substring(lastIndex, s.length())),
                    null
            ));
        }


//        System.out.println("Haakjesset");
//        System.out.println(haakjesList);

//        System.exit(1);
//        regExpGroup.forEach(System.out::println);
        ArrayList<RegExpGroup> test = new ArrayList<>();
        for (RegExpGroup expGroup : regExpGroup) {
            if (expGroup.left !=  null && expGroup.left.regex != null && expGroup.left.regex.contains("(")) { // if not done removing parantheses.
                expGroup.left = splitsOpBasisVanHaakjes(expGroup.left.regex);
            }
            if (expGroup.right != null && expGroup.right.regex != null && expGroup.right.regex.contains("(")) {
                expGroup.right = splitsOpBasisVanHaakjes(expGroup.right.regex);
            }
            test.add(expGroup);
        }
        RegExpGroup prev = null;
        for (RegExpGroup regExpGroup1 : test) {
            if (test.size() > 1) {
                if (prev == null) {
                    prev = regExpGroup1;
                    continue;
                }
                if (prev.right == null) {
                    // we don't need to make a new one, we can just add it right.
                    prev.right = regExpGroup1;
                } else {
                    prev = new RegExpGroup(Operator.PUNT, prev, regExpGroup1);

                }
            } else {
                prev = regExpGroup1;
            }
        }

//        System.out.println("TEST");
////        System.out.println(prev);
//        prev.traverse();

        return prev;
    }

    public RegExp loopDoorSegment(String s) {
        for (int i = 0; i < s.length(); i++) {
            System.out.println(s.charAt(i));
        }
        return null;
    }

    public RegExp loopRecursiefDoorString(String s, RegExp r) {
        System.out.println("Loop door " + s);
        RegExp retval = null;
        int count = 0;
        int begin = 0;
        int eind = 0;
        boolean cut = false;
        boolean cutting = false;

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == '(') {
                if (count == 0) {
                    begin = i + 1;
                    cutting = true;
                }
                count++;
            }

            if (c == ')') {
                if (count == 1) { // if last count
                    eind = i;
                    cut = true;
                    cutting = false;
                }
                count--;
            }
            if (cut) {
                String sub = s.substring(begin, eind);
                String links = s.substring(0, begin - 1);
                String rechts = s.substring(eind + 1, s.length());
                System.out.println("L");
                System.out.println(links);
                System.out.println("M");
                System.out.println(sub);
                System.out.println("R");
                System.out.println(rechts);
                System.out.println("----");
//                if (links.contains("(")) {
//                    loopRecursiefDoorString(links, r);
//                }
//                if (sub.contains("(")) {
//                    loopRecursiefDoorString(sub, r);
//                }
//                if (rechts.contains("(")) {
//                    loopRecursiefDoorString(rechts, r);
//                }
                retval = loopRecursiefDoorString(sub, r);
                cut = false;
            }
        }
        System.out.println("RETURN");
        return retval;
    }

    public RegExp naarRegExp(String s) {
        // String s is zonder spaces
        RegExp retval = new RegExp();
        // Als eerste moet de string in groupen gedeeld worden op basis van haakjes.
        // bijv. "a+((ab)*b|ab|(b)*bb)+(abba|baab)+" naar ["a+", [ [["ab"], "*b"], ["ab"], [["b"], "*bb"], "+" ], [ ["abba", "baab"], "+"] ]
        // Dit moet dan naar dit worden omgezet:
        // a.plus().punt( (ab.ster().punt(b).of(ab).of(b.ster().punt(bb)).plus()) ).punt( abba.punt().baab.plus() )

//        splitsOpBasisVanHaakjes(s);
        System.out.println("We lopen door " + s);
        RegExp r = loopRecursiefDoorString(s, new RegExp());
        System.out.println(r.getTaal(20));
        System.out.println("---");
//        System.out.println("PRINTSTART");
////        r.traverse();
//        System.out.println("PRINTEND");
//        System.out.println("TEST");
//        System.out.println(stringNaarRegExp("a|bb", new RegExp("b")).getTaal(10));
//        System.out.println("----");

        return retval;
    }

    public RegExp stringNaarRegExp(String s, RegExp initieel) {
        ArrayList<RegExp> toDot = new ArrayList<>();
        RegExp retval = new RegExp();
        toDot.add(initieel);
        for (int i = 0; i < s.length(); i++) {
            char current = s.charAt(i);
            RegExp prev;
            switch(current) {
                case '*':
                    prev = toDot.get(toDot.size() -1);
                    toDot.add(prev.ster());
                    toDot.remove(prev);
//                    System.out.println("STER");
                    break;
                case '+':
                    prev = toDot.get(toDot.size() -1);
                    toDot.add(prev.plus());
                    toDot.remove(prev);
//                    System.out.println("PLUS");
                    break;
                case '|':
                default:
                    toDot.add(new RegExp("" + current));
//                    System.out.println("NEW CHAR");
                    break;
            }
        }

        for (RegExp regExp : toDot) {
            retval = retval.punt(regExp);
        }

        return retval;
    }

    private class RegExpGroup {
        public Operator op;
        public RegExpGroup left;
        public RegExpGroup right;
        public String regex = null;

        public RegExpGroup(Operator op, String substring) {
            this.op = op;
            regex = substring;
        }

        public RegExpGroup(Operator op, RegExpGroup left, RegExpGroup right) {
            this.op = op;
            this.left = left;
            this.right = right;
        }

        public boolean isOf() {
            return this.op == Operator.OF;
        }

        public void traverse() {
            System.out.println(this.regex);
            if (this.left != null) {
//                System.out.println("LEFT");
                this.left.traverse();
            }
            if (this.right != null) {
//                System.out.println("RIGHT");
                this.right.traverse();
            }
        }

        @Override
        public String toString() {
            return "(" + left + " --  " + regex + "    --   " + right + ")";
        }
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
