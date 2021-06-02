package util;

public class Tuple<X, Y> {
    public final X x;
    public Y y;

    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    public X getValue1() {
        return this.x;
    }
    public Y getValue2() {
        return this.y;
    }
    public void setValue2(Y y) {
        this.y = y;
    }

}