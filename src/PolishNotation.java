import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class PolishNotation {
    private Deque<Token> outputStack = new ArrayDeque<>();

    private Stack<Operator> operatorStack = new Stack<>();
    private Stack<Operand> operandStack = new Stack<>();

    InputStream in;

    PolishNotation(InputStream in) {
        this.in = in;

        readInput();
    }

    /**
     * Reads input from System.in line for line, performs infix to PN on each line and prints out the results.
     */
    private void readInput() {
        int caseCounter = 1;

        try (Scanner caseScanner = new Scanner(in)) {

            while (caseScanner.hasNext()) {
                String infix = caseScanner.nextLine();

                Tokenizer tokenizer = new Tokenizer(infix.split(" "));
                Stack<Token> InfixTokens = tokenizer.getTokenizedStack();

                String calculation;
                calculation = convertInfixToPN(InfixTokens);

                System.out.println("Case " + caseCounter++ + ": " + calculation);
            }

        }catch(Exception e){
            e.printStackTrace(); //TODO REMOVE CATCH WHEN SWITCHING TO SYSTEM.IN
        }
    }

    /**
     * Takes a stack of tokens and performs polish notation calculations.
     *
     * @param stack
     * @return
     */
    private String convertInfixToPN(Stack<Token> stack) {
        while (!stack.isEmpty()) {
            Token t = stack.pop();
            t.handle(operatorStack, operandStack);
        }

        StringJoiner sj = new StringJoiner(" ");
        if (!operandStack.isEmpty())
            sj.add(operandStack.pop().toString());

        while (!outputStack.isEmpty()) {
            sj.add(outputStack.pop().toString());
        }

        return sj.toString();

    }

    private class Expression implements Token{
        Operand o1;
        Operand o2;
        Operator op;
        Integer simplifiedValue;
        String rawValue;

        Expression(Operator op, Operand o1, Operand o2) {
            this.op = op;
            this.o1 = o1;
            this.o2 = o2;

            rawValue = op.toString() + " " + o1.toString() + " " + o2.toString();
        }

        private boolean canSimplifyExpression() {
            return o1 instanceof Constant &&
                    o2 instanceof Constant;
        }

        void performOperation() {
            if (canSimplifyExpression()) {
                switch (op.getValue()) {
                    case "*":
                        simplifiedValue = (int) o1.getValue() * (int) o2.getValue();
                        break;
                    case "-":
                        simplifiedValue = (int) o1.getValue() - (int) o2.getValue();
                        break;
                    case "+": // + operator
                        simplifiedValue = (int) o1.getValue() + (int) o2.getValue();
                        break;
                    default:
                        System.out.println("If you got this message, you've somehow used an unsupported operator");
                }
            }
        }

        @Override
        public String toString() {
            return rawValue;
        }

        @Override
        public void handle(Stack<Operator> operatorStack, Stack<Operand> operandStack) {
            performOperation();
            if(simplifiedValue == null) {
                while (!operandStack.isEmpty())
                    outputStack.push(operandStack.pop());
                outputStack.push(this);
            }
            else
                operandStack.push(new Constant(simplifiedValue));

        }
    }

    /**
     * Creates tokens from a (split) string according to PN types.
     * Token types are documented further down in this class.
     */
    private class Tokenizer {
        Stack<Token> stack = new Stack<>();

        Tokenizer(String[] in) {
            for (String s : in)
                stack.push(getToken(s));
        }

        Stack<Token> getTokenizedStack() {
            return stack;
        }

        /**
         * Checks string to see which Token type it matches best.
         *
         * @param input String value to be tokenized
         * @return new Token with class and value depending on token type and input.
         */
        private Token getToken(String input) {
            if (isOperator(input))
                return new Operator(input);
            else if (isConstant(input))
                return new Constant(Integer.parseInt(input));
            else
                return new Variable(input);
        }

        /**
         * Simple string comparison to see if it matches any of the supported operators.
         *
         * @param input
         * @return
         */
        private boolean isOperator(String input) {
            return input.equals("*") || input.equals("-") || input.equals("+");
        }

        /**
         * Try to parse input as int.
         *
         * @param input string to check if it's an integer constant
         * @return true if it could be parsed, false otherwise
         */
        private boolean isConstant(String input) {
            try {
                Integer.parseInt(input);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }

    }

    /**
     * Token interface for Constants, Operators and variables. Makes checking and handling of types easier in later stages.
     */
    private interface Token {
        void handle(Stack<Operator> operatorStack, Stack<Operand> operandStack);
    }

    private interface Operand extends Token {
        Object getValue();
    }

    private class Constant implements Operand {
        int value;

        Constant(int value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }

        @Override
        public void handle(Stack<Operator> operatorStack, Stack<Operand> operandStack) {
            operandStack.push(this);
        }

        @Override
        public String toString() {
            return "" + value;
        }
    }

    private class Operator implements Token {
        String value;

        Operator(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        /**
         * Handles operators according to PN algorithms.
         * Contains 2 special cases that handles events that can occur due to unknown variables.
         *
         * @param operatorStack
         * @param operandStack
         */
        @Override
        public void handle(Stack<Operator> operatorStack, Stack<Operand> operandStack) {
            //2 special cases when dealing with an expression that can't be simplified
            if (operandStack.isEmpty()) {
                outputStack.push(this);
                return;
            } else if (operandStack.size() == 1) {
                outputStack.push(operandStack.pop());
                outputStack.push(this);
                return;
            }

            Operand o1 = operandStack.pop();
            Operand o2 = operandStack.pop();

            Expression e = new Expression(this,o1,o2);
            e.handle(operatorStack, operandStack);
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private class Variable implements Operand {
        String value;

        Variable(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public void handle(Stack<Operator> operatorStack, Stack<Operand> operandStack) {
            operandStack.push(this);
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public static void main(String[] args) {
        try {
            new PolishNotation(new FileInputStream(new File("testPN.txt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
