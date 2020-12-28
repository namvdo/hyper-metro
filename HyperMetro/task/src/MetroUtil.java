import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

class MetroUtil {
    private static final Logger log = Logger.getLogger(MetroUtil.class.getName());
    public static final String NAME = "name";
    public static final String TRANSFER = "transfer";
    public static final String LINE = "line";
    public static final String STATION = "station";
    public static final String APPEND = "/append";
    public static final String ADD_HEAD = "/add-head";
    public static final String REMOVE = "/remove";
    public static final String EXIT = "/exit";
    public static final String PRINT = "/output";
    public static final String CONNECT = "/connect";

    private MetroUtil() {

    }

    public static void addFirstStationToLineFromCommand(Metro metro, String command) {
        List<String> validCmds = parseCommand(command);
        JsonObject stationJson = new JsonObject();
        stationJson.addProperty(NAME, validCmds.get(1));
        stationJson.addProperty(TRANSFER, (String) null);
        metro.addFirstStation(validCmds.get(0), stationJson);
    }

    public static void appendStationToLineFromCommand(Metro metro, String command) {
        List<String> validCmds = parseCommand(command);
        JsonObject stationJson = new JsonObject();
        stationJson.addProperty(NAME, validCmds.get(1));
        stationJson.addProperty(TRANSFER, (String) null);
        metro.appendStation(validCmds.get(0), stationJson);
    }

    public static void removeStationFromLineFromCommand(Metro metro, String command) {
        List<String> validCmds = parseCommand(command);
        metro.removeStation(validCmds.get(0), validCmds.get(1));
    }

    public static void printLineFromCommand(Metro metro, String command) {
        String line = parseCommand(command).get(0);
        System.out.println(metro.printLine(line));
    }


    public static void connectLines(Metro metro, String command) {
        List<String> validCmds = parseCommand(command);
        String line1 = validCmds.get(0);
        String station1 = validCmds.get(1);
        String line2 = validCmds.get(2);
        String station2 = validCmds.get(3);
        metro.addInterchangeStations(line1, station1, line2, station2);

    }

    private static List<String> parseCommand(String commands) {
        String[] arr = commands.split(" ");
        int idx = 0;
        int pairCount = 0;
        List<String> list = new ArrayList<>();
        StringBuilder tempStr = new StringBuilder();
        while (idx != arr.length) {
            if (arr[idx].contains("/")) {
                idx++;
                continue;
            }

            if (Util.countQuote(arr[idx]) % 2 == 1) {
                tempStr.append(arr[idx]).append(" ");
                pairCount++;
                if (pairCount == 2) {
                    pairCount = 0;
                    list.add(tempStr.toString());
                    tempStr = new StringBuilder();
                }
            } else if (Util.countQuote(arr[idx]) % 2 == 0 && pairCount == 1) {
                tempStr.append(arr[idx]).append(" ");
            } else {
                list.add(arr[idx]);
            }
            idx++;

        }
        for (int i = 0; i < list.size(); i++) {
            list.set(i, Util.removeQuote(list.get(i).trim()));
        }
        return list;
    }
}
