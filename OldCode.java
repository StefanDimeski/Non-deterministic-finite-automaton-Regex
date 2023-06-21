import java.util.*;
import java.util.stream.Collectors;

// THIS CLASS CODE MAY NOT WORK NOW!
// I'M ONLY LEAVING IT FOR REFERENCE PUROPSES!
public class OldCode {
    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//
//        String line = "";
//
//        while (!line.equals("/q")) {
//            line = sc.nextLine();
//            System.out.println(infixToPostfix(line));
//        }

        // Create the graph that represents the Non-Deterministic Finite State Automaton
        // using adjacency lists
        // ♀ (ALT+12) represents an epsilon transition i.e. a transition that consumes no input character
        //List<Map<Character, List<Integer>>> nfsa = buildMachineOne(); // a@(a+)
        //List<Map<Character, List<Integer>>> nfsa = buildMachineTwo(); // (a|b)(c+)(a|b)
//        List<Map<Character, List<Integer>>> nfsa = buildMachineThree(); // Test epsilon transitions // this line should be
        // uncommented. It is only commented because i removed the buildMachineThree() func.

        // This line is just to make errors go away because of the previous line that is commented
        List<Map<Character, List<Integer>>> nfsa = new ArrayList<>();

        // boolean[] isFinal = new boolean[] {false, false, false, true};
        boolean[] isFinal = new boolean[] {false, false, false, false, true}; // This is correct for both machine one and machine two

        // Get the input
        Scanner sc = new Scanner(System.in);
        String input = "";

        while (!input.equals("/q")) {
            input = sc.nextLine();

            LinkedList<Integer> currStates = new LinkedList<>();
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

            for (Character ch : input.toCharArray()) {
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

            boolean accepted = false;
            for (Integer state : currStates) {
                if (isFinal[state]) {
                    System.out.println("Accept");
                    accepted = true;
                    break;
                }
            }

            if (!accepted)
                System.out.println("Discard");
        }
    }
}
