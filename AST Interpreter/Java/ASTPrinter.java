package Java;
import static Java.Expr.*;

public class ASTPrinter implements Visitor<String> {
    String print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitBinaryExpr(Binary expr) {
        return String.format("(%s %s %s)", expr.operator.lexeme, expr.left.accept(this), expr.right.accept(this));
    }

    @Override
    public String visitGroupingExpr(Grouping expr) {
        return String.format("(group %s)", expr.expression.accept(this));
    }

    @Override
    public String visitLiteralExpr(Literal expr) {
        if (expr.value == null) {
            return "nil";
        }
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Unary expr) {
        return String.format("(%s %s)", expr.operator.lexeme, expr.right.accept(this));
    }
    
}
