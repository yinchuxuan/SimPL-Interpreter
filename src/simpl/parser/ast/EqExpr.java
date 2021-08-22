package simpl.parser.ast;

import simpl.typing.Substitution;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;

public abstract class EqExpr extends BinaryExpr {

    public EqExpr(Expr l, Expr r) {
        super(l, r);
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        TypeResult r1 = l.typecheck(E);
        TypeResult r2 = r.typecheck(E);
        Substitution s = r1.s.compose(r2.s);
        Type t1 = s.apply(r1.t);
        Type t2 = s.apply(r2.t);
        s = s.compose(Type.genSubstitution(t1, t2));
        return TypeResult.of(s, Type.BOOL);
    }
}
