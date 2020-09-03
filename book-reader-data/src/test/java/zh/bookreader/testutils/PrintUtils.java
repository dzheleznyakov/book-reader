package zh.bookreader.testutils;

public class PrintUtils {
    private static final String RESET = "\u001B[0m";

    private PrintUtils() {
    }

    public static void print(String s, Color color) {
        System.out.print(color.value + s + RESET);
    }

    public static void print(String s, Color color, Background background) {
        System.out.print(background.value + color.value + s + RESET);
    }

    public static void println(String s, Color color) {
        System.out.println(color.value + s + RESET);
    }

    public static void println(String s, Color color, Background background) {
        System.out.println(background.value + color.value + s + RESET);
    }

    public enum Color {
        BLACK("\u001B[30m"),
        RED("\u001B[31m"),
        GREEN("\u001B[32m"),
        YELLOW("\u001B[33m"),
        BLUE("\u001B[34m"),
        PURPLE("\u001B[35m"),
        CYAN("\u001B[36m"),
        WHITE("\u001B[37m");

        private final String value;

        Color(String value) {
            this.value = value;
        }
    }

    public enum Background {
        BLACK("\u001B[40m"),
        RED("\u001B[41m"),
        GREEN("\u001B[42m"),
        YELLOW("\u001B[43m"),
        BLUE("\u001B[44m"),
        PURPLE("\u001B[45m"),
        CYAN("\u001B[46m"),
        WHITE("\u001B[47m");

        private final String value;

        Background(String value) {
            this.value = value;
        }
    }
}
