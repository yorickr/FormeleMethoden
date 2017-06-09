package com.imegumii;

/**
 * Created by kenny on 9-6-2017.
 */
public class Importable {
    public static enum Type {DFA, NDFA, REGEX, ERROR};

    public Type type;

    public Importable(Type t)
    {
        this.type = t;
    }
}
