package management;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.Movie;
import fileio.User;
import utils.Constants;
import utils.Errors;

import java.util.ArrayList;

public class MovieActions {
    private final ArrayList<User> users;
    private final ArrayList<Movie> movies;
    private final String currentMovieOnPage;
    private final ArrayList<Movie> filteredMovieList;
    private final int currentUserIdx;
    private final Errors err = Errors.getErrorsInstance();
    public MovieActions(final ArrayList<Movie> movies,
                        final ArrayList<User> users, final ArrayList<Movie> filteredMovieList,
                        final int currentUserIdx, final String currentMovieOnPage) {
        this.users = users;
        this.movies = movies;
        this.filteredMovieList = filteredMovieList;
        this.currentMovieOnPage = currentMovieOnPage;
        this.currentUserIdx = currentUserIdx;
    }

    /**
     * print credentials and info for current user
     * @param movieList - movie list available for current user
     * @param output for output file
     */
    public void printDetails(final ArrayList<Movie> movieList, final ArrayNode output,
                             final int currentUserIdx) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        String error = null;
        node.put("error", error);
        node.set("currentMoviesList", objectMapper.convertValue(movieList, JsonNode.class));
        node.set("currentUser", objectMapper.convertValue(users.get(currentUserIdx),
                JsonNode.class));
        output.add(node);
    }

    /**
     * @return list of movies available for current user
     */
    public ArrayList<Movie> getNotBannedMovies() {
        ArrayList<Movie> movieList = new ArrayList<>();
        for (Movie movie : movies) {
            if (!movie.getCountriesBanned().contains(users.get(currentUserIdx).
                    getCredentials().getCountry())) {
                movieList.add(movie);
            }
        }
        return movieList;
    }

    /**
     * @param movieName - movie wanted by user
     * @param output for output data
     */
    public String seeDetails(final String movieName, final ArrayNode output) {
        String currentMovie;
        ArrayList<Movie> searchedMovies = new ArrayList<>();
        for (Movie movie : filteredMovieList) {
            if (movie.getName().equals(movieName)) {
                searchedMovies.add(movie);
                currentMovie = movie.getName();
                printDetails(searchedMovies, output, currentUserIdx);
                return currentMovie;
            }
        }
        currentMovie = null;
        err.pageErr(output);
        return currentMovie;
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
        printDetails(searchMovieList, output, currentUserIdx);
    }

    /**
     * purchase movie
     */
    public void purchaseMovie(final ArrayNode output) {
        int numTokens = users.get(currentUserIdx).getTokensCount();
        int numFreePremiumMovies = users.get(currentUserIdx).
                getNumFreePremiumMovies();

        boolean freePremiumMovies = getPurchaseType(numFreePremiumMovies);

        if (!(users.get(currentUserIdx).getCredentials().getAccountType().
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
                users.get(currentUserIdx).getPurchasedMovies().add(movie);
                if (freePremiumMovies) {
                    users.get(currentUserIdx).
                            setNumFreePremiumMovies(numFreePremiumMovies - 1);
                } else {
                    users.get(currentUserIdx).
                            setTokensCount(numTokens - Constants.MOVIE_PRICE);
                }
                printDetails(mov, output, currentUserIdx);
                return;
            }
        }
        err.pageErr(output);
    }

    /**
     * @param numFreePremiumMovies = number of free premium movies
     * @return true if there are free premium movies available
     */
    public boolean getPurchaseType(final int numFreePremiumMovies) {
        return numFreePremiumMovies > 0 && users.get(currentUserIdx).
                getCredentials().getAccountType().equals("premium");
    }

    /**
     * watch movie
     */
    public void watchMovie(final ArrayNode output) {
        for (Movie movie : users.get(currentUserIdx).getPurchasedMovies()) {
            if (movie.getName().equals(currentMovieOnPage)) {
                ArrayList<Movie> mov = new ArrayList<>();
                mov.add(movie);
                users.get(currentUserIdx).getWatchedMovies().add(movie);
                printDetails(mov, output, currentUserIdx);
                return;
            }
        }
        err.pageErr(output);
    }

    /**
     * like movie
     */
    public void likeMovie(final ArrayNode output) {
        for (Movie movie : users.get(currentUserIdx).getWatchedMovies()) {
            if (movie.getName().equals(currentMovieOnPage)) {
                movie.setNumLikes(movie.getNumLikes() + 1);
                ArrayList<Movie> mov = new ArrayList<>();
                mov.add(movie);
                users.get(currentUserIdx).getLikedMovies().add(movie);
                printDetails(mov, output, currentUserIdx);
                return;
            }
        }
        err.pageErr(output);
    }

    /**
     * rate movie
     */
    public void rateMovie(final double rate, final ArrayNode output) {
        if (rate > Constants.MAX_RATE) {
            err.pageErr(output);
            return;
        }
        for (Movie movie : users.get(currentUserIdx).getWatchedMovies()) {
            if (movie.getName().equals(currentMovieOnPage)) {
                movie.setNumRatings(movie.getNumRatings() + 1);
                movie.getRatingsList().add(rate);
                double sum = 0;
                for (Double rating : movie.getRatingsList()) {
                    sum += rating;
                }
                movie.setRating(sum);
                ArrayList<Movie> mov = new ArrayList<>();
                mov.add(movie);
                users.get(currentUserIdx).getRatedMovies().add(movie);
                printDetails(mov, output, currentUserIdx);
                return;
            }
        }
        err.pageErr(output);
    }
}
