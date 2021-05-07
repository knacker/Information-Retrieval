package data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FilterList {

    List<String> filter;

    public FilterList(List<String> lines) {
        this.filter = lines;
    }

    public static FilterList createSW() {
        //filterlist used for Stopwords
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get("/res/englishST.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new FilterList(lines);
    }

    //grundstamm reduktions liste kommt hier dann hinzu
    public static FilterList createRE() {
        return null;
    }

    public List<String> getList() {
        return this.filter;
    }
}
