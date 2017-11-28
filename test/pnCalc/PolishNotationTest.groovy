package pnCalc

import org.junit.Ignore
import token.Expression
import token.Operand
import token.Operator
import token.Token
import token.TokenRandomizer

class PolishNotationTest extends GroovyTestCase {

    void testSampleInput(){
        PolishNotation pn = new PolishNotation()
        String [] sampleInput =  new String[3]

        sampleInput[0] = "+ 3 4"
        sampleInput[1] = "- x x"
        sampleInput[2] = "* - 6 + x -6 - - 9 6 * 0 c"

        pn.readString(sampleInput)

        assertEquals("Case 1: 7\nCase 2: - x x\nCase 3: * - 6 + x -6 - 3 * 0 c", pn.getCasesAsString())

        pn.readString("- + - 3 4 - x x + 3 5")
        assertEquals("Case 1: - + -1 - x x 8", pn.getCasesAsString())

        pn.readString("- + - x c * 5 b - 3 4")
        assertEquals("Case 1: - + - x c * 5 b -1", pn.getCasesAsString())

        pn.readString("- * + 3 4 * 5 4 - - 3 3 * 4 5")
        assertEquals("Case 1: 160", pn.getCasesAsString())

        pn.readString("- * + 3 4 * 5 x - - 3 3 * 4 5")
        assertEquals("Case 1: - * 7 * 5 x -20", pn.getCasesAsString())

        pn.readString("* - + * x 5 + 3 4 - 5 5 * c x")
        assertEquals("Case 1: * - + * x 5 7 0 * c x", pn.getCasesAsString())
    }

    /**
     * DEPRECATED, needs new test condition
     */
    @Ignore
    void testPN(){
        PolishNotation pn = new PolishNotation()
        String [] cases = new String[5]

        for(int i=0; i<1000;i++) {
            for(int j=0;j<5; j++){
                cases[j] = generateValidPN(true)
            }
            pn.readString(cases)
        }
    }


    /**
     * Generates an expression for PolishNotation to run.
     * @return A valid PN expression (can be completely resolved to a single constant, if no variables are included)
     */
    String generateValidPN(boolean includeVariables){
        Random rand = new Random();
        TokenRandomizer tr = new TokenRandomizer(includeVariables); //Boolean constructor. Set true if you want to include variables. False if you only want constants.

        int noOfSubExpressions;
        int noOfSingleOperators;
        int noOfCycles;

        Stack<Token> stack = new Stack<>()

        noOfCycles = rand.nextInt(5) + 1

        for(int n=0; n < noOfCycles; n++){
            noOfSubExpressions = rand.nextInt(5) + 1
            if(noOfSubExpressions % 2 != 0) {
                noOfSubExpressions++
            }

            noOfSingleOperators = noOfSubExpressions-1;

            for(int i=0;i < noOfSubExpressions; i++){
                Operator op = tr.generateOperator();
                Operand o1 = tr.generateOperand();
                Operand o2 = tr.generateOperand();

                Expression e = new Expression(op, o1, o2);
                stack.add(e)
            }

            for(int i=0; i<noOfSingleOperators; i++){
                stack.add(tr.generateOperator())
            }
        }

        //add amount of operators at end to make a resolvable expression
        for(int i=0; i<noOfCycles-1; i++){
            stack.add(tr.generateOperator())
        }

        StringJoiner sj = new StringJoiner(" ")

        while(!stack.isEmpty()){
            sj.add(stack.pop().toString());
        }

        PolishNotation pn = new PolishNotation(); //TEMP!!

        if(!checkValidPN(sj.toString())) {
            println "generatedPN did not pass check, returning null"
            return null;
        }

        return sj.toString()
    }

    /**
     * Test if generation generates a resolvable expression.
     */
    void testGenerateValidPN(){
        PolishNotation pn = new PolishNotation();

        for(int i=0; i<5000; i++) {
            pn.readString(generateValidPN(false));

            assert pn.getRawCases().size() == 1
            String s = pn.getRawCases().get(0);

            try{
                Integer.parseInt(s);
                assert true
            }
            catch(NumberFormatException e ){
                assert false
            }
        }
    }

    void testCheckValidPN(){

        assertEquals(true, checkValidPN("+ 3 4"))
        assertEquals(true, checkValidPN("- x x"))
        assertEquals(true, checkValidPN("* - 6 + x -6 - - 9 6 * 0 c"))

        assertEquals(false, checkValidPN("- - 3 4"))
        assertEquals(false, checkValidPN("- - 4"))
    }

    /**
     * Checks the validity by going through the expression and checking if the expression holds up using a counter
     * @param s
     * @return
     */
    boolean checkValidPN(String s){
        int counter = 0;
        Stack<Token> stack;

        Tokenizer tokenizer = new Tokenizer();
        stack = tokenizer.getTokenizedStack(s)

        while(!stack.isEmpty()){
            Token t = stack.pop();

            if (t instanceof Operand) {
                counter++;
            }
            if (t instanceof Operator){
                counter -= 2;
                if(counter < 0)
                    return false;
                counter++;
            }
            if(counter < 0){
                return false;
            }
        }

        if(counter == 1)
            return true;
        return false;
    }


}
