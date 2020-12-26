import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

public class Main {


    public static void main(String[] args) {
        String file = args[0];
        Metro metro = new Metro();
        try {
            metro.addLines(file);
        } catch (IOException ignored) {
            System.out.println("error");
            return;
        }
        Scanner sc = new Scanner(System.in);
        String command;
        do {
            command = sc.nextLine();
            switch (command.split(" ")[0]) {
                case Util.ADD_HEAD:
                    MetroUtil.addFirstStationToLineFromCommand(metro, command);
                    break;
                case Util.APPEND:
                    MetroUtil.appendStationToLineFromCommand(metro, command);
                    break;
                case Util.REMOVE:
                    MetroUtil.removeStationFromLineFromCommand(metro, command);
                    break;
                case Util.PRINT:
                    MetroUtil.printLineFromCommand(metro, command);
                    break;
                default:
                    System.out.println("Invalid command");
            }
        } while (!command.split(" ")[0].equals(Util.EXIT));
    }
}


class Metro {
    private final Map<String, LinkedList<String>> linesToStations;

    public Metro() {
        linesToStations = new HashMap<>();
    }



    public void appendStation(String line, String station) {
        linesToStations.get(line).addLast(station);
    }

    public void addFirstStation(String line, String station) {
        linesToStations.get(line).addFirst(station);
    }

    public void removeStation(String line, String station) {
        linesToStations.get(line).remove(station);
    }
    public void removeLine(String line) {
        linesToStations.remove(line);
    }

    public String printLine(String line) {
        StringBuilder str = new StringBuilder();
        int idx = 0;
        LinkedList<String> stations = linesToStations.get(line);
        if (stations.size() == 2) {
            str.append("depot").append(" - ").append(stations.get(0)).append(" - ").append(stations.get(1));
            str.append(stations.get(0)).append(" - ").append(stations.get(1)).append(" - destination");
        } else {
            while (idx != stations.size()) {
                if (idx == 0) {
                    str.append("depot - ").append(stations.get(idx)).append(" - ").append(stations.get(idx + 1)).append("\n");
                } else if (idx == stations.size() - 1) {
                    str.append(stations.get(idx - 1)).append(" - ").append(stations.get(idx)).append(" - ").append("depot").append("\n");
                } else {
                    str.append(stations.get(idx - 1)).append(" - ").append(stations.get(idx)).append(" - ").append(stations.get(idx + 1)).append("\n");
                }
                idx++;
            }
        }
        return str.toString();
    }

    public void addLines(String fileName) throws IOException {
        String fileContent = Util.getContentFromFile(fileName);
        Map<String, Map<String, String>> json = new Gson().fromJson(fileContent, HashMap.class);
        for(var entry: json.entrySet()) {
            Map<String, String> idToStation = entry.getValue();
            LinkedList<String> stations = new LinkedList<>();
            for(var slot: idToStation.entrySet()) {
                stations.add(slot.getValue());
            }
            linesToStations.put(entry.getKey(), stations);
        }
    }

}

class MetroUtil {
    private static final Logger log = Logger.getLogger(MetroUtil.class.getName());
    private MetroUtil() {

    }
    public static void addFirstStationToLineFromCommand(Metro metro, String command) {
        String line = Util.removeQuote(command.substring(command.indexOf("\""), command.indexOf("\"", command.indexOf("\"") + 1)));
        String parser = command.substring(0, command.indexOf("\"", command.indexOf("\"") + 1));
        String station = Util.removeQuote(command.substring(command.indexOf("\"", parser.length() + 1)
        == -1 ? parser.length() + 2 : command.indexOf("\"", parser.length() + 1)));
        metro.addFirstStation(line, station);
    }
    public static void appendStationToLineFromCommand(Metro metro, String command) {
        String line = Util.removeQuote(command.substring(command.indexOf("\""), command.indexOf("\"", command.indexOf("\"") + 1)));
        String parser = command.substring(0, command.indexOf("\"", command.indexOf("\"") + 1));
        String station = Util.removeQuote(command.substring(command.indexOf("\"", parser.length() + 1)
                == -1 ? parser.length() + 2 : command.indexOf("\"", parser.length() + 1)));
        metro.appendStation(line, station);
    }
    public static void removeStationFromLineFromCommand(Metro metro, String command) {
        String line = Util.removeQuote(command.substring(command.indexOf("\""), command.indexOf("\"", command.indexOf("\"") + 1)));
        String parser = command.substring(0, command.indexOf("\"", command.indexOf("\"") + 1));
        String station = Util.removeQuote(command.substring(command.indexOf("\"", parser.length() + 1)
                == -1 ? parser.length() + 2 : command.indexOf("\"", parser.length() + 1)));
        metro.removeStation(line, station);
    }
    public static void printLineFromCommand(Metro metro, String command) {
        String line = Util.removeQuote(command.substring(command.indexOf("\""), command.indexOf("\"", command.indexOf("\"") + 1)));
        System.out.println(metro.printLine(line));
    }
}
class Util {
    private Util() {

    }
    public static final String APPEND = "/append";
    public static final String ADD_HEAD = "/add-head";
    public static final String REMOVE = "/remove";
    public static final String EXIT = "/exit";
    public static final String PRINT = "/output";


    public static String removeQuote(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        for(char i: str.toCharArray()) {
            if (i != '"') {
                stringBuilder.append(i);
            }
        }
        return stringBuilder.toString();
    }
    public static String getContentFromFile(String fileName) throws IOException{
        String absolutePath = "/Users/namvdo/Desktop/learntocodetogether.com/scala/HyperMetro/HyperMetro/task/";
        try (FileReader file = new FileReader(absolutePath + fileName);
             BufferedReader bufferedReader = new BufferedReader(file);
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



