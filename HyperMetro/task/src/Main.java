import java.io.IOException;
import java.util.*;

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
                case MetroUtil.ADD_HEAD:
                    MetroUtil.addFirstStationToLineFromCommand(metro, command);
                    break;
                case MetroUtil.APPEND:
                    MetroUtil.appendStationToLineFromCommand(metro, command);
                    break;
                case MetroUtil.REMOVE:
                    MetroUtil.removeStationFromLineFromCommand(metro, command);
                    break;
                case MetroUtil.PRINT:
                    MetroUtil.printLineFromCommand(metro, command);
                    break;
                case MetroUtil.CONNECT:
                    MetroUtil.connectLines(metro, command);
                    break;
                default:
                    System.out.println("Invalid command");
            }
        } while (!command.split(" ")[0].equals(MetroUtil.EXIT));
    }
}










