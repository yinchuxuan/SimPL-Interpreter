package simpl.interpreter.pcf;

import simpl.interpreter.BoolValue;
import simpl.interpreter.FunValue;
import simpl.interpreter.IntValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.parser.Symbol;
import simpl.parser.ast.Expr;
import simpl.typing.Substitution;
import simpl.typing.Type;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;
import simpl.typing.TypeVar;

public class iszero extends FunValue {

    public iszero(int varNum) {
        super(null, Symbol.symbol("tmpvar" + varNum), new Expr(){
            public TypeResult typecheck(TypeEnv E) throws TypeError{
                Type t = E.get(Symbol.symbol("tmpvar" + varNum));

                if(t == Type.INT){
                    return TypeResult.of(Type.BOOL);
                }

                if(t instanceof TypeVar){
                    return TypeResult.of(Substitution.of((TypeVar)t, Type.INT), Type.BOOL);
                }

                throw new TypeError("the typecheck of fst is faulty!");
            }
    
            public Value eval(State s) throws RuntimeError{
                IntValue v = (IntValue)s.E.get(Symbol.symbol("tmpvar" + varNum));
                return new BoolValue(v.n == 0);
            }});
    }
}
