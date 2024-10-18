package Java;

public class Token {
    final TokenType type;
    final String lexeme; // the blob of characters
    final Object literal;
    final int line;

    Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    public String toString() {
        return String.format("%s %s %s", type, lexeme, literal);
    }
}
