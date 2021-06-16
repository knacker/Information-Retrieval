package data;

import util.Tuple;

import java.util.List;

public class InvertedListObject {

    private String word;

    private List<Tuple<Integer, Integer>> idCount;

    public InvertedListObject(String word, List<Tuple<Integer, Integer>>idCount) {
        this.word = word;
        this.idCount = idCount;
    }

    public boolean containsID(int num) {
        for(Tuple<Integer, Integer> ic : idCount) {
            if(ic.getValue1() == num) {
                return true;
            }
            else return false;
        }
        return false;
    }
    public void addEntryIC(Tuple<Integer, Integer> entry) {
        idCount.add(entry);
    }

    public String getWord() {
        return word;
    }

    public List<Tuple<Integer, Integer>> getIdCount() {
        return idCount;
    }

}
