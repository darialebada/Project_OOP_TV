package pages;

public class SeeDetails extends Page {
    public SeeDetails(){
        setType("see details");
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
