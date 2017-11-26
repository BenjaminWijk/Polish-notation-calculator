package pnCalc;

import token.Operand;
import token.Operator;
import token.Token;

import java.io.*;
import java.util.*;

public class PolishNotation {
    private Deque<Token> outputStack = new ArrayDeque<>();

    private Stack<Operator> operatorStack;
    private Stack<Operand> operandStack;

    private List<String> cases;

    public PolishNotation() { }

    /**
     * Reads a polish notation expression from an InputStream line for line, tokenizes on each line and prints out the results.
     * @param in Inputstream of valid Polish notation
     */
    public void readStreamInput(InputStream in) {
        try (Scanner caseScanner = new Scanner(in)){
            cases = new ArrayList<>();
            String calculation;

            while (caseScanner.hasNext()) {
                String input = caseScanner.nextLine();
                calculation = computeInput(input);

                addCase(calculation);
            }
        }
    }

    /**
     * Same as readStreamInput, but reads strings instead of a stream.
     * @param in
     */
    public void readStringInput(String... in){
        cases = new ArrayList<>();
        String calculation;
        String output;

        for(String s: in){
            calculation = computeInput(s);
            addCase(calculation);
        }
    }


    private String computeInput(String in){
        Tokenizer tokenizer = new Tokenizer();
        Stack<Token> tokens = tokenizer.getTokenizedStack(in);

        String calculation = calculatePN(tokens);

        return calculation;
    }

    /**
     * Takes a stack of tokens and performs polish notation calculations.
     */
    private String calculatePN(Stack<Token> stack) {
        operatorStack = new Stack<>();
        operandStack = new Stack<>();

        while (!stack.isEmpty()) {
            Token t = stack.pop();
            t.handle(operatorStack, operandStack, outputStack);
        }

       return pNToString();

    }

    private String pNToString(){
        StringJoiner sj = new StringJoiner(" ");

        //If "last" part of expression resolved, pop operandstack
        if (!operandStack.isEmpty()) {
            String s1 = operandStack.pop().toString();
            sj.add(s1);
        }
        while (!outputStack.isEmpty()) {
            String s2 = outputStack.pop().toString();
            sj.add(s2);
        }

        return sj.toString();

    }

    /**
     * Adds cases to be printed on System.out
     * @param calculatedInput
     */
    private void addCase(String calculatedInput){
        cases.add(calculatedInput);
    }

    private void printCases(){
        int caseCounter = 1;
        for(String s: cases){
            System.out.println("Case " + caseCounter++ + ": " + s);
        }
    }

    /**
     * for testing purposes only
     * @return
     */
    public String getCasesAsString(){
        int caseCounter = 1;

        StringJoiner sj = new StringJoiner("\n");
        for(String s: cases){
            sj.add("Case " + caseCounter++ + ": " + s);
        }

        return sj.toString();
    }

    public List<String> getRawCases(){
        return cases;
    }

    public Deque<Token> getOutputStack(){
        return outputStack;
    }

    public Stack<Operand> getOperandStack(){
        return operandStack;
    }

    public Stack<Operator> getOperatorStack() {
        return operatorStack;
    }

    /**
     * The commented code is just used for some lazy testing.
     * @param args
     */
    public static void main(String[] args) {
        PolishNotation pn = new PolishNotation();
        pn.readStreamInput(System.in);
        pn.printCases();
    }
}
