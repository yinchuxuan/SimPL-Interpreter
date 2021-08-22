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

public class Not extends UnaryExpr {

    public Not(Expr e) {
        super(e);
    }

    public String toString() {
        return "(not " + e + ")";
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        TypeResult r = e.typecheck(E);
        Substitution s = r.s;
        Type t = s.apply(r.t);

        if(t == Type.BOOL){
            return TypeResult.of(s, Type.BOOL);
        }

        if(t instanceof TypeVar){
            return TypeResult.of(s.compose(Substitution.of((TypeVar)t, Type.BOOL)), Type.BOOL);
        }

        throw new TypeError("The typecheck of Not is faulty!");
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        BoolValue v = (BoolValue)e.eval(s);

        if(v.b){
            return new BoolValue(false);
        }else{
            return new BoolValue(true);
        }
    }
}
