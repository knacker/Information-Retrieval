package service;

import data.*;
import util.*;

import javax.print.Doc;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

public class DocumentOperator {

    public DocumentOperator() {
    }

    public List<Document> searchDocuments(List<Document> docs, List<String> search, Model m) {
        List<Document> foundDocs = new ArrayList<Document>();
        if (m == Model.BOOL) {
            foundDocs = linearSearch(docs, search);
        }
        return foundDocs;
    }

    public static List<Document> searchSignatures(List<DocumentSignature> docs, List<String> search) {

        List<Document> matchingSignatureDocs = new ArrayList<>();
        List<Document> foundDocs;

        List<String> newSearch = new ArrayList<>(search);
        List<BitSet> searchSignatures = new ArrayList<>();

        boolean konjunktion = search.get(0).equals("&");

        //remove parantheses
        newSearch.remove("(");
        newSearch.remove(")");
        newSearch.remove("&");
        newSearch.remove("|");
         SignatureUtil.hashStrings(newSearch.subList(0, 1));
        //konjunktion/disjunktion von 2 suchbegriffen
        searchSignatures.add(SignatureUtil.hashStrings(newSearch.subList(0, 1)));
        if (newSearch.size() > 1) {
            if (konjunktion) {
                searchSignatures.get(0).and(SignatureUtil.hashStrings(newSearch.subList(1, 2)));
            } else {
                searchSignatures.get(0).or(SignatureUtil.hashStrings(newSearch.subList(1, 2)));
            }
        }

        for (DocumentSignature doc : docs) {
            for (BitSet set : searchSignatures) {
                if (doc.containsSignature(set)) {
                    matchingSignatureDocs.add(doc.getDoc());
                }
            }
        }
        //filter false drops
        foundDocs = linearSearch(matchingSignatureDocs, search);

        return foundDocs;
    }
    public static long convert(BitSet bits) {
        long value = 0L;
        for (int i = 0; i < bits.length(); ++i) {
            value += bits.get(i) ? (1L << i) : 0L;
        }
        return value;
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
                content = content.replaceAll(" " + filter + " ", " ");
            }

            // twice, because of a bug
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
        Parser pars = new Parser();

        if (docs.isEmpty()) {
            return foundDocs;
        }

        for (Document doc : docs) {
            if (pars.evalExpression(search, doc.getContent(), null)) {
                foundDocs.add(doc);
            }
        }
        return foundDocs;
    }

    public static List<Document> invertedSearch(List<Document> docs, List<InvertedListObject> invertedDocuments, List<String> search) {

        List<Document> foundDocs = new ArrayList<>();

        List<InvertedListObject> docsToCheck = InvertedListChecker.checkDocs(search, invertedDocuments);
        List<Integer> matchedDocs = InvertedListChecker.matchIDs(search, docsToCheck);

        for (Integer i : matchedDocs) {
            foundDocs.add(docs.get(i));
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

    public static double calculateRecall(List<String> statement, List<Document> response) {
        int relevante_Dokumente_im_Ergebnis = getNumberOfRelevantDocs(statement, response);
        int gesamte_relevante_Dokumente = getAllRelevantDocs(statement);

        double recall = 0;

        if (gesamte_relevante_Dokumente > 0) {
            recall = (double) relevante_Dokumente_im_Ergebnis / gesamte_relevante_Dokumente;
        }

        return recall;
    }

    public static double calculatePrecision(List<String> statement, List<Document> response) {
        int relevante_Dokumente_im_Ergebnis = getNumberOfRelevantDocs(statement, response);
        int gesamte_Dokumente_im_Ergebnis = response.size();

        double precision = 0;

        if (gesamte_Dokumente_im_Ergebnis > 0) {
            precision = (double) relevante_Dokumente_im_Ergebnis / gesamte_Dokumente_im_Ergebnis;
        }

        return precision;
    }

    /**
     * @param statement
     * @param response  of our IR-System
     * @return
     */
    public static int getNumberOfRelevantDocs(List<String> statement, List<Document> response) {

        ArrayList<Integer> found_ids = new ArrayList<>();

        for (Document doc : response) {
            found_ids.add(doc.getId());
        }

        List<String> ids = relevantDocsFromGroundTruth(statement);

        return compareDocLists(ids, found_ids);
    }

    private static int getAllRelevantDocs(List<String> statement) {
        return relevantDocsFromGroundTruth(statement).size();
    }

    private static List<String> relevantDocsFromGroundTruth(List<String> statement) {
        String fileName;

        Parser parser = new Parser();

        List<String> all_words = new ArrayList<>();
        List<List<String>> all_words_doc_id = new ArrayList<>();

        if (DocumentManager.isWindows()) {
            fileName = ".\\res\\ground_truth_updated.txt";
        } else {
            fileName = "./res/ground_truth_updated.txt";
        }

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line = br.readLine().replace(",", "");

            while (!line.equals("")) {

                all_words.add(getWord(line));
                all_words_doc_id.add(getDocs(line));

                line = br.readLine().replace(",", "");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return parser.getRelevantIDs(statement, all_words, all_words_doc_id);
    }

    private static int compareDocLists(List<String> relevant, List<Integer> response) {
        int count = 0;

        if (relevant == null) return 0;

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

    private static List<String> getDocs(String line) {
        int i = 0;
        List<String> doc_list;

        while (line.charAt(i) != '-') {
            i++;
        }

        i += 2;

        doc_list = Arrays.asList(line.substring(i).split(" "));

        return doc_list;
    }
}
