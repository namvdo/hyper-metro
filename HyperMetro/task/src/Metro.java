import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;


class Metro {
    private final Map<String, LinkedList<JsonObject>> linesToStations;

    public Metro() {
        linesToStations = new HashMap<>();
    }


    public void appendStation(String line, JsonObject station) {
        linesToStations.get(line).addLast(station);
    }

    public void addFirstStation(String line, JsonObject station) {
        linesToStations.get(line).addFirst(station);
    }

    public void removeStation(String line, String station) {
        int stationIdx = getStationIdxFromLinkedList(line, station);
        linesToStations.get(line).remove(stationIdx);
    }


    public String printLine(String line) {
        StringBuilder str = new StringBuilder();
        int idx = 0;
        LinkedList<JsonObject> stations = linesToStations.get(line);
        if (stations.size() == 2) {
            str.append("depot").append(" - ")
                    .append(stations.get(0).get(MetroUtil.NAME).getAsString())
                    .append(" - ")
                    .append(stations.get(1).get(MetroUtil.NAME).getAsString());
            str.append(stations.get(0).get(MetroUtil.NAME).getAsString())
                    .append(" - ")
                    .append(stations.get(1).get(MetroUtil.NAME).getAsString())
                    .append(" - destination");
        } else {
            while (idx != stations.size()) {
                JsonObject curStation = stations.get(idx);
                if (idx == 0) {
                    if (curStation.get(MetroUtil.TRANSFER) != JsonNull.INSTANCE) {
                        str.append("depot\n");
                        str.append(linkInterchangeStations(curStation)).append("\n");
                    } else {
                        str.append("depot  \n")
                                .append(curStation.get(MetroUtil.NAME).getAsString()).append("\n");
                    }
                } else if (idx == stations.size() - 1) {
                    if (curStation.get(MetroUtil.TRANSFER) != JsonNull.INSTANCE) {
                        str.append(linkInterchangeStations(curStation)).append("\n");
                        str.append("depot\n");
                    } else {
                        str.append(curStation.get(MetroUtil.NAME).getAsString()).append("\n").append("depot");

                    }
                } else {
                    if (curStation.get(MetroUtil.TRANSFER) != JsonNull.INSTANCE) {
                        str.append(curStation.get(MetroUtil.NAME)).append(" - ")
                                .append(curStation.get(MetroUtil.TRANSFER).getAsJsonObject().get(MetroUtil.STATION).getAsString())
                                .append(" (")
                                .append(curStation.get(MetroUtil.TRANSFER).getAsJsonObject().get(MetroUtil.LINE).getAsString())
                                .append(" line)").append("\n");
                    } else {
                        str.append(curStation.get(MetroUtil.NAME).getAsString()).append("\n");
                    }
                }
                idx++;
            }
        }
        return str.toString();
    }

    private String linkInterchangeStations(JsonObject curStation) {
        StringBuilder str = new StringBuilder();
        String interchangeLine = curStation.get(MetroUtil.TRANSFER)
                .getAsJsonObject().get(MetroUtil.LINE).getAsString();
        String interchangeStation = curStation.get(MetroUtil.TRANSFER)
                .getAsJsonObject().get(MetroUtil.STATION).getAsString();
        str.append(curStation.get(MetroUtil.NAME)).append(" - ")
                .append(interchangeStation)
                .append(" (").append(interchangeLine).append(")");
        return str.toString();
    }

    public void addLines(String fileName) throws IOException {
        String fileContent = Util.getContentFromFile(fileName);
        JsonObject json = new Gson().fromJson(fileContent, JsonObject.class);
        for (var entry : json.entrySet()) {
            String line = entry.getKey();
            LinkedList<JsonObject> stations = new LinkedList<>();
            JsonObject stationsJson = entry.getValue().getAsJsonObject();
            for (var station : stationsJson.entrySet()) {
                var curStation = station.getValue().getAsJsonObject();
                stations.add(curStation);
            }
            linesToStations.put(line, stations);
        }
    }

    public void addInterchangeStations(String line1Name, String station1Name, String line2Name, String station2Name) {
        addInterChangeStationsHelper(line1Name,
                station1Name,
                line2Name,
                station2Name);
        addInterChangeStationsHelper(line2Name,
                station2Name,
                line1Name,
                station1Name);
    }

    private void addInterChangeStationsHelper(String line1Name, String station1Name, String line2Name, String station2Name) {
        int station1Idx = getStationIdxFromLinkedList(line1Name, station1Name);
        Map<String, String> transferToStation1 = new HashMap<>();
        transferToStation1.put(MetroUtil.LINE, line2Name);
        transferToStation1.put(MetroUtil.STATION, station2Name);
        var json1 = new JsonParser().parse(new Gson().toJson(transferToStation1)).getAsJsonObject();
        this.linesToStations.get(line1Name).get(station1Idx).add(MetroUtil.TRANSFER, json1);
    }

    private int getStationIdxFromLinkedList(String lineName, String stationName) {
        LinkedList<JsonObject> line = linesToStations.get(lineName);
        Iterator<JsonObject> iterator = line.iterator();
        int idx = 0;
        while (iterator.hasNext()) {
            if (iterator.next().get(MetroUtil.NAME).getAsString().equals(stationName)) {
                return idx;
            }
            idx++;
        }
        return -1;
    }

}