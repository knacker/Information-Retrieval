package util;

import data.InvertedListObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Parser {

    //variable, so the parser knows where it currently is
    int i;
    List<String> search;
    String docContent;
    List<InvertedListObject> invertedList;
    List<InvertedListObject> docsToCheck;

    int j;
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
            result = (op == "|") ? (result || bool) : (result && bool);
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

    public  List<Integer> evalInvertedExpression(List<String> expression, List<InvertedListObject> invertedList) {
        j = 0;
        return parseAnal(expression, docsToCheck);
    }

    private List<Integer> parseAnal(List<String> expression, List<InvertedListObject> invertedList) {

        InvertedListObject obj1 = null;
        InvertedListObject obj2 = null;
        String op = expression.get(j++);

        List<Integer> relevantDocs = new ArrayList<>();

        while(j < expression.size()) {
            String st = search.get(j++).toLowerCase();

            if(st.equals("(")) {
                continue;
            }
            if(!st.equals("|") && !st.equals("!") && !st.equals("&") && !st.equals(")")) {
                for(int k = 0; k < invertedList.size(); k++) {
                    if(invertedList.get(k).getWord().toLowerCase().equals(st)) {
                        obj1 = invertedList.get(k);
                        if(obj2 == null) {

                        }
                    }
                }
            }
        }
    }

  //  private Map<Object, Object> matchID() {
   // }
}
