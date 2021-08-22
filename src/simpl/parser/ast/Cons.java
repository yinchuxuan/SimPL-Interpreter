package simpl.parser.ast;

import simpl.typing.Type;
import simpl.interpreter.ConsValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.typing.ListType;
import simpl.typing.Substitution;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;
import simpl.typing.TypeVar;

public class Cons extends BinaryExpr {

    public Cons(Expr l, Expr r) {
        super(l, r);
    }

    public String toString() {
        return "(" + l + " :: " + r + ")";
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        TypeResult r1 = l.typecheck(E);
        TypeResult r2 = r.typecheck(E);
        Substitution s = r1.s.compose(r2.s);
        Type t1 = s.apply(r1.t);
        Type t2 = s.apply(r2.t);

        if(r instanceof Nil){
            return TypeResult.of(s, new ListType(t1));
        }

        if(t2 instanceof ListType){
            ListType l = (ListType)t2;
            s = s.compose(Type.genSubstitution(t1, l.t));
            s.apply(t1);
            s.apply(l.t);
            return TypeResult.of(s, l);
        }

        if(t2 instanceof TypeVar){
            s.compose(Substitution.of((TypeVar)t2, new ListType(t1))).apply(t1);
            return TypeResult.of(s.compose(Substitution.of((TypeVar)t2, new ListType(t1))), new ListType(t1));
        }

        throw new TypeError("The typecheck of Cons is faulty!");
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        Value v1 = l.eval(s);
        Value v2 = r.eval(s);

        return new ConsValue(v1, v2);
    }
}
