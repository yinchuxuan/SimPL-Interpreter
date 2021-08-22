package simpl.typing;

public abstract class Type {

    public abstract boolean isEqualityType();

    public abstract Type replace(TypeVar a, Type t);

    public abstract boolean contains(TypeVar tv);

    public abstract Substitution unify(Type t) throws TypeError;

    public static final Type INT = new IntType();
    public static final Type BOOL = new BoolType();
    public static final Type UNIT = new UnitType();

    public static Substitution genSubstitution(Type a, Type b) throws TypeError{
        if(a instanceof ListType && b instanceof ListType){
            return genSubstitution(((ListType)a).t, ((ListType)b).t);
        }

        if(a instanceof RefType && b instanceof RefType){
            return genSubstitution(((RefType)a).t, ((RefType)b).t);
        }

        if(a instanceof ArrowType && b instanceof ArrowType){
            return genSubstitution(((ArrowType)a).t1, ((ArrowType)b).t1).compose(genSubstitution(((ArrowType)a).t2, ((ArrowType)b).t2));
        }

        if(a instanceof PairType && b instanceof PairType){
            return genSubstitution(((PairType)a).t1, ((PairType)b).t1).compose(genSubstitution(((PairType)a).t2, ((PairType)b).t2));
        }

        if(a instanceof TypeVar){
            return Substitution.of((TypeVar)a, b);
        }

        if(b instanceof TypeVar){
            return Substitution.of((TypeVar)b, a);
        }

        if(a == b){
            return Substitution.IDENTITY;
        }

        throw new TypeError("the generation of Substitution is faulty!");
    }
}
