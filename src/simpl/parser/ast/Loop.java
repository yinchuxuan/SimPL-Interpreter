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

public class Loop extends Expr {

    public Expr e1, e2;

    public Loop(Expr e1, Expr e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public String toString() {
        return "(while " + e1 + " do " + e2 + ")";
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        TypeResult r1 = e1.typecheck(E);
        TypeResult r2 = e2.typecheck(E);
        Substitution s = r1.s.compose(r2.s);
        Type t1 = s.apply(r1.t);
        Type t2 = s.apply(r2.t);

        if(t1 == Type.BOOL){
            return TypeResult.of(s, t2);
        }

        if(t1 instanceof TypeVar){
            return TypeResult.of(s.compose(Substitution.of((TypeVar)t1, Type.BOOL)), t2);
        }

        throw new TypeError("The typecheck of Loop is faulty!");
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        BoolValue v1 = (BoolValue)e1.eval(s);

        if(v1.b){
            e2.eval(s);
            Loop l = new Loop(e1, e2);
            l.eval(s);

            return Value.UNIT;
        }else{
            return Value.UNIT;
        }
    }
}
