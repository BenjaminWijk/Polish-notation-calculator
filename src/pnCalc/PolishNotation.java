package pnCalc;

import token.Operand;
import token.Operator;
import token.Token;

import java.io.*;
import java.util.*;

public class PolishNotation {
    private Deque<Token> outputStack;
    private Stack<Operator> operatorStack;
    private Stack<Operand> operandStack;

    private List<String> cases;

    public PolishNotation() { }

    /**
     * Reads a polish notation expression from an InputStream line for line. The expression is calculated and put into a list of cases, accessible through the following methods:
     * getRawCases(), getCasesAsString(), and printCases().
     *
     * @param in Inputstream of valid Polish notation. Each line is a separate expression. Each Operator/operand/variable needs to be separated by whitespace.
     */
    public void readStream(InputStream in) {
        try (Scanner caseScanner = new Scanner(in)){
            cases = new ArrayList<>();
            String calculation;

            while (caseScanner.hasNext()) {
                String input = caseScanner.nextLine();
                calculation = calculatePN(input);

                addCase(calculation);
            }
        }
    }

    /**
     * Reads polish notation expressions from strings. The expressions are calculated and put into a list of cases, accessible through the following methods:
     * getRawCases(), getCasesAsString(), and printCases().
     * @param in Vararg of strings containing valid polish notation. Each string is a separate expression. Each Operator/operand/variable needs to be separated by whitespace.
     */
    public void readString(String... in){
        cases = new ArrayList<>();
        String calculation;

        for(String input: in){
            calculation = calculatePN(input);
            addCase(calculation);
        }
    }

    /**
     * Calculates the string by turning the string into a stack of tokens,
     * and then handling them according to the right-to-left PN algorithm
     * @param input a valid polish notation statement. All operators/operands/variables should be separated by whitespace.
     */
    private String calculatePN(String input) {
        outputStack = new ArrayDeque<>();
        operatorStack = new Stack<>();
        operandStack = new Stack<>();

        Tokenizer tokenizer = new Tokenizer();
        Stack<Token> stack = tokenizer.getTokenizedStack(input);

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
     * @param calculatedInput expression after it has been calculated by calculatePN. Will be a constant if expression fully resolved.
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
     * Mainly for testing purposes
     * @return a string containing all calculated PN expressions from the input, separated by line and given a case number.
     */
    public String getCasesAsString(){
        int caseCounter = 1;

        StringJoiner sj = new StringJoiner("\n");
        for(String s: cases){
            sj.add("Case " + caseCounter++ + ": " + s);
        }

        return sj.toString();
    }

    /**
     * Mainly for testing purposes
     * @return List of case strings without "Case x: " template at start
     */
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

}
