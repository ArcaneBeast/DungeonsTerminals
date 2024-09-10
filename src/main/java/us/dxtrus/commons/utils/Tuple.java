package us.dxtrus.commons.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * An immutable pair of objects.
 *
 * @param <A> object 1
 * @param <B> object 2
 */
@Getter
@AllArgsConstructor
public class Tuple<A, B> {
    private final A a;
    private final B b;

    public A getFirst() {
        return a;
    }

    public B getSecond() {
        return b;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Tuple<?,?> tuple)) return false;
        return tuple.getA().equals(this.getA()) && tuple.getB().equals(this.getB());
    }

    @Override
    public int hashCode() {
        return a.hashCode() + b.hashCode();
    }
}
