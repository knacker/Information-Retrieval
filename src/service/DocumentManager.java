package service;

import data.Document;
import data.FilterList;
import data.Model;

import javax.print.Doc;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DocumentManager {

    //initialize stopword list and "grundstammreduktionslist", welche aber noch nicht vorhanden ist
    private FilterList sw = FilterList.createSW();
    //private FilterList re = FilterList.createRE();

    //initialize doclist
    private List<Document> docs = new ArrayList<Document>();

    public DocumentManager() {
    }

    public void createDocuments() {

        File file = new File(".\\res\\aesopa10.txt");

        try {
            int i = 0;
            Scanner sc = new Scanner(file);
            while(sc.hasNextLine()) {

                String title = "";
                String content = "";

                sc.nextLine();
                sc.nextLine();
                sc.nextLine();

                title = sc.nextLine();

                sc.nextLine();
                sc.nextLine();

                while(sc.nextLine() != "") {
                    content += sc.nextLine();
                }

                Document doc = new Document(i, title, content);
                docs.add(doc);

                i++;
            }
            sc.close();
        }
        catch (FileNotFoundException e) {
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
