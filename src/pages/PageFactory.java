package pages;

public class PageFactory {
    public static Page createPage (String pageType) {
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
