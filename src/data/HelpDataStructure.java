package data;

import util.Tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelpDataStructure {
    private List<Tuple<Integer, Double>> pairs = new ArrayList<>();

    public boolean isInDS(int docID) {
        for (Tuple<Integer, Double> pair: this.pairs) {
            if (pair.getValue1() == docID) return true;
        }

        return false;
    }

    public void insertIntoDS(int docID, double g) {
        this.pairs.add(new Tuple<>(docID,g));
    }

    public void addToDSEntry(int docID, double delta) {
        for (Tuple<Integer, Double> pair: this.pairs) {
            if (pair.getValue1() == docID) {
                double new_g = pair.getValue2() + delta;
                pair.setValue2(new_g);
            }
        }
    }

    private double getWeightFromDS(int docID) {
        for (Tuple<Integer, Double> pair: this.pairs) {
            if (pair.getValue1() == docID) {
                return pair.getValue2();
            }
        }
        return 0;
    }

    public List<Tuple<Integer,Double>> getTopDocs(int gamma) {

        for (int i = 0; i < this.pairs.size(); i++) {
            for (int j = 0; j < this.pairs.size(); j++) {
                if (this.pairs.get(i).getValue2() > this.pairs.get(j).getValue2()) {
                    Tuple<Integer, Double> temp = this.pairs.get(i);
                    this.pairs.set(i, this.pairs.get(j));
                    this.pairs.set(j, temp);
                }
            }
        }

        int from = Math.min(this.pairs.size(), gamma + 1);

        return this.pairs.subList(0, from);
    }
}
