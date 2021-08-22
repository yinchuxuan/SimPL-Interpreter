package simpl.interpreter.lib;

import simpl.interpreter.FunValue;
import simpl.interpreter.PairValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.parser.Symbol;
import simpl.parser.ast.Expr;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;
import simpl.typing.TypeVar;
import simpl.typing.PairType;
import simpl.typing.Substitution;
import simpl.typing.Type;

public class fst extends FunValue {

    public fst(int varNum) {
        super(null, Symbol.symbol("tmpVar" + varNum), new Expr(){
            public TypeResult typecheck(TypeEnv E) throws TypeError{
                Type t = E.get(Symbol.symbol("tmpVar" + varNum));

                if(t instanceof PairType){
                    return TypeResult.of(((PairType)t).t1);
                }

                if(t instanceof TypeVar){
                    TypeVar a = new TypeVar(false);
                    TypeVar b = new TypeVar(false);
                    return TypeResult.of(Substitution.of((TypeVar)t, new PairType(a, b)), a);
                }

                throw new TypeError("the typecheck of fst is faulty!");
            }
    
            public Value eval(State s) throws RuntimeError{
                PairValue v = (PairValue)s.E.get(Symbol.symbol("tmpVar" + varNum));
                return v.v1;
            }});
    }
}
