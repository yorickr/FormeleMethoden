package com.imegumii;

import java.util.ArrayList;
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
    }

    public DFA<String> minimize()
    {
        while(splitGroups())
            updateGroupReferences();

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
                if (t.vanState == s.state) {
                    found = true;
                    currentSet = s;
                    break;
                }
            }

            if(!found) {
                String groupName = beginGroup;
                boolean endState = false;
                if(automata.eindStates.contains(t.vanState)) {
                    groupName = endGroup;
                    endState = true;
                }
                currentSet = new HopcroftSet(groupName, (String)t.vanState, endState);
            }

            if(t.symbol == 'a')
                currentSet.transitionAState = (String)t.naarState;
            else if(t.symbol == 'b')
                currentSet.transitionBState = (String)t.naarState;
        }
    }

    private DFA<String> HopcrofttoDFA()
    {
        Character [] characters = {'a', 'b'};
        DFA<String> result = new DFA<String>(characters);

        ArrayList<String> groups = new ArrayList<String>();

        for(HopcroftSet s : sets)
        {
            if(!groups.contains(s.group))
            {
                result.addTransition(new Transition<String>(s.group, 'a', s.transitionAGroup));
                result.addTransition(new Transition<String>(s.group, 'b', s.transitionBGroup));

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
            for(HopcroftSet h : sets)
            {
                if(s.transitionAState == h.state)
                    s.transitionAGroup = h.group;

                if(s.transitionBState == h.state)
                    s.transitionBGroup = h.group;
            }
        }
    }

    private boolean splitGroups()
    {
        int splitCount = 0;





        if(splitCount == 0)
            return false;

        return true;
    }


    private int count = 0;

    private String getGroupName()
    {
        count++;
        return "G" + count;
    }

}

class HopcroftSet {

    public String group;
    public String state;

    public String transitionAState;
    public String transitionAGroup;

    public String transitionBState;
    public String transitionBGroup;

    public boolean isEndState = false;

    public HopcroftSet(String group, String state, boolean isEndState)
    {
        this.group = group;
        this.state = state;
        this.isEndState = isEndState;
    }
}