import java.util.*;

public class FSM {

    // IMPORTANT NODE!
    // This is the graph that represents the Non-Deterministic Finite State Automaton
    // using adjacency lists
    // ♀ (ALT+12) represents an epsilon transition i.e. a transition that consumes no input character
    ArrayList<Map<Character, ArrayList<Integer>>> nfsa;
    boolean[] isFinal;

    List<Integer> currStates;

    FSM()
    {
        nfsa = new ArrayList<>();
        currStates = new LinkedList<>();

        isFinal = null;
    }

    FSM(ArrayList<Map<Character, ArrayList<Integer>>> gotova, boolean[] fin)
    {
        nfsa = gotova;
        isFinal = fin;

        currStates = new LinkedList<>();
    }

    // This function should be only called when the entire machine is already built
    // or at least all nodes added, so that nfsa.size() gets the right size
    void makeFinal(int index)
    {
        if (isFinal == null)
            isFinal = new boolean[nfsa.size()];

        isFinal[index] = true;
    }

    void unmakeFinal(int index)
    {
        isFinal[index] = false;
    }

    void addNode()
    {
        nfsa.add(new HashMap<>());
    }

    void addNodes(int numNodesToAdd)
    {
        for (int i = 0; i < numNodesToAdd; i++)
            addNode();
    }

    void addTransition(int from, int to, char ch)
    {
        Map<Character, ArrayList<Integer>> f = nfsa.get(from);

        List<Integer> lst = f.get(ch);

        if (lst == null)
            f.put(ch, Utilities.listOf(to));
        else
            lst.add(to);
    }

    // State at index zero is always the one and only starting state
    void startMachine()
    {
        currStates = new LinkedList<>();
        currStates.add(0);

        // Add epsilon transition states for the first state
        if (nfsa.get(0).get('♀') != null) {
            LinkedList<Integer> epsilon = new LinkedList<>(nfsa.get(0).get('♀'));
            while (!epsilon.isEmpty()) {
                Integer curr = epsilon.get(0);
                epsilon.remove(0);

                currStates.add(curr);

                if (nfsa.get(curr).get('♀') != null)
                    epsilon.addAll(nfsa.get(curr).get('♀'));
            }
        }
    }

    void simulateStep(char ch)
    {
        LinkedList<Integer> newCurrStates = new LinkedList<>();

        for (Integer state : currStates) {
            List<Integer> next = nfsa.get(state).get(ch);

            if (next == null)
                continue;

            for (Integer i : next) {
                newCurrStates.add(i);

                // Add also all states from epsilon transition from these new states
                if (nfsa.get(i).get('♀') != null) {
                    LinkedList<Integer> epsil = new LinkedList<>(nfsa.get(i).get('♀'));
                    while (!epsil.isEmpty()) {
                        Integer c = epsil.get(0);
                        epsil.remove(0);

                        newCurrStates.add(c);

                        if (nfsa.get(c).get('♀') != null)
                            epsil.addAll(nfsa.get(c).get('♀'));
                    }
                }
            }
        }

        currStates = newCurrStates;
    }

    boolean simulate(String input)
    {
        startMachine();

        for (char c : input.toCharArray())
        {
            simulateStep(c);
        }

        return accepted();
    }

    boolean accepted()
    {
        boolean accepted = false;
        for (Integer state : currStates) {
            if (isFinal[state]) {
                accepted = true;
                break;
            }
        }

        return accepted;
    }
}