package simpl.typing;

public final class ArrowType extends Type {

    public Type t1, t2;

    public ArrowType(Type t1, Type t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    @Override
    public boolean isEqualityType() {
        return false;
    }

    @Override
    public Substitution unify(Type t) throws TypeError {
        if(t instanceof TypeVar){
            return t.unify(this);
        }

        if(t instanceof ArrowType){
            Substitution a = this.t1.unify(((ArrowType)t).t1);
            Substitution b = this.t2.unify(((ArrowType)t).t2);
            return b.compose(a);
        }

        throw new TypeMismatchError();
    }

    @Override
    public boolean contains(TypeVar tv) {
        return t1.contains(tv) || t2.contains(tv);
    }

    @Override
    public Type replace(TypeVar a, Type t) {
        if(contains(a)){
            t1 = t1.replace(a, t);
            t2 = t2.replace(a, t);
        }

        return this;
    }

    public String toString() {
        return "(" + t1 + " -> " + t2 + ")";
    }
}
