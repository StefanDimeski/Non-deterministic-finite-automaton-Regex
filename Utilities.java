import java.util.*;
import java.util.stream.Collectors;

public class Utilities {

    public static boolean isBracket(char c)
    {
        return c == '(' || c == ')';
    }

    public static boolean isOperator(char c)
    {
        return isBinaryOperator(c) || isUnaryOperator(c);
    }

    public static boolean isBinaryOperator(char c)
    {
        return c == '.' || c == '|';
    }

    public static boolean isUnaryOperator(char c)
    {
        return c == '+' || c == '*' || c == '?';
    }

    public static boolean isLetterOrDigit(char c)
    {
        return c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c >= '0' && c <= '9';
    }

    public static int getPrecedance(char operator)
    {
        switch (operator)
        {
            case '(': return 100;
            case '?': case '+': case '*': return 3;
            case '.': return 2;
            case '|': return 1;
            default: return 0;
        }
    }

    public static String preprocess(String input)
    {
        // Adds concatenation character wherever needed

        // Convert the string to a list of Character so that we can add between them
        // It's way better if the returned list is a linked list, but whatever.
        List<Character> in = input.chars().mapToObj(c -> (char)c).collect(Collectors.toList());

        for (int i = 0; i < in.size() - 1; i++)
        {
            Character curr = in.get(i), next = in.get(i+1);

            if (isLetterOrDigit(curr) && isLetterOrDigit(next)
                    || isLetterOrDigit(curr) && next == '('
                    || isUnaryOperator(curr) && isLetterOrDigit(next)
                    || curr == ')' && isLetterOrDigit(next)
                    || curr == ')' && next == '(')
            {
                in.add(i + 1, '.');
                i++;
            }
        }

        return in.stream().map(String::valueOf).collect(Collectors.joining());
    }

    public static String infixToPostfix(String infix)
    {
        String res = "";

        infix = preprocess(infix);

        Stack<Character> st = new Stack<>();

        for (char c : infix.toCharArray())
        {
            if (!isOperator(c) && !isBracket(c))
                res += c;
            else
            {
                // Here it is an operator or a bracket for sure
                if (st.size() == 0 || st.peek().equals('('))
                {
                    // This is the first operator either globally or locally within parenthesis

                    // This case deals with () cases
                    if (!st.isEmpty() && st.peek().equals('(') && c == ')')
                        st.pop();
                    else
                        st.push(c);
                }
                else
                {
                    // This is not the first operator either globally or locally within parenthesis
                    if (c == ')')
                    {
                        // Empty the stack until the first open bracket
                        while (st.peek() != '(')
                        {
                            res += st.pop();
                        }

                        // Then pop the open bracket
                        st.pop();

                        continue;
                    }

                    if (getPrecedance(c) > getPrecedance(st.peek()))
                    {
                        st.push(c);
                    }
                    else if (getPrecedance(c) <= getPrecedance(st.peek()))
                    {
                        res += st.pop();
                        st.push(c);
                    }
                }
            }

        }

        // Empty the stack printing all remaining operators
        while (!st.isEmpty()) {
            if (!isBracket(st.peek()))
                res += st.pop();
            else
                st.pop();
        }

        return res;
    }



    public static ArrayList<Integer> listOf(Integer... ints)
    {
        ArrayList<Integer> lst = new ArrayList<>(ints.length);

        lst.addAll(Arrays.asList(ints));

        return lst;
    }

    // Changes list
    public static void shiftIndicies(ArrayList<Map<Character, ArrayList<Integer>>> list, int shiftAmount)
    {
        for (Map<Character, ArrayList<Integer>> map : list)
        {
            Collection<ArrayList<Integer>> l = map.values();

            Iterator<ArrayList<Integer>> it = l.iterator();

            while (it.hasNext())
            {
                List<Integer> nxt = it.next();

                for (int i = 0; i < nxt.size(); i++)
                {
                    nxt.set(i, nxt.get(i) + shiftAmount);
                }
            }
        }
    }

