import service.DocumentManager;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        System.out.println("Launching...");

        DocumentManager dm = new DocumentManager();
        handle(dm);

        System.out.println("Done!");

    }

    //hier console eingaben handlen(gui quasi)
    public static void handle(DocumentManager dm) {
        dm.createDocuments();
        System.out.println("kongo");
    }
}
