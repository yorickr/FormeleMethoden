package com.imegumii;

import sun.misc.Regexp;

import java.util.*;

/**
 * Created by kenny on 2-5-2017.
 */
public class RegExp extends Importable{

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
        super(Type.REGEX);
        operator = Operator.EEN;
        characters = "";
        links = null;
        rechts = null;
    }

    public RegExp(String chars){
        super(Type.REGEX);
        operator = Operator.EEN;
        characters = chars;
        links = null;
        rechts = null;
    }

    public RegExp loopRecursiefDoorString(String s, RegExp r) {
        Deque<Integer> start = new ArrayDeque<>();
        Deque<Integer> end = new ArrayDeque<>();

        ArrayList<Tuple<Integer, Integer>> segments = new ArrayList<>();

        Deque<RegExp> processedParantheses = new ArrayDeque<>();

        String toProcessAfter = s;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == '(') {
                start.addLast(i);
            }

            if (c == ')') {
                end.addFirst(i);
            }

            if (!start.isEmpty() && !end.isEmpty()) {
                int startPos = start.pollLast();
                int endPos = end.pollFirst();

                String sub = s.substring(startPos + 1, endPos);
//                System.out.println("Sub is " + sub);

                StringBuilder toReplace = new StringBuilder(s.substring(startPos, endPos + 1));
                StringBuilder replaceWith = new StringBuilder("" + Transition.ENDCHAR);
//                System.out.println("Toreplace is " + toReplace.toString());
                toProcessAfter = toProcessAfter.replace(toReplace, replaceWith);

                processedParantheses.add(stringNaarRegExp(sub, new RegExp(), null));
            }
        }

        RegExp reg = stringNaarRegExp(toProcessAfter, r, processedParantheses);
//        System.out.println(reg.getTaal(10));
//        System.out.println("----------");
        return reg;
    }

    public RegExp naarRegExp(String s) {

        StringBuilder noSpacesString = new StringBuilder();

        for (char c : s.toCharArray()) {
            if (c != ' ') {
                noSpacesString.append(c);
            }
        }
//        RegExp retval = new RegExp();
        // Als eerste moet de string in groupen gedeeld worden op basis van haakjes.
        // bijv. "a+((ab)*b|ab|(b)*bb)+(abba|baab)+" naar ["a+", [ [["ab"], "*b"], ["ab"], [["b"], "*bb"], "+" ], [ ["abba", "baab"], "+"] ]
        // Dit moet dan naar dit worden omgezet:
        // a.plus().punt( (ab.ster().punt(b).of(ab).of(b.ster().punt(bb)).plus()) ).punt( abba.punt().baab.plus() )
        // Niet gelukt.
//        System.out.println("We lopen door " + s);
        RegExp r = loopRecursiefDoorString(noSpacesString.toString(), new RegExp());
        return r;
    }

    public RegExp stringNaarRegExp(String start, RegExp initieel, Deque<RegExp> alreadyParsed) {
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

//        System.out.println("OfStrings: " + strings);

        RegExp retval = new RegExp();
//        toDot.add(initieel);

        for (String s : strings) {
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
//                    System.out.println("STER");
                        break;
                    case '+':
                        prev = toDot.get(toDot.size() -1);
                        toDot.add(prev.plus());
                        toDot.remove(prev);
//                    System.out.println("PLUS");
                        break;
                    case Transition.ENDCHAR:
                        toDot.add(alreadyParsed.pollFirst());
                        break;
                    default:
                        toDot.add(new RegExp("" + current));
//                    System.out.println("NEW CHAR");
                        break;
                }
            }

            for (RegExp regExp : toDot) {
                total = total.punt(regExp);
            }

            toOf.add(total);
        }
        RegExp prev = null;
        for (RegExp regExp : toOf) {
            if (toOf.size() > 1) {
                if (prev == null) {
                    prev = regExp;
                    continue;
                }
                retval = prev.of(regExp);
                prev = retval;
            } else {
                retval = retval.punt(regExp);
            }

        }


        return retval;
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

    public class Tuple<X, Y> {
        public final X left;
        public final Y right;
        public Tuple(X left, Y right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return "(" + left + "," + right + ")";
        }

    }

    @Override
    public String toString() {
        return "Chars: " + this.characters + " en operator: " + this.operator;
    }
}
