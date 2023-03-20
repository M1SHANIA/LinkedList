package Zasobnik;

import DoubleList.LinkedListException;

public interface IAbstrLifo<T> {

    void zrus();

    boolean jePrazdny();

    void vloz(T data);

    T odeber() throws LinkedListException;
}
