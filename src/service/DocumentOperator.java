package service;

import data.Document;
import data.FilterList;
import data.InvertedListObject;
import data.Model;
import util.Parser;
import util.Stemmer;
import util.Tuple;
import util.WordListUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
            content = content.replaceAll("[.,;:\"!?\n]", " ");
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

    public static List<Document> invertedSearch(List<Document> docs, List<String> search) {

        List<InvertedListObject> invertedDocuments = createInvertList(docs);
        List<Document> foundDocs = new ArrayList<>();

        for (InvertedListObject obj : invertedDocuments) {
            if (search.equals(obj.getWord())) {
                //add every doc associated with the string
                for (Tuple<Integer, Integer> idCount : obj.getIdCount()) {
                    foundDocs.add(docs.get(idCount.getValue1()));
                }

            }
        }

        return foundDocs;
    }

    public static List<Document> stemming(List<Document> docs) {
        List<Document> reduceDocs = new ArrayList<>();

        for (Document doc : docs) {
            String content = doc.getContent();

            content = Stemmer.reduceContent(content);

            Document new_doc = new Document(doc.getId(), doc.getName(), content);

            reduceDocs.add(new_doc);
        }

        return reduceDocs;
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
            if (matchString(searchTerms.subList(1, searchTerms.size()), content)) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    public static List<InvertedListObject> createInvertList(List<Document> docs) {

        //list, which contains every word and its list of documents, which it is in
        List<InvertedListObject> invertDocs = new ArrayList<>();

        for (Document doc : docs) {

            //create List of words of a document, which contains the number of occurences for each word
            List<Tuple<String, Integer>> wordsCounted = WordListUtil.createWordList(doc);

            for (int i = 0; i < wordsCounted.size(); i++) {
                List<Tuple<Integer, Integer>> tpList = new ArrayList<>();
                tpList.add(new Tuple<>(doc.getId(), wordsCounted.get(i).getValue2()));
                invertDocs.add(new InvertedListObject(wordsCounted.get(i).getValue1(), tpList));
            }
            //merge duplicates

            for (int i = 0; i < invertDocs.size(); i++) {
                for (int j = i + 1; j < invertDocs.size(); j++) {
                    if (invertDocs.get(i).getWord().equals(invertDocs.get(j).getWord())) {
                        invertDocs.get(i).addEntryIC(new Tuple(doc.getId(), invertDocs.get(j).getIdCount().get(0).getValue2()));
                        invertDocs.remove(j);
                    }
                }
            }
        }

        return invertDocs;
    }

    public static double calculateRecall(String statement, List<Document> response) {
        int relevante_Dokumente_im_Ergebnis = getNumberOfRelevantDocs(statement, response);
        int gesamte_relevante_Dokumente = getAllRelevantDocs(statement);

        double recall = 0;

        try {
            recall = (double) relevante_Dokumente_im_Ergebnis / gesamte_relevante_Dokumente;
        } catch (Exception e) {

        }

        return recall;
    }

    public static double calculatePrecision(String statement, List<Document> response) {
        int relevante_Dokumente_im_Ergebnis = getNumberOfRelevantDocs(statement, response);
        int gesamte_Dokumente_im_Ergebnis = response.size();

        double precision = 0;

        try {
            precision = (double) relevante_Dokumente_im_Ergebnis / gesamte_Dokumente_im_Ergebnis;
        } catch (Exception e) {

        }

        return precision;
    }

    public static int getNumberOfRelevantDocs(String statement, List<Document> response) {
        int count = 0;

        String fileName;

        ArrayList<Integer> found_ids = new ArrayList<>();

        for (Document doc : response) {
            found_ids.add(doc.getId());
        }

        if (DocumentManager.isWindows()) {
            fileName = ".\\res\\ground_truth_updated.txt";
        } else {
            fileName = "./res/ground_truth_updated.txt";
        }

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line = br.readLine().replace(",","");

            while (!line.equals("")) {

                if (getWord(line).equals(statement)) {
                     return compareDocLists(getDocs(line), found_ids);
                }

                line = br.readLine().replace(",","");

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return count;
    }

    private static int getAllRelevantDocs(String statement) {
        String fileName;

        if (DocumentManager.isWindows()) {
            fileName = ".\\res\\ground_truth_updated.txt";
        } else {
            fileName = "./res/ground_truth_updated.txt";
        }

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line = br.readLine().replace(",","");

            while (!line.equals("")) {

                if (getWord(line).equals(statement)) {
                    return getDocs(line).length;
                }

                line = br.readLine().replace(",","");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private static int compareDocLists(String[] relevant, List<Integer> response) {
        int count = 0;

        for (String id : relevant) {
            int relevant_id = Integer.parseInt(id);

            if (response.contains(relevant_id - 1)) {
                count++;
            }
        }

        return count;
    }

    private static String getWord(String line) {
        StringBuilder word = new StringBuilder();
        int i = 0;

        while (line.charAt(i) != ' ') {
            word.append(line.charAt(i));
            i++;
        }

        return word.toString();
    }

    private static String[] getDocs(String line) {
        int i = 0;
        String[] doc_list;

        while (line.charAt(i) != '-') {
            i++;
        }

        i += 2;

        doc_list = line.substring(i).split(" ");

        return doc_list;
    }
}
