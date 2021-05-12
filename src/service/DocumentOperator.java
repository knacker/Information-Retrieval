package service;

import data.Document;
import data.FilterList;
import data.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DocumentOperator {

    public DocumentOperator() {
    }

    public List<Document> searchDocuments(List<Document> docs, List<String> search, Model m) {
        List<Document> foundDocs = new ArrayList<Document>();

        return foundDocs;
    }


    public List<Document> filterWords(List<Document> docs, FilterList filterL) {

        List<Document> filteredDocs = new ArrayList<Document>();

        String content;

        //filter
        for (Document doc: docs) {

            int i = 0;
            content = doc.getContent();
            content.replaceAll("[.,;:\"!?]", "");
            content = content.toLowerCase();

            for(String filter : filterL.getList()) {
                content.replaceAll(filter, "");
            }

            Document filteredDoc = new Document(i, doc.getName(), content);
            filteredDocs.add(filteredDoc);
            i++;

        }

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
