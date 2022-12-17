package pages;

public class Register extends Page {
    public Register() {
        setType("register");
    }

    public boolean isNextPageCorrect(String pageType) {
        if (pageType.equals("homepageLoggedOut")) {
            return true;
        }
        return false;
    }
}
