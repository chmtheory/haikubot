package haiku;

import control.HaikuWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public final class HaikuGen {

    private final static Logger log = LoggerFactory.getLogger(HaikuGen.class);

    private static List<String> FIRST_LINES = new ArrayList<>();
    private static List<String> SECOND_LINES = new ArrayList<>();
    private static List<String> THIRD_LINES = new ArrayList<>();

    private HaikuGen() {

    }

    public static void loadLines(HaikuWindow outputWindow) {
        try {
            loadFromFile("config/one", FIRST_LINES);
        } catch (FileNotFoundException ex) {
            outputWindow.postMessage("config/one not found, skipping...");
        } catch (IOException ex) {
            outputWindow.postMessage("Unexpected error occurred while attempting to load config/one!");
        }

        try {
            loadFromFile("config/two", SECOND_LINES);
        } catch (FileNotFoundException ex) {
            outputWindow.postMessage("config/two not found, skipping...");
        } catch (IOException ex) {
            outputWindow.postMessage("Unexpected error occurred while attempting to load config/two!");
        }

        try {
            loadFromFile("config/three", THIRD_LINES);
        } catch (FileNotFoundException ex) {
            outputWindow.postMessage("config/three not found, skipping...");
        } catch (IOException ex) {
            outputWindow.postMessage("Unexpected error occurred while attempting to load config/three!");
        }
    }

    public static void reloadLines(HaikuWindow outputWindow) {
        FIRST_LINES = new ArrayList<>();
        SECOND_LINES = new ArrayList<>();
        THIRD_LINES = new ArrayList<>();

        loadLines(outputWindow);
    }

    public static boolean willProbablyNotWork() {
        return FIRST_LINES.size() == 0 || SECOND_LINES.size() == 0 || THIRD_LINES.size() == 0;
    }

    private static void loadFromFile(String file, List<String> loadTo) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line;
        while ((line = reader.readLine()) != null) {
            loadTo.add(line);
        }
    }

    public static String getRandomFirstLine() {
        return FIRST_LINES.get((int) (Math.random() * FIRST_LINES.size()));
    }

    public static String getRandomSecondLine() {
        return SECOND_LINES.get((int) (Math.random() * SECOND_LINES.size()));
    }

    public static String getRandomThirdLine() {
        return THIRD_LINES.get((int) (Math.random() * THIRD_LINES.size()));
    }

    public static String generateRandomHaiku() {
        return getRandomFirstLine() + "\n" + getRandomSecondLine() + "\n" + getRandomThirdLine();
    }

}
