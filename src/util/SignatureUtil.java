package util;

import java.util.BitSet;
import java.util.List;

import static java.lang.Math.abs;

public class SignatureUtil {

    final static int F = 64;
    final static int D = 4;

    static int primesSize = 1000000;
    static int [] primes = PrimeNumberUtil.createPrimeArray(primesSize);

    public static BitSet hashStrings(String word) {

        BitSet bits = new BitSet(F);
        int index = 1000;
            int hashposition = 0;

            for (int k = 0; k < word.length(); k++) {
                index++;
                hashposition = (hashposition + Character.toString(word.charAt(k)).hashCode()) * primes[index];


            hashposition = hashposition % 2^F;
            bits.set(abs(hashposition));

        }
        return bits;
    }
    public int getSignatureWeight() {
        int m = 0;

        return m;
    }
}
