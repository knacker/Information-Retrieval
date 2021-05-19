import service.DocumentManager;

/**
 * TODOS:
 *  - Ordner "saved_documents" muss erstellt werden, falls er nicht vorhanden ist
 *  - User-Inputs in handle() ermöglichen
 *  - 3.2
 *  - eventuell createDocuments() überarbeiten
 */

public class Main {
    public static void main(String[] args) {

        System.out.println("Launching...");

        DocumentManager dm = new DocumentManager();
        dm.handle();

        System.out.println("\nDone!");

    }
}