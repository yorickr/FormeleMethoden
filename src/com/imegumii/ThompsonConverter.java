package com.imegumii;

/**
 * Created by kenny on 17-5-2017.
 */
public class ThompsonConverter {

    private static int count = 0;

    public static NDFA<String> Convert(RegExp regex)
    {
        Character [] characters = {'a', 'b'};
        NDFA generated = new NDFA<String>(characters);

        ConvertStatement(regex, generated);

        return generated;
    }

    private static void ConvertStatement(RegExp regex, NDFA automata)
    {
        switch(regex.operator)
        {
            case PLUS:

                //Links
                ConvertStatement(regex.links, automata);
                String endLink1 = (String) automata.eindStates.first();
                String beginLink1 = (String) automata.beginStates.first();

                //Repeat and skip
                automata.addTransition(new Transition(endLink1, Transition.EPSILON, beginLink1));

                automata.clearEindStates();
                automata.clearBeginStates();

                automata.defineAsStartState(endLink1);
                automata.defineAsEndState(beginLink1);

                break;
            case STER:

                //Links
                ConvertStatement(regex.links, automata);
                String endLink2 = (String) automata.eindStates.first();
                String beginLink2 = (String) automata.beginStates.first();

                //New states
                String start2 = getState();
                String end2 = getState();

                //Repeat and skip
                automata.addTransition(new Transition(start2, Transition.EPSILON, end2));
                automata.addTransition(new Transition(endLink2, Transition.EPSILON, beginLink2));
                automata.addTransition(new Transition(start2, Transition.EPSILON, beginLink2));
                automata.addTransition(new Transition(endLink2, Transition.EPSILON, end2));

                automata.clearEindStates();
                automata.clearBeginStates();

                automata.defineAsStartState(start2);
                automata.defineAsEndState(end2);

                break;
            case OF:

                //Links
                ConvertStatement(regex.links, automata);
                String topEnd = (String) automata.eindStates.first();
                String topBegin = (String) automata.beginStates.first();

                //Rechts
                ConvertStatement(regex.rechts, automata);
                String bottomBegin = (String) automata.beginStates.first();
                String bottomEnd = (String) automata.eindStates.first();

                //New states
                String start3 = getState();
                String end3 = getState();

                //Link both
                automata.addTransition(new Transition(start3, Transition.EPSILON, topBegin));
                automata.addTransition(new Transition(start3, Transition.EPSILON, bottomBegin));
                automata.addTransition(new Transition(topEnd, Transition.EPSILON, end3));
                automata.addTransition(new Transition(bottomEnd, Transition.EPSILON, end3));

                automata.clearEindStates();
                automata.clearBeginStates();

                automata.defineAsStartState(start3);
                automata.defineAsEndState(end3);


                break;
            case PUNT:

                //Links
                ConvertStatement(regex.links, automata);
                String endLink = (String) automata.eindStates.first();
                String newBeginLink = (String) automata.beginStates.first();

                //Rechts
                ConvertStatement(regex.rechts, automata);
                String beginLink = (String) automata.beginStates.first();
                String newEndLink = (String) automata.eindStates.first();

                //Link both
                automata.addTransition(new Transition(endLink, Transition.EPSILON, beginLink));

                automata.clearEindStates();
                automata.clearBeginStates();

                automata.defineAsStartState(newBeginLink);
                automata.defineAsEndState(newEndLink);

                break;
            case EEN:
                char[] s = regex.characters.toCharArray();

                automata.clearBeginStates();
                automata.clearEindStates();

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
