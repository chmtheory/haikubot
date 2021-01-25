import java.io.*;
import java.nio.file.NotDirectoryException;
import java.util.LinkedList;

public class HaikuConfig {

    private final String commandWord;

    private final String[] firstLines;
    private final String[] secondLines;
    private final String[] thirdLines;

    private HaikuConfig(String command, String[] one, String[] two, String[] three) {
        this.commandWord = command;
        this.firstLines = one;
        this.secondLines = two;
        this.thirdLines = three;
    }

    public static HaikuConfig loadConfigFromDirectory(String dir) throws NotDirectoryException, FileNotFoundException, IOException {
        File parent = new File(dir);
        if (!parent.isDirectory()) {
            throw new NotDirectoryException("Expected a directory!");
        }

        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(parent, "command")));
            line = reader.readLine();
        } catch (FileNotFoundException ex) {
            line = "haiku";
        }

        String command = line;

        BufferedReader reader = new BufferedReader(new FileReader(new File(parent, "one")));

        LinkedList<String> list = new LinkedList<String>();

        while ((line = reader.readLine()) != null) {
            list.add(line);
        }

        String[] one = list.toArray(new String[0]);
        reader.close();

        reader = new BufferedReader(new FileReader(new File(parent, "two")));
        list = new LinkedList<>();

        while ((line = reader.readLine()) != null) {
            list.add(line);
        }

        String[] two = list.toArray(new String[0]);
        reader.close();

        reader = new BufferedReader(new FileReader(new File(parent, "three")));
        list = new LinkedList<>();

        while ((line = reader.readLine()) != null) {
            list.add(line);
        }

        String[] three = list.toArray(new String[0]);
        reader.close();

        return new HaikuConfig(command, one, two, three);
    }

    public String getCommandWord() {
        return commandWord;
    }

    public String getRandomFirstLine() {
        return firstLines[(int) (Math.random() * firstLines.length)];
    }

    public String getRandomSecondLine() {
        return secondLines[(int) (Math.random() * secondLines.length)];
    }

    public String getRandomThirdLine() {
        return thirdLines[(int) (Math.random() * thirdLines.length)];
    }
}
