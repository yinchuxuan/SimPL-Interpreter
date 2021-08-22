package simpl.interpreter.lib;

import simpl.interpreter.ConsValue;
import simpl.interpreter.FunValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.parser.Symbol;
import simpl.parser.ast.Expr;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;
import simpl.typing.ListType;
import simpl.typing.Type;
import simpl.typing.Substitution;
import simpl.typing.TypeVar;

public class hd extends FunValue {

    public hd(int varNum) {
        super(null, Symbol.symbol("tmpvar" + varNum), new Expr(){
            public TypeResult typecheck(TypeEnv E) throws TypeError{
                Type t = E.get(Symbol.symbol("x"));

                if(t instanceof ListType){
                    return TypeResult.of(((ListType)t).t);
                }

                if(t instanceof TypeVar){
                    TypeVar a = new TypeVar(false);
                    return TypeResult.of(Substitution.of((TypeVar)t, new ListType(a)), a);
                }

                throw new TypeError("the typecheck of fst is faulty!");
            }
    
            public Value eval(State s) throws RuntimeError{
                ConsValue v = (ConsValue)s.E.get(Symbol.symbol("tmpvar" + varNum));
                return v.v1;
            }});
    }
}
