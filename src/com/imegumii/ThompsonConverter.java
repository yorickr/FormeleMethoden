package com.imegumii;

/**
 * Created by kenny on 17-5-2017.
 */
public class ThompsonConverter {

    private static int count = 0;

    public static NDFA<String> convert(RegExp regex)
    {
        NDFA<String> generated = new NDFA<String>();

        convertStatement(regex, generated);

        return generated;
    }

    private static void convertStatement(RegExp regex, NDFA<String> automata)
    {
        switch(regex.operator)
        {
            case PLUS:

                //Links
                convertStatement(regex.links, automata);
                String endLink1 = automata.eindStates.first();
                String beginLink1 = automata.beginStates.first();

                String newstart = getState();
                String newEnd = getState();

                //Repeat
                automata.addTransition(new Transition<String>(newstart, Transition.EPSILON, beginLink1));
                automata.addTransition(new Transition<String>(endLink1, Transition.EPSILON, newEnd));
                automata.addTransition(new Transition<String>(endLink1, Transition.EPSILON, beginLink1));

                automata.clearEindStates();
                automata.clearBeginStates();

                automata.defineAsStartState(newstart);
                automata.defineAsEndState(newEnd);

                break;
            case STER:

                //Links
                convertStatement(regex.links, automata);
                String endLink2 = automata.eindStates.first();
                String beginLink2 = automata.beginStates.first();

                //New states
                String start2 = getState();
                String end2 = getState();

                //Repeat and skip
                automata.addTransition(new Transition<String>(start2, Transition.EPSILON, end2));
                automata.addTransition(new Transition<String>(endLink2, Transition.EPSILON, beginLink2));
                automata.addTransition(new Transition<String>(start2, Transition.EPSILON, beginLink2));
                automata.addTransition(new Transition<String>(endLink2, Transition.EPSILON, end2));

                automata.clearEindStates();
                automata.clearBeginStates();

                automata.defineAsStartState(start2);
                automata.defineAsEndState(end2);

                break;
            case OF:

                //Links
                convertStatement(regex.links, automata);
                String topEnd = automata.eindStates.first();
                String topBegin = automata.beginStates.first();

                //Rechts
                convertStatement(regex.rechts, automata);
                String bottomBegin = automata.beginStates.first();
                String bottomEnd = automata.eindStates.first();

                //New states
                String start3 = getState();
                String end3 = getState();

                //Link both
                automata.addTransition(new Transition<String>(start3, Transition.EPSILON, topBegin));
                automata.addTransition(new Transition<String>(start3, Transition.EPSILON, bottomBegin));
                automata.addTransition(new Transition<String>(topEnd, Transition.EPSILON, end3));
                automata.addTransition(new Transition<String>(bottomEnd, Transition.EPSILON, end3));

                automata.clearEindStates();
                automata.clearBeginStates();

                automata.defineAsStartState(start3);
                automata.defineAsEndState(end3);


                break;
            case PUNT:

                //Links
                convertStatement(regex.links, automata);
                String endLink = automata.eindStates.first();
                String newBeginLink = automata.beginStates.first();

                //Rechts
                convertStatement(regex.rechts, automata);
                String beginLink = automata.beginStates.first();
                String newEndLink = automata.eindStates.first();

                //Link both
                automata.addTransition(new Transition<String>(endLink, Transition.EPSILON, beginLink));

                automata.clearEindStates();
                automata.clearBeginStates();

                automata.defineAsStartState(newBeginLink);
                automata.defineAsEndState(newEndLink);

                break;
            case EEN:

                char[] s = regex.characters.toCharArray();

                automata.clearBeginStates();
                automata.clearEindStates();

                if(regex.characters.length() <= 0)
                {
                    String state1 = getState();
                    String state2 = getState();

                    Transition<String> t = new Transition<String>(state1, Transition.EPSILON, state2);

                    automata.addTransition(t);
                    automata.defineAsStartState(state1);
                    automata.defineAsEndState(state2);

                    break;
                }

                String prevState = getState();
                String newState = "";
                automata.defineAsStartState(prevState);

                for(char c : s)
                {
                    newState = getState();
                    Transition<String> t = new Transition<String>(prevState, c, newState);
                    prevState = newState;
                    automata.addTransition(t);
                }

                automata.defineAsEndState(newState);

                break;
            default:
                System.out.println("Error #8.1");
                break;
        }
    }

    private static String getState()
    {
        count++;
        return "q" + count;
    }

}
