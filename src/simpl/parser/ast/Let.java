package simpl.parser.ast;

import simpl.interpreter.Env;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.parser.Symbol;
import simpl.typing.Substitution;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;
import simpl.typing.Type;

public class Let extends Expr {

    public Symbol x;
    public Expr e1, e2;

    public Let(Symbol x, Expr e1, Expr e2) {
        this.x = x;
        this.e1 = e1;
        this.e2 = e2;
    }

    public String toString() {
        return "(let " + x + " = " + e1 + " in " + e2 + ")";
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        TypeResult r1 = e1.typecheck(E);
        Type t1 = r1.s.apply(r1.t);
        TypeEnv E1 = TypeEnv.of(E, x, t1);
        TypeResult r2 = e2.typecheck(E1);
        Substitution s = r1.s.compose(r2.s);
        Type t2 = s.apply(r2.t);

        return TypeResult.of(s, t2);
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        Value v1 = e1.eval(s);
        return e2.eval(State.of(new Env(s.E, x, v1), s.M, s.p));
    }
}
