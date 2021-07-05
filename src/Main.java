import util.SignatureUtil;

import java.util.BitSet;

public class Main {
    public static void main(String[] args) {

        System.out.println("Launching...");

        //DocumentManager dm = new DocumentManager();
        //dm.handle();
        BitSet b1 = new BitSet();
        b1.set(2, 5, true);

        BitSet b2 = SignatureUtil.hashStrings("text");

        b1.and(b2);
        System.out.println(SignatureUtil.hashStrings("text").toString());


        System.out.println("\nDone!");
    }
}