package com.imegumii;

import java.util.Comparator;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

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
                for (String s1 : resLinks)
                    for (String s2 : resRechts)
                    {resultaat.add (s1 + s2);}
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

}
