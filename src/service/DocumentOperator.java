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
            content.replaceAll("[.,;:\"!?\n]", "");
            content = content.toLowerCase();

            for (String filter : filterL.getList()) {
                content.replaceAll(filter, "");
            }

            Document filteredDoc = new Document(i, doc.getName(), content);
            filteredDocs.add(filteredDoc);
            i++;

        }

        return filteredDocs;
    }

    private List<Document> linearSearch(List<Document> docs, List<String> search, List<Document> foundDocs) {
        for (Document doc : docs) {
            if (matchString(search, doc)) {
                foundDocs.add(doc);
            }
        }
        return foundDocs;
    }


    public void compareSignature() {

    }

    public void hash() {

    }

    public boolean matchString(List<String> searchTerms, Document doc) {
        if (searchTerms.size() == 0) {
            return true;
        }
        if (doc.getContent().contains(searchTerms.get(0))) {
            matchString(searchTerms.subList(0, searchTerms.size()), doc);
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
