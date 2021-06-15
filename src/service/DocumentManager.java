package service;

import data.Document;
import data.FilterList;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DocumentManager {

    //initialize stopword list and "grundstammreduktionslist", welche aber noch nicht vorhanden ist
    private FilterList sw = FilterList.createSW();

    //initialize doclist
    private List<Document> docs = new ArrayList<>();

    DocumentOperator operator;

    private final int task_linear_search_original = 1;
    private final int task_linear_search_stopWords = 2;
    private final int task_linear_search_reduction = 3;
    private final int task_inverted_Search = 4;
    private final int task_save_docs = 5;
    private final int task_quit_program = 6;

    public DocumentManager() {
        operator = new DocumentOperator();
    }

    /**
     * handle user inputs here
     */
    public void handle() {
        boolean done = false;

        createDocuments();

        List<Document> response = new ArrayList<>();
        double recall = 0;
        double precision = 0;

        while (!done) {
            int task = getTask();

            switch (task) {
                case task_linear_search_original -> {
                   // String searchWord = getSearchWord();
                    List<String> searchTerm = new ArrayList<>();
                   // searchTerm.add(searchWord);
                    String a = "&";
                    String b = "(";
                    String c = "he";
                    String d = "runs";
                    String e = ")";
                    searchTerm.add(a);
                    searchTerm.add(b);
                    searchTerm.add(c);
                    searchTerm.add(d);
                    searchTerm.add(e);

                    response = DocumentOperator.linearSearch(docs, searchTerm);
                    printSearchResponse(response, recall, precision);
                }

                case task_linear_search_stopWords -> {
                    //String searchWord = getSearchWord();
                    List<String> searchTerm = new ArrayList<>();
                    //searchTerm.add(searchWord);
                    String a = "&";
                    String b = "(";
                    String c = "he";
                    String d = "runs";
                    String e = ")";
                    searchTerm.add(a);
                    searchTerm.add(b);
                    searchTerm.add(c);
                    searchTerm.add(d);
                    searchTerm.add(e);

                    List<Document> clearedDocs = operator.filterWords(docs, sw);
                    response = DocumentOperator.linearSearch(clearedDocs, searchTerm);
                    printSearchResponse(response, recall, precision);
                }

                case task_linear_search_reduction -> {
                    List<String> searchTerm = getSearchTerm();
                    List<Document> reducedDocs = DocumentOperator.stemming(docs);
                    response = DocumentOperator.linearSearch(reducedDocs, searchTerm);
                    recall = DocumentOperator.calculateRecall("beast", response);
                    precision = DocumentOperator.calculatePrecision("beast", response);
                    printSearchResponse(response, recall, precision);
                }

                case task_inverted_Search ->  {
                    List<String> searchTerm = new ArrayList<>();

                    printSearchResponse(DocumentOperator.invertedSearch(docs, searchTerm), recall, precision);

                }

                case task_save_docs -> saveDocs();

                case task_quit_program -> done = true;
            }
        }
    }

    /**
     * print each document from response list
     * @param response relevant documents
     */
    private void printSearchResponse(List<Document> response, double recall, double precision) {
        System.out.println("Deine Suche hat folgende relevante Dokumente geliefert : ");
        for (Document doc: response) {
            System.out.println(doc.getName() + " " + doc.getId());
        }

        System.out.println("Recall : " + recall);
        System.out.println("Precision : " + precision);
    }

    /**
     * @return search input of the user
     */
    private String getSearchWord() {
        Scanner scanner = new Scanner(new InputStreamReader(System.in));

        System.out.print("Gebe ein Suchwort ein : ");

        return scanner.nextLine();
    }

    /**
     * new input: instead of &(fish) use: & ( fish )
     * @return
     */
    private List<String> getSearchTerm() {
        Scanner scanner = new Scanner(new InputStreamReader(System.in));

        System.out.print("Gebe einen Suchterm ein : ");

        String[] input = scanner.nextLine().split(" ");

        return new ArrayList<>(Arrays.asList(input));
    }

    /**
     * @return choosen task
     */
    private int getTask() {

        Scanner scanner = new Scanner(new InputStreamReader(System.in));

        System.out.println("\n\nInformation Retrieval System");
        System.out.println("==================");

        System.out.println(task_linear_search_original + ". Lineare Suche (Originaldokumente)");
        System.out.println(task_linear_search_stopWords + ". Lineare Suche (Stoppwort-freie Dokumente)");
        System.out.println(task_linear_search_reduction + ". Lineare Suche (auf Stammform reduzierte Dokumente)");
        System.out.println(task_inverted_Search + ". Suche auf Basis einer invertierten Liste");
        System.out.println(task_save_docs + ". Dokumente speichern");
        System.out.println(task_quit_program + ". Programm beenden");
        System.out.println();
        System.out.print("> ");

        int task = 0;

        try {
            task = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Fehlerhafte Eingabe " + e);
        }

        return task;
    }

    /**
     * create Documents from the data aesopa10.txt
     */
    public void createDocuments() {

        int i = 0;
        int blanklineCount = 3;

        String fileName;

        if (isWindows()) {
            fileName = ".\\res\\aesopa10.txt";
        } else {
            fileName = "./res/aesopa10.txt";
        }

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            for (int j = 0; j < 307; j++) {
                br.readLine();
            }

            String line = "";
            String title = "";
            String content = "";

            //start reading content
            while ((line = br.readLine()) != null) {

                if (blanklineCount == 3) {

                    if (!content.equals("")) {

                        Document doc = new Document(i, title, content);
                        docs.add(doc);

                        content = "";
                        title = "";

                        i++;
                    }
                    title = removePrefixSpace(line);
                    blanklineCount = 0;

                    continue;
                }
                if (!line.equals("")) blanklineCount = 0;

                if (blanklineCount < 3) {

                    if (line.equals("")) {
                        blanklineCount++;
                    }
                    content += " " + line + "\n";
                }
            }
            //add last document
            Document doc = new Document(i, title, content);
            docs.add(doc);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveDocs() {

        for (Document doc : docs) {

            String filename = doc.getName().toLowerCase() + ".txt";
            filename.strip();
            filename = filename.replaceAll("\\s", "_");

            String dirName;

            if (isWindows()) {
                dirName = ".\\saved_documents\\" + filename;
            } else {
                dirName = "./saved_documents/" + filename;
            }

            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(dirName));

                bw.write(doc.getName() + "\n");
                bw.write(doc.getContent());

                bw.flush();
                bw.close();

            } catch (IOException e) {
                // if failed then create directory and run saveDocs again
                String newDirName;

                if (isWindows()) {
                    newDirName = ".\\saved_documents";
                } else {
                    newDirName = "./saved_documents";
                }

                new File(newDirName).mkdirs();

                saveDocs();
            }

        }
    }

    public static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();

        return (os.contains("win"));
    }

    public boolean loadDocs() {

        if (!docs.isEmpty()) {
            System.out.println("Documents already loaded!");
            return false;
        } else {
            Path dir = Paths.get(".\\saved_documents");
            try {
                Files.walk(dir).forEach(path -> {
                    try {
                        loadFile(path.toFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        //if successful
        if (!docs.isEmpty()) {
            System.out.println("Documents loaded!");
            return true;
        }
        return false;
    }

    public void loadFile(File file) throws IOException {

        if (!file.isDirectory()) {
            String title = "";
            String content = "";
            String line = "";

            try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {

                title = br.readLine();

                while ((line = br.readLine()) != null) {
                    content += line;

                }
                Document doc = new Document(docs.size(), title, content);
                docs.add(doc);
            }
        } else {
            return;
        }
    }

    private String removePrefixSpace(String name) {
        String newName = name;
        int i = 0;

        while (name.charAt(i) == ' ') {
            i++;
            newName = name.substring(i);
        }

        return newName;
    }
}
