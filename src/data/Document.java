package data;

public class Document {

    private int id;
    private String name;
    private String content;

    public Document(int id, String name, String content) {
        this.id = id;
        this.name = name;
        this.content = content;
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
