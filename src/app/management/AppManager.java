package app.management;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.Action;
import fileio.Movie;
import fileio.User;
import utils.Errors;

import java.util.ArrayList;

public final class AppManager {
    private Pages page = new Pages();
    private ArrayList<User> users;
    private ArrayList<Movie> movies;
    private ArrayList<Movie> filteredMovieList = new ArrayList<>();
    private String currentMovieOnPage;
    private final Errors err = Errors.getErrorsInstance();

    /**
     * get the type of wanted action
     */
    public void debug(final ArrayList<User> usersList, final ArrayList<Movie> moviesList,
                      final ArrayList<Action> actions, final ArrayNode output) {
        users = new ArrayList<>(usersList);
        movies = new ArrayList<>(moviesList);
        for (Action action : actions) {
            switch (action.getType()) {
                case "change page" -> changePage(action, output);
                case "on page" -> onPage(action, output);
                default -> System.out.println("error\n");
            }
        }
    }

    /**
     * possible cases for changing the current page
     */
    public void changePage(final Action action, final ArrayNode output) {
        MovieActions movieActions = new MovieActions(page, movies, users,
                filteredMovieList, currentMovieOnPage);
        switch (action.getPage()) {
            case "login" -> {
                if (page.isHomepageLoggedOut()) {
                    page.changePageLogin();
                    return;
                }
            }
            case "register" -> {
                if (page.isHomepageLoggedOut()) {
                    page.changePageRegister();
                    return;
                }
            }
            case "logout" -> {
                if (page.getCurrentUserIdx() > -1) {
                    page.changePageHomepageLoggedOut();
                    return;
                }
            }
            case "movies" -> {
                if (page.isHomepageLogged() || page.isUpgrades() || page.isSeeDetails()) {
                    page.changePageMovies();
                    filteredMovieList = movieActions.getNotBannedMovies();
                    movieActions.printDetails(filteredMovieList, output);
                    return;
                }
            }
            case "see details" -> {
               if (page.isMovies() || page.isUpgrades()
                       || page.isSeeDetails() || page.isHomepageLogged()) {
                    page.changePageSeeDetails();
                    currentMovieOnPage = movieActions.seeDetails(action.getMovie(), output);
                    return;
               }
            }
            case "upgrades" -> {
                if (page.isMovies() || page.isUpgrades()
                        || page.isSeeDetails() || page.isHomepageLogged()) {
                    page.changePageUpgrades();
                    return;
                }
            }
            default -> System.out.println("error\n");
        }
        err.pageErr(output);
    }

    /**
     * possible cases for action performed on current page
     */
    public void onPage(final Action action, final ArrayNode output) {
        MovieActions movieActions = new MovieActions(page, movies, users,
                filteredMovieList, currentMovieOnPage);
        Upgrades upgrade = new Upgrades(users, page);
        UserActions userActions = new UserActions(users, page);
        switch (action.getFeature()) {
            case "login" -> {
                if (page.isLogin()) {
                    userActions.login(movieActions, action, output);
                    return;
                }
            }
            case "register" -> {
                if (page.isRegister()) {
                    userActions.register(movieActions, action, output);
                    return;
                }
            }
            case "search" -> {
                if (page.isMovies()) {
                    movieActions.printSearchMovies(action.getStartsWith(), output);
                    return;
                }
            }
            case "filter" -> {
                if (page.isMovies() || page.isSeeDetails()) {
                    FilterActions filterActions = new FilterActions();
                    filterActions.filter(movieActions, action.getFilters(), output);
                    if (action.getFilters().getContains() != null) {
                        filteredMovieList = filterActions.
                                getFilteredMovies(action.getFilters().getContains(), movieActions);
                    }
                    return;
                }
            }
            case "buy tokens" -> {
                upgrade.buyTokens(action, output);
                return;
            }
            case "buy premium account" -> {
                upgrade.buyPremiumAccount(output);
                return;
            }
            case "purchase" -> {
                movieActions.purchaseMovie(output);
                return;
            }
            case "watch" -> {
                movieActions.watchMovie(output);
                return;
            }
            case "like" -> {
                movieActions.likeMovie(output);
                return;
            }
            case "rate" -> {
                movieActions.rateMovie(action.getRate(), output);
                return;
            }
            default -> System.out.println("error\n");
        }
        err.pageErr(output);
    }

    public Pages getPage() {
        return page;
    }

    public void setPage(final Pages page) {
        this.page = page;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(final ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(final ArrayList<Movie> movies) {
        this.movies = movies;
    }
}
