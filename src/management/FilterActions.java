package management;

import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.ContainsInput;
import fileio.FilterInput;
import fileio.Movie;

import java.util.ArrayList;

public final class FilterActions {
    /**
     * sorts movie list by duration/ rating
     */
    public void filter(final MovieActions movieActions, final FilterInput filter,
                       final ArrayNode output) {
        ArrayList<Movie> filteredMovies;
        if (filter.getContains() == null) {
            filteredMovies = movieActions.getNotBannedMovies();
        } else {
            filteredMovies = getFilteredMovies(filter.getContains(), movieActions);
        }
        /* sort */
        if (filter.getSort() != null) {
            if (filter.getSort().getDuration() != null) {
                /* sort after duration */
                switch (filter.getSort().getDuration()) {
                    case "increasing" -> sortByDurationIncreasing(filter, filteredMovies);
                    case "decreasing" -> sortByDurationDecreasing(filter, filteredMovies);
                    default -> System.out.println("error\n");
                }
            } else {
                /* sort after rating */
                switch (filter.getSort().getRating()) {
                    case "increasing" -> sortByRatingIncreasing(filteredMovies);
                    case "decreasing" -> sortByRatingDecreasing(filteredMovies);
                    default -> System.out.println("error\n");
                }
            }
            movieActions.printDetails(filteredMovies, output);
        } else if (filter.getContains() != null) {
            movieActions.printDetails(filteredMovies, output);
        }
    }

    /**
     * @return movie list filtered after actors/ genres
     */
    public ArrayList<Movie> getFilteredMovies(final ContainsInput contains,
                                              final MovieActions movieActions) {
        ArrayList<Movie> movieList = new ArrayList<>();
        ArrayList<Movie> notBannedMovies = movieActions.getNotBannedMovies();
        for (Movie movie : notBannedMovies) {
            int ok = 1;
            /* check if requested actors appear in movie */
            if (contains.getActors() != null) {
                ok = filterAfterActors(contains, movie);
            }
            /* check if movie is requested genre */
            if (contains.getGenre() != null && ok == 1) {
                ok = filterAfterGenre(contains, movie);
            }
            if (ok == 1) {
                movieList.add(movie);
            }
        }
        return movieList;
    }

    /**
     * @return 1 if the movie contains requested actors
     */
    public int filterAfterActors(final ContainsInput contains, final Movie movie) {
        for (String name : contains.getActors()) {
            if (!movie.getActors().contains(name)) {
                return 0;
            }
        }
        return 1;
    }

    /**
     * @return 1 if the movie contains requested genres
     */
    public int filterAfterGenre(final ContainsInput contains, final Movie movie) {
        for (String genre : contains.getGenre()) {
            if (!movie.getGenres().contains(genre)) {
                return 0;
            }
        }
        return 1;
    }

    /**
     * sort after duration increasing
     */
    public void sortByDurationIncreasing(final FilterInput filter,
                                         final ArrayList<Movie> filteredMovies) {
        filteredMovies.sort((o1, o2) -> {
            if (o1.getDuration() < o2.getDuration()) {
                return -1;
            } else if (o1.getDuration() > o2.getDuration()) {
                return 1;
            } else {
                switch (filter.getSort().getRating()) {
                    case "increasing" -> {
                        if (o1.getRating() < o2.getRating()) {
                            return -1;
                        } else if (o1.getRating() > o2.getRating()) {
                            return 1;
                        }
                    }
                    case "decreasing" -> {
                        if (o1.getRating() > o2.getRating()) {
                            return -1;
                        } else if (o1.getRating() < o2.getRating()) {
                            return 1;
                        }
                    }
                    default -> System.out.println("error\n");
                }
            }
            return 0;
        });
    }

    /**
     * sort after duration decreasing
     */
    public void sortByDurationDecreasing(final FilterInput filter,
                                         final ArrayList<Movie> filteredMovies) {
        filteredMovies.sort((o1, o2) -> {
            if (o1.getDuration() > o2.getDuration()) {
                return -1;
            } else if (o1.getDuration() < o2.getDuration()) {
                return 1;
            } else {
                switch (filter.getSort().getRating()) {
                    case "increasing" -> {
                        if (o1.getRating() < o2.getRating()) {
                            return -1;
                        } else if (o1.getRating() > o2.getRating()) {
                            return 1;
                        }
                    }
                    case "decreasing" -> {
                        if (o1.getRating() > o2.getRating()) {
                            return -1;
                        } else if (o1.getRating() < o2.getRating()) {
                            return 1;
                        }
                    }
                    default -> System.out.println("error\n");
                }
            }
            return 0;
        });
    }

    /**
     * sort after rating increasing
     */
    public void sortByRatingIncreasing(final ArrayList<Movie> filteredMovies) {
        filteredMovies.sort((o1, o2) -> {
            if (o1.getRating() < o2.getRating()) {
                return -1;
            } else if (o1.getRating() > o2.getRating()) {
                return 1;
            }
            return 0;
        });
    }

    /**
     * sort after rating decreasing
     */
    public void sortByRatingDecreasing(final ArrayList<Movie> filteredMovies) {
        filteredMovies.sort((o1, o2) -> {
            if (o1.getRating() > o2.getRating()) {
                return -1;
            } else if (o1.getRating() < o2.getRating()) {
                return 1;
            }
            return 0;
        });
    }
}
