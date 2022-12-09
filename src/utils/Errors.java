package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.Movie;

import java.util.ArrayList;

public final class Errors {
    /* singleton design instance */
    private static Errors errorsInstance = null;
    private final String user = null;
    private final ArrayList<Movie> currentMovieList = new ArrayList<>();
    private Errors() {
    }
    public static Errors getErrorsInstance() {
        if (errorsInstance == null) {
            errorsInstance = new Errors();
        }
        return errorsInstance;
    }

    /**
     * error in case of wrong action
     *
     * @param output for where to write data
     */
    public void pageErr(final ArrayNode output) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("error", "Error");
        node.set("currentMoviesList", objectMapper.convertValue(currentMovieList, JsonNode.class));
        node.put("currentUser", user);
        output.add(node);
    }
}
