package pnCalc;


import token.Constant;
import token.Operator;
import token.Token;
import token.Variable;

import java.util.Stack;

/**
 * Creates tokens from a (split) string according to PN types.
 * Token types are documented further down in this class.
 */
 class Tokenizer {
    Stack<Token> stack = new Stack<>();

    Tokenizer() {}

    Stack<Token> getTokenizedStack(String in) {
        for (String t : in.split(" "))
            stack.push(getToken(t));

        return stack;
    }

    /**
     * Checks string to see which Token type it matches best.
     *
     * @param input String value to be tokenized
     * @return new Token with class and value depending on token type and input.
     */
    private Token getToken(String input) {
        if (isOperator(input)) {
            return new Operator(input);
        }
        else if (isConstant(input)) {
            return new Constant(Integer.parseInt(input));
        }
        else {
            return new Variable(input);
        }
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
            int x = Integer.parseInt(input);
            if (x >= -10 && x <= 10) {
                return true;
            }else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

}