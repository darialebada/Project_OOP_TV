package myclasses;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.Action;
import fileio.ContainsInput;
import fileio.FilterInput;
import fileio.Movie;
import fileio.User;
import utils.Constants;
import utils.Errors;

import java.util.ArrayList;

public final class Debug {
    private Pages page;
    private ArrayList<User> users;
    private ArrayList<Movie> movies;
    private ArrayList<Movie> currentMovieList;
    private ArrayList<Movie> filteredMovieList;
    private String currentMovieOnPage;
    private final String error = null;
    private Errors err;

    /**
     * get the type of wanted action
     */
    public void debug(final ArrayList<User> usersList, final ArrayList<Movie> moviesList,
                      final ArrayList<Action> actions, final ArrayNode output) {
        users = new ArrayList<>(usersList);
        movies = new ArrayList<>(moviesList);
        currentMovieList = new ArrayList<>();
        filteredMovieList = new ArrayList<>();
        err = new Errors();
        page =  new Pages();
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
                    filteredMovieList = getNotBannedMovies();
                    getCurrentMovieList(output);
                    return;
                }
            }
            case "see details" -> {
               if (!page.isHomepageLoggedOut()) {
                    page.changePageSeeDetails();
                    seeDetails(action.getMovie(), output);
                    return;
               }
            }
            case "upgrades" -> {
                if (!page.isHomepageLoggedOut()) {
                    page.changePageUpgrades();
                    return;
                }
            }
            default -> System.out.println("error\n");
        }
        err.pageErr(output, currentMovieList);
    }

    /**
     * possible cases for action performed on current page
     */
    public void onPage(final Action action, final ArrayNode output) {
        switch (action.getFeature()) {
            case "login" -> {
                if (page.isLogin()) {
                    login(action, output);
                } else {
                    err.pageErr(output, currentMovieList);
                }
            }
            case "register" -> register(action, output);
            case "search" -> {
                if (page.isMovies()) {
                    printSearchMovies(action.getStartsWith(), output);
                }
            }
            case "filter" -> {
                if (page.isMovies() || page.isSeeDetails()) {
                    filter(action.getFilters(), output);
                } else {
                    err.pageErr(output, currentMovieList);
                }
            }
            case "buy tokens" -> buyTokens(action, output);
            case "buy premium account" -> buyPremiumAccount(output);
            case "purchase" -> purchaseMovie(output);
            case "watch" -> watchMovie(output);
            case "like" -> likedMovie(output);
            case "rate" -> rateMovie(action.getRate(), output);
            default -> System.out.println("error\n");
        }
    }

    /**
     * check if there is a valid user with given credentials
     */
    public void login(final Action action, final ArrayNode output) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getCredentials().getName().equals(action.getCredentials().getName())
                && users.get(i).getCredentials().getPassword().equals(action.getCredentials().
                getPassword())) {
                    page.changePageHomepageLogged(i);
                    printUser(currentMovieList, output);
                    return;
            }
        }
        page.changePageHomepageLoggedOut();
        err.pageErr(output, currentMovieList);
    }

    /**
     * register a new user (add it to userList)
     */
    public void register(final Action action, final ArrayNode output) {
        for (User user : users) {
            if (user.getCredentials().getName().equals(action.getCredentials().getName())) {
                page.changePageHomepageLoggedOut();
                err.pageErr(output, currentMovieList);
                return;
            }
        }
        User newUser = new User(action.getCredentials());
        users.add(newUser);
        page.changePageHomepageLogged(users.size() - 1);
        printUser(currentMovieList, output);
    }

    /**
     * print credentials and info for current user
     * @param movieList - movie list available for current user
     * @param output for output file
     */
    public void printUser(final ArrayList<Movie> movieList, final ArrayNode output) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("error", error);
        node.set("currentMoviesList", objectMapper.convertValue(movieList, JsonNode.class));
        node.set("currentUser", objectMapper.convertValue(users.get(page.getCurrentUserIdx()),
                JsonNode.class));
        output.add(node);
    }

    /**
     * find movies starting with given string
     * @param startsWith - what to search for
     * @param output for output file
     */
    public void printSearchMovies(final String startsWith, final ArrayNode output) {
        ArrayList<Movie> notBannedMovies = getNotBannedMovies();
        ArrayList<Movie> searchMovieList = new ArrayList<>();
        for (Movie movie : notBannedMovies) {
            if (movie.getName().startsWith(startsWith)) {
                searchMovieList.add(movie);
            }
        }
        printUser(searchMovieList, output);
    }

    /**
     * print all available movies for current user filtered or not
     */
    public void getCurrentMovieList(final ArrayNode output) {
        //ArrayList<Movie> movieList = getNotBannedMovies();
        //printUser(movieList, output);
        printUser(filteredMovieList, output);
    }

    /**
     * @return list of movies available for current user
     */
    public ArrayList<Movie> getNotBannedMovies() {
        ArrayList<Movie> movieList = new ArrayList<>();
        for (Movie movie : movies) {
            if (!movie.getCountriesBanned().contains(users.get(page.getCurrentUserIdx()).
                    getCredentials().getCountry())) {
                movieList.add(movie);
            }
        }
        return movieList;
    }

    /**
     * @return movie list filtered after actors/ genres
     */
    public ArrayList<Movie> getFilteredMovies(final ContainsInput contains) {
        ArrayList<Movie> movieList = new ArrayList<>();
        ArrayList<Movie> notBannedMovies = getNotBannedMovies();
        for (Movie movie : notBannedMovies) {
            if (contains.getActors() != null) {
                for (String name : contains.getActors()) {
                    if (movie.getActors().contains(name)) {
                        if (contains.getGenre() != null) {
                            for (String genre : contains.getGenre()) {
                                if (movie.getGenres().contains(genre)) {
                                    movieList.add(movie);
                                }
                            }
                        } else {
                            movieList.add(movie);
                        }
                    }
                }
            }
            if (contains.getGenre() != null && contains.getActors() == null) {
                for (String genre : contains.getGenre()) {
                    if (movie.getGenres().contains(genre)) {
                        movieList.add(movie);
                    }
                }
            }
        }
        filteredMovieList = new ArrayList<>(movieList);
        return movieList;
    }

    /**
     * sorts movie list by duration/ rating
     */
    public void filter(final FilterInput filter, final ArrayNode output) {
        ArrayList<Movie> filteredMovies;
        if (filter.getContains() == null) {
            filteredMovies = getNotBannedMovies();
        } else {
            filteredMovies = getFilteredMovies(filter.getContains());
        }
        if (filter.getSort() != null) {
            if (filter.getSort().getDuration() != null) {
                switch (filter.getSort().getDuration()) {
                    case "increasing" -> filteredMovies.sort((o1, o2) -> {
                        if (o1.getDuration() < o2.getDuration()) {
                            return -1;
                        } else if (o1.getDuration() > o2.getDuration()) {
                            return 1;
                        } else {
                            if (filter.getSort().getRating().equals("increasing")) {
                                if (o1.getRating() < o2.getRating()) {
                                    return -1;
                                } else if (o1.getRating() > o2.getRating()) {
                                    return 1;
                                }
                            } else {
                                if (o1.getRating() > o2.getRating()) {
                                    return -1;
                                } else if (o1.getRating() < o2.getRating()) {
                                    return 1;
                                }
                            }
                        }
                        return 0;
                    });
                    case "decreasing" -> filteredMovies.sort((o1, o2) -> {
                        if (o1.getDuration() > o2.getDuration()) {
                            return -1;
                        } else if (o1.getDuration() < o2.getDuration()) {
                            return 1;
                        } else {
                            if (filter.getSort().getRating().equals("increasing")) {
                                if (o1.getRating() < o2.getRating()) {
                                    return -1;
                                } else if (o1.getRating() > o2.getRating()) {
                                    return 1;
                                }
                            } else {
                                if (o1.getRating() > o2.getRating()) {
                                    return -1;
                                } else if (o1.getRating() < o2.getRating()) {
                                    return 1;
                                }
                            }
                        }
                        return 0;
                    });
                    default -> System.out.println("error\n");
                }
            } else {
                switch (filter.getSort().getRating()) {
                    case "increasing" -> filteredMovies.sort((o1, o2) -> {
                        if (o1.getRating() < o2.getRating()) {
                            return -1;
                        } else if (o1.getRating() > o2.getRating()) {
                            return 1;
                        }
                        return 0;
                    });
                    case "decreasing" -> filteredMovies.sort((o1, o2) -> {
                        if (o1.getRating() > o2.getRating()) {
                            return -1;
                        } else if (o1.getRating() < o2.getRating()) {
                            return 1;
                        }
                        return 0;
                    });
                    default -> System.out.println("error\n");
                }
            }
            printUser(filteredMovies, output);
        } else if (filter.getContains() != null) {
            printUser(filteredMovies, output);
        }
    }

    /**
     * @param movieName - movie wanted by user
     * @param output for output data
     */
    public void seeDetails(final String movieName, final ArrayNode output) {
        ArrayList<Movie> searchedMovies = new ArrayList<>();
        //ArrayList<Movie> notBannedMovies = getNotBannedMovies();
        for (Movie movie : filteredMovieList) {
            if (movie.getName().equals(movieName)) {
                searchedMovies.add(movie);
                currentMovieOnPage = movie.getName();
                printUser(searchedMovies, output);
                return;
            }
        }
        currentMovieOnPage = null;
        err.pageErr(output, currentMovieList);
    }

    /*
    public void printMovie(final Movie movie,final ArrayNode output) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("error", error);
        node.set("currentMoviesList", objectMapper.convertValue(movie, JsonNode.class));
        output.add(node);
    }
     */

    /**
     * action for buying tokens
     */
    public void buyTokens(final Action action, final ArrayNode output) {
        int nrTokens = users.get(page.getCurrentUserIdx()).getTokensCount();
        int newBalance = Integer.parseInt(users.get(page.getCurrentUserIdx()).
                getCredentials().getBalance());
        int count = Integer.parseInt(action.getCount());
        if (newBalance < count) {
            err.pageErr(output, currentMovieList);
            return;
        }
        newBalance -= count;
        nrTokens += count;
        users.get(page.getCurrentUserIdx()).setTokensCount(nrTokens);
        users.get(page.getCurrentUserIdx()).getCredentials().
                setBalance(Integer.toString(newBalance));
    }

    public void buyPremiumAccount(final ArrayNode output) {
        int nrTokens = users.get(page.getCurrentUserIdx()).getTokensCount();
        if (nrTokens < Constants.PREMIUM_PRICE) {
            err.pageErr(output, currentMovieList);
            return;
        }
        nrTokens -= Constants.PREMIUM_PRICE;
        users.get(page.getCurrentUserIdx()).setTokensCount(nrTokens);
        users.get(page.getCurrentUserIdx()).getCredentials().setAccountType("premium");
    }


    public void purchaseMovie(final ArrayNode output) {
        int numTokens = users.get(page.getCurrentUserIdx()).getTokensCount();
        int numFreePremiumMovies = users.get(page.getCurrentUserIdx()).
                getNumFreePremiumMovies();
        if (!(users.get(page.getCurrentUserIdx()).getCredentials().getAccountType().
                equals("premium") && numFreePremiumMovies > 0)) {
                if (numTokens < Constants.MOVIE_PRICE) {
                    return;
                }
        }

        ArrayList<Movie> notBannedMovies = getNotBannedMovies();
        for (Movie movie : notBannedMovies) {
            if (movie.getName().equals(currentMovieOnPage)) {
                ArrayList<Movie> mov = new ArrayList<>();
                mov.add(movie);
                users.get(page.getCurrentUserIdx()).getPurchasedMovies().add(movie);
                if (numFreePremiumMovies > 0 && users.get(page.getCurrentUserIdx()).
                        getCredentials().getAccountType().equals("premium")) {
                    users.get(page.getCurrentUserIdx()).
                            setNumFreePremiumMovies(numFreePremiumMovies - 1);
                } else {
                    users.get(page.getCurrentUserIdx()).
                            setTokensCount(numTokens - Constants.MOVIE_PRICE);
                }
                printUser(mov, output);
                return;
            }
        }
        err.pageErr(output, currentMovieList);
    }

    public void watchMovie(final ArrayNode output) {
        for (Movie movie : users.get(page.getCurrentUserIdx()).getPurchasedMovies()) {
            if (movie.getName().equals(currentMovieOnPage)) {
                ArrayList<Movie> mov = new ArrayList<>();
                mov.add(movie);
                users.get(page.getCurrentUserIdx()).getWatchedMovies().add(movie);
                printUser(mov, output);
                return;
            }
        }
        err.pageErr(output, currentMovieList);
    }

    public void likedMovie(final ArrayNode output) {
        for (Movie movie : users.get(page.getCurrentUserIdx()).getWatchedMovies()) {
            if (movie.getName().equals(currentMovieOnPage)) {
                movie.setNumLikes(movie.getNumLikes() + 1);
                ArrayList<Movie> mov = new ArrayList<>();
                mov.add(movie);
                users.get(page.getCurrentUserIdx()).getLikedMovies().add(movie);
                printUser(mov, output);
                return;
            }
        }
        err.pageErr(output, currentMovieList);
    }

    public void rateMovie(final int rate, final ArrayNode output) {
        if (rate > Constants.MAX_RATE) {
            err.pageErr(output, currentMovieList);
            return;
        }
        for (Movie movie : users.get(page.getCurrentUserIdx()).getWatchedMovies()) {
            if (movie.getName().equals(currentMovieOnPage)) {
                movie.setNumRatings(movie.getNumRatings() + 1);
                movie.getRatingsList().add(rate);
                double sum = 0;
                for (Integer rating : movie.getRatingsList()) {
                    sum += rating;
                }
                movie.setRating((int) sum);
                ArrayList<Movie> mov = new ArrayList<>();
                mov.add(movie);
                users.get(page.getCurrentUserIdx()).getRatedMovies().add(movie);
                printUser(mov, output);
                return;
            }
        }
        err.pageErr(output, currentMovieList);
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

    public String getError() {
        return error;
    }
}
