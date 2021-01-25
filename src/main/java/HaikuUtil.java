import java.io.*;

public class HaikuUtil {

    public static String getClientSecret(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();

        if (line == null) {
            return "";
        }

        return line;
    }



}
