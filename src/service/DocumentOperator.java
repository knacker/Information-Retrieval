package service;

import data.Document;
import data.FilterList;
import data.InvertedListObject;
import data.Model;

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

        // rule not matched (else longest match for Step 1a found)
        if (response.equals("")) {
            response = rule("ies", "i", statement);
        }

        if (response.equals("")) {
            response = rule("ss", "ss", statement);
        }

        if (response.equals("")) {
            response = rule("s", "", statement);
        }

        return response;
    }

    public static String step1b(String statement) {
        return null;
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
     * @param word in lowercase
     * @return [C](VC){m}[V] notation
     */
    public static String convertToConsonantVowelForm(String word) {
        StringBuilder cv_form = new StringBuilder();

        for (int i = 0; i < word.length(); i++) {
            if (i == 0) {
                if (isCharConsonant(word.charAt(i)) || word.charAt(i) == 'y') {
                    cv_form.append(consonant);
                } else {
                    cv_form.append(vowel);
                }
            } else {
                if (isCharConsonant(word.charAt(i)) != isCharConsonant(word.charAt(i-1)) || word.charAt(i) == 'y') {
                    if (isCharConsonant(word.charAt(i))) {
                        cv_form.append(consonant);
                    } else {
                        if (!isCharConsonant(word.charAt(i-1)) && word.charAt(i) == 'y') {
                            cv_form.append(consonant);
                        } else {
                            cv_form.append(vowel);
                        }
                    }
                }
            }
        }

        return cv_form.toString();
    }

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
     * @return true, if statement ends with double consonant
     */
    public static boolean statementEndsWithDoubleConsonant(String statement) {
        char second_last = statement.charAt(statement.length() - 2);
        char last = statement.charAt(statement.length() - 1);

        return (second_last == last && isCharConsonant(second_last));
    }

    /**
     * condition *v*
     *
     * @param statement
     * @return true, if statement contains a vowel
     */
    public static boolean statementContainsVowel(String statement) {
        return convertToConsonantVowelForm(statement).contains("" + vowel);
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
        List<String> words = createWordList(docs);

        for(Document doc : docs) {

        }

        return invertDocs;
    }

    private List<String> createWordList(List<Document> docs) {

        List<String> words = new ArrayList<>();

        for(Document doc : docs) {

            String content = doc.getContent();
            List<String> wordList = Arrays.asList(content.split(" "));

            for(String word : wordList) {
                if(word.matches(".*[.,;:\"!?\n]*.")) {
                    wordList.remove(word);
                }
            }
            for(String word : wordList) {
                if(!word.contains(word) && !word.equals(" ")) {
                    words.add(word);
                }
            }
        }

        return words;
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
