package service;

import data.Document;
import data.FilterList;
import data.Model;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class DocumentManager {

    //initialize stopword list and "grundstammreduktionslist", welche aber noch nicht vorhanden ist
    private FilterList sw = FilterList.createSW();
    //private FilterList re = FilterList.createRE();

    //initialize doclist
    private List<Document> docs = null;

    public DocumentManager() {
    }

    public void createDocuments(File txt) {

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
