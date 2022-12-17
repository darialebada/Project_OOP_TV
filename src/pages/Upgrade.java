package pages;

public class Upgrade extends Page {
    public Upgrade() {
        setType("upgrades");
    }

    public boolean isNextPageCorrect(String pageType) {
        if (pageType.equals("see details")
                || pageType.equals("movies")
                || pageType.equals("upgrades")
                || pageType.equals("logout")) {
            return true;
        }
        return false;
    }
}
