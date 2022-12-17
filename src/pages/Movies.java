package pages;

public class Movies extends Page {
    public Movies() {
        setType("movies");
    }

    public boolean isNextPageCorrect(String pageType) {
        if (pageType.equals("see details")
                || pageType.equals("upgrades")
                || pageType.equals("movies")
                || pageType.equals("logout")) {
            return true;
        }
        return false;
    }
}
