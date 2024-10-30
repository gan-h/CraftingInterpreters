package Java.Backend;
import static Java.Frontend.Expr.*;
import static Java.Frontend.TokenType.AND;
import static Java.Frontend.TokenType.OR;

import java.util.ArrayList;
import java.util.List;

import Java.Backend.Error.InterpretError;
import Java.Backend.Error.Return;
import Java.Frontend.Expr;
import Java.Frontend.Statement;
import Java.Frontend.Expr.Binary;
import Java.Frontend.Expr.Call;
import Java.Frontend.Expr.Grouping;
import Java.Frontend.Expr.Identifier;
import Java.Frontend.Expr.Literal;
import Java.Frontend.Expr.Logical;
import Java.Frontend.Expr.Unary;
import Java.Frontend.Statement.*;

public class Interpreter implements Expr.Visitor<Object>, Statement.Visitor<Void> {

    public Environment environment = new Environment();

    public Interpreter() {
        environment.define("clock", new LoxCallable() {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> args) {
                return (double) System.currentTimeMillis() / 1000.0;
            }

            @Override
            public String toString() {
                return "<native clock function>";
            }
            
        });
    }


    Object print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public Void visitPrintStatement(PrintStatement p) {
        System.out.println(stringify(evaluate(p.expression)));
        return null;
    }

    @Override
    public Object visitAssignExpr(Expr.Assign expr) {
        Object value = evaluate(expr.value);
        environment.assign(expr.name.lexeme, value);
        return value;
    }


    @Override
    public Void visitExpressionStatement(ExpressionStatement s) {
        evaluate(s.expression);
        return null;
    }

    private String stringify(Object object) {
        if (object == null) return "nil";
        if (object instanceof Double) {
        String text = object.toString();
        if (text.endsWith(".0")) {
            text = text.substring(0, text.length() - 2);
            }
            return text;
        }
        return object.toString();
    }

    public void evaluate(List<Statement> statements) {
        for (Statement s : statements) {
            evaluate(s);
        }
    }

    public void evaluate(Statement expr) {
        expr.accept(this);
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
                if (left instanceof String && right instanceof Double || 
                    right instanceof String && left instanceof Double) {
                    return left + "" + right;
                }
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

    @Override
    public Void visitVariableDeclaration(VariableDeclaration s) {
        environment.define(s.name.lexeme, evaluate(s.initializer));
        return null;
    }

    @Override
    public Object visitIdentifierExpr(Identifier expr) {
        return environment.get(expr.identifier.lexeme);
    }

    @Override
    public Void visitBlockStatement(BlockStatement block) {
        // Executes block with new environment, afterwards backtracks to old environment.
        Environment previous = this.environment;
        Environment new_environment = new Environment(previous);
        this.environment = new_environment;

        try {
            for(Statement stmt : block.statements) {
                evaluate(stmt);
            }
        } finally {
            this.environment = previous;
        }
        return null;
    }

    @Override
    public Void visitIfStatement(IfStatement s) {
        boolean truthy = isTruthy(evaluate(s.condition));
        if (truthy) {
            evaluate(s.body);
        } else if(s.elseBody != null) {
            evaluate(s.elseBody);
        };
        return null;
    }

    @Override
    public Object visitLogicalExpr(Logical expr) {
        if (expr.operator.type == AND) {
            return isTruthy(evaluate(expr.left)) && isTruthy(evaluate(expr.right));
        } else if (expr.operator.type == OR) {
            return isTruthy(evaluate(expr.left)) || isTruthy(evaluate(expr.right));
        }
        throw new InterpretError("This should not happen");
    }

    @Override
    public Void visitWhileStatement(WhileStatement s) {
        while (isTruthy(evaluate(s.expression))) {
            evaluate(s.statement);
        } 
        return null;
    }

    @Override
    public Object visitCallExpr(Call expr) {
        Object function = evaluate(expr.function);
        List<Object> arguments = new ArrayList<>();
        for (Expr argument : expr.argumentList) {
            arguments.add(evaluate(argument));
        }
        LoxCallable callable = (LoxCallable) function;
        
        return callable.call(this, arguments);
    }


    @Override
    public Void visitFuncDeclStatement(FuncDecl funcDecl) {
        environment.define(funcDecl.name.lexeme, new LoxFunction(funcDecl, environment));
        return null;
    }


    @Override
    public Void visitReturnStatement(ReturnStatement returnStatement) {
        throw new Return(evaluate(returnStatement.expression));
    }
    
}
