package simpl.parser.ast;

import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.parser.Symbol;
import simpl.typing.ArrowType;
import simpl.typing.ListType;
import simpl.typing.PairType;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;
import simpl.typing.TypeVar;
import simpl.interpreter.lib.hd;
import simpl.interpreter.lib.tl;
import simpl.interpreter.lib.fst;
import simpl.interpreter.lib.snd;
import simpl.interpreter.pcf.iszero;
import simpl.interpreter.pcf.pred;
import simpl.interpreter.pcf.succ;

public class Name extends Expr {

    public Symbol x;

    private static int varcnt = 0;

    public Name(Symbol x) {
        this.x = x;
    }

    public String toString() {
        return "" + x;
    }

    @Override
    public TypeResult typecheck(TypeEnv E) throws TypeError {
        if(x.is("fst")){
            TypeVar a = new TypeVar(false);
            TypeVar b = new TypeVar(false);
            return TypeResult.of(new ArrowType(new PairType(a, b), a));
        }

        if(x.is("snd")){
            TypeVar a = new TypeVar(false);
            TypeVar b = new TypeVar(false);
            return TypeResult.of(new ArrowType(new PairType(a, b), b));
        }

        if(x.is("tl") || x.is("hd")){
            TypeVar a = new TypeVar(false);
            return TypeResult.of(new ArrowType(new ListType(a), a));
        }

        if(x.is("iszero")){
            return TypeResult.of(new ArrowType(Type.INT, Type.BOOL));
        }

        if(x.is("pred") || x.is("succ")){
            return TypeResult.of(new ArrowType(Type.INT, Type.INT));
        }

        return TypeResult.of(E.get(x));
    }

    @Override
    public Value eval(State s) throws RuntimeError {
        if(x.is("fst")){
            return new fst(varcnt++);
        }

        if(x.is("snd")){
            return new snd(varcnt++);
        }

        if(x.is("hd")){
            return new hd(varcnt++);
        }

        if(x.is("tl")){
            return new tl(varcnt++);
        }

        if(x.is("iszero")){
            return new iszero(varcnt++);
        }

        if(x.is("pred")){
            return new pred(varcnt++);
        }

        if(x.is("succ")){
            return new succ(varcnt++);
        }

        return s.E.get(x);
    }
}
