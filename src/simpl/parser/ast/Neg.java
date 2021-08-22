package simpl.parser.ast;

import simpl.interpreter.IntValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.typing.Substitution;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;
import simpl.typing.TypeVar;

public class Neg extends UnaryExpr {

    public Neg(Expr e) {
        super(e);
    }

    public String toString() {
        return "~" + e;
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        TypeResult r = e.typecheck(E);
        Substitution s = r.s;
        Type t = s.apply(r.t);

        if(t == Type.INT){
            return TypeResult.of(s, Type.INT);
        }

        if(t instanceof TypeVar){
            return TypeResult.of(s.compose(Substitution.of((TypeVar)t, Type.INT)), Type.INT);
        }

        throw new TypeError("The typecheck of Neg is faulty!");
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        IntValue a = (IntValue)e.eval(s);
        return new IntValue(- a.n);
    }
}
