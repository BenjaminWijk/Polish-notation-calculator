package token;

import java.util.Deque;
import java.util.Stack;

public class Constant implements Operand {
    int value;

    public Constant(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public void handle(Stack<Operator> operatorStack, Stack<Operand> operandStack, Deque<Token> outputStack) {
        operandStack.push(this);
    }

    @Override
    public String toString() {
        return "" + value;
    }
}