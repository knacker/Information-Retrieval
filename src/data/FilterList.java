package data;

import service.DocumentManager;

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

        String path;
        if (DocumentManager.isWindows()) {
            path = ".\\res\\englishST.txt";
        } else {
            path = "./res/englishST.txt";
        }


        try {
            lines = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new FilterList(lines);
    }

    //grundstamm reduktions liste kommt hier dann hinzu
    public static FilterList createRE() {
        //filterlist used for Stopwords
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get("null"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new FilterList(lines);
    }

    public List<String> getList() {
        return this.filter;
    }

    private static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();

        return (os.contains("win"));
    }

}

