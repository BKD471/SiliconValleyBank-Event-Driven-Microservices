package com.siliconvalley.loansservices.helpers;

@FunctionalInterface
public interface TriPredicateCustom<T,U,V> {
    boolean test(T t,U u,V v);

    default TriPredicateCustom<T,U,V> negate(){
        TriPredicateCustom<T,U,V> predicate=(t,u,v) -> !test(t,u,v);
        return predicate;
    }

    default TriPredicateCustom<T,U,V> and(TriPredicateCustom<T,U,V> tp){
        TriPredicateCustom<T,U,V> predicate=(t,u,v) -> tp.test(t,u,v) && test(t,u,v);
        return predicate;
    }

    default TriPredicateCustom<T,U,V> or(TriPredicateCustom<T,U,V> tp){
        TriPredicateCustom<T,U,V> predicate=(t,u,v) -> tp.test(t,u,v) || test(t,u,v);
        return predicate;
    }
}
