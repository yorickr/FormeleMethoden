package com.imegumii;

import javafx.geometry.HPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by kenny on 30-5-2017.
 */
public class HopcroftConverter {

    private DFA<String> automata;
    private SortedSet<HopcroftSet> sets;

    public HopcroftConverter(DFA<String> automata)
    {
        this.automata = automata;
        this.sets = new TreeSet<HopcroftSet>();

        DFAtoHopcroft();
        updateGroupReferences();

        System.out.println("\nFound " + sets.size() + " sets.");
    }

    public DFA<String> minimize()
    {
        int lastround = 0;
        int newround = 2;

        int count = 0;

        while(lastround != newround) {

            count++;
            System.out.println("Minimize round " + count + " (" + lastround + "/" + newround + ")");

            lastround = newround;
            newround = splitGroups(lastround);

            updateGroupReferences();
        }

        return HopcrofttoDFA();
    }

    private void DFAtoHopcroft()
    {
        String beginGroup = getGroupName();
        String endGroup = getGroupName();

        for(Transition t : automata.transistions)
        {
            boolean found = false;
            HopcroftSet currentSet = null;

            for(HopcroftSet s : sets) {
                if (t.vanState.equals(s.state)) {
                    found = true;
                    currentSet = s;
                    break;
                }
            }

            if(!found) {
                String groupName = beginGroup;
                boolean endState = false;
                if(automata.eindStates.contains((String)t.vanState)) {
                    groupName = endGroup;
                    endState = true;
                }
                currentSet = new HopcroftSet(groupName, (String)t.vanState, endState);
                sets.add(currentSet);
            }

            currentSet.addColum(new HopcroftColumn(t.symbol, (String)t.naarState, "-"));
        }
    }

    private DFA<String> HopcrofttoDFA()
    {
        DFA<String> result = new DFA<String>();

        ArrayList<String> groups = new ArrayList<String>();

        for(HopcroftSet s : sets)
        {
            if(!groups.contains(s.group))
            {
                for(HopcroftColumn c : s.columns)
                    result.addTransition(new Transition<String>(s.group, c.symbol, c.group));

                if(s.isEndState)
                    result.defineAsEndState(s.group);

                groups.add(s.group);
            }

            if(automata.beginStates.contains(s.state))
                result.defineAsStartState(s.group);
        }

        return result;
    }

    private void updateGroupReferences()
    {
        for(HopcroftSet s : sets)
        {
            for(HopcroftColumn c : s.columns)
            {
                for(HopcroftSet h : sets)
                {
                    if(c.state.equals(h.state))
                        c.group = h.group;
                }
            }
        }
    }

    private int splitGroups(int oldsize)
    {
        HashMap<String, ArrayList<HopcroftSet>> groups = new HashMap<String, ArrayList<HopcroftSet>>();

        for(HopcroftSet s : sets)
        {
            String key = s.group;

            for(HopcroftColumn c : s.columns)
                key += c.symbol + c.group;

            if(!groups.containsKey(key))
            {
                groups.put(key, new ArrayList<HopcroftSet>());
            }

            groups.get(key).add(s);
        }

        if(oldsize == groups.size())
            return oldsize;

        sets.clear();

        for(ArrayList<HopcroftSet> arr : groups.values())
        {
            String newGroupName = getGroupName();

            for(HopcroftSet ns : arr)
            {
                ns.group = newGroupName;
                sets.add(ns);
            }
        }

        return groups.size();
    }


    private int count = -1;
    private int suffix = 0;
    private String[] alphabet = {"A", "B", "C", "D", "E", "F", "G","H","I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private String getGroupName()
    {
        count++;
        if(count >= alphabet.length) {
            count = 0;
            suffix++;
        }

        return alphabet[count] + suffix;
    }

}

class HopcroftSet implements Comparable<HopcroftSet>{

    public String group;
    public String state;

    public ArrayList<HopcroftColumn> columns;

    public boolean isEndState = false;

    public HopcroftSet(String group, String state, boolean isEndState)
    {
        this.group = group;
        this.state = state;
        this.isEndState = isEndState;

        columns = new ArrayList<>();
    }

    public void addColum(HopcroftColumn c)
    {
        this.columns.add(c);
    }

    public void clearColumns()
    {
        this.columns.clear();
    }

    @Override
    public int compareTo(HopcroftSet o) {
        return (this.group + this.state).compareTo(o.group + o.state);
    }

    @Override
    public String toString()
    {
        String s =  group + " " + state + " | ";

        for(HopcroftColumn c : columns)
            s += "(" + c.symbol + ") " + c.state + " " + c.group + " | ";

        s+= " " + (isEndState?"*":"");

        return s;
    }
}

class HopcroftColumn {

    public char symbol;
    public String state;
    public String group;

    public HopcroftColumn(char s, String state, String group)
    {
        this.symbol = s;
        this.state = state;
        this.group = group;
    }
}