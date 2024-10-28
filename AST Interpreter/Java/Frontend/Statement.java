package Java.Frontend;
import java.util.ArrayList;

// program -> statement* EOF
// statement -> exprStatement | printStatement
// exprStatement -> expression ";"
// printStatement -> "print" expression ";"

public abstract class Statement {

    public abstract <R> R accept(Visitor<R> visitor);
    public interface Visitor<R> {
        R visitPrintStatement(PrintStatement p);
        R visitExpressionStatement(ExpressionStatement s);
        R visitVariableDeclaration(VariableDeclaration s);
        R visitBlockStatement(BlockStatement s);
    }

    public static class VariableDeclaration extends Statement {

        public Token name; 
        public Expr initializer;

        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitVariableDeclaration(this);
        }

        public VariableDeclaration(Token name, Expr initializer) {
            this.name = name;
            this.initializer = initializer;
        }

    }

    public static class ExpressionStatement extends Statement {
        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitExpressionStatement(this);
        }
        public Expr expression;
        ExpressionStatement(Expr expression) {
            this.expression = expression;
        }
    }

    public static class BlockStatement extends Statement {
        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitBlockStatement(this);
        }
        public ArrayList<Statement> statements;
        BlockStatement(ArrayList<Statement> statements) {
            this.statements = statements;
        }
    }

    public static class PrintStatement extends Statement {
        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitPrintStatement(this);
        }
        public Expr expression;
        PrintStatement(Expr expression) {
            this.expression = expression;
        }
    }
}
