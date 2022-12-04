package myclasses;
public final class Pages {
    private int currentUserIdx;
    private boolean homepageLoggedOut;
    private boolean login;
    private boolean register;
    private boolean homepageLogged;
    private boolean movies;
    private boolean upgrades;
    private boolean seeDetails;

    public Pages() {
       changePageHomepageLoggedOut();
       currentUserIdx = -1;
    }

    /**
     * changing the current page
     */
    public void changePage() {
        homepageLoggedOut = false;
        login = false;
        register = false;
        homepageLogged = false;
        movies = false;
        upgrades = false;
        seeDetails = false;
    }

    /**
     * page successfully changed to login page
     */
    public void changePageLogin() {
        changePage();
        login = true;
    }

    /**
     * page successfully changed to register page
     */
    public void changePageRegister() {
        changePage();
        register = true;
    }

    /**
     * page successfully changed to homepage logged out page
     */
    public void changePageHomepageLoggedOut() {
        changePage();
        currentUserIdx = -1;
        homepageLoggedOut = true;
    }

    /**
     * page successfully changed to homepage logged in page
     */
    public void changePageHomepageLogged(final int idx) {
        changePage();
        currentUserIdx = idx;
        homepageLogged = true;
    }

    /**
     * page successfully changed to movies page
     */
    public void changePageMovies() {
        changePage();
        movies = true;
    }

    /**
     * page successfully changed to see details page
     */
    public void changePageSeeDetails() {
        changePage();
        seeDetails = true;
    }

    /**
     * page successfully changed to upgrades page
     */
    public void changePageUpgrades() {
        changePage();
        upgrades = true;
    }

    public int getCurrentUserIdx() {
        return currentUserIdx;
    }

    public boolean isHomepageLoggedOut() {
        return homepageLoggedOut;
    }

    public boolean isLogin() {
        return login;
    }

    public boolean isHomepageLogged() {
        return homepageLogged;
    }

    public boolean isMovies() {
        return movies;
    }

    public boolean isUpgrades() {
        return upgrades;
    }
    public boolean isSeeDetails() {
        return seeDetails;
    }
}
