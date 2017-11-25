package token;

import java.util.Deque;
import java.util.Stack;

/**
 * Token interface for Constants, Operators and variables. Makes checking and handling of types easier in later stages.
 */
public interface Token {
    void handle(Stack<Operator> operatorStack, Stack<Operand> operandStack, Deque<Token> outputStack);
}