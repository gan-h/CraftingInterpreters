package Java.Frontend;

// program -> statement* EOF
// statement -> exprStatement | printStatement
// exprStatement -> expression ";"
// printStatement -> "print" expression ";"

public abstract class Statement {

    public abstract <R> R accept(Visitor<R> visitor);
    public interface Visitor<R> {
        R visitPrintStatement(PrintStatement p);
        R visitExpressionStatement(ExpressionStatement s);
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
