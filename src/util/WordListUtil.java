package util;

import data.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WordListUtil {
    public static List<Tuple<String, Integer>> createWordList(List<Document> docs) {

        ArrayList<String> words = new ArrayList<>();

        for(Document doc : docs) {

            String content = doc.getContent();
            List<String> wordList = Arrays.asList(content.split(" "));

            for(String word : wordList) {
                if(word.matches(".*[.,;:\"!?\n]*.")) {
                    wordList.remove(word);
                }
            }
            for(String word : wordList) {
                if(!word.contains(word) && !word.equals(" ")) {
                    words.add(word);
                }
            }
        }

        List<Tuple<String, Integer>> wordListCounted = new ArrayList<>();

        ListOccurenceCounter.countFrequencies(words, wordListCounted);

        return wordListCounted;
    }

}
