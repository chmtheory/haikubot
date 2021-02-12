package haiku;

public final class HaikuGen {

    private final static String[] FIRST_LINES = {"One two three four five"};
    private final static String[] SECOND_LINES = {"One two three four five six seven"};
    private final static String[] THIRD_LINES = {"One two three four five"};

    private HaikuGen() {

    }

    public static String getRandomFirstLine() {
        return FIRST_LINES[(int) (Math.random() * FIRST_LINES.length)];
    }

    public static String getRandomSecondLine() {
        return SECOND_LINES[(int) (Math.random() * SECOND_LINES.length)];
    }

    public static String getRandomThirdLine() {
        return THIRD_LINES[(int) (Math.random() * THIRD_LINES.length)];
    }

    public static String generateRandomHaiku() {
        return getRandomFirstLine() + "\n" + getRandomSecondLine() + "\n" + getRandomThirdLine();
    }

}
