package service;

import data.Document;
import data.FilterList;
import data.InvertedListObject;
import data.Model;
import util.Parser;
import util.Stemmer;
import util.Tuple;
import util.WordListUtil;

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
                content = content.replaceAll(" " + filter + " ", "");
            }

            Document filteredDoc = new Document(i, doc.getName(), content);
            filteredDocs.add(filteredDoc);
            i++;

        }

        return filteredDocs;
    }

    public static List<Document> linearSearch(List<Document> docs, List<String> search) {
        List<Document> foundDocs = new ArrayList<>();
        Parser pars = new Parser();

        for (Document doc : docs) {
            if (pars.evalExpression(search, doc.getContent())) {
                foundDocs.add(doc);
            }
        }
        return foundDocs;
    }
    private List<Document> invertedSearch(List<Document> docs, List<String> search) {

        List<InvertedListObject> invertedDocuments = createInvertList(docs);
        List<Document> foundDocs = new ArrayList<>();

        for(InvertedListObject obj : invertedDocuments) {
            if(search.equals(obj.getWord())) {
                //add every doc associated with the string
                for(Tuple<Integer, Integer> idCount : obj.getIdCount()) {
                    foundDocs.add(docs.get(idCount.getValue1()));
                }

            }
        }

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

    public static String removeSuffix(String statement) {
        return Stemmer.applyRules(statement);
    }


    public List<InvertedListObject> createInvertList(List<Document> docs) {

        //list, which contains every word and its list of documents, which it is in
        List<InvertedListObject> invertDocs= new ArrayList<>();
        for(Document doc : docs) {

            //create List of words of a document, which contains the number of occurences for each word
            List<Tuple<String, Integer>> wordsCounted = WordListUtil.createWordList(doc);

            for(int i = 0; i < invertDocs.size(); i++) {
                for(int j = 0; j < wordsCounted.size(); i++) {
                    //if entry of a word already exists, add a new pair( Document id, Number of occurences)
                    if(invertDocs.get(i).getWord().equals(wordsCounted.get(j).getValue1())) {
                        invertDocs.get(i).addEntryIC(new Tuple(doc.getId(), wordsCounted.get(j).getValue2()));
                    } else {
                        //if no entry exists, create a new list of ids and counts of the word for each document
                        List<Tuple<Integer, Integer>> tpList = new ArrayList<>();
                        tpList.add(new Tuple<>(doc.getId(), wordsCounted.get(j).getValue2()));
                        invertDocs.add(new InvertedListObject(wordsCounted.get(j).getValue1(), tpList));
                    }
                }
            }
        }

        return invertDocs;
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
