package myclasses;
public class Pages {
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
    }

    public void changePage() {
        homepageLoggedOut = false;
        login = false;
        register = false;
        homepageLogged = false;
        movies = false;
        upgrades = false;
        seeDetails = false;
    }

    public void changePageLogin() {
        changePage();
        login = true;
    }

    public void changePageRegister() {
        changePage();
        register = true;
    }

    public void changePageHomepageLoggedOut() {
        changePage();
        homepageLoggedOut = true;
    }

    public void changePageHomepageLogged(final int idx) {
        changePage();
        currentUserIdx = idx;
        homepageLogged = true;
    }

    public void changePageMovies() {
        changePage();
        movies = true;
    }

    public void changePageSeeDetails() {
        changePage();
        seeDetails = true;
    }

    public void changePageUpgrades() {
        changePage();
        upgrades = true;
    }

    public int getCurrentUserIdx() {
        return currentUserIdx;
    }

    public void setCurrentUserIdx(final int currentUserIdx) {
        this.currentUserIdx = currentUserIdx;
    }

    public boolean isHomepageLoggedOut() {
        return homepageLoggedOut;
    }

    public void setHomepageLoggedOut(final boolean homepageLoggedOut) {
        this.homepageLoggedOut = homepageLoggedOut;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(final boolean login) {
        this.login = login;
    }

    public boolean isRegister() {
        return register;
    }

    public void setRegister(final boolean register) {
        this.register = register;
    }

    public boolean isHomepageLogged() {
        return homepageLogged;
    }

    public void setHomepageLogged(final boolean homepageLogged) {
        this.homepageLogged = homepageLogged;
    }

    public boolean isMovies() {
        return movies;
    }

    public void setMovies(final boolean movies) {
        this.movies = movies;
    }

    public boolean isUpgrades() {
        return upgrades;
    }

    public void setUpgrades(final boolean upgrades) {
        this.upgrades = upgrades;
    }

    public boolean isSeeDetails() {
        return seeDetails;
    }

    public void setSeeDetails(final boolean seeDetails) {
        this.seeDetails = seeDetails;
    }
}
