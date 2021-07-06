import util.SignatureUtil;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        System.out.println("Launching...");

        //DocumentManager dm = new DocumentManager();
        //dm.handle();
        List<String> ls = new ArrayList<>();
        ls.add("döner");
        ls.add("box");
        SignatureUtil.hashStrings(ls, 2);
        System.out.println("\nDone!");
    }
}