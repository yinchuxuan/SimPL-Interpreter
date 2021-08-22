package simpl.interpreter.pcf;

import simpl.interpreter.FunValue;
import simpl.interpreter.IntValue;
import simpl.interpreter.RuntimeError;
import simpl.interpreter.State;
import simpl.interpreter.Value;
import simpl.parser.Symbol;
import simpl.parser.ast.Expr;
import simpl.typing.TypeEnv;
import simpl.typing.TypeError;
import simpl.typing.TypeResult;
import simpl.typing.Type;
import simpl.typing.TypeVar;
import simpl.typing.Substitution;

public class pred extends FunValue {

    public pred(int varNum) {
        super(null, Symbol.symbol("tmpvar" + varNum), new Expr(){
            public TypeResult typecheck(TypeEnv E) throws TypeError{
                Type t = E.get(Symbol.symbol("tmpvar" + varNum));

                if(t == Type.INT){
                    return TypeResult.of(Type.INT);
                }

                if(t instanceof TypeVar){
                    return TypeResult.of(Substitution.of((TypeVar)t, Type.INT), Type.INT);
                }

                throw new TypeError("the typecheck of fst is faulty!");
            }
    
            public Value eval(State s) throws RuntimeError{
                IntValue v = (IntValue)s.E.get(Symbol.symbol("tmpvar" + varNum));
                return new IntValue(v.n - 1);
            }});
    }
}
