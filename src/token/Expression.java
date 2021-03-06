package token;

import java.util.Deque;
import java.util.Stack;

public class Expression implements Token{
    Operand o1;
    Operand o2;
    Operator op;
    Integer simplifiedValue;
    String rawValue;

    public Expression(Operator op, Operand o1, Operand o2) {
        this.op = op;
        this.o1 = o1;
        this.o2 = o2;

        rawValue = op.toString() + " " + o1.toString() + " " + o2.toString();

    }

    private boolean canSimplifyExpression() {
        return o1 instanceof Constant &&
                o2 instanceof Constant;
    }

    private void performOperation() {
        if (canSimplifyExpression()) {

            switch (op.getValue()) {
                case "*":
                    simplifiedValue = (Integer) o1.getValue() * (Integer) o2.getValue();
                    break;
                case "-":
                    simplifiedValue = (Integer) o1.getValue() - (Integer) o2.getValue();
                    break;
                case "+":
                    simplifiedValue = (Integer) o1.getValue() + (Integer) o2.getValue();
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
    public void handle(Stack<Operator> operatorStack, Stack<Operand> operandStack, Deque<Token> outputStack) {
        performOperation();
        if (simplifiedValue == null) {

            //Ugly hack to reverse whatever is on operandStack before pushing to output
            Stack<Token> stack = new Stack<>();
            while (!operandStack.isEmpty()) {
                stack.push(operandStack.pop());
            }
            while(!stack.isEmpty()){
                outputStack.push(stack.pop());
            }

            outputStack.push(this);
        } else {
            operandStack.push(new Constant(simplifiedValue));
        }
    }
}
