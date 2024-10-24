package Java;


// program -> statement* EOF
// statement -> exprStatement | printStatement
// exprStatement -> expression ";"
// printStatement -> "print" expression ";"

public abstract class Statement {

    abstract <R> R accept(Visitor<R> visitor);
    interface Visitor<R> {
        R visitPrintStatement(PrintStatement p);
        R visitExpressionStatement(ExpressionStatement s);
        R visitGlobalVariableDeclarationStatement(GlobalVariableDeclarationStatement s);
    }

    public static class GlobalVariableDeclarationStatement extends Statement {
        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitGlobalVariableDeclarationStatement(this);
        }
        Expr expression;
        GlobalVariableDeclarationStatement(Expr expression) {
            this.expression = expression;
        }
    }

    public static class ExpressionStatement extends Statement {
        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitExpressionStatement(this);
        }
        Expr expression;
        ExpressionStatement(Expr expression) {
            this.expression = expression;
        }
    }

    public static class PrintStatement extends Statement {
        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitPrintStatement(this);
        }
        Expr expression;
        PrintStatement(Expr expression) {
            this.expression = expression;
        }
    }
}
