import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Logger;

class Main {
    public static void main(String[] args) {
        // put your code here
        Logger log = Logger.getLogger(Main.class.getName());
        Scanner sc = new Scanner(System.in);
        StringBuilder s = new StringBuilder(sc.nextLine());
        int n = sc.nextInt();
        sc.nextLine();
        String[] cmd = new String[n];
        for(int i = 0; i < n; i++) {
            cmd[i] = sc.nextLine();
        }
        log.info("fuck the length: " + String.valueOf(s.length()));
        for (String value : cmd) {
            String c = value.split(" ")[0];
            if (c.equals("reverse")) {
                s.reverse();
            } else {
                log.info(Arrays.toString(value.split(" ")));
                if (value.split(" ").length <= 1) continue;
                int num = Integer.parseInt(value.split(" ")[1]);
                StringBuilder b = new StringBuilder(s.substring(num));
                s = b.append(s.substring(0, num));
            }
        }
        System.out.println(s);
    }
}