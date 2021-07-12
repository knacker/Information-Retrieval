package data;

import java.util.BitSet;
import java.util.List;

public class DocumentSignature {

    Document doc;

    List<BitSet> signatures;

    public DocumentSignature(Document doc, List<BitSet> signatures) {
        this.doc = doc;
        this.signatures = signatures;
    }

    public boolean containsSignature(BitSet signature) {
        for(BitSet set : signatures) {
            BitSet temp = set;
            temp.and(signature);
            if(temp.equals(signature)) {
                return true;
            }
        }

        return false;
    }

    public Document getDoc() {
        return doc;
    }
    public List<BitSet> getSignatures() {
        return signatures;
    }

}
