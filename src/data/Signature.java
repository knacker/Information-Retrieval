package data;

public class Signature {

    private final int F = 64;
    private final int D = 4;

    private int id;
    private int f;
    private int d;

    /**
     * if f and d are -1, then we will use the default values for f and d
     */
    public Signature(int id, int f, int d) {
        this.id = id;
        this.f = (f == -1) ? F : f;
        this.d = (d == -1) ? D : d;
    }
    public int calculateM() {
        int m = 0;

        return m;
    }
}
