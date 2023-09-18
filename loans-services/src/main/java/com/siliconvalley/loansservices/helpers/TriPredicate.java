package com.siliconvalley.loansservices.helpers;

@FunctionalInterface
public interface TriPredicate<T,U,V> {
    boolean test(T t,U u,V v);
}
