package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.Movie;

import java.util.ArrayList;

public class Errors {
    public Errors() {
    }

    public void loginErr(final ArrayNode output, final ArrayList<Movie> currentMovieList) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("error", "Error");
        //node.set("currentMoviesList", objectMapper.convertValue(currentMovieList, JsonNode.class));
        node.putPOJO("currentMoviesList", currentMovieList);
        String us = null;
        node.put("currentUser", us);
        output.add(node);
    }
}
