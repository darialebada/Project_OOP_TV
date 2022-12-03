package myclasses;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.Action;
import fileio.FilterInput;
import fileio.Movie;
import fileio.User;
import utils.Errors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static java.util.Comparator.comparing;

public final class Debug {
    private Pages page;
    private ArrayList<User> users;
    private ArrayList<Movie> movies;
    private ArrayList<Movie> currentMovieList = new ArrayList<>();
    private String err = null;
    public void debug(final ArrayList<User> usersList, final ArrayList<Movie> moviesList,
                      final ArrayList<Action> actions, final ArrayNode output) {
        users = new ArrayList<>(usersList);
        movies = new ArrayList<>(moviesList);
        page =  new Pages();
        for (Action action : actions) {
            switch (action.getType()) {
                case "change page" -> changePage(action, output);
                case "on page" -> onPage(action, output);
            }
        }
    }

    public void changePage(final Action action, final ArrayNode output) {
        Errors err = new Errors();
        switch (action.getPage()) {
            case "login" -> {
                if (page.isHomepageLoggedOut()) {
                    page.changePageLogin();
                } else {
                    err.loginErr(output, currentMovieList);
                }
            }
            case "register" -> {
                if (page.isHomepageLoggedOut()) {
                    page.changePageRegister();
                }
            }
            case "logout" -> {
                page.changePageHomepageLoggedOut();
            }
            case "movies" -> {
                if (page.isHomepageLogged()) {
                    page.changePageMovies();

                    ArrayList<Movie> mov = new ArrayList<>();
                    for (Movie movie : movies) {
                        if (!movie.getCountriesBanned().contains(users.get(page.getCurrentUserIdx()).getCredentials().getCountry())) {
                            mov.add(movie);
                        }
                    }
                    printUser(mov, output);
                }
            }
            case "see details" -> {
                if (page.isMovies())
                    seeDetails(action.getMovie(), output);
            }
        }
    }

    public void onPage(final Action action, final ArrayNode output) {
        Errors err = new Errors();
        switch (action.getFeature()) {
            case "login" -> {
                if (page.isLogin()) {
                    login(action, output);
                } else {
                    err.loginErr(output, currentMovieList);
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
                    err.loginErr(output, currentMovieList);
                }
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
        Errors err = new Errors();
        err.loginErr(output, currentMovieList);
    }

    public void register(final Action action, final ArrayNode output) {
        for (User user : users) {
            if (user.getCredentials().getName().equals(action.getCredentials().getName())) {
                page.changePageHomepageLoggedOut();
                Errors err = new Errors();
                err.loginErr(output, currentMovieList);
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
        node.put("error", err);
        node.set("currentMoviesList", objectMapper.convertValue(movieList, JsonNode.class));
        //node.putPOJO("currentMoviesList", movieList);
        node.set("currentUser", objectMapper.convertValue(users.get(page.getCurrentUserIdx()), JsonNode.class));
        output.add(node);
    }

    public void printSearchMovies(final String startsWith, final ArrayNode output) {
        ArrayList<Movie> searchMovieList = new ArrayList<>();
        for (Movie movie : movies) {
            if (movie.getName().contains(startsWith)) {
                searchMovieList.add(movie);
            }
        }
        printUser(searchMovieList, output);
    }

    public void filter(final FilterInput filter, final ArrayNode output) {
        ArrayList<Movie> filteredMovieList = new ArrayList<>();
        if (filter.getContains() == null) {
            for (Movie movie : movies) {
                if (!movie.getCountriesBanned().contains(users.get(page.getCurrentUserIdx()).getCredentials().getCountry())) {
                    filteredMovieList.add(movie);
                }
            }
            //TODO: MODIFY
            switch (filter.getSort().getDuration()) {
                case "decreasing" -> {
                    filteredMovieList.sort(new Comparator<Movie>() {
                        @Override
                        public int compare(Movie o1, Movie o2) {
                            if (o1.getDuration() > o2.getDuration())
                                return -1;
                            else if (o1.getDuration() < o2.getDuration())
                                return 1;
                            else if (o1.getRating() > o2.getRating())
                                return -1;
                            else if (o1.getRating() < o2.getRating())
                                return 1;
                            return 0;
                        }
                    });
                }
                case "increasing" -> {
                    filteredMovieList.sort(new Comparator<Movie>() {
                        @Override
                        public int compare(Movie o1, Movie o2) {
                            if (o1.getDuration() < o2.getDuration())
                                return -1;
                            else if (o1.getDuration() > o2.getDuration())
                                return 1;
                            else if (o1.getRating() < o2.getRating())
                                return -1;
                            else if (o1.getRating() > o2.getRating())
                                return 1;
                            return 0;
                        }
                    });
                }
            }
            printUser(filteredMovieList, output);
        }
    }

    public void seeDetails(final String movieName, final ArrayNode output) {
        for (Movie movie : currentMovieList) {
            if (movie.getName().equals(movieName)) {
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode node = objectMapper.createObjectNode();
                node.put("error", err);
                node.set("currentMoviesList", objectMapper.convertValue(movie, JsonNode.class));
                output.add(node);
                return;
            }
        }
        Errors err = new Errors();
        err.loginErr(output, currentMovieList);
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
