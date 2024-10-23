package Java;
import static Java.Expr.*;

import Java.Expr.Binary;
import Java.Expr.Grouping;
import Java.Expr.Literal;
import Java.Expr.Unary;
import Java.Expr.Visitor;

public class Interpreter implements Visitor<Object> {
    Object print(Expr expr) {
        return expr.accept(this);
    }

    public Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    private boolean isEqual(Object left, Object right) {
        if (left == null && right == null) {
            return true;
        } else if(left == null) {
            return false;
        } else if (right == null) {
            return false;
        } else {
            return left.equals(right);
        }
    }

    @Override
    public Object visitBinaryExpr(Binary expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double)left + (double)right;
                }
                if (left instanceof String && right instanceof String) {
                    return (String)left + (String)right;
                }
                break;
            case MINUS:
                return (double) left - (double) right;
            case SLASH:
                return (double) left / (double) right;
            case STAR:
                return (double) left * (double) right;
            case GREATER:
                return (double)left > (double)right;
            case GREATER_EQUAL:
                return (double)left >= (double)right;
            case LESS:
                return (double)left < (double)right;
            case LESS_EQUAL:
                return (double)left <= (double)right;
            case BANG_EQUAL: 
                return !isEqual(left, right);
            case EQUAL_EQUAL: 
                return isEqual(left, right);
            default:
                throw new InterpretError("Binary expression switch statement fell through");
        }
        throw new InterpretError("Binary expression switch statement fell through");
    }

    @Override
    public Object visitGroupingExpr(Grouping expr) {
        return evaluate(expr);
    }

    @Override
    public Object visitLiteralExpr(Literal expr) {
        return expr.value;
    }

    private boolean isTruthy(Object o) {
        if (o instanceof Double) {
            return (double) o != 0;
        } else if (o instanceof Boolean) {
            return (Boolean) o;
        } else if (o instanceof String) {
            return !((String) o).isEmpty();
        } else if (o == null) {
            return false;
        }
        throw new InterpretError("isTruthy fall through");
    }
    

    @Override
    public Object visitUnaryExpr(Unary expr) {
        Object rhs = evaluate(expr.right);
        switch (expr.operator.type) {
            case BANG:
                return !isTruthy(rhs);
            case MINUS:
                return -(double) rhs;
            default:
                throw new InterpretError("Unary expression fall-fallthrough at switch statement");
        }
    }
    
}
