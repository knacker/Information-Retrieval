package service;

import data.Document;
import data.FilterList;
import data.Model;

import java.util.ArrayList;
import java.util.List;

public class DocumentOperator {

    public DocumentOperator() {
    }

    public List<Document> searchDocuments(List<Document> docs, List<String> search, Model m) {
        List<Document> foundDocs = new ArrayList<Document>();

        return foundDocs;
    }


    public List<Document> filterWords(List<Document> docs, FilterList filter) {

        List<Document> filteredDocs = new ArrayList<Document>();
        //filtert irgendwas irgendwie
        return filteredDocs;
    }



    public void linearSearch() {

    }

    public void compareSignature() {

    }

    public void hash() {

    }

    public void matchString() {

    }

    public void invertList() {

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
