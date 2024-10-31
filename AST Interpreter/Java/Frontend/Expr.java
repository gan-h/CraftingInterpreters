package Java.Frontend;

import java.util.List;

abstract public class Expr {

    public static interface Visitor<R> {
        R visitBinaryExpr(Binary expr);
        R visitGroupingExpr(Grouping expr);
        R visitLiteralExpr(Literal expr);
        R visitUnaryExpr(Unary expr);
        R visitIdentifierExpr(Identifier expr);
        R visitAssignExpr(Assign expr);
        R visitLogicalExpr(Logical expr);
        R visitCallExpr(Call call);
        R visitGetExpr(Get get);
        R visitSetExpr(Set set);
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

    public static class Call extends Expr {
        public final Expr function;
        public final List<Expr> argumentList;

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitCallExpr(this);
        }

        public Call(Expr function, List<Expr> argumentList) {
            this.function = function;
            this.argumentList = argumentList;
        }
    }

    public static class Assign extends Expr {

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitAssignExpr(this);
        }

        public final Token name;
        public final Expr value;

        public Assign(Token name, Expr value) {
            this.name = name;
            this.value = value;
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

    public static class Identifier extends Expr {
        public final Token identifier;

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitIdentifierExpr(this);
        }
        Identifier(Token value) {
            this.identifier = value;
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

    public static class Logical extends Expr {
        public final Expr left;
        public final Expr right;
        public final Token operator;

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitLogicalExpr(this);
        }
        
        public Logical(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }
    }

    public static class Get extends Expr {
        public final Expr object;
        public final Token name;

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitGetExpr(this);
        }
        
        public Get(Expr object, Token name) {
            this.object = object;
            this.name = name;
        }
    }

    public static class Set extends Expr {
        public final Expr object;
        public final Token name;
        public final Expr value;

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitSetExpr(this);
        }
        
        public Set(Expr object, Token name, Expr value) {
            this.object = object;
            this.name = name;
            this.value = value;
        }
    }
}
