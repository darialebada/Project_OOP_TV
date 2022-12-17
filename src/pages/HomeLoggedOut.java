package pages;

public final class HomeLoggedOut extends Page {
    /**
     * set page type
     */
    public HomeLoggedOut() {
        setType("homeLoggedOut");
    }

    /**
     * check if next page can be accessed
     */
    public boolean isNextPageCorrect(final String pageType) {
        return pageType.equals("login")
                || pageType.equals("register");
    }
}
