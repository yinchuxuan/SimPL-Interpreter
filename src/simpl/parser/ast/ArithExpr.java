package simpl.parser.ast;

import simpl.typing.Substitution;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;
import simpl.typing.TypeVar;

public abstract class ArithExpr extends BinaryExpr {

    public ArithExpr(Expr l, Expr r) {
        super(l, r);
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        TypeResult r1 = l.typecheck(E);
        TypeResult r2 = r.typecheck(E);
        Substitution s = r1.s.compose(r2.s);
        Type t1 = s.apply(r1.t);
        Type t2 = s.apply(r2.t);

        if(t1 == Type.INT && t2 == Type.INT){
            return TypeResult.of(s, Type.INT);
        }

        if(t1 instanceof TypeVar && t2 == Type.INT){
            return TypeResult.of(s.compose(Substitution.of((TypeVar)t1, Type.INT)), Type.INT);
        }

        if(t2 instanceof TypeVar && t1 == Type.INT){
            return TypeResult.of(s.compose(Substitution.of((TypeVar)t2, Type.INT)), Type.INT);
        }

        if(t1 instanceof TypeVar && t2 instanceof TypeVar){
            return TypeResult.of(s.compose(Substitution.of((TypeVar)t1, Type.INT).compose(Substitution.of((TypeVar)t2, Type.INT))), Type.INT);
        }

        throw new TypeError("The typecheck of ArithExpr is faulty!");
    }
}
