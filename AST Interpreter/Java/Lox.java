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

import Java.Backend.Interpreter;
import Java.Frontend.Statement;
import Java.Frontend.Token;
import Java.Frontend.Scanner;
import Java.Frontend.Parser;

public class Lox {
    static boolean hadError = false;
    public static void error(int line, String message) {
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
        List<Statement> ast = (new Parser(tokens)).parseTokens();
        var interpreter = new Interpreter();
        for (Statement s : ast) {
            s.accept(interpreter);
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