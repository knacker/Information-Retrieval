package service;

import data.Document;
import data.FilterList;
import data.InvertedListObject;
import data.Model;
import util.ListOccurenceCounter;
import util.Tuple;
import util.WordListUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DocumentOperator {

    private static final char consonant = 'C';
    private static final char vowel = 'V';

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

        for (Document doc : docs) {
            if (matchString(search, doc.getContent())) {
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
        return applyRules(statement);
    }

    public static String applyRules(String statement) {
        String s1 = step1a(statement);
        String s2 = step1b(s1);
        String s3 = step1c(s2);

        return s3;
    }

    public static String step1a(String statement) {
        String response = statement;

        response = rule("sses", "ss", statement);

        // SSES -> SS
        if (response.equals("")) {
            // rule not matched (else longest match for Step 1a found)
            response = rule("ies", "i", statement);
        }

        // IES -> I
        if (response.equals("")) {
            response = rule("ss", "ss", statement);
        }

        // SS -> SS
        if (response.equals("")) {
            response = rule("s", "", statement);
        }

        // S ->
        if (response.equals("")) {
            // no rule matched -> no change
            response = statement;
        }

        return response;
    }

    public static String step1b2(String statement) {
        String response = "";

        // AT -> ATE
        response = rule("at", "ate", statement);

        // BL -> BLE
        if (response.equals("")) {
            response = rule("bl", "ble", statement);
        }

        // IZ -> IZE
        if (response.equals("")) {
            response = rule("iz", "ize", statement);
        }

        // TODO:
        // (*d and not (*L or *S or *Z)) -> single letter
        // example: hopp -> hop, tann -> tan, fall -> fall

        // (m=1 and *o) -> E

        if (response.equals("")) {
            response = statement;
        }

        return response;
    }

    public static String step1b(String statement) {
        String response = "";

        // (m>0) EED -> EE
        if (getMeasure(rule("eed","", statement)) > 0) {
            response = rule("eed", "ee", statement);
        } else if (leftPartMatched("eed", statement)) {
            response = statement;
        }

        // (*v*) ING ->
        if (response.equals("")) {
            if (statementContainsVowel(rule("ing", "" , statement))) {
                response = rule("ing" , "", statement);
            } else if (leftPartMatched("ing", statement)) {
                response = statement;
            }
        }

        // (*v*) ED ->
        if (response.equals("")) {
            if (statementContainsVowel(rule("ed", "", statement))) {
                response = rule("ed","", statement);
            }  else if (leftPartMatched("ed", statement)) {
                response = statement;
            }
        }

        if (response.equals("")) {
            response = statement;
        }

        return response;
    }

    private static boolean leftPartMatched(String left, String statement) {
        return !rule(left, left, statement).equals("");
    }

    public static String step1c(String statement) {
        return null;
    }

    private static String rule(String left, String right, String statement) {
        String response = "";
        // left -> right
        String statement_left = statement.substring(statement.length() - left.length());
        if (statement_left.equals(left)) {
            response = statement.substring(0, statement.length() - left.length());
            response += right;
        }
        return response;
    }

    /**
     * @param statement in lowercase
     * @return [C](VC){m}[V] notation
     */
    public static String convertToCVForm(String statement) {
        StringBuilder cv_form = new StringBuilder();

        for (int i = 0; i < statement.length(); i++) {
            if (statement.charAt(i) == 'y') {
                if (cv_form.length() == 0 || cv_form.charAt(cv_form.length()-1) == vowel) {
                    cv_form.append(consonant);
                } else {
                    cv_form.append(vowel);
                }
            } else if (isCharConsonant(statement.charAt(i))) {
                if (cv_form.length() == 0 || cv_form.charAt(cv_form.length()-1) == vowel) {
                    cv_form.append(consonant);
                }
            } else {
                if (cv_form.length() == 0 || cv_form.charAt(cv_form.length()-1) == consonant) {
                    cv_form.append(vowel);
                }
            }
        }

        return cv_form.toString();
    }

    private static boolean isCharConsonant(char c) {
        List<Character> consonants = new ArrayList<>();
        consonants.add('a');
        consonants.add('e');
        consonants.add('i');
        consonants.add('o');
        consonants.add('u');

        return !consonants.contains(c);
    }

    /**
     * condition *o
     *
     * @param statement
     * @return true, if the statement ends cvc, where the second c is not W, X or Y (e.g.
     *        -WIL, -HOP)
     */
    public static boolean statementEndsWithCVC(String statement) {
        char third_last = statement.charAt(statement.length() - 3);
        char second_last = statement.charAt(statement.length() - 2);
        char last = statement.charAt(statement.length() - 1);

        return (
                isCharConsonant(third_last) &&
                !isCharConsonant(second_last) &&
                isCharConsonant(last) &&
                (last != 'w' && last != 'y' && last != 'x')
        );

    }

    /**
     * condition *d
     *
     * @param statement
     * @return      return consonant, if true
     *              else return ' '
     */
    public static char statementEndsWithDoubleConsonant(String statement) {
        if (statement.length() >= 2) {
            char second_last = statement.charAt(statement.length() - 2);
            char last = statement.charAt(statement.length() - 1);

            if (second_last == last && isCharConsonant(second_last)) {
                return second_last;
            }
        }

        return ' ';
    }

    /**
     * condition *v*
     *
     * @param statement
     * @return true, if statement contains a vowel
     */
    public static boolean statementContainsVowel(String statement) {
        return convertToCVForm(statement).contains("" + vowel);
    }

    /**
     * condition *S
     *
     * @param letter
     * @param statement
     * @return true, if statement ends with letter
     */
    public static boolean statementEndWithLetter(char letter, String statement) {
        return statement.charAt(statement.length() - 1) == letter;
    }

    /**
     * @param statement in lowercase notation
     * @return m from converted word
     */
    public static int getMeasure(String statement) {
        // in [C](VC){m}[V] notation
        String notation = convertToCVForm(statement);

        int measure = 0;

        boolean v_read = false;

        for (int i = 0; i < notation.length(); i++) {
            if (v_read) {
                if (notation.charAt(i) == consonant) {
                    measure++;
                    v_read = false;
                }
            } else {
                if (notation.charAt(i) == vowel) {
                    v_read = true;
                }
            }
        }

        return measure;
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
