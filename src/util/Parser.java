package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Parser {

    //variable, so the parser knows where it currently is
    int i;
    List<String> search;
    String docContent;

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
    public boolean evalExpression(List<String> expression, String content) {

        i = 0;
        search = expression;
        docContent = content;

        return parse();
    }

    private boolean parse() {
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
                bools.add(parse());

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
}
