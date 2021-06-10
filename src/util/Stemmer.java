package util;

import java.util.ArrayList;
import java.util.List;

public class Stemmer {
    private static final char consonant = 'C';
    private static final char vowel = 'V';

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
        String temp_rule = rule("", "e",statement);
        if (response.equals("")) {
            if (statementEndsWithCVC(statement)) {
                if (getMeasure(temp_rule) == 1) {
                    response = temp_rule;
                }
            }
        }

        if (response.equals("")) {
            response = statement;
        }

        return response;
    }

    public static String step1b(String statement) {
        String response = "";
        String temp_rule;

        // (m>0) EED -> EE
        temp_rule = rule("eed","ee", statement);

        if (getMeasure(temp_rule) > 0) {
            response = temp_rule;
        } else if (!temp_rule.equals("")) {
            response = statement;
        }

        // (*v*) ING ->
        if (response.equals("")) {
            temp_rule = rule("ing", "" , statement);
            if (statementContainsVowel(temp_rule)) {
                response = temp_rule;
                if (!response.equals("")) {
                    // third of the rules in step 1b is successful
                    response = step1b2(response);
                }
            } else if (!temp_rule.equals("")) {
                response = statement;
            }
        }

        // (*v*) ED ->
        if (response.equals("")) {
            temp_rule = rule("ed", "", statement);
            if (statementContainsVowel(temp_rule)) {
                response = temp_rule;

                if (!response.equals("")) {
                    // second of the rules in step 1b is successful
                    response = step1b2(response);
                }

            }  else if (!temp_rule.equals("")) {
                response = statement;
            }
        }

        if (response.equals("")) {
            response = statement;
        }

        return response;
    }

    public static String step1c(String statement) {
        // TODO
        // (*v*) Y -> I
        // verstehe ich nicht
        return null;
    }

    public static String step2(String statement) {
        String response = "";
        String temp_rule;

        // (m>0) ATIONAL ->  ATE
        temp_rule = rule("ational" , "ate" , statement);
        if (getMeasure(temp_rule) > 0) {
            response = temp_rule;
        }

        // (m>0) TIONAL  ->  TION
        if (response.equals("")) {
            temp_rule = rule("tional" , "tion" , statement);
            if (getMeasure(temp_rule) > 0) {
                response = temp_rule;
            }
        }

        if (response.equals("")) {
            response = statement;
        }

        return response;
    }

    public static String step3 (String statement) {
        String response = "";

        if (response.equals("")) {
            response = statement;
        }

        return response;
    }

    public static String step4 (String statement) {
        String response = "";

        if (response.equals("")) {
            response = statement;
        }

        return response;
    }

    public static String step5a (String statement) {
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

    public static String step5b (String statement) {
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

}
