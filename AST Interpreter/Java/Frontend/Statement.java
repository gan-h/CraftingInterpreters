package Java.Frontend;
import java.util.ArrayList;
import java.util.List;

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
        R visitIfStatement(IfStatement s);
        R visitWhileStatement(WhileStatement s);
        R visitFuncDeclStatement(FuncDecl funcDecl);
        R visitReturnStatement(ReturnStatement returnStatement);
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

    public static class ReturnStatement extends Statement {
        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitReturnStatement(this);
        }
        public Expr expression;
        public ReturnStatement(Expr expression) {
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

    public static class WhileStatement extends Statement {
        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitWhileStatement(this);
        }
        public Expr expression;
        public Statement statement;
        WhileStatement(Expr e, Statement statement) {
            this.expression = e;
            this.statement = statement;
        }
    }

    public static class IfStatement extends Statement {
        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitIfStatement(this);
        }
        public Expr condition;
        public Statement body;
        public Statement elseBody;
        public IfStatement(Expr condition, Statement body, Statement elseBody) {
            this.condition = condition;
            this.body = body;
            this.elseBody = elseBody;
        }
    }

    public static class FuncDecl extends Statement {
        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitFuncDeclStatement(this);
        }
        public Token name;
        public List<Token> params;
        public List<Statement> body;

        public FuncDecl(Token name, List<Token> params, List<Statement> body) {
            this.name = name;
            this.params = params;
            this.body = body;
        }
    }
}
