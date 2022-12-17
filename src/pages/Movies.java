package pages;

public final class Movies extends Page {
    /**
     * set page type
     */
    public Movies() {
        setType("movies");
    }

    /**
     * check if next page can be accessed
     */
    public boolean isNextPageCorrect(final String pageType) {
        return pageType.equals("see details")
                || pageType.equals("upgrades")
                || pageType.equals("movies")
                || pageType.equals("logout");
    }
}
