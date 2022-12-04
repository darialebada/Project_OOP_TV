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
    private String currentMovieOnPage;
    private final String error = null;
    private Errors err;
    public void debug(final ArrayList<User> usersList, final ArrayList<Movie> moviesList,
                      final ArrayList<Action> actions, final ArrayNode output) {
        users = new ArrayList<>(usersList);
        movies = new ArrayList<>(moviesList);
        currentMovieList = new ArrayList<>();
        err = new Errors();
        page =  new Pages();
        for (Action action : actions) {
            switch (action.getType()) {
                case "change page" -> changePage(action, output);
                case "on page" -> onPage(action, output);
            }
        }
    }

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
                    getCurrentMovieList(currentMovieList, output);
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
        }
        err.pageErr(output, currentMovieList);
    }

    public void onPage(final Action action, final ArrayNode output) {
        switch (action.getFeature()) {
            case "login" -> {
                if (page.isLogin()) {
                    login(action, output);
                } else {
                    err.pageErr(output, currentMovieList);
                }
            }
            case "register" -> {
                register(action, output);
            }
            case "search" -> {
                if (page.isMovies()) {
                    printSearchMovies(action.getStartsWith(), output);
                }
            }
            case "filter" -> {
                if (page.isMovies()) {
                    filter(action.getFilters(), output);
                } else {
                    err.pageErr(output, currentMovieList);
                }
            }
            case "buy tokens" -> {
                buyTokens(action, output);
            }
            case "buy premium account" -> {
                buyPremiumAccount(output);
            }
            case "purchase" -> {
               // if (action.getObjectType() != null) {
                   // if (action.getObjectType().equals("movie")) {
                        purchaseMovie(output);
                   // }
               // }
            }
            case "watch" -> {
                watchMovie(output);
            }
            case "like" -> {
                likedMovie(output);
            }
            case "rate" -> {
                rateMovie(action.getRate(), output);
            }
        }
    }

    public void login(final Action action, final ArrayNode output) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getCredentials().getName().equals(action.getCredentials().getName()) &&
                users.get(i).getCredentials().getPassword().equals(action.getCredentials().getPassword())) {
                    page.changePageHomepageLogged(i);
                    printUser(currentMovieList, output);
                    return;
            }
        }
        page.changePageHomepageLoggedOut();
        err.pageErr(output, currentMovieList);
    }

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

    public void printUser(final ArrayList<Movie> movieList, final ArrayNode output) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("error", error);
        node.set("currentMoviesList", objectMapper.convertValue(movieList, JsonNode.class));
        //node.putPOJO("currentMoviesList", movieList);
        node.set("currentUser", objectMapper.convertValue(users.get(page.getCurrentUserIdx()), JsonNode.class));
        output.add(node);
    }

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

    public void getCurrentMovieList(ArrayList<Movie> movieList, final ArrayNode output) {
        movieList = getNotBannedMovies();
        printUser(movieList, output);
    }

    public ArrayList<Movie> getNotBannedMovies() {
        ArrayList<Movie> movieList = new ArrayList<>();
        for (Movie movie : movies) {
            if (!movie.getCountriesBanned().contains(users.get(page.getCurrentUserIdx()).getCredentials().getCountry())) {
                movieList.add(movie);
            }
        }
        return movieList;
    }

    public ArrayList<Movie> getFilteredMovies(final ContainsInput contains) {
        ArrayList<Movie> movieList = new ArrayList<>();
        // TODO: MODIFY
        for (Movie movie : movies) {
            if (!movie.getCountriesBanned().contains(users.get(page.getCurrentUserIdx()).getCredentials().getCountry())) {
                if (contains.getActors() != null) {
                    for (String name : contains.getActors()) {
                        if (contains.getActors().contains(name)) {
                            movieList.add(movie);
                        }
                    }
                }
                if (contains.getGenre() != null) {
                    for (String genre : contains.getGenre()) {
                        if (contains.getGenre().contains(genre)) {
                            movieList.add(movie);
                        }
                    }
                }
            }
        }
        return movieList;
    }

    public void filter(final FilterInput filter, final ArrayNode output) {
        ArrayList<Movie> filteredMovieList;
        if (filter.getContains() == null) {
            filteredMovieList = getNotBannedMovies();
        } else {
            filteredMovieList = getFilteredMovies(filter.getContains());
        }
        if (filter.getSort() != null) {
            if (filter.getSort().getDuration() != null) {
                switch (filter.getSort().getDuration()) {
                    case "increasing" -> {
                        filteredMovieList.sort((o1, o2) -> {
                            if (o1.getDuration() > o2.getDuration()) {
                                return -1;
                            } else if (o1.getDuration() < o2.getDuration()) {
                                return 1;
                            } else {
                                if (filter.getSort().getRating().equals("increasing")) {
                                    if (o1.getRating() > o2.getRating()) {
                                        return -1;
                                    } else if (o1.getRating() < o2.getRating()) {
                                        return 1;
                                    }
                                } else {
                                    if (o1.getRating() < o2.getRating()) {
                                        return -1;
                                    } else if (o1.getRating() > o2.getRating()) {
                                        return 1;
                                    }
                                }
                            }
                            return 0;
                        });
                    }
                    case "decreasing" -> {
                        filteredMovieList.sort((o1, o2) -> {
                            if (o1.getDuration() < o2.getDuration()) {
                                return -1;
                            } else if (o1.getDuration() > o2.getDuration()) {
                                return 1;
                            } else {
                                if (filter.getSort().getRating().equals("increasing")) {
                                    if (o1.getRating() > o2.getRating()) {
                                        return -1;
                                    } else if (o1.getRating() < o2.getRating()) {
                                        return 1;
                                    }
                                } else {
                                    if (o1.getRating() < o2.getRating()) {
                                        return -1;
                                    } else if (o1.getRating() > o2.getRating()) {
                                        return 1;
                                    }
                                }
                            }
                            return 0;
                        });
                    }
                }
            } else {
                switch (filter.getSort().getRating()) {
                    case "increasing" -> {
                        filteredMovieList.sort((o1, o2) -> {
                            if (o1.getRating() < o2.getRating()) {
                                return -1;
                            } else if (o1.getRating() > o2.getRating()) {
                                return 1;
                            }
                            return 0;
                        });
                    }
                    case "decreasing" -> {
                        filteredMovieList.sort((o1, o2) -> {
                            if (o1.getRating() > o2.getRating()) {
                                return -1;
                            } else if (o1.getRating() < o2.getRating()) {
                                return 1;
                            }
                            return 0;
                        });
                    }
                }
            }
            printUser(filteredMovieList, output);
        }
    }

    public void seeDetails(final String movieName, final ArrayNode output) {
        ArrayList<Movie> searchedMovies = new ArrayList<>();
        ArrayList<Movie> notBannedMovies = getNotBannedMovies();
        //int ok = 0;
        for (Movie movie : notBannedMovies) {
            if (movie.getName().equals(movieName)) {
                searchedMovies.add(movie);
                currentMovieOnPage = new String(movie.getName());
                printUser(searchedMovies, output);
                return;
            }
        }
        currentMovieOnPage = null;
        err.pageErr(output, currentMovieList);
    }

    public void printMovie(final Movie movie,final ArrayNode output) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("error", error);
        node.set("currentMoviesList", objectMapper.convertValue(movie, JsonNode.class));
        output.add(node);
    }

    public void buyTokens(final Action action, final ArrayNode output) {
        int nrTokens = users.get(page.getCurrentUserIdx()).getTokensCount();
        int newBalance = Integer.parseInt(users.get(page.getCurrentUserIdx()).getCredentials().getBalance());
        int count = Integer.parseInt(action.getCount());
        if (newBalance < count) {
            err.pageErr(output, currentMovieList);
            return;
        }
        newBalance -= count;
        nrTokens += count;
        users.get(page.getCurrentUserIdx()).setTokensCount(nrTokens);
        users.get(page.getCurrentUserIdx()).getCredentials().setBalance(Integer.toString(newBalance));
    }

    public void buyPremiumAccount(final ArrayNode output) {
        int nrTokens = users.get(page.getCurrentUserIdx()).getTokensCount();
        if (nrTokens < Constants.premiumPrice) {
            err.pageErr(output, currentMovieList);
            return;
        }
        nrTokens -= Constants.premiumPrice;
        users.get(page.getCurrentUserIdx()).setTokensCount(nrTokens);
        users.get(page.getCurrentUserIdx()).getCredentials().setAccountType("premium");
    }

    public void purchaseMovie(final ArrayNode output) {
        int numTokens = users.get(page.getCurrentUserIdx()).getTokensCount();
        int numFreePremiumMovies = users.get(page.getCurrentUserIdx()).getNumFreePremiumMovies();
        if (!(users.get(page.getCurrentUserIdx()).getCredentials().getAccountType().equals("premium")
            && numFreePremiumMovies > 0)) {
                if (numTokens < Constants.moviePrice) {
                    return;
                }
        }

        ArrayList<Movie> notBannedMovies = getNotBannedMovies();
        for (Movie movie : notBannedMovies) {
            if (movie.getName().equals(currentMovieOnPage)) {
                ArrayList<Movie> mov = new ArrayList<>();
                mov.add(movie);
                users.get(page.getCurrentUserIdx()).getPurchasedMovies().add(movie);
                if (numFreePremiumMovies > 0 && users.get(page.getCurrentUserIdx()).getCredentials().getAccountType().equals("premium")) {
                    users.get(page.getCurrentUserIdx()).setNumFreePremiumMovies(numFreePremiumMovies - 1);
                } else {
                    users.get(page.getCurrentUserIdx()).setTokensCount(numTokens - Constants.moviePrice);
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
        if (rate > 5) {
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
                movie.setRating((int)sum);

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

    public ArrayList<Movie> getCurrentMovieList() {
        return currentMovieList;
    }

    public void setCurrentMovieList(final ArrayList<Movie> currentMovieList) {
        this.currentMovieList = currentMovieList;
    }
}
