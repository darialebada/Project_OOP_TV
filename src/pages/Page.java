package pages;

public abstract class Page {
    private String type;

    /**
     * check if next page can be accessed
     */
    public abstract boolean isNextPageCorrect(String pageType);

    /**
     * @return type of page
     */
    public String getType() {
        return type;
    }

    /**
     * set type of page
     */
    public void setType(final String type) {
        this.type = type;
    }
}
