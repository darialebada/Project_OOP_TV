package pages;

public class HomeLoggedIn extends Page {
    public HomeLoggedIn() {
        setType("homeLoggedIn");
    }
    public boolean isNextPageCorrect(String pageType) {
        if (pageType.equals("seeDetails")
            || pageType.equals("movies")
            || pageType.equals("upgrades")
            || pageType.equals("logout")) {
            return true;
        }
        return false;
    }
}
