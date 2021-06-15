package util;

import data.InvertedListObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Parser {

    //variable, so the parser knows where it currently is
    int i;
    List<String> search;
    String docContent;
    List<InvertedListObject> invertedList;
    List<InvertedListObject> docsToCheck;
    /**
    * @param expression in the form of a boolean operator, following a parantheses ( and after that the contents, closing with a ).
     *                   This expression will be called a block. A block itself can have other blocks aswell.
     *                  For Example, the input should look like that : &(word1, word2). Negating expressions works the same:
     *                  For Example, !(word1, word2). A combined expression, for example, looks like that:
     *                  &(word1, word2, |(word3, word4, !(|(word2, word3)))), which translates to:
     *                  word1 AND word2 AND (word3 OR word4 OR NOT(word2 OR word3))
     *
    * @return boolean
    *
     */
    public boolean evalExpression(List<String> expression, String content, List<InvertedListObject> list) {

        i = 0;
        search = expression;
        docContent = content;
        invertedList = list;

        if(invertedList == null) {
            return parseBool();
        }

        if(docContent.isBlank()) {
            return parseInverted();
        }
        return false;
    }

    public List<String> getRelevantIDs(List<String> expression, List<String> words, List<List<String>> words_relevant_ids) {
        List<String> relevant_ids = new ArrayList<>();

        for (int i = 0; i < expression.size(); i++) {
            String symbol = expression.get(i);

            switch (symbol) {
                case "|":
                    List<List<String>> or_list = new ArrayList<>();
                    int j = i+2;

                    while (!expression.get(j).equals(")")) {
                        int insertIndex = words.indexOf(expression.get(j));
                        if (insertIndex >= 0) {
                            or_list.add(words_relevant_ids.get(insertIndex));
                        }
                        j++;
                    }

                    relevant_ids = OR_lists(or_list);
                    break;

                case "&":
                    List<List<String>> and_list = new ArrayList<>();
                    int j1 = i+2;

                    while (!expression.get(j1).equals(")")) {
                        int insertIndex = words.indexOf(expression.get(j1));
                        if (insertIndex >= 0) {
                            and_list.add(words_relevant_ids.get(insertIndex));
                        }
                        j1++;
                    }

                    relevant_ids = AND_lists(and_list);

                    break;

                default:
                    break;
            }
        }

        return relevant_ids;
    }

    private List<String> AND_lists(List<List<String>> and_list) {
        List<String> and = new ArrayList<>();

        if (and_list.isEmpty()) return new ArrayList<>();

        for (int i = 0; i < and_list.get(0).size(); i++) {
            boolean b = true;
            String s = and_list.get(0).get(i);

            for (int j = 1; j < and_list.size(); j++) {
                if (!and_list.get(j).contains(s)) {
                    b = false;
                    break;
                }
            }

            if (b) {
                and.add(s);
            }
        }

        return and;
    }

    private List<String> OR_lists(List<List<String>> or_list) {

        if (or_list.isEmpty()) return new ArrayList<>();

        int i = 0;
        List<String> or = new ArrayList<>(or_list.get(i));

        i++;
        while (i < or_list.size()) {
            for (String s : or_list.get(i)) {
                if (!or.contains(s)) {
                    or.add(s);
                }
            }
            i++;
        }

        return or;
    }

    private boolean parseBool() {
        String op = search.get(i++);
        List<Boolean> bools = new ArrayList<>();

        while(i < search.size()) {
            String st = search.get(i++);

            if(st.equals("(")) {
                continue;
            }

            if(!st.equals("|") && !st.equals("!") && !st.equals("&") && !st.equals(")")) {
                bools.add(matchString(Collections.singletonList(st), docContent));
            } else if(st.equals("|") || st.equals("!") || st.equals("&") ){
                //calls itself, if there is another block within a block, decrease i by 1, so it starts at the same point for the new block
                i--;
                bools.add(parseBool());

              //end of block
            } else if(st.equals(")")) {
                break;
            }
        }
        return eval(bools, op);
    }
    public boolean parseInverted() {

        String op = search.get(i++);
        List<Boolean> bools = new ArrayList<>();

        while(i < search.size()) {
            String st = search.get(i++).toLowerCase();

            if(st.equals("(")) {
                continue;
            }

            if(!st.equals("|") && !st.equals("!") && !st.equals("&") && !st.equals(")")) {
               //TODO docsToCheck.add(invertedList.get(invertedList.indexOf(st)));
            } else if(st.equals("|") || st.equals("!") || st.equals("&") ){
                //calls itself, if there is another block within a block, decrease i by 1, so it starts at the same point for the new block
                i--;
                bools.add(parseInverted());

                //end of block
            } else if(st.equals(")")) {
                break;
            }
        }

        return eval(bools, op);
    }

    private boolean eval(List<Boolean> bools, String op) {
        if(op.equals("!")) {
            return !bools.get(0);
        }
        boolean result = (op.equals("|")) ? false : true;
        for(boolean bool : bools) {
            result = (op.equals("|")) ? (result || bool) : (result && bool);
        }
        return result;
    }

    private boolean matchString(List<String> searchTerms, String content) {
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
}
