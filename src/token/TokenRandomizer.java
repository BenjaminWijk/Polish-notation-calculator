package token;
import java.util.Random;

/**
 * Used for fuzzing
 */
public class TokenRandomizer {
    private Random rand = new Random();
    private Boolean includeVariables;

    public TokenRandomizer(boolean includeVariables){
        this.includeVariables = includeVariables;

    }

    public Operand generateOperand(){
        int r;
        if(includeVariables)
            r = rand.nextInt(2);
        else
            r = 0;

        switch(r) {
            case 0:
                return new Constant(rand.nextInt(20) - 9);
            case 1:
                char x = 'a';
                x += rand.nextInt(25);
                return new Variable("" + x);
            default:
                return null;
        }
    }

    public Operator generateOperator(){
        int r = rand.nextInt(3);

        switch(r){
            case 0:
                return new Operator("*");
            case 1:
                return new Operator("-");
            case 2:
                return new Operator("+");
            default:
                return null;
        }
    }




}
