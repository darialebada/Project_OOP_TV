import myclasses.Debug;
import fileio.Input;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import utils.Constants;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class Main {
    private Main() {
    }
    public static void main(final String[] args) throws IOException {
        File out = new File(args[1]);
        String filename = args[0];
        boolean isCreated = out.createNewFile();
        if (isCreated) {
            action(filename, out.getName());
        }
    }

    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        /*
        Input inputData = objectMapper.readValue(new File(filePath1),
                Input.class);
         */
        Input inputData = objectMapper.readValue(new File("checker/resources/in/basic_1.json"),
                Input.class);

        ArrayNode output = objectMapper.createArrayNode();

        Debug commands = new Debug();
        commands.debug(inputData.getUsers(), inputData.getMovies(),
                inputData.getActions(), output);


        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
        objectWriter.writeValue(new File("out.json"), output);
    }
}

/*
public class Main {
    private Main() {
    }
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            String filepath = Constants.OUT_PATH + file.getName();
            //String filepath = "results.out";
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }
    }

    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Input inputData = objectMapper.readValue(new File(Constants.TESTS_PATH + filePath1),
                Input.class);

        ArrayNode output = objectMapper.createArrayNode();

        Debug commands = new Debug();
        commands.debug(inputData.getUsers(), inputData.getMovies(),
                inputData.getActions(), output);


        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
        objectWriter.writeValue(new File("results.out"), output);
    }
}
*/