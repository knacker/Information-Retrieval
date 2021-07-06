import service.DocumentManager;
import util.SignatureUtil;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        System.out.println("Launching...");

        DocumentManager dm = new DocumentManager();
        dm.handle();

        System.out.println("\nDone!");
    }
}