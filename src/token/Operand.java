package token;

/**
 * Just an interface to define that the token is an operand and not operator
 */
public interface Operand extends Token {
    Object getValue();
}