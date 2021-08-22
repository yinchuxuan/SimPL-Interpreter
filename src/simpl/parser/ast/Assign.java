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

public class Assign extends BinaryExpr {

    public Assign(Expr l, Expr r) {
        super(l, r);
    }

    public String toString() {
        return l + " := " + r;
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        TypeResult r1 = l.typecheck(E);
        TypeResult r2 = r.typecheck(E);
        Substitution s = r1.s.compose(r2.s);
        Type t1 = s.apply(r1.t);
        Type t2 = s.apply(r2.t);

        if(t1 instanceof RefType){
            RefType ref = (RefType)t1;
            s = s.compose(Type.genSubstitution(ref.t, t2));
            return TypeResult.of(s, Type.UNIT);
        }

        if(t1 instanceof TypeVar){
            return TypeResult.of(s.compose(Substitution.of((TypeVar)t1, new RefType(t2))), Type.UNIT);
        }

        throw new TypeError("The typecheck of Assign is faulty!");
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        RefValue x = (RefValue)l.eval(s);
        Value y = r.eval(s);
        s.M.put(x.p, y);

        return Value.UNIT;
    }
}
