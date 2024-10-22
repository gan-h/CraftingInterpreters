package Java;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lox {
    static boolean hadError = false;
    static void error(int line, String message) {
        report(line, "", message);
    }

    private static void report(int line, String where, String message) {
        String errorMessage = String.format("[line %d] Error %s: %s", line, where, message);
        System.err.println(errorMessage);
        hadError = true;
    }

    static void runFile(String filepath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filepath));
        run(String.join("", lines));
        if (hadError) System.exit(65);
        
    }

    static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (;;) {
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null) break;
            run(line);
            hadError = false;
        }
    }
    public static void main(String[] args) throws IOException {
        Expr expression = new Expr.Binary(
        new Expr.Unary(
        new Token(TokenType.MINUS, "-", null, 1),
        new Expr.Literal(123)),
        new Token(TokenType.STAR, "*", null, 1),
        new Expr.Grouping(
        new Expr.Literal(45.67)));
        System.out.println(new ASTPrinter().print(expression));
        if (args.length > 1) {
            System.out.println("Usage: jlox [script]");
            System.exit(0);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }
}