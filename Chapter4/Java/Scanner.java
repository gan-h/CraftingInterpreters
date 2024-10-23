package Java;

import java.util.ArrayList;
import java.util.List;
import static Java.TokenType.*;
import java.util.HashMap;
import java.util.Map;

public class Scanner {
    private final String source; 
    private final List<Token> tokens = new ArrayList<Token>();
    private int start = 0; // points to first character in lexeme
    private int current = 0; // points at the character being considered
    private int line = 1;
    public Scanner(String source) {
        this.source = source;
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private char advance() {
        return source.charAt(current++);
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            case ' ': break;
            case '\r': break;
            case '\t': break;
            case '\n':
                line++;
                break;
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '{': addToken(LEFT_BRACE); break;
            case '}': addToken(RIGHT_BRACE); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case '-': addToken(MINUS); break;
            case '+': addToken(PLUS); break;
            case ';': addToken(SEMICOLON); break;
            case '*': addToken(STAR); break;
            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;
            case '/':
                if (match('/')) {
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else {
                    addToken(SLASH);
                }
                break;
            case '"':
                string();
                break;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                number();
                break;
            default:
                if (isAlpha(c)) {
                    identifier();
                    break;
                }
                Lox.error(line, "Unexpected character.");
        }
    }

    private static final Map<String, TokenType> keywords;
    static {
        keywords = new HashMap<>();
        keywords.put("and", AND);
        keywords.put("class", CLASS);
        keywords.put("else", ELSE);
        keywords.put("false", FALSE);
        keywords.put("for", FOR);
        keywords.put("fun", FUN);
        keywords.put("if", IF);
        keywords.put("nil", NIL);
        keywords.put("or", OR);
        keywords.put("print", PRINT);
        keywords.put("return", RETURN);
        keywords.put("super", SUPER);
        keywords.put("this", THIS);
        keywords.put("true", TRUE);
        keywords.put("var", VAR);
        keywords.put("while", WHILE);
    }


    private void identifier() {
        // Precondition: we munched a letter already
        while(isAlphaNumeric(peek())) {
            advance();
        }
        String identifier_string = source.substring(start, current);
        if (keywords.get(identifier_string) == null) {
            addToken(IDENTIFIER, identifier_string);
        } else {
            TokenType tokenType = keywords.get(identifier_string);
            if (tokenType == TRUE) {
                addToken(tokenType, true);
            } else if (tokenType == FALSE) {
                addToken(tokenType, false);
            } else {
                addToken(keywords.get(identifier_string));
            }
        }
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private boolean isAlpha(char c) {
        return 'a' <= c && c <= 'z' || 'A' <= c && c <= 'Z' || c == '_';
    }

    private boolean isDigit(char c) {
        return '9' >= c && c >= '0';
    }
    private void number() {
        boolean periodSeen = false;
        int trailingDecimals = 0;
        while (true) {
            if (isDigit((peek()))) {
                trailingDecimals += periodSeen ? 1 : 0;
                advance();
            } else if (peek() == '.') {
                if (periodSeen) {
                    Lox.error(line, "Double period");
                    advance();
                    return;
                } else {
                    periodSeen = true;
                    advance();
                }
            } else {
                if (periodSeen && trailingDecimals == 0) {
                    Lox.error(line, "You used a period in a number literal, but didn't add extra numbers after the period.");
                    return;
                }
                addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
                return;
            } 
        }
    }

    private void string() {
        // Precondition: We have consumed 1 quotation mark and current is set to the index after the quotation mark
        // Postcondition: We have consumed everything in the string literal
        
        while(peek() != '"' && !isAtEnd()) {
            advance();
        }

        if (isAtEnd()) {
            Lox.error(line, "Unterminated string.");
            return;
        } else {
            // Quotation mark
            advance();
            addToken(STRING, source.substring(start+1, current-1));
        }
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private boolean match(char expected) {
        // Initial condition: the code from [start, current) is being parsed
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;
        current++;
        return true;
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    List<Token> scanTokens() {
        // Logic: 
        // Initial Condition: The source code from [0, current) has been parsed already
        // After scan token: The source code from [0, current) has been parsed already
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }

        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }


}
