import service.DocumentManager;

public class Main {
    public static void main(String[] args) {

        /* TODO
        * inputs für boolsche suche anpassen
        * invertierte liste fixen
        * */
        System.out.println("Launching...");

        DocumentManager dm = new DocumentManager();
        dm.handle();

        System.out.println("\nDone!");
    }
}