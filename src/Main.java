import management.AppManager;
import fileio.Input;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.File;
import java.io.IOException;

public final class Main {
    private Main() {
    }

    /**
     * get args from Test file (checker)
     * @param args from checker (input/ output files)
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File out = new File(args[1]);
        String filename = args[0];
        boolean isCreated = out.createNewFile();
        if (isCreated) {
            action(filename, out.getName());
        }
    }

    /**
     * @param inputFilePath - path to input file
     * @param outputFilePath - path to output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String inputFilePath,
                              final String outputFilePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        /* get data from input file */
        Input inputData = objectMapper.readValue(new File(inputFilePath), Input.class);

        ArrayNode output = objectMapper.createArrayNode();

        /* actual start for my implementation */
        AppManager appManager = new AppManager();
        appManager.debug(inputData.getUsers(), inputData.getMovies(),
                inputData.getActions(), output);

        /* write output in JSON format in file */
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(outputFilePath), output);
    }
}
