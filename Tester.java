import java.util.*;

public class Tester {

    static Scanner sc;

    public static void testFSM(FSM toTest) {

        FSM machine = toTest;

        String line = sc.nextLine();
        while (!line.equals("/q")) {
            if (machine.simulate(line))
            {
                System.out.println("Accept!");
            }
            else
            {
                System.out.println("Discard!");
            }

            line = sc.nextLine();
        }
    }

    public static void testStringProcessing()
    {
        String line = "";

        while (!line.equals("/q")) {
            line = sc.nextLine();
            System.out.println(Utilities.infixToPostfix(line));
        }
    }

    public static void testShifting()
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

        Utilities.shiftIndicies(nfsa, 10);
        System.out.println("Hey");
    }

    public static void testAll()
    {
        String in = sc.nextLine();

        while (!in.equals("/q"))
        {
            in = Utilities.preprocess(in);
            in = Utilities.infixToPostfix(in);

            FSM machine = Utilities.createMachineFromRegex(in);

            testFSM(machine);

            in = sc.nextLine();
        }
    }


    public static void main(String[] args) {

        sc = new Scanner(System.in);

        //testFSM(FSMFactory.buildTwo());
        //testStringProcessing();
        //testShifting();
        testAll();
    }
}
