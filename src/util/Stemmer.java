package util;

import java.util.ArrayList;
import java.util.List;

public class Stemmer {
    private static final char consonant = 'C';
    private static final char vowel = 'V';

    public static String reduceContent(String content) {
        content = content.replaceAll("[.,;:\"!?\n]", " ");
        content = content.toLowerCase();

        String[] words = content.split(" ");
        ArrayList<String> converted_words = new ArrayList<>();

        // remove empty Array-Elements and so on
        for (String word : words) {
            if (!word.isEmpty()) {
                converted_words.add(word);
            }
        }

        ArrayList<String> reduce_words = new ArrayList<>();

        for (String word : converted_words) {
            reduce_words.add(applyRules(word));
        }

        return String.join(" " , reduce_words);
    }

    public static String applyRules(String statement) {

        String s1 = step1a(statement);
        String s2 = step1b(s1);
        String s3 = step1c(s2);
        String s4 = step2(s3);
        String s5 = step3(s4);
        String s6 = step4(s5);
        String s7 = step5a(s6);
        String s8 = step5b(s7);

        return s8;
    }

    private static String step1a(String statement) {
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

    private static String step1b2(String statement) {
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

        // (*d and not (*L or *S or *Z)) -> single letter
        // example: hopp -> hop, tann -> tan, fall -> fall
        if (response.equals("")) {
            char double_consonant = statementEndsWithDoubleConsonant(statement);
            if (double_consonant != ' ' &&
                    !(statementEndWithLetter('l', statement) ||
                        statementEndWithLetter('s', statement) ||
                        statementEndWithLetter('z', statement))) {
                response = rule("" + double_consonant + double_consonant, "" + double_consonant, statement);
            }
        }

        // (m=1 and *o) -> E
        String left = "";
        String right = "e";
        if (response.equals("")) {
            if (statementEndsWithCVC(getPrefix(left,statement)) && getMeasure(getPrefix(left,statement)) == 1) {
                response = rule(left, right, statement);
            }
        }

        if (response.equals("")) {
            response = statement;
        }

        return response;
    }

    private static String step1b(String statement) {
        String response = "";
        String left;
        String right;

        // (m>0) EED -> EE
        left = "eed";
        right = "ee";
        if (getMeasure(getPrefix(left, statement)) > 0) {
            response = rule(left, right, statement);
        } else if (!(rule(left,right,statement)).equals("")) {
            response = statement;
        }

        // (*v*) ING ->
        if (response.equals("")) {
            left = "ing";
            right = "";
            if (statementContainsVowel(getPrefix(left,statement))) {
                response = rule(left,right,statement);
                if (!response.equals("")) {
                    // third of the rules in step 1b is successful
                    response = step1b2(response);
                }
            } else if (!(rule(left,right,statement)).equals("")) {
                response = statement;
            }
        }

        // (*v*) ED ->
        if (response.equals("")) {
            left = "ed";
            right = "";
            if (statementContainsVowel(getPrefix(left,statement))) {
                response = rule(left,right,statement);

                if (!response.equals("")) {
                    // second of the rules in step 1b is successful
                    response = step1b2(response);
                }

            }  else if (!(rule(left,right,statement)).equals("")) {
                response = statement;
            }
        }

        if (response.equals("")) {
            response = statement;
        }

        return response;
    }

    private static String step1c(String statement) {
        String response = "";

        String left = "y";
        String right = "i";

        // (*v*) Y -> I
        if (statementContainsVowel(getPrefix(left,statement))) {
            response = rule(left,right,statement);
        }

        if (response.equals("")) {
            response = statement;
        }

        return response;
    }

    private static String step2(String statement) {
        String response = "";

        // (m>0) ATIONAL ->  ATE
        response = onlyMeasureCond("ational","ate",0,statement);

        // (m>0) TIONAL  ->  TION
        if (response.equals("")) {
            response = onlyMeasureCond("tional","tion",0,statement);
        }

        // (m>0) ENCI  ->  ENCE
        if (response.equals("")) {
            response = onlyMeasureCond("enci", "ence",0,statement);
        }

        // (m>0) ANCI  ->  ANCE
        if (response.equals("")) {
            response = onlyMeasureCond("anci","ance",0,statement);
        }

        // (m>0) IZER  ->  IZE
        if (response.equals("")) {
            response = onlyMeasureCond("izer","ize",0,statement);
        }

        // (m>0) ABLI  ->  ABLE
        if (response.equals("")) {
            response = onlyMeasureCond("abli","able",0,statement);
        }

        // (m>0) ALLI  ->  AL
        if (response.equals("")) {
            response = onlyMeasureCond("alli","al",0,statement);
        }

        // (m>0) ENTLI  ->  ENT
        if (response.equals("")) {
            response = onlyMeasureCond("entli","ent",0,statement);
        }

        // (m>0) ELI  ->  E
        if (response.equals("")) {
            response = onlyMeasureCond("eli","e",0,statement);
        }

        // (m>0) OUSLI  ->  OUS
        if (response.equals("")) {
            response = onlyMeasureCond("ousli","ous",0,statement);
        }

        // (m>0) IZATION  ->  IZE
        if (response.equals("")) {
            response = onlyMeasureCond("ization","ize",0,statement);
        }

        // (m>0) ATION  ->  ATE
        if (response.equals("")) {
            response = onlyMeasureCond("ation","ate",0,statement);
        }

        // (m>0) ATOR  ->  ATE
        if (response.equals("")) {
            response = onlyMeasureCond("ator","ate",0,statement);
        }

        // (m>0) ALISM  ->  AL
        if (response.equals("")) {
            response = onlyMeasureCond("alism","al",0,statement);
        }

        // (m>0) IVENESS  ->  IVE
        if (response.equals("")) {
            response = onlyMeasureCond("iveness","ive",0,statement);
        }

        // (m>0) FULNESS  ->  FUL
        if (response.equals("")) {
            response = onlyMeasureCond("fulness","ful",0,statement);
        }

        // (m>0) OUSNESS  ->  OUS
        if (response.equals("")) {
            response = onlyMeasureCond("ousness","ous",0,statement);
        }

        // (m>0) ALITI  ->  AL
        if (response.equals("")) {
            response = onlyMeasureCond("aliti","al",0,statement);
        }

        // (m>0) IVITI  ->  IVE
        if (response.equals("")) {
            response = onlyMeasureCond("iviti","ive",0,statement);
        }

        // (m>0) BILITI  ->  BLE
        if (response.equals("")) {
            response = onlyMeasureCond("biliti","ble",0,statement);
        }

        if (response.equals("")) {
            response = statement;
        }

        return response;
    }

    private static String step3 (String statement) {
        String response = "";

        // (m>0) ICATE  ->  IC
        response = onlyMeasureCond("icate","ic",0,statement);

        // (m>0) ATIVE  ->
        if (response.equals("")) {
            response = onlyMeasureCond("ative","",0,statement);
        }

        // (m>0) ALIZE  ->  AL
        if (response.equals("")) {
            response = onlyMeasureCond("alize","al",0,statement);
        }

        // (m>0) ICITI  ->  IC
        if (response.equals("")) {
            response = onlyMeasureCond("iciti","ic",0,statement);
        }

        // (m>0) ICAL  ->  IC
        if (response.equals("")) {
            response = onlyMeasureCond("ical","ic",0,statement);
        }

        // (m>0) FUL  ->
        if (response.equals("")) {
            response = onlyMeasureCond("ful","",0,statement);
        }

        // (m>0) NESS  ->
        if (response.equals("")) {
            response = onlyMeasureCond("ness","",0,statement);
        }

        if (response.equals("")) {
            response = statement;
        }

        return response;
    }

    private static String step4 (String statement) {
        String response = "";

        // (m>1) AL  ->
        response = onlyMeasureCond("al","",1,statement);

        // (m>1) ANCE  ->
        if (response.equals("")) {
            response = onlyMeasureCond("ance","",1,statement);
        }

        // (m>1) ENCE  ->
        if (response.equals("")) {
            response = onlyMeasureCond("ence","",1,statement);
        }

        // (m>1) ER  ->
        if (response.equals("")) {
            response = onlyMeasureCond("er","",1,statement);
        }

        // (m>1) IC  ->
        if (response.equals("")) {
            response = onlyMeasureCond("ic","",1,statement);
        }

        // (m>1) ABLE  ->
        if (response.equals("")) {
            response = onlyMeasureCond("able","",1,statement);
        }

        // (m>1) IBLE  ->
        if (response.equals("")) {
            response = onlyMeasureCond("ible","",1,statement);
        }

        // (m>1) ANT  ->
        if (response.equals("")) {
            response = onlyMeasureCond("ant","",1,statement);
        }

        // (m>1) EMENT  ->
        if (response.equals("")) {
            response = onlyMeasureCond("ement","",1,statement);
        }

        // (m>1) MENT  ->
        if (response.equals("")) {
            response = onlyMeasureCond("ment","",1,statement);
        }

        // (m>1) ENT  ->
        if (response.equals("")) {
            response = onlyMeasureCond("ent","",1,statement);
        }

        // (m>1 and (*S or *T)) ION ->
        String suffix = "ion";
        if (response.equals("")) {
            if (getMeasure(getPrefix(suffix,statement)) > 1 &&
                (statementEndWithLetter('s',getPrefix(suffix,statement)) || statementEndWithLetter('t',getPrefix(suffix,statement)) )) {
               response = rule(suffix, "" , statement);
            }
        }

        // (m>1) OU  ->
        if (response.equals("")) {
            response = onlyMeasureCond("ou","",1,statement);
        }

        // (m>1) ISM  ->
        if (response.equals("")) {
            response = onlyMeasureCond("ism","",1,statement);
        }

        // (m>1) ATE  ->
        if (response.equals("")) {
            response = onlyMeasureCond("ate","",1,statement);
        }

        // (m>1) ITI  ->
        if (response.equals("")) {
            response = onlyMeasureCond("iti","",1,statement);
        }

        // (m>1) OUS  ->
        if (response.equals("")) {
            response = onlyMeasureCond("ous","",1,statement);
        }

        // (m>1) IVE  ->
        if (response.equals("")) {
            response = onlyMeasureCond("ive","",1,statement);
        }

        // (m>1) IZE  ->
        if (response.equals("")) {
            response = onlyMeasureCond("ize","",1,statement);
        }

        if (response.equals("")) {
            response = statement;
        }

        return response;
    }

    private static String step5a (String statement) {
        String response = "";
        String temp_rule;

        // (m>1) E ->
        temp_rule = rule("e" , "" , statement);
        if (getMeasure(temp_rule) > 1) {
            response = temp_rule;
        }

        // (m=1 and not *o) E ->
        if (response.equals("")) {
            temp_rule = rule("e","",statement);
            if (getMeasure(temp_rule) == 1 && !(statementEndsWithCVC(temp_rule))) {
                response = temp_rule;
            }
        }

        if (response.equals("")) {
            response = statement;
        }

        return response;
    }

    private static String step5b (String statement) {
        String response = "";
        String temp_rule;

        temp_rule = rule("ll" , "l" , statement);
        // (m > 1 and *d and *L) -> single letter
        if (statementEndsWithDoubleConsonant(statement) == 'l' && getMeasure(temp_rule) > 1) {
            response = temp_rule;
        }

        if (response.equals("")) {
            response = statement;
        }

        return response;
    }

    private static String rule(String left, String right, String statement) {
        String response = "";
        // left -> right
        if (left.length() <= statement.length()) {
            String statement_left = statement.substring(statement.length() - left.length());
            if (statement_left.equals(left)) {
                response = statement.substring(0, statement.length() - left.length());
                response += right;
            }
        }

        return response;
    }

    /**
     * @param statement in lowercase
     * @return [C](VC){m}[V] notation
     */
    private static String convertToCVForm(String statement) {
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
    private static boolean statementEndsWithCVC(String statement) {
        if (statement.length() >= 3) {
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
        return false;
    }

    /**
     * condition *d
     *
     * @param statement
     * @return      return consonant, if true
     *              else return ' '
     */
    private static char statementEndsWithDoubleConsonant(String statement) {
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
    private static boolean statementContainsVowel(String statement) {
        return convertToCVForm(statement).contains("" + vowel);
    }

    /**
     * condition *S
     *
     * @param letter
     * @param statement
     * @return true, if statement ends with letter
     */
    private static boolean statementEndWithLetter(char letter, String statement) {
        return statement.charAt(statement.length() - 1) == letter;
    }

    /**
     * @param statement in lowercase notation
     * @return m from converted word
     */
    private static int getMeasure(String statement) {
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

    private static String onlyMeasureCond(String left, String right, int measureGreaterThen, String statement) {
        String response = "";
        if (getMeasure(getPrefix(left,statement)) > measureGreaterThen) {
            response = rule(left,right,statement);
        }

        return response;
    }

    private static String getPrefix(String suffix, String statement) {
        return rule(suffix, "", statement);
    }

    public static void testPorter() {
        String[] test_step1a = {"caresses", "ponies", "ties", "caress", "cats"};

        System.out.println("Step 1a");

        for (String s : test_step1a) {
            System.out.println("\t" + s + " -> " + Stemmer.step1a(s) + " | " + applyRules(s));
        }

        System.out.println();
        System.out.println("Step 1b");

        String[] test_step1b = {"feed", "agreed", "plastered", "bled", "motoring", "sing"};

        for (String s : test_step1b) {
            System.out.println("\t" + s + " -> " + Stemmer.step1b(s) + " | " + applyRules(s));
        }

        System.out.println();

        String[] test_step1b2 = {"conflated", "troubled", "sized", "hopping", "tanned", "falling", "hissing", "fizzed", "failing", "filing"};

        for (String s : test_step1b2) {
            System.out.println("\t" + s + " -> " + Stemmer.step1b(s) + " | " + applyRules(s));
        }

        System.out.println();
        System.out.println("Step 1c");

        String[] test_step1c = {"happy", "sky"};

        for (String s : test_step1c) {
            System.out.println("\t" + s + " -> " + Stemmer.step1c(s) + " | " + applyRules(s));
        }

        System.out.println();
        System.out.println("Step 2");

        String[] test_step2 = {"relational", "conditional", "rational", "valenci", "hesitance", "digitizer", "conformabli", "radicalli", "differentli", "vileli", "analogousli", "vietnamization", "predication", "operator", "feudalism", "decisiveness", "hopefulness", "callousness", "formaliti", "sensitiviti", "sensibiliti"};

        for (String s : test_step2) {
            System.out.println("\t" + s + " -> " + Stemmer.step2(s) + " | " + applyRules(s));
        }

        System.out.println();
        System.out.println("Step 3");

        String[] test_step3 = {"triplicate", "formative", "formalize", "electriciti", "electrical", "hopeful", "goodness"};

        for (String s : test_step3) {
            System.out.println("\t" + s + " -> " + Stemmer.step3(s) + " | " + applyRules(s));
        }

        System.out.println();
        System.out.println("Step 4");

        String[] test_step4 = {"revival", "allowance", "inference", "airliner", "gyroscopic", "adjustable", "defensible", "irritant", "replacement", "adjustment", "dependent", "adoption", "homologou", "communism", "activate", "angulariti", "homologous", "effective", "bowdlerize"};

        for (String s : test_step4) {
            System.out.println("\t" + s + " -> " + Stemmer.step4(s) + " | " + applyRules(s));
        }

        System.out.println();
        System.out.println("Step 5a");

        String[] test_step5a = {"probate", "rate", "cease"};

        for (String s : test_step5a) {
            System.out.println("\t" + s + " -> " + Stemmer.step5a(s) + " | " + applyRules(s));
        }

        System.out.println();
        System.out.println("Step 5b");

        String[] test_step5b = {"controll", "roll"};

        for (String s : test_step5b) {
            System.out.println("\t" + s + " -> " + Stemmer.step5b(s) + " | " + applyRules(s));
        }
    }
}