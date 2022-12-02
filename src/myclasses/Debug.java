package myclasses;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.Action;
import fileio.Movie;
import fileio.User;
import utils.Errors;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public final class Debug {
    private Pages page;
    private ArrayList<User> users;
    private ArrayList<Movie> movies;
    private ArrayList<Movie> currentMovieList = new ArrayList<>();
    private String err = null;
    public void debug(final ArrayList<User> usersList, final ArrayList<Movie> moviesList,
                      final ArrayList<Action> actions, final ArrayNode output) {
        //ChangePage pg = new ChangePage();
        //OnPage pgOn = new OnPage();
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
        }
    }

    public void login(final Action action, final ArrayNode output) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getCredentials().getName().equals(action.getCredentials().getName()) &&
                users.get(i).getCredentials().getPassword().equals(action.getCredentials().getPassword())) {
                    page.changePageHomepageLogged(i);
                    printUser(users.get(i), output);
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
        printUser(newUser, output);
        page.changePageHomepageLogged(users.size() - 1);
    }

    public void printUser(final User user, final ArrayNode output) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("error", err);
        //node.set("currentMoviesList", objectMapper.convertValue(currentMovieList, JsonNode.class));
        node.putPOJO("currentMoviesList", currentMovieList);
        node.set("currentUser", objectMapper.convertValue(user, JsonNode.class));
        output.add(node);
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
