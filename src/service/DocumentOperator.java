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
        if (m == Model.BOOL) {
            foundDocs = linearSearch(docs, search, foundDocs);
        }
        return foundDocs;
    }

    public List<Document> filterWords(List<Document> docs, FilterList filterL) {

        List<Document> filteredDocs = new ArrayList<Document>();

        String content;

        //filter
        for (Document doc : docs) {

            int i = 0;
            content = doc.getContent();
            content = content.replaceAll("[.,;:\"!?\n]", "");
            content = content.toLowerCase();

            for (String filter : filterL.getList()) {
                content = content.replaceAll(" " + filter + " ", " ");
            }

            Document filteredDoc = new Document(i, doc.getName(), content);
            filteredDocs.add(filteredDoc);
            i++;

        }

        return filteredDocs;
    }

    public List<Document> linearSearch(List<Document> docs, List<String> search, List<Document> foundDocs) {
        for (Document doc : docs) {
            if (matchString(search, doc.getContent())) {
                foundDocs.add(doc);
            }
        }
        return foundDocs;
    }


    public void compareSignature() {

    }

    public void hash() {

    }

    private boolean matchString(List<String> searchTerms, String content) {
        if (searchTerms.size() == 0) {
            return true;
        }
        if (content.toLowerCase().contains(searchTerms.get(0).toLowerCase())) {
            if(true & matchString(searchTerms.subList(1, searchTerms.size()), content)) {
                return true;
            }
        } else {
            return false;
        }
        return false;
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
