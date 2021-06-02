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
            foundDocs = linearSearch(docs, search);
        }
        if (m == Model.INVERTED) {
            foundDocs = invertedSearch(docs, search);
        }
        return foundDocs;
    }


    public List<Document> filterWords(List<Document> docs, FilterList filterL) {

        List<Document> filteredDocs = new ArrayList<Document>();

        String content;

        // initialize document id
        int i = 0;

        //filter
        for (Document doc : docs) {

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

    public static List<Document> linearSearch(List<Document> docs, List<String> search) {
        List<Document> foundDocs = new ArrayList<>();

        for (Document doc : docs) {
            if (matchString(search, doc.getContent())) {
                foundDocs.add(doc);
            }
        }
        return foundDocs;
    }
    private List<Document> invertedSearch(List<Document> docs, List<String> search) {
        List<Document> foundDocs = new ArrayList<>();

        return foundDocs;
    }

    public void compareSignature() {

    }

    public void hash() {

    }

    private static boolean matchString(List<String> searchTerms, String content) {
        if (searchTerms.size() == 0) {
            return true;
        }
        if (content.toLowerCase().contains(" " + searchTerms.get(0).toLowerCase() + " ")) {
            if(matchString(searchTerms.subList(1, searchTerms.size()), content)) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }
    /*The \rules\ for removing a suffix will be given in the form

    (condition) S1 -> S2

    This means that if a word ends with the suffix S1, and the stem before S1
    satisfies the given condition, S1 is replaced by S2. The condition is
    usually given in terms of m, e.g. ....
    Ab Zeile 175 weiterlesen
     */
    public String removeSuffix(String word) {
        String newWord = "";
        return newWord;
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
