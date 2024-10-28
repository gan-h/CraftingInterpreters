package Java.Frontend;

import static Java.Frontend.Statement.*;
import static Java.Frontend.TokenType.*;

import java.util.ArrayList;
import java.util.List;

import Java.Frontend.Error.ParseError;


public class Parser {
    private final List<Token> tokens;
    private int current = 0; // A B C != D E F

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    //Program -> Declaration* EOF
    public ArrayList<Statement> parseProgram() {
        ArrayList<Statement> statements = new ArrayList<Statement>();
        while(!isAtEnd()) {
            if (matchOne(EOF) != null) break;
            statements.add(declaration());
        }
        return statements;
    }

    public Statement declaration() {
        if(getCurrentToken().type == VAR) {
            return vardecl();
        }
        return statement();
    }

    public Statement vardecl() {
        consumeOne(VAR);
        Token name = consumeOne(IDENTIFIER);
        
        if (matchOne(EQUAL) != null) {
            Expr initializer = expression();
            consumeOne(SEMICOLON);
            return new Statement.VariableDeclaration(name, initializer);
        }

        consumeOne(SEMICOLON);

        return new Statement.VariableDeclaration(name, null);
    }

    public Statement statement() {
        switch (getCurrentToken().type) {
            case PRINT:
                current += 1;
                var printStatement = new PrintStatement(expression());
                consumeOne(SEMICOLON);
                return printStatement;
            default:
                var expressionStatement = new ExpressionStatement(expression());
                consumeOne(SEMICOLON);
                return expressionStatement;
        }
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
            Token operator = previous();
            Expr next = comparison();
            first = new Expr.Binary(first, operator, next);
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
            consumeOne(RIGHT_PAREN);
            return expr;
        } else {
            if (matchOne(NUMBER, STRING, TRUE, FALSE, NIL) != null) {
                return new Expr.Literal(previous().literal);
            } else if (matchOne(IDENTIFIER) != null) {
                return new Expr.Identifier(previous());
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

    private Token consumeOne(TokenType token) {
        Token t;
        if ((t = matchOne(token)) != null) {
            return t;
        }
        throw new ParseError("Expected " + token);
    }

    private Token getCurrentToken() {
        return tokens.get(current);
    }

    private Token matchOne(TokenType... tokens) {
        if (isAtEnd()) {
            throw new ParseError("Read one past the end");
        }
        for (TokenType t : tokens) {
            if (t == this.tokens.get(current).type) {
                this.current += 1;
                return previous();
            }
        }
        return null;
    }


    
}
