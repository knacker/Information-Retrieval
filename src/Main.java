import service.DocumentManager;
import util.Stemmer;

public class Main {
    public static void main(String[] args) {

        System.out.println("Launching...");

        DocumentManager dm = new DocumentManager();
        // dm.handle();

        Stemmer.testPorter();

        System.out.println("\nDone!");
    }


}