package management;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.Action;
import fileio.Movie;
import fileio.User;
import pages.Page;
import pages.PageFactory;
import utils.Errors;

import java.util.ArrayList;

public final class AppManager {
    private Page page = PageFactory.createPage("homeLoggedOut");
    private ArrayList<User> users;
    private ArrayList<Movie> movies;
    private ArrayList<Movie> filteredMovieList = new ArrayList<>();
    private ArrayList<Movie> currentMovieList = new ArrayList<>();
    private String currentMovieOnPage;
    private int currentUserIdx;
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
                filteredMovieList, currentUserIdx, currentMovieOnPage);
        switch (action.getPage()) {
            case "login" -> {
                if (page.isNextPageCorrect("login")) {
                    page = PageFactory.createPage("login");
                    return;
                }
            }
            case "register" -> {
                if (page.isNextPageCorrect("register")) {
                    page = PageFactory.createPage("register");
                    return;
                }
            }
            case "logout" -> {
                if (page.isNextPageCorrect("logout")) {
                    page = PageFactory.createPage("homeLoggedOut");
                    return;
                }
            }
            case "movies" -> {
                if (page.isNextPageCorrect("movies")) {
                    page = PageFactory.createPage("movies");
                    filteredMovieList = movieActions.getNotBannedMovies();
                    movieActions.printDetails(filteredMovieList, output, currentUserIdx);
                    return;
                }
            }
            case "see details" -> {
               if (page.isNextPageCorrect("see details")) {
                    page = PageFactory.createPage("see details");
                    currentMovieOnPage = movieActions.seeDetails(action.getMovie(), output);
                    return;
               }
            }
            case "upgrades" -> {
                if (page.isNextPageCorrect("upgrades")) {
                    page = PageFactory.createPage("upgrades");
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
                filteredMovieList, currentUserIdx, currentMovieOnPage);
        Upgrades upgrade = new Upgrades(users, page);
        switch (action.getFeature()) {
            case "login" -> {
                if (page.getType().equals("login")) {
                    login(movieActions, action, output);
                    //page.setCurrentUserIdx();
                    return;
                }
            }
            case "register" -> {
                if (page.getType().equals("register")) {
                    register(movieActions, action, output);
                    return;
                }
            }
            case "search" -> {
                if (page.getType().equals("movies")) {
                    movieActions.printSearchMovies(action.getStartsWith(), output);
                    return;
                }
            }
            case "filter" -> {
                if (page.getType().equals("movies") || page.getType().equals("see details")) {
                    FilterActions filterActions = new FilterActions();
                    filterActions.filter(movieActions, action.getFilters(), output, currentUserIdx);
                    if (action.getFilters().getContains() != null) {
                        filteredMovieList = filterActions.
                                getFilteredMovies(action.getFilters().getContains(), movieActions);
                    }
                    return;
                }
            }
            case "buy tokens" -> {
                upgrade.buyTokens(action, output, currentUserIdx);
                return;
            }
            case "buy premium account" -> {
                upgrade.buyPremiumAccount(output, currentUserIdx);
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

    /**
     * check if there is a valid user with given credentials
     */
    public void login(final MovieActions movieActions,
                      final Action action, final ArrayNode output) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getCredentials().getName().equals(action.getCredentials().getName())
                    && users.get(i).getCredentials().getPassword().equals(action.getCredentials().
                    getPassword())) {
                /* valid login credentials -> print current user's details */
                page = PageFactory.createPage("homeLoggedIn");
                currentUserIdx = i;
                movieActions.printDetails(currentMovieList, output, currentUserIdx);
                return;
            }
        }
        page = PageFactory.createPage("homeLoggedOut");
        err.pageErr(output);
    }

    /**
     * register a new user (add it to userList)
     */
    public void register(final MovieActions movieActions,
                         final Action action, final ArrayNode output) {
        for (User user : users) {
            if (user.getCredentials().getName().equals(action.getCredentials().getName())) {
                page = PageFactory.createPage("homeLoggedOut");
                err.pageErr(output);
                return;
            }
        }
        User newUser = new User(action.getCredentials());
        users.add(newUser);
        page = PageFactory.createPage("homeLoggedIn");
        currentUserIdx = users.size() - 1;
        /* print new user -> current user */
        movieActions.printDetails(currentMovieList, output, currentUserIdx);
    }

    public Page getPage() {
        return page;
    }

    public void setPage(final Page page) {
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
