package data;

import data.FilterList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Document {

    private int id;
    private String name;
    private String content;

    public Document(int id, String name, String content) {
        this.id = id;
        this.name = name;
        this.content = content;
    }

    public void filter(FilterList filter) {
        //filtert irgendwas irgendwie
    }

    public boolean isRelevant() {
        return true;
    }

    public String getName () {
        return this.name;
    }

    public String getContent() {
        return this.content;
    }
    public void showContent() {
        System.out.println(content);
    }

    public void showName() {
        System.out.println(name);
    }
}
