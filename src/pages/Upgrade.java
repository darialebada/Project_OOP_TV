package pages;

public final class Upgrade extends Page {
    /**
     * set page type
     */
    public Upgrade() {
        setType("upgrades");
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
