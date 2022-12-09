package app.management;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.Action;
import fileio.Movie;
import fileio.User;
import utils.Errors;

import java.util.ArrayList;

public final class UserActions {
    private ArrayList<User> users;
    private Pages page;
    private final ArrayList<Movie> currentMovieList = new ArrayList<>();
    private Errors err = Errors.getErrorsInstance();

    public UserActions(final ArrayList<User> users, final Pages page) {
        this.users = users;
        this.page = page;
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
                page.changePageHomepageLogged(i);
                movieActions.printDetails(currentMovieList, output);
                return;
            }
        }
        page.changePageHomepageLoggedOut();
        err.pageErr(output);
    }

    /**
     * register a new user (add it to userList)
     */
    public void register(final MovieActions movieActions,
                         final Action action, final ArrayNode output) {
        for (User user : users) {
            if (user.getCredentials().getName().equals(action.getCredentials().getName())) {
                page.changePageHomepageLoggedOut();
                err.pageErr(output);
                return;
            }
        }
        User newUser = new User(action.getCredentials());
        users.add(newUser);
        page.changePageHomepageLogged(users.size() - 1);
        /* print new user -> current user */
        movieActions.printDetails(currentMovieList, output);
    }
}
