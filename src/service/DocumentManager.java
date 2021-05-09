package service;

import data.Document;
import data.FilterList;
import data.Model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentManager {

    enum State {
        READ_CONTENT, READ_TITLE
    }
    //initialize stopword list and "grundstammreduktionslist", welche aber noch nicht vorhanden ist
    private FilterList sw = FilterList.createSW();
    //private FilterList re = FilterList.createRE();

    //initialize doclist
    private List<Document> docs = new ArrayList<Document>();

    public DocumentManager() {
    }

    public void createDocuments() {

        int i = 0;
        int blanklineCount = 3;
       // File file = new File(".\\res\\aesopa10.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(".\\res\\aesopa10.txt"))) {

            for (int j = 0; j < 307; j++) {
                br.readLine();
            }
            
            String line = "";
            String title = "";
            String content = "";

            //start reading content
            while((line = br.readLine()) != null) {

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
                    content += line;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Document> searchDocuments(List<String> search, Model.modelType m) {
        return docs;
    }

    public boolean saveDocs(String path) {

        //if successful
        return true;
    }

    public boolean loadDocs(String path) {

        //if successful
        return true;
    }

    public double calculateRecall() {
        double recall = 0;

        return recall;
    }

    public double calculatePrecision() {
        double precision = 0;

        return precision;
    }
}
