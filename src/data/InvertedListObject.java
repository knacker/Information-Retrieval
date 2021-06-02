package data;

import util.Tuple;

public class InvertedListObject {

    private String word;
    private Tuple<Integer, Integer> idCount;

    public InvertedListObject(String word, Tuple<Integer, Integer> idCount) {
        this.word = word;
        this.idCount = idCount;
    }

    public void increaseCount() {
        int count = idCount.getValue2();
        count++;
        this.idCount.setValue2(count);
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Tuple<Integer, Integer> getIdCount() {
        return idCount;
    }

    public void setIdCount(Tuple<Integer, Integer> idCount) {
        this.idCount = idCount;
    }

}
