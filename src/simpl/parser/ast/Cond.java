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

public class Cond extends Expr {

    public Expr e1, e2, e3;

    public Cond(Expr e1, Expr e2, Expr e3) {
        this.e1 = e1;
        this.e2 = e2;
        this.e3 = e3;
    }

    public String toString() {
        return "(if " + e1 + " then " + e2 + " else " + e3 + ")";
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        TypeResult r1 = e1.typecheck(E);
        TypeResult r2 = e2.typecheck(E);
        TypeResult r3 = e3.typecheck(E);
        Substitution s = r1.s.compose(r2.s.compose(r3.s));
        Type t1 = s.apply(r1.t);
        Type t2 = s.apply(r2.t);
        Type t3 = s.apply(r3.t);

        if(t1 instanceof TypeVar){
            s = s.compose(Substitution.of((TypeVar)t1, Type.BOOL).compose(Type.genSubstitution(t2, t3)));
            s.apply(t2);
            s.apply(t3);
            return TypeResult.of(s, t2);
        }

        if(t1 == Type.BOOL){
            s = s.compose(Type.genSubstitution(t2, t3));
            s.apply(t2);
            s.apply(t3);
            return TypeResult.of(s, t2);
        }

        throw new TypeError("The typecheck of Cond is faulty!");
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        BoolValue v1 = (BoolValue)e1.eval(s);
        
        if(v1.b){
            return e2.eval(s);
        }else{
            return e3.eval(s);
        }
    }
}
