package util;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

public class SignatureUtil {

    final int F = 64;
    final int D = 4;

    //count i-th hash function
    int i = 0;

    static int primesSize = 1000000;
    static int [] primes = PrimeNumberUtil.createPrimeArray(primesSize);

    public int hashStrings(List<String> words) {

        int hash = 0;

        for(int k = 0; k < words.size(); k++) {
            hash = (hash + words.get(k).hashCode()) * primes[i];
        }

        i++;
        hash = hash % F;

        return hash;
    }
    public int getSignatureWeight() {
        int m = 0;

        return m;
    }
}
