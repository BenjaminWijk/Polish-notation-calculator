package token;

import java.util.Deque;
import java.util.Stack;

public class Variable implements Operand {
    String value;

    public Variable(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public void handle(Stack<Operator> operatorStack, Stack<Operand> operandStack, Deque<Token> outputStack) {
        operandStack.push(this);
    }

    @Override
    public String toString() {
        return value;
    }
}