package com.imegumii;

import java.util.Comparator;

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

}
