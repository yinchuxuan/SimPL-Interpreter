package simpl.parser.ast;

import simpl.interpreter.RefValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.typing.RefType;
import simpl.typing.Substitution;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;
import simpl.typing.TypeVar;

public class Deref extends UnaryExpr {

    public Deref(Expr e) {
        super(e);
    }

    public String toString() {
        return "!" + e;
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        TypeResult r = e.typecheck(E);
        Substitution s = r.s;
        Type t = s.apply(r.t);

        if(t instanceof RefType){
            return TypeResult.of(s, ((RefType)t).t);
        }

        if(t instanceof TypeVar){
            TypeVar a = new TypeVar(false);
            return TypeResult.of(s.compose(Substitution.of((TypeVar)t, new RefType(a))), a);
        }

        throw new TypeError("The typecheck of Deref is faulty!");
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        RefValue r = (RefValue)e.eval(s);
        return s.M.get(r.p);
    }
}
