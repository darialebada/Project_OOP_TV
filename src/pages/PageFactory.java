package pages;

/**
 * Factory Design Pattern
 */
public final class PageFactory {
    /**
     * for coding style
     */
    private PageFactory() {
    }
    /**
     * Factory design pattern for a page
     * @return new page
     */
    public static Page createPage(final String pageType) {
        switch (pageType) {
            case "homeLoggedIn" -> {
                return new HomeLoggedIn();
            }
            case "homeLoggedOut" -> {
                return new HomeLoggedOut();
            }
            case "login" -> {
                return new Login();
            }
            case "register" -> {
                return new Register();
            }
            case "movies" -> {
                return new Movies();
            }
            case "see details" -> {
                return new SeeDetails();
            }
            case "upgrades" -> {
                return new Upgrade();
            }
            default -> {
                return null;
            }
        }
    }
}
