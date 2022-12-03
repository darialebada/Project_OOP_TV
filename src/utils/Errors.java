package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.Movie;

import java.util.ArrayList;

public class Errors {
    private String user = null;
    public Errors() {
    }

    public void pageErr(final ArrayNode output, final ArrayList<Movie> currentMovieList) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("error", "Error");
        node.set("currentMoviesList", objectMapper.convertValue(currentMovieList, JsonNode.class));
        //node.putPOJO("currentMoviesList", currentMovieList);
        node.put("currentUser", user);
        output.add(node);
    }
}
