package pages;

public final class SeeDetails extends Page {
    /**
     * set page type
     */
    public SeeDetails() {
        setType("see details");
    }

    /**
     * check if next page can be accessed
     */
    public boolean isNextPageCorrect(final String pageType) {
        return pageType.equals("see details")
                || pageType.equals("movies")
                || pageType.equals("upgrades")
                || pageType.equals("logout");
    }
}
