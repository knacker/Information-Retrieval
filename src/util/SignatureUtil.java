package util;

import java.util.BitSet;
import java.util.List;

import static java.lang.Math.abs;

public class SignatureUtil {

    final static int F = 64;
    final static int D = 4;

    static int primesSize = 1000000;
    static int[] primes = PrimeNumberUtil.createPrimeArray(primesSize);

    public static BitSet hashStrings(List<String> words, int primeIndex) {

        BitSet bits = new BitSet(F);

        int initialPrimeIndex = primeIndex;

        for(String word : words) {

            BitSet wordbits = new BitSet(F);

            int setBits = 0;
            int hashposition = 0;
            int m = getSignatureWeight();

            for (int i = 0; i < m; i++) {

                hashposition = (hashposition + word.hashCode()) * primes[primeIndex];
                hashposition = hashposition % F;

                //only set bits, if the number of bits set doesn't exceed the signature weight
                if (setBits < m) {
                    wordbits.set(abs(hashposition));
                    setBits++;
                    primeIndex++;
                }
            }
            bits.or(wordbits);
            primeIndex = initialPrimeIndex;
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
