package pages;

public final class HomeLoggedIn extends Page {
    /**
     * set page type
     */
    public HomeLoggedIn() {
        setType("homeLoggedIn");
    }

    /**
     * check if next page can be accessed
     */
    public boolean isNextPageCorrect(final String pageType) {
        return pageType.equals("seeDetails")
                || pageType.equals("movies")
                || pageType.equals("upgrades")
                || pageType.equals("logout");
    }
}
