package pages;

public final class Login extends Page {
    /**
     * set page type
     */
    public Login() {
        setType("login");
    }

    /**
     * check if next page can be accessed
     */
    public boolean isNextPageCorrect(final String pageType) {
        return pageType.equals("register");
    }
}
