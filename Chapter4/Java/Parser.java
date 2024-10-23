package Java;

import static Java.TokenType.*;

import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int current = 0; // A B C != D E F

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Expr parseTokens() {
        return expression();
    }

    // Expression -> Equality
    // Equality -> Comparison (== Comparison | != Comparison)*
    // Comparison -> 
    private Expr expression() {
        return equality();
    }
    // ((3 + 2) + 3)
    private Expr equality() {
        Expr first = comparison();
        while (matchOne(BANG_EQUAL, EQUAL_EQUAL) != null) {
            Expr next = comparison();
            first = new Expr.Binary(first, previous(), next);
        }
        return first;
    }

    private Expr comparison() {
        Expr expr = term();
        while (matchOne(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL) != null) {
            Token operator = previous();
            Expr right = term();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr term() {
        Expr expr = factor();
        while (matchOne(MINUS, PLUS) != null) {
            Token operator = previous();
            Expr right = factor();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr factor() {
        Expr expr = unary();
        while (matchOne(SLASH, STAR) != null) {
            Token operator = previous();
            Expr right = unary();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr unary() {
        if (matchOne(TokenType.BANG, TokenType.MINUS) != null) {
            return new Expr.Unary(previous(), unary());
        } else {
            return primary();
        }
    }

    private Expr primary() {
        if(matchOne(TokenType.LEFT_PAREN) != null) {
            Expr expr = expression();
            if (matchOne(TokenType.RIGHT_PAREN) != null) {
                return expr;
            } else {
                // error
                throw new ParseError("Missing right parentheses after " + expr.accept(new ASTPrinter()), getCurrentToken().line);
            }
        } else {
            if (matchOne(NUMBER, STRING, TRUE, FALSE, NIL) != null) {
                return new Expr.Literal(previous().literal);
            } else {
                throw new ParseError("Expected primitive type but got " + getCurrentToken().type + " instead", getCurrentToken().line);
            }
        }

    }
        



    private Token previous() {
        return this.tokens.get(this.current-1);
    }

    private boolean isAtEnd() {
        return this.tokens.size() == current;
    }

    private boolean check(TokenType token) {
        if (isAtEnd() && token == EOF) return true;
        else return tokens.get(current).type == token;
    }

    private Token getCurrentToken() {
        if(isAtEnd()) return tokens.get(tokens.size()-1); // EOF token
        return tokens.get(current);
    }

    private TokenType matchOne(TokenType... tokens) {
        if (isAtEnd()) {
            for (TokenType t: tokens) {
                if (t == EOF) return EOF;
            }
            return null;
        }
        for (TokenType t : tokens) {
            if (t == this.tokens.get(current).type) {
                this.current += 1;
                return t;
            }
        }
        return null;
    }


    
}
