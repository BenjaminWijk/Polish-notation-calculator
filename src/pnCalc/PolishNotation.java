package pnCalc;

import token.Operand;
import token.Operator;
import token.Token;

import java.io.File;

import java.io.*;
import java.util.*;

public class PolishNotation {
    private Deque<Token> outputStack = new ArrayDeque<>();

    private Stack<Operator> operatorStack;
    private Stack<Operand> operandStack;

    private InputStream in;

    PolishNotation(InputStream in) {
        this.in = in;

        readInput();
    }

    /**
     * Reads input from System.in line for line, performs infix to PN on each line and prints out the results.
     */
    private void readInput() {
        int caseCounter = 1;

        try (Scanner caseScanner = new Scanner(in)){

            while (caseScanner.hasNext()) {
                String input = caseScanner.nextLine();

                Tokenizer tokenizer = new Tokenizer(input.split(" "));
                Stack<Token> tokens = tokenizer.getTokenizedStack();

                String calculation;
                calculation = calculatePN(tokens);

                System.out.println("Case " + caseCounter++ + ": " + calculation);
            }

        }
    }

    /**
     * Takes a stack of tokens and performs polish notation calculations.
     *
     * @param stack
     * @return
     */
    private String calculatePN(Stack<Token> stack) {
        operatorStack = new Stack<>();
        operandStack = new Stack<>();

        while (!stack.isEmpty()) {
            Token t = stack.pop();
            t.handle(operatorStack, operandStack);
        }

       return pNToString();

    }

    private String pNToString(){
        StringJoiner sj = new StringJoiner(" ");

        //If "last" part of expression resolved, pop operandstack
        if (!operandStack.isEmpty()) {
            if(!outputStack.isEmpty())
                System.out.println("outputstack not empty");
            sj.add(operandStack.pop().toString());
        }
        while (!outputStack.isEmpty()) {
            sj.add(outputStack.pop().toString());
        }

        return sj.toString();

    }

    /**
     * The commented code is just used for some lazy testing.
     * @param args
     */
    public static void main(String[] args) {
       // new pnCalc.PolishNotation(System.in);

        try {
            new PolishNotation(new FileInputStream(new File("testPN.txt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
