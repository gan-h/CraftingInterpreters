package Java.Frontend;

abstract public class Expr {

    public static interface Visitor<R> {
        R visitBinaryExpr(Binary expr);
        R visitGroupingExpr(Grouping expr);
        R visitLiteralExpr(Literal expr);
        R visitUnaryExpr(Unary expr);
    }

    public abstract <R> R accept(Visitor<R> visitor);

    public static class Binary extends Expr {
        public final Expr left;
        public final Token operator;
        public final Expr right;

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinaryExpr(this);
        }

        Binary(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }
    }

    public static class Grouping extends Expr {

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitGroupingExpr(this);
        }

        public final Expr expression;
        Grouping(Expr expression) {
            this.expression = expression;
        }
    }

    public static class Literal extends Expr {
        public final Object value;

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteralExpr(this);
        }
        Literal(Object value) {
            this.value = value;
        }
    }

    public static class Unary extends Expr {
        public final Token operator;
        public final Expr right;
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnaryExpr(this);
        }
        Unary(Token operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }
    }
}
