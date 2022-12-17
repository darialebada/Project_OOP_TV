package pages;

public class Login extends Page {
    public Login() {
        setType("login");
    }

    public boolean isNextPageCorrect(String pageType) {
        if (pageType.equals("register")) {
            return true;
        }
        return false;
    }
}
