package service;

import data.Document;
import data.FilterList;
import data.Model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DocumentManager {

    //initialize stopword list and "grundstammreduktionslist", welche aber noch nicht vorhanden ist
    private FilterList sw = FilterList.createSW();

    //initialize doclist
    private List<Document> docs = new ArrayList<Document>();

    DocumentOperator operator;

    public DocumentManager() {
        operator = new DocumentOperator();
    }

   //hier eingaben handlen
    public void handle() {
        List<String> testi = new ArrayList<>();
        List<Document> ergebnis = new ArrayList<>();
        testi.add("wolf");
        createDocuments();
        operator.linearSearch(docs, testi, ergebnis);
        System.out.println(ergebnis);
        //saveDocs();
    }

    public void createDocuments() {

        int i = 0;
        int blanklineCount = 3;

        try (BufferedReader br = new BufferedReader(new FileReader(".\\res\\aesopa10.txt"))) {

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
                    title = line;
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

    public boolean saveDocs() {

        int i = 0;
        for (Document doc : docs) {

            String filename = docs.get(i).getName().toLowerCase().substring(2) + ".txt";
            filename.strip();
            filename = filename.replaceAll("\\s", "_");

            try {

                BufferedWriter bw = new BufferedWriter(new FileWriter(".\\saved_documents\\" + filename));
                bw.write(docs.get(i).getName() + "\n");
                bw.write(docs.get(i).getContent());

                bw.flush();
                bw.close();

                i++;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return true;
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
