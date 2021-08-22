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
import simpl.typing.PairType;
import simpl.typing.Type;
import simpl.typing.TypeVar;
import simpl.typing.Substitution;


public class snd extends FunValue {

    public snd(int varNum) {
        super(null, Symbol.symbol("tmpvar" + varNum), new Expr(){
            public TypeResult typecheck(TypeEnv E) throws TypeError{
                Type t = E.get(Symbol.symbol("tmpvar" + varNum));

                if(t instanceof PairType){
                    return TypeResult.of(((PairType)t).t1);
                }

                if(t instanceof TypeVar){
                    TypeVar a = new TypeVar(false);
                    TypeVar b = new TypeVar(false);
                    return TypeResult.of(Substitution.of((TypeVar)t, new PairType(a, b)), b);
                }

                throw new TypeError("the typecheck of fst is faulty!");
            }
    
            public Value eval(State s) throws RuntimeError{
                PairValue v = (PairValue)s.E.get(Symbol.symbol("tmpvar" + varNum));
                return v.v2;
            }});
    }
}