    public static FSM createMachineFromRegex(String regex)
    {
        Stack<FSM> st = new Stack<>();

        for (char ch : regex.toCharArray())
        {
            if (isOperator(ch))
            {
                if (ch == '.')
                {
                    FSM fsm2 = st.pop();
                    FSM fsm1 = st.pop();

                    st.push(concatenation(fsm1, fsm2));
                }
                else if (ch == '|')
                {
                    FSM fsm2 = st.pop();
                    FSM fsm1 = st.pop();

                    st.push(or(fsm1, fsm2));
                }
                else if (ch == '+')
                {
                    FSM fsm = st.pop();
                    st.push(plus(fsm));
                }
                else if (ch == '?')
                {
                    FSM fsm = st.pop();
                    st.push(questionMark(fsm));
                }
                else if (ch == '*')
                {
                    FSM fsm = st.pop();
                    st.push(kleeneStar(fsm));
                }
            }
            else
            {
                st.push(oneCharFSM(ch));
            }
        }

        return st.pop();
    }

    public static FSM oneCharFSM(char ch)
    {
        FSM oneChar = new FSM();

        oneChar.addNodes(2);
        oneChar.addTransition(0, 1, ch);
        oneChar.makeFinal(1);

        return oneChar;
    }

    public static FSM concatenation(FSM fsm1, FSM fsm2)
    {
        int fsm1Final = fsm1.nfsa.size() - 1;
        shiftIndicies(fsm2.nfsa, fsm1.nfsa.size());
        fsm1.nfsa.addAll(fsm2.nfsa);

        fsm1.addTransition(fsm1Final, fsm1Final + 1, '♀');

        fsm1.isFinal = new boolean[fsm1.nfsa.size()];
        Arrays.fill(fsm1.isFinal, false);
        fsm1.isFinal[fsm1.nfsa.size() - 1] = true;

        return fsm1;
    }


    public static FSM or(FSM fsm1, FSM fsm2)
    {
        FSM res = new FSM();
        res.addNode();

        int fsm1Start = 1;
        shiftIndicies(fsm1.nfsa, 1);
        res.nfsa.addAll(fsm1.nfsa);
        int fsm1Final = res.nfsa.size() - 1;

        int fsm2Start = res.nfsa.size();
        shiftIndicies(fsm2.nfsa, res.nfsa.size());
        res.nfsa.addAll(fsm2.nfsa);
        int fsm2Final = res.nfsa.size() - 1;

        res.addNode();

        res.addTransition(0, fsm1Start, '♀');
        res.addTransition(0, fsm2Start, '♀');

        res.addTransition(fsm1Final, res.nfsa.size() - 1, '♀');
        res.addTransition(fsm2Final, res.nfsa.size() - 1, '♀');

        res.isFinal = new boolean[res.nfsa.size()];
        Arrays.fill(res.isFinal, false);
        res.isFinal[res.nfsa.size() - 1] = true;

        return res;
    }

    public static FSM plus(FSM fsm)
    {
        fsm.addTransition(fsm.nfsa.size() - 1, 0, '♀');

        return fsm;
    }

    public static FSM questionMark(FSM fsm)
    {
        fsm.addTransition(0, fsm.nfsa.size() - 1, '♀');

        return fsm;
    }

    public static FSM kleeneStar(FSM fsm)
    {
        shiftIndicies(fsm.nfsa, 1);
        fsm.nfsa.add(0, new HashMap<>());

        fsm.addTransition(0, 1, '♀');
        fsm.addTransition(0, fsm.nfsa.size() - 1, '♀');
        fsm.addTransition(fsm.nfsa.size() - 1, 1, '♀');

        fsm.isFinal = new boolean[fsm.nfsa.size()];
        Arrays.fill(fsm.isFinal, false);
        fsm.isFinal[fsm.nfsa.size() - 1] = true;

        return fsm;
    }

}
