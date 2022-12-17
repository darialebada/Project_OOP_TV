package pages;

public final class Register extends Page {
    /**
     * set page type
     */
    public Register() {
        setType("register");
    }

    /**
     * check if next page can be accessed
     */
    public boolean isNextPageCorrect(final String pageType) {
        return pageType.equals("homepageLoggedOut");
    }
}
