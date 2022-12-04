import myclasses.Debug;
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
     * @param filePath1 - path to input file
     * @param filePath2 - path to output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        Input inputData = objectMapper.readValue(new File(filePath1),
                Input.class);

        ArrayNode output = objectMapper.createArrayNode();

        Debug commands = new Debug();
        commands.debug(inputData.getUsers(), inputData.getMovies(),
                inputData.getActions(), output);

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }
}
