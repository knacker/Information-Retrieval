import service.DocumentManager;
import service.DocumentOperator;

public class Main {
    public static void main(String[] args) {

        System.out.println("Launching...");

        DocumentManager dm = new DocumentManager();
        dm.handle();
/*Ü
        String[] test_step1a = {"caresses", "ponies", "ties" , "caress", "cats"};

        for (String s : test_step1a) {
            System.out.println(s + " -> " + DocumentOperator.step1a(s));
        }
*/
        System.out.println("\nDone!");

    }
}