package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListOccurenceCounter {

    public static void countFrequencies(ArrayList<String> list, List<Tuple<String, Integer>> wordListSmall) {
        // hashmap to store the frequency of element
        Map<String, Integer> hm = new HashMap<String, Integer>();

        for (String i : list) {
            Integer j = hm.get(i);
            hm.put(i, (j == null) ? 1 : j + 1);
        }

        // saving the occurrence of elements in the arraylist
        for (Map.Entry<String, Integer> val : hm.entrySet()) {
            System.out.println(val.getValue());
            wordListSmall.add(new Tuple<>(val.getKey(), val.getValue()));
        }
    }

}
