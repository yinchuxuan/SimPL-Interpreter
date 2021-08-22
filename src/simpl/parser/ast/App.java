package simpl.parser.ast;

import simpl.interpreter.Env;
import simpl.interpreter.FunValue;
import simpl.interpreter.RecValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.typing.ArrowType;
import simpl.typing.Substitution;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;
import simpl.typing.TypeVar;

public class App extends BinaryExpr {

    public App(Expr l, Expr r) {
        super(l, r);
    }

    public String toString() {
        return "(" + l + " " + r + ")";
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        TypeResult r1 = l.typecheck(E);
        TypeResult r2 = r.typecheck(E);
        Substitution s = r1.s.compose(r2.s);
        Type t1 = s.apply(r1.t);
        Type t2 = s.apply(r2.t);

        if(t1 instanceof ArrowType){
            ArrowType funcType = (ArrowType)t1;
            Type paraType = t2;
            s = s.compose(Type.genSubstitution(funcType.t1, paraType));
            s.apply(funcType);
            return TypeResult.of(s, funcType.t2);
        }

        if(t1 instanceof TypeVar){
            TypeVar a = new TypeVar(false);
            return TypeResult.of(s.compose(Substitution.of((TypeVar)t1, new ArrowType(t2, a))), a);
        }

        throw new TypeError("The typecheck of App is faulty!");
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        Value v = l.eval(s);

        if(v instanceof RecValue){
            RecValue rec = (RecValue)v;
            FunValue f = (FunValue)rec.e.eval(s);
            Value pa = r.eval(s);
            Env e1 = new Env(rec.E, rec.x, rec);
            Env e2 = new Env(e1, f.x, pa);
            return f.e.eval(State.of(e2, s.M, s.p));
        }
        
        if(v instanceof FunValue){
            FunValue f = (FunValue)v;
            Value pa = r.eval(s);
            Env e = new Env(f.E, f.x, pa);
            return f.e.eval(State.of(e, s.M, s.p));
        }

        throw new RuntimeError("The eval of App is faulty!");
    }
}
