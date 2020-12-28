import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class Util {
    private static final String ABSOLUTE_PATH = "/Users/namvdo/Desktop/learntocodetogether.com/scala/HyperMetro/HyperMetro/task/";

    private Util() {

    }


    public static String removeQuote(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        for (char i : str.toCharArray()) {
            if (i != '"') {
                stringBuilder.append(i);
            }
        }
        return stringBuilder.toString();
    }

    public static int countQuote(String str) {
        int count = 0;
        for (char c : str.toCharArray()) {
            if (c == '"') {
                count++;
            }
        }
        return count;
    }

    public static String getContentFromFile(String fileName) throws IOException {
        try (FileReader file = new FileReader(ABSOLUTE_PATH + fileName);
             BufferedReader bufferedReader = new BufferedReader(file)
        ) {
            StringBuilder str = new StringBuilder();
            String info;
            while ((info = bufferedReader.readLine()) != null) {
                str.append(info);
            }
            return str.toString();
        }
    }
}
