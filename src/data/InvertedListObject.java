package data;

import util.Tuple;

import java.util.List;

public class InvertedListObject {

    private String word;

    private List<Tuple<Integer, Integer>> idCount;

    private int continuousPointer;

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

    @Override
    public String toString () {
        StringBuilder builder = new StringBuilder();

        builder.append(word);

        for (Tuple<Integer, Integer> integerIntegerTuple : idCount) {
            builder.append("\n\t");
            builder.append("DocID: ").append(integerIntegerTuple.getValue1());
            builder.append("\t");
            builder.append("Count: ").append(integerIntegerTuple.getValue2());
        }

        builder.append("\n");

        return builder.toString();
    }

    // functions for vector space model
    public void setContinuousPointer(int value) {
        this.continuousPointer = value;
    }

    public int getContinuousPointer() {
        return this.continuousPointer;
    }
}
