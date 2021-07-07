package service;

import data.Document;
import data.DocumentSignature;
import data.FilterList;
import data.InvertedListObject;
import util.SignatureUtil;
import util.Tuple;
import util.WordListUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DocumentManager {

    //initialize stopword list and "grundstammreduktionslist", welche aber noch nicht vorhanden ist
    private FilterList sw = FilterList.createSW();

    //initialize doclist
    private List<Document> docs = new ArrayList<>();
    private List<InvertedListObject> invertedDocuments = new ArrayList<>();
    private List<DocumentSignature> documentSignatures = new ArrayList<>();
    DocumentOperator operator;

    private final int task_linear_search_original = 1;
    private final int task_linear_search_stopWords = 2;
    private final int task_linear_search_reduction = 3;
    private final int task_inverted_Search = 4;
    private final int task_signature_Search = 6;
    private final int task_save_docs = 7;
    private final int task_quit_program = 8;


    public DocumentManager() {
        operator = new DocumentOperator();
    }

    /**
     * handle user inputs here
     */
    public void handle() {
        boolean done = false;

        createDocuments();
        createInvertList();
        createDocumentSignatures();

        long timeStart = 0;
        long timeEnd = 0;
        long timeDiff = 0;

        List<Document> response = new ArrayList<>();
        double recall = 0;
        double precision = 0;

        while (!done) {
            int task = getTask();

            switch (task) {
                case task_linear_search_original -> {
                    List<String> searchTerm = getSearchTerm();
                    createDocumentSignatures();
                    timeStart = System.nanoTime();
                    response = DocumentOperator.linearSearch(docs, searchTerm);
                    timeEnd = System.nanoTime();
                    timeDiff = timeEnd - timeStart;
                    recall = DocumentOperator.calculateRecall(searchTerm, response);
                    precision = DocumentOperator.calculatePrecision(searchTerm, response);
                    printSearchResponse(response, recall, precision, timeDiff);
                }

                case task_linear_search_stopWords -> {
                    List<String> searchTerm = getSearchTerm();
                    List<Document> clearedDocs = operator.filterWords(docs, sw);
                    timeStart = System.nanoTime();
                    response = DocumentOperator.linearSearch(clearedDocs, searchTerm);
                    timeEnd = System.nanoTime();
                    timeDiff = timeEnd - timeStart;
                    recall = DocumentOperator.calculateRecall(searchTerm, response);
                    precision = DocumentOperator.calculatePrecision(searchTerm, response);
                    printSearchResponse(response, recall, precision, timeDiff);
                }

                case task_linear_search_reduction -> {
                    List<String> searchTerm = getSearchTerm();
                    timeStart = System.nanoTime();
                    List<Document> reducedDocs = DocumentOperator.stemming(docs);
                    response = DocumentOperator.linearSearch(reducedDocs, searchTerm);
                    timeEnd = System.nanoTime();
                    timeDiff = timeEnd - timeStart;
                    recall = DocumentOperator.calculateRecall(searchTerm, response);
                    precision = DocumentOperator.calculatePrecision(searchTerm, response);
                    printSearchResponse(response, recall, precision, timeDiff);
                }

                case task_inverted_Search ->  {
                    List<String> searchTerm = getSearchTerm();
                    timeStart = System.nanoTime();
                    response = DocumentOperator.invertedSearch(docs, invertedDocuments, searchTerm);
                    timeEnd = System.nanoTime();
                    timeDiff = timeEnd - timeStart;
                    recall = DocumentOperator.calculateRecall(searchTerm, response);
                    precision = DocumentOperator.calculatePrecision(searchTerm, response);
                    printSearchResponse(response, recall, precision, timeDiff);
                }
                case task_signature_Search ->  {
                    List<String> searchTerm = getSearchTerm();
                    timeStart = System.nanoTime();
                    response = DocumentOperator.searchSignatures(documentSignatures, searchTerm);
                    timeEnd = System.nanoTime();
                    timeDiff = timeEnd - timeStart;
                    recall = DocumentOperator.calculateRecall(searchTerm, response);
                    precision = DocumentOperator.calculatePrecision(searchTerm, response);
                    printSearchResponse(response, recall, precision, timeDiff);
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
    private void printSearchResponse(List<Document> response, double recall, double precision, long nanos) {
        if (response.isEmpty()) {
            System.out.println("\nDeine Suche hat keine relevanten Dokumente geliefert.");
        } else {
            System.out.println("\nDeine Suche hat folgende relevante Dokumente geliefert : ");

            for (Document doc: response) {
                System.out.println("\t" + doc.getName());
            }

            if (recall != 0 || precision != 0) {
                System.out.println("\nRecall : " + recall);
                System.out.println("Precision : " + precision);
            }

            double millis =  (double) nanos / 1000 / 1000;

            DecimalFormat formatter = new DecimalFormat("#.###");

            System.out.println("\nDas finden von " + response.size() + " Dokumenten hat " + formatter.format(millis) + " ms gedauert.");

        }

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
     *
     * @return
     */
    private List<String> getSearchTerm() {
        Scanner scanner = new Scanner(new InputStreamReader(System.in));

        System.out.print("Gebe einen Suchterm ein : ");

        String[] input = scanner.nextLine().split(" ");

        // if only 1 word input change it to our notation
        if (input.length == 1) {
            String word = input[0];
            input = new String[]{"&", "(" , word , ")"};
        }

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
        System.out.println(task_signature_Search + ". Suche auf Basis von Signaturen");
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
                    title = WordListUtil.removePrefixSpace(line);
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

    private void createDocumentSignatures() {
        List<Document> clearedDocs = operator.filterWords(docs, sw);
        for(Document doc : clearedDocs) {
            List<Tuple<String, Integer>> wordsCounted = WordListUtil.createWordList(doc);
            List<String> words = new ArrayList<>();
            for(Tuple<String, Integer> tuple : wordsCounted) {
                words.add(tuple.getValue1());
            }
            DocumentSignature data = new DocumentSignature(doc, SignatureUtil.blockHash(words));
            documentSignatures.add(data);
        }
    }

    private void createInvertList() {

        //list, which contains every word and its list of documents, which it is in

        for (Document doc : docs) {

            //create List of words of a document, which contains the number of occurences for each word
            List<Tuple<String, Integer>> wordsCounted = WordListUtil.createWordList(doc);

            for (int i = 0; i < wordsCounted.size(); i++) {
                List<Tuple<Integer, Integer>> tpList = new ArrayList<>();
                tpList.add(new Tuple<>(doc.getId(), wordsCounted.get(i).getValue2()));
                invertedDocuments.add(new InvertedListObject(wordsCounted.get(i).getValue1(), tpList));
            }
            //merge duplicates

            for (int i = 0; i < invertedDocuments.size(); i++) {
                for (int j = i + 1; j < invertedDocuments.size(); j++) {
                    if (invertedDocuments.get(i).getWord().equals(invertedDocuments.get(j).getWord())) {
                        invertedDocuments.get(i).addEntryIC(new Tuple(doc.getId(), invertedDocuments.get(j).getIdCount().get(0).getValue2()));
                        invertedDocuments.remove(j);
                    }
                }
            }
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
}
