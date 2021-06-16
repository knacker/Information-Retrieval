package util;

import data.InvertedListObject;

import java.util.ArrayList;
import java.util.List;

public class InvertedListChecker {

    public static List<InvertedListObject> checkDocs(List<String> expression, List<InvertedListObject> invertedList) {

        List<InvertedListObject> checkedDocs = new ArrayList<>();

        String st = "";
        for (int i = 2; i < expression.size(); i++) {
            st = expression.get(i);
            for (InvertedListObject obj : invertedList) {
                if (st.equals(obj.getWord())) {
                    checkedDocs.add(obj);
                }
            }
        }
        return checkedDocs;
    }

    public static List<Integer> matchIDs(List<String> search, List<InvertedListObject> docsToCheck) {

        List<Integer> docIDs = new ArrayList<>();

        String op = search.get(0);

        for (int i = 1; i < search.size(); i++) {
            if (op.equals("|")) {
                for (int j = 0; j < docsToCheck.size(); j++) {
                    InvertedListObject obj = docsToCheck.get(j);
                    for (Tuple<Integer, Integer> idCount : obj.getIdCount()) {
                        docIDs.add(idCount.getValue1());
                    }
                }
            }

            if (op.equals("&")) {
                for (int j = 0; j < docsToCheck.size() - 1; j++) {
                    InvertedListObject obj1 = docsToCheck.get(j);
                    InvertedListObject obj2 = docsToCheck.get(j + 1);

                    for (Tuple<Integer, Integer> idCount1 : obj1.getIdCount()) {
                        for (Tuple<Integer, Integer> idCount2 : obj2.getIdCount()) {
                            if (idCount1.getValue1() == idCount2.getValue1()) {
                                docIDs.add(idCount1.getValue1());
                            } else {
                                docIDs.remove(idCount1.getValue1());
                            }
                        }
                    }
                }
            }
            if (op.equals("!")) {

            }


        }
        return docIDs;
    }
}


