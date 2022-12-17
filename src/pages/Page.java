package pages;

public abstract class Page {
    private String type;

    public abstract boolean isNextPageCorrect(String pageType);

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
