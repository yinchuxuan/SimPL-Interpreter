package simpl.parser.ast;

import simpl.interpreter.RecValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.parser.Symbol;
import simpl.typing.Substitution;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;
import simpl.typing.TypeVar;

public class Rec extends Expr {

    public Symbol x;
    public Expr e;

    public Rec(Symbol x, Expr e) {
        this.x = x;
        this.e = e;
    }

    public String toString() {
        return "(rec " + x + "." + e + ")";
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        TypeVar a = new TypeVar(false);
        TypeEnv E1 = TypeEnv.of(E, x, a);
        TypeResult r = e.typecheck(E1);
        Type t = r.s.apply(r.t);

        return TypeResult.of(r.s.compose(Substitution.of(a, t)), t);
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        return new RecValue(s.E, x, e);
    }
}
