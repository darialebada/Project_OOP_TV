package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.Movie;

import java.util.ArrayList;

public final class Errors {
    private final String user;
    public Errors() {
        user = null;
    }

    /**
     * error in case of wrong action
     * @param output for where to write data
     * @param currentMovieList for list of current movies (null for error)
     */
    public void pageErr(final ArrayNode output, final ArrayList<Movie> currentMovieList) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("error", "Error");
        node.set("currentMoviesList", objectMapper.convertValue(currentMovieList, JsonNode.class));
        node.put("currentUser", user);
        output.add(node);
    }
}
