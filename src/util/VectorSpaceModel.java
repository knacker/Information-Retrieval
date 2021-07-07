package util;

import data.HelpDataStructure;
import data.InvertedListObject;

import java.text.DecimalFormat;
import java.util.List;
import java.lang.Math;

public class VectorSpaceModel {

    private List<InvertedListObject> invertedDocuments;

    private double[][] vectors;

    private final int gamma = 5;

    private double[] qVector;

    public VectorSpaceModel(List<InvertedListObject> invertedDocuments, int docs_count) {
        this.invertedDocuments = invertedDocuments;
        this.vectors = new double[docs_count][invertedDocuments.size()];

        // initial nur Vorkommenshäufigkeit der Begriffe in den Dokumenten
        for (InvertedListObject obj : this.invertedDocuments) {
            for (Tuple<Integer, Integer> idCount : obj.getIdCount()) {
                this.vectors[idCount.getValue1()][getWordIndex(obj.getWord())] = idCount.getValue2();
            }
        }

        this.vectors = tfidf();
    }

    public int[] getTopDocs() {

        HelpDataStructure ds = new HelpDataStructure();

        for (int i = 0; i < this.qVector.length; i++) {
            if (this.qVector[i] > 0) {
                InvertedListObject k = this.invertedDocuments.get(i);

                initList(k);

                while (notEndOfList(k)) {
                    Tuple<Integer, Double> nextElement = getNextElemOfList(k);
                    int docID = nextElement.getValue1();
                    double wdk = this.vectors[docID][getWordIndex(k.getWord())];

                    double value = wdk * this.qVector[i];

                    if (ds.isInDS(docID)) {
                        ds.addToDSEntry(docID, value);
                    } else {
                        ds.insertIntoDS(docID, value);
                    }
                }

                List<Tuple<Integer, Double>> topDocs = ds.getTopDocs(gamma);

                if (topDocs.size() > 1) {
                    if (topDocs.get(topDocs.size() - 2).getValue2() >= topDocs.get(topDocs.size() - 1).getValue2() + maxRemainingWeight(i+1)) {
                        int[] docs = new int[topDocs.size()];

                        for (int j = 0; j < docs.length; j++) {
                            docs[j] = topDocs.get(j).getValue1();
                        }

                        return docs;
                    }
                }
            }
        }

        return new int[]{};
    }

    private double maxRemainingWeight(int start_index) {
        double weight = 0;

        if (start_index < this.qVector.length) {
            for (int i = start_index; i < this.qVector.length; i++) {
                weight += this.qVector[i];
            }
        }

        return weight;
    }

    private double[][] tfidf() {
        double[][] new_vectors = new double[this.vectors.length][this.vectors[0].length];

        int N = this.vectors.length;

        for (int i = 0; i < this.vectors.length; i++) {
            double divider = 0;
            for (int j = 0; j < this.vectors[i].length; j++) {
                divider += Math.pow(this.vectors[i][j] * Math.log10((double) N/this.invertedDocuments.get(j).getIdCount().size()), 2);
            }

            for (int k = 0; k < this.vectors[i].length; k++) {
                new_vectors[i][k] = this.vectors[i][k] * Math.log10((double) N/this.invertedDocuments.get(k).getIdCount().size()) / Math.sqrt(divider);
            }
        }

        return new_vectors;
    }

    private double[][] correctionFactorVectors() {
        double[][] new_vectors = new double[this.vectors.length][this.vectors[0].length];

        int N = this.vectors.length;

        for (int i = 0; i < this.vectors.length; i++) {
            double divider = 0;
            for (int j = 0; j < this.vectors[i].length; j++) {
                divider += Math.pow(this.vectors[i][j] * N/this.invertedDocuments.get(j).getIdCount().size(), 2);
            }

            for (int k = 0; k < this.vectors[i].length; k++) {
                new_vectors[i][k] = Math.sqrt(this.vectors[i][k] * N/this.invertedDocuments.get(k).getIdCount().size() / divider);
            }
        }

        return new_vectors;
    }

    private double[][] normalizeVectors() {
        double[][] new_vectors = new double[this.vectors.length][this.vectors[0].length];

        for (int i = 0; i < this.vectors.length; i++) {
            double divider = 0;
            for (int j = 0; j < this.vectors[i].length; j++) {
                divider += Math.pow(this.vectors[i][j], 2);
            }

            for (int k = 0; k < this.vectors[i].length; k++) {
                new_vectors[i][k] = Math.sqrt(this.vectors[i][k] / divider);
            }
        }

        return new_vectors;
    }

    private int getWordIndex(String word) {
        for (int i = 0 ; i < this.invertedDocuments.size(); i++) {
            if (this.invertedDocuments.get(i).getWord().equals(word)) {
                return i;
            }
        }
        return -1;
    }

    public double[] getSimilarities(double[] request_vector) {
        double[] similar_list = new double[this.vectors.length];

        for (int i = 0; i < this.vectors.length; i++) {
            double result = 0;
            for (int j = 0 ; j < this.vectors[i].length; j++) {
                result += this.vectors[i][j] * request_vector[j];
            }
            similar_list[i] = result;
        }

        return similar_list;
    }

    public void showList() {
        for (InvertedListObject obj : this.invertedDocuments) {
            System.out.println(obj);
        }
    }

    public void showVectors(double[][] vectors) {

        DecimalFormat formatter = new DecimalFormat("#.###");

        for (int i = 0; i < vectors.length; i++) {
            for (int j = 0 ;  j < vectors[i].length; j++) {
                System.out.print(formatter.format(vectors[i][j]) + "; ");
            }
            System.out.println(" ENDE ");
        }
    }

    public void setQueryVector(String[] request) {
        this.qVector = new double[this.vectors[0].length];

        // reset query vector
        for (double d : this.qVector) {
            d = 0;
        }

        // get TF max
        int maxTF = 0;
        for (String s : request) {
            int max_new = getTFquery(request, s);

            if (max_new > maxTF) {
                maxTF = max_new;
            }
        }

        for (int i = 0; i < this.qVector.length; i++) {
            String word = this.invertedDocuments.get(i).getWord();
            int tf = getTFquery(request, word);

            if (tf > 0) {
                this.qVector[i] = (0.5 + (0.5 * tf / maxTF)) * Math.log10((double) this.vectors.length / this.invertedDocuments.get(getWordIndex(word)).getIdCount().size());
            }
        }
    }

    private int getTFquery(String[] request, String word) {
        int count = 0;

        for (String s : request) {
            if (s.equals(word)) count++;
        }

        return count;
    }

    // functions for inverted List
    private void initList(InvertedListObject k) {
        k.setContinuousPointer(0);
    }

    private Tuple<Integer, Double> getNextElemOfList(InvertedListObject k) {
        int actual_pointer = k.getContinuousPointer();
        int docID = k.getIdCount().get(actual_pointer).getValue1();
        double wdk = k.getIdCount().get(actual_pointer).getValue2();

        k.setContinuousPointer(actual_pointer + 1);

        return new Tuple<>(docID, wdk);
    }

    private boolean notEndOfList(InvertedListObject k) {
        return k.getContinuousPointer() < k.getIdCount().size();
    }

}
