import org.hyperskill.hstest.dynamic.input.DynamicTestingMethod;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.TestedProgram;

public class Tests extends StageTest<String> {
    // test from the previous stage with one line
    @DynamicTestingMethod
    CheckResult test1() {
        TestedProgram main = new TestedProgram(Main.class);
        main.start("./test/baltimore.json");

        String output = main.execute("/output \"SubwayLink\"").trim();
        checkDepots(output);
        checkOutputLength(output, 14);

        assertStations(output, new String[]{"Owings Mills", "Old Court", "Milford Mill", "Reiserstown Plaza",
                "Rogers Avenue", "West Cold Spring", "Mondawmin", "Penn North", "Uptown", "State Center",
                "Lexington Market", "Charles Center", "Shot Tower/Market Place", "Johns Hopkins Hospital"});

        return CheckResult.correct();
    }

    // test of example
    @DynamicTestingMethod
    CheckResult test2() {
        TestedProgram main = new TestedProgram(Main.class);
        main.start("./test/lausanne.json");

        String output = main.execute("/output \"m1\"").trim();
        checkDepots(output);

        checkOutputLength(output, 15);

        assertStations(output, new String[]{"Renes—Gare", "Epenex", "Crochy", "Cerisaie",
                "Bassenges", "EPFL", "UNL—Sorge", "Mouline", "UNL—Chemberonne", "Bourdonnette", "Melley",
                "Provence", "Montelly", "Vigie", "Lausanne—Flon"});

        output = main.execute("/output \"m2\"");
        checkDepots(output);
        checkOutputLength(output, 14);

        assertStations(output, new String[]{"Croisettes", "Vennes", "Fourmi", "Sallaz", "CHUV", "Ours",
                "Riponne M.Bejart", "Bessieres", "Lausanne—Flon", "Lausanne Gare CFF", "Grancy", "Delices", "Jourdils",
                "Ouchy—Olympique"});

        return CheckResult.correct();
    }

    // example test pt.2 (with addition)
    @DynamicTestingMethod
    CheckResult test2_1() {
        TestedProgram main = new TestedProgram(Main.class);
        main.start("./test/lausanne.json");

        // added a station to the end of the line
        main.execute("/append \"m1\" \"Test station 1\"");
        String output = main.execute("/output \"m1\"");

        checkDepots(output);
        checkOutputLength(output, 16);
        assertStations(output, new String[]{"Renes—Gare", "Epenex", "Crochy", "Cerisaie",
                "Bassenges", "EPFL", "UNL—Sorge", "Mouline", "UNL—Chemberonne", "Bourdonnette", "Melley",
                "Provence", "Montelly", "Vigie", "Lausanne—Flon", "Test station 1"});

        // added another one
        main.execute("/append \"m1\" \"Test station 2\"");
        output = main.execute("/output \"m1\"");

        checkDepots(output);
        checkOutputLength(output, 17);
        assertStations(output, new String[]{"Renes—Gare", "Epenex", "Crochy", "Cerisaie",
                "Bassenges", "EPFL", "UNL—Sorge", "Mouline", "UNL—Chemberonne", "Bourdonnette", "Melley",
                "Provence", "Montelly", "Vigie", "Lausanne—Flon", "Test station 1", "Test station 2"});

        // added one station to the beginning of the line
        main.execute("/add-head \"m1\" \"Head\"");
        output = main.execute("/output \"m1\"");

        checkDepots(output);
        checkOutputLength(output, 18);
        assertStations(output, new String[]{"Head", "Renes—Gare", "Epenex", "Crochy", "Cerisaie",
                "Bassenges", "EPFL", "UNL—Sorge", "Mouline", "UNL—Chemberonne", "Bourdonnette", "Melley",
                "Provence", "Montelly", "Vigie", "Lausanne—Flon", "Test station 1", "Test station 2"});

        return CheckResult.correct();
    }

    // not existing file check
    @DynamicTestingMethod
    CheckResult test4() {
        TestedProgram main = new TestedProgram(Main.class);
        String output = main.start("tHiS_fIlE_DoEs_nOt_ExIsT.txt").toLowerCase();

        if (output.trim().length() == 0) {
            return CheckResult.wrong("The program did not print anything when the file that doesn't exist was passed.");
        }
        if (output.startsWith("depot") || output.endsWith("depot") || !output.contains("error")) {
            return CheckResult.wrong("It looks like the program did not print an error message when the file that doesn't exist was passed.\n" +
                "Your output should contain 'error'.");
        }

        return CheckResult.correct();
    }

    // additional test case
    @DynamicTestingMethod
    CheckResult test5() {
        TestedProgram main = new TestedProgram(Main.class);
        main.start("./test/samara.json");

        main.execute("/append \"line 1\" \"Krylya Sovetov\"");
        String output = main.execute("/output \"line 1\"");

        checkDepots(output);

        checkOutputLength(output, 11);

        assertStations(output, new String[]{"Alabinskaya", "Rossiyskaya", "Moskovskaya",
                "Gagarinskaya", "Sportivnaya", "Sovetskaya", "Pobeda", "Bezymyanka", "Kirovskaya", "Yungorodok",
                "Krylya Sovetov"});

        main.execute("/add-head \"line 1\" Samarskaya");
        output = main.execute("/output \"line 1\"");

        checkDepots(output);
        checkOutputLength(output, 12);

        assertStations(output, new String[]{"Samarskaya", "Alabinskaya", "Rossiyskaya", "Moskovskaya",
                "Gagarinskaya", "Sportivnaya", "Sovetskaya", "Pobeda", "Bezymyanka", "Kirovskaya", "Yungorodok",
                "Krylya Sovetov"});

        return CheckResult.correct();
    }

    // checks for "depot" at the start and at the end
    void checkDepots(String output) {
        output = output.trim().toLowerCase();
        if (!output.startsWith("depot")) {
            throw new WrongAnswer("Your output should start with 'depot'.");
        } else if (!output.endsWith("depot")) {
            throw new WrongAnswer("Your output should end with 'depot'.");
        }
    }

    // checks number of stations in output
    void checkOutputLength(String output, int correctLength) {
        int length = output.trim().split("\n").length;
        if (length != correctLength) {
            throw new WrongAnswer("You output contains wrong number of lines.\n" +
                "Expected: " + correctLength + " lines\n" +
                "Your output: " + length + " lines");
        }
    }

    // checks stations
    void assertStations(String output, String[] stations) {

        String[] sOutput = output.trim().split("\n");

        for (int i = 0; i < stations.length; i++) {

            String currentLine = sOutput[i].trim();
            if (currentLine.split("-").length != 3) {
                throw new WrongAnswer("There is should be 3 stations in one line.\n" +
                    "Treat 'depot' as a station name");
            }

            // Checking the first line
            if (i == 0) {
                for (int j = 0; j < 2; j++) {
                    if (!currentLine.contains(stations[i + j])) {
                        throw new WrongAnswer("Can't find station '" + stations[i + j] + "' in the line number " + (i + 1));
                    }
                }
                // Checking the last line
            } else if (i == stations.length - 1) {
                for (int j = 0; j < 2; j++) {
                    if (!currentLine.contains(stations[i + j - 1])) {
                        throw new WrongAnswer("Can't find station '" + stations[i + j - 1] + "' in the line number " + (i + 1));
                    }
                }
                // Checking the rest lines
            } else {
                for (int j = 0; j < 3; j++) {
                    if (!currentLine.contains(stations[i + j - 1])) {
                        throw new WrongAnswer("Can't find station '" + stations[i + j - 1] + "' in the line number " + (i + 1));
                    }
                }
            }
        }
    }
}