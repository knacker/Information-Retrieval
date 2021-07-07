package util;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import static java.lang.Math.abs;

public class SignatureUtil {

    final static int F = 64;
    final static int D = 4;
    final static int PRIMEINDEX = 10;

    static int primesSize = 1000000;
    static int[] primes = PrimeNumberUtil.createPrimeArray(primesSize);

    public static List<BitSet> blockHash(List<String> words) {

        List<BitSet> blocks = new ArrayList<>();

        while(words.size() > 0) {
            if(words.size() < 4) {
                List<String> lastWords = new ArrayList<>();
                for(String word : words) {
                    lastWords.add(word);
                }
                blocks.add(hashStrings(lastWords));
                return blocks;
            } else {
                blocks.add(hashStrings(words.subList(0, D)));
                words = words.subList(D, words.size());
            }
        }
        return blocks;
    }
    public static BitSet hashStrings(List<String> words) {

        BitSet bits = new BitSet(F);

        int primeIndex = PRIMEINDEX;

        for (String word : words) {

            BitSet wordbits = new BitSet(F);

            int hashposition = 0;
            int m = getSignatureWeight();

            for (int i = 0; i < m; i++) {

                hashposition = (hashposition + word.hashCode()) * primes[primeIndex];
                hashposition = hashposition % F;

                wordbits.set(abs(hashposition));
                primeIndex++;

            }
            bits.or(wordbits);
            primeIndex = PRIMEINDEX;
        }

        return bits;
    }

    /**
     * @return the value m, which represents the number of bits, that should be set in a bitset
     */
    public static int getSignatureWeight() {
        double m = F * (Math.log(2) / D);
        return (int) m;
    }
}
