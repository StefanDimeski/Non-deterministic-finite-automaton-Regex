import java.util.*;

public class FSMFactory {

    public static FSM buildOne()
    {
        ArrayList<Map<Character, ArrayList<Integer>>> nfsa = new ArrayList<>();

        nfsa.add(new HashMap<>());
        nfsa.add(new HashMap<>());
        nfsa.add(new HashMap<>());
        nfsa.add(new HashMap<>());

        nfsa.get(0).put('a', Utilities.listOf(1));
        nfsa.get(1).put('@', Utilities.listOf(2));
        nfsa.get(2).put('a', Utilities.listOf(2, 3));

        boolean isFinal[] = new boolean[] {false, false, false, true };

        return new FSM(nfsa, isFinal);
    }

    public static FSM buildTwo()
    {
        ArrayList<Map<Character, ArrayList<Integer>>> nfsa = new ArrayList<>();

        nfsa.add(new HashMap<>());
        nfsa.add(new HashMap<>());
        nfsa.add(new HashMap<>());
        nfsa.add(new HashMap<>());

        nfsa.get(0).put('a', Utilities.listOf(1));
        nfsa.get(0).put('b', Utilities.listOf(1));
        nfsa.get(1).put('c', Utilities.listOf(2));
        nfsa.get(2).put('c', Utilities.listOf(2));
        nfsa.get(2).put('a', Utilities.listOf(3));
        nfsa.get(2).put('b', Utilities.listOf(3));

        boolean[] isFinal = new boolean[] {false, false, false, true};

        return new FSM(nfsa, isFinal);
    }

    public static FSM buildThree()
    {
        ArrayList<Map<Character, ArrayList<Integer>>> nfsa = new ArrayList<>();

        nfsa.add(new HashMap<>());
        nfsa.add(new HashMap<>());
        nfsa.add(new HashMap<>());
        nfsa.add(new HashMap<>());
        nfsa.add(new HashMap<>());

        nfsa.get(0).put('♀', Utilities.listOf(1, 2));
        nfsa.get(1).put('a', Utilities.listOf(4));
        nfsa.get(1).put('♀', Utilities.listOf(3)); // ♀ (ALT+12) represents an epsilon transition i.e. a transition that consumes no input char
        nfsa.get(3).put('c', Utilities.listOf(4));
        nfsa.get(2).put('b', Utilities.listOf(4));

        boolean[] isFinal = new boolean[] { false, false, false, false, true };

        return new FSM(nfsa, isFinal);
    }
}
