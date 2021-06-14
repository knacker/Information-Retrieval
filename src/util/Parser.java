package util;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    int i;
    List<String> search;
    String docContent;

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
            if(!st.equals("|") || !st.equals("!") || !st.equals("&") ) {
                bools.add(matchString(st, docContent));
            } else if(st.equals("|") || st.equals("!") || st.equals("&") ){
                i--;
                bools.add(parse());
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

    private static boolean matchString(String term, String content) {
        if (content.toLowerCase().contains(" " + term.toLowerCase() + " ")) {
            return true;
        } else {
            return false;
        }
    }
}
