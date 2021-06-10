import service.DocumentManager;
import service.DocumentOperator;
import util.Stemmer;

public class Main {
    public static void main(String[] args) {

        System.out.println("Launching...");

        DocumentManager dm = new DocumentManager();
        // dm.handle();

        testPorter();

        System.out.println("\nDone!");
    }

    private static void testPorter() {
        String[] test_step1a = {"caresses", "ponies", "ties" , "caress", "cats"};

        /**

        System.out.println("Step 1a");

        for (String s : test_step1a) {
            System.out.println("\t" + s + " -> " + Stemmer.step1a(s));
        }

        System.out.println();
        System.out.println("Step 1b");

        String[] test_step1b = {"feed", "agreed" , "plastered" , "bled" , "motoring" , "sing"};

        for (String s : test_step1b) {
            System.out.println("\t" + s + " -> " + Stemmer.step1b(s));
        }

        System.out.println();

        String[] test_step1b2 = {"conflated" , "troubled" , "sized" , "hopping" , "tanned" , "falling" , "hissing" , "fizzed" , "failing" , "filing"};

        for (String s : test_step1b2) {
            System.out.println("\t" + s + " -> " + Stemmer.step1b(s));
        }

        System.out.println();
        System.out.println("Step 1c");

        String[] test_step1c = {"happy", "sky"};

        for (String s : test_step1c) {
            System.out.println("\t" + s + " -> " + Stemmer.step1c(s));
        }

        */

        System.out.println();
        System.out.println("Step 2");

        String[] test_step2 = {"relational" , "conditional" , "rational"};

        for (String s : test_step2) {
            System.out.println("\t" + s + " -> " + Stemmer.step2(s) + " " + Stemmer.convertToCVForm(Stemmer.step2(s)));
        }

         /**

        System.out.println();
        System.out.println("Step 3");


        String[] test_step3 = {"triplicate", "formative" , "formalize" , "electriciti" , "electrical" , "hopeful" , "goodness"};

        for (String s : test_step3) {
            System.out.println("\t" + s + " -> " + Stemmer.step3(s));
        }

        System.out.println();
        System.out.println("Step 4");

        String[] test_step4 = {"revival", "allowance" , "inference" , "airliner" , "gyroscopic" , "adjustable" , "defensible" , "irritant" , "replacement" , "adjustment" , "dependent" , "adoption" , "homologou" , "communism" , "activate" , "angulariti" , "homologous" , "effective" , "bowdlerize"};

        for (String s : test_step4) {
            System.out.println("\t" + s + " -> " + Stemmer.step4(s));
        }

         */

        System.out.println();
        System.out.println("Step 5a");

        String[] test_step5a = {"probate", "rate" , "cease"};

        for (String s : test_step5a) {
            System.out.println("\t" + s + " -> " + Stemmer.step5a(s));
        }

        System.out.println();
        System.out.println("Step 5b");

        String[] test_step5b = {"controll", "roll"};

        for (String s : test_step5b) {
            System.out.println("\t" + s + " -> " + Stemmer.step5b(s));
        }
    }
}