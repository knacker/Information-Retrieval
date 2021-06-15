package util;

import data.Document;

import java.util.*;

public class WordListUtil {
    public static List<Tuple<String, Integer>> createWordList(Document doc) {

        String content = doc.getContent();
        List<String> wordList = new ArrayList<>(Arrays.asList(content.split(" ")));
        ArrayList<String> newList = new ArrayList<>();
        for (String word : wordList) {

            word = word.replaceAll("[.,;:\"!?\n]", "");
            if (!word.isBlank()) {
                newList.add(word);
            }
        }

        List<Tuple<String, Integer>> wordListCounted = new ArrayList<>();

        //counts the occurence of each word in the list
        countOccurence(newList, wordListCounted);

        return wordListCounted;
    }

    public static void countOccurence(ArrayList<String> list, List<Tuple<String, Integer>> wordListSmall) {
        // hashmap to store the frequency of element
        Map<String, Integer> hm = new HashMap<String, Integer>();

        for (String word : list) {
            Integer count = hm.get(word);
            hm.put(word, (count == null) ? 1 : count + 1);
        }

        // saving the occurrence of elements in the arraylist
        for (Map.Entry<String, Integer> val : hm.entrySet()) {
            wordListSmall.add(new Tuple<>(val.getKey(), val.getValue()));
        }
    }

}
