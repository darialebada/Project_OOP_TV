package pages;

public class HomeLoggedOut extends Page {
    public HomeLoggedOut() {
        setType("homeLoggedOut");
    }

    public boolean isNextPageCorrect(String pageType) {
        if (pageType.equals("login")
            || pageType.equals("register")) {
            return true;
        }
        return false;
    }
}
