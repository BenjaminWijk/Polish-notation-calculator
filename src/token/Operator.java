package token;

import java.util.Deque;
import java.util.Stack;

public class Operator implements Token {
    String value;

    public Operator(String value) {
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
    public void handle(Stack<Operator> operatorStack, Stack<Operand> operandStack, Deque<Token> outputStack) {
        //2 special cases when dealing with an expression that can't be simplified
        if (operandStack.isEmpty()) {
            outputStack.push(this);
            return;
        } else if (operandStack.size() == 1) {
            outputStack.push(operandStack.pop());
            outputStack.push(this);
            return;
        }

        //Is a valid expression, let an expression object handle logic
        Operand o1 = operandStack.pop();
        Operand o2 = operandStack.pop();

        Expression e = new Expression(this,o1,o2);
        e.handle(operatorStack, operandStack, outputStack);
    }

    @Override
    public String toString() {
        return value;
    }
}