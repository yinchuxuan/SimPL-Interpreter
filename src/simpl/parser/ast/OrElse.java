package simpl.parser.ast;

import simpl.interpreter.BoolValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.typing.Substitution;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;
import simpl.typing.TypeVar;

public class OrElse extends BinaryExpr {

    public OrElse(Expr l, Expr r) {
        super(l, r);
    }

    public String toString() {
        return "(" + l + " orelse " + r + ")";
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        TypeResult r1 = l.typecheck(E);
        TypeResult r2 = l.typecheck(E);
        Substitution s = r1.s.compose(r2.s);
        Type t1 = s.apply(r1.t);
        Type t2 = s.apply(r2.t);

        if(t1 instanceof TypeVar && t2 == Type.BOOL){
            return TypeResult.of(s.compose(Substitution.of((TypeVar)t1, Type.BOOL)), Type.BOOL);
        }

        if(t1 == Type.BOOL && t2 instanceof TypeVar){
            return TypeResult.of(s.compose(Substitution.of((TypeVar)t2, Type.BOOL)), Type.BOOL);
        }

        if(t1 instanceof TypeVar && t2 instanceof TypeVar){
            return TypeResult.of(s.compose(Substitution.of((TypeVar)t1, Type.BOOL)).compose(Substitution.of((TypeVar)t2, Type.BOOL)), Type.BOOL);
        }

        if(t1 == Type.BOOL && t2 == Type.BOOL){
            return TypeResult.of(s, Type.BOOL);
        }

        throw new TypeError("The typecheck of OrElse is faulty!");
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        BoolValue a = (BoolValue)l.eval(s);
        BoolValue b = (BoolValue)r.eval(s);

        if(!a.b && !b.b){
            return new BoolValue(false);
        }else{
            return new BoolValue(true);
        }
    }
}
