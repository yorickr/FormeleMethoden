package com.imegumii;

/**
 * Created by imegumii on 18/04/2017.
 */
public class Transition <T extends Comparable> implements Comparable<Transition<T>> {
    public static final char ENDCHAR = '$';

    public T vanState;
    public char symbol;
    public T naarState;

    public Transition(T from, T to) {
        this(from, ENDCHAR, to);
    }

    public Transition(T van, char s, T naar) {
        this.vanState = van;
        this.symbol = s;
        this.naarState = naar;
    }

    public boolean equals (Object other) {
        if (other == null) {
            return false;
        } else if (other instanceof  Transition) {
            Transition otherTransition = (Transition) other;
            return this.vanState.equals(otherTransition.vanState) && this.naarState.equals(otherTransition.naarState) && this.symbol == otherTransition.symbol;
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(Transition<T> t) {
        int vanCompare = vanState.compareTo(t.vanState);
        int symbolCompare = new Character (symbol).compareTo(t.symbol);
        int naarCompare = naarState.compareTo(t.naarState);

        return (vanCompare != 0 ? vanCompare : (symbolCompare != 0 ? symbolCompare : naarCompare));
    }

    @Override
    public String toString() {
        return "(" + this.vanState + ", " + this.symbol + ")-->" + this.naarState;
    }
}
