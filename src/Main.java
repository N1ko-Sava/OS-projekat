import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        OSKernel kernel = new OSKernel();
        kernel.boot();

        Scanner scanner = new Scanner(System.in);

        System.out.println();
        System.out.println("================================");
        System.out.println("        OS SIMULATOR");
        System.out.println("================================");
        System.out.println("Ukucaj 'help' za listu komandi.");
        System.out.println("Ukucaj 'exit' za izlaz.");
        System.out.println();

        while (true) {

            System.out.print("OS> ");

            String command = scanner.nextLine().trim();

            if (command.isEmpty()) {
                continue;
            }

            if (command.equalsIgnoreCase("exit")) {
                System.out.println("Gasim OS simulator...");
                break;
            }

            if (command.equalsIgnoreCase("help")) {
                printHelp();
                continue;
            }

            if (command.equalsIgnoreCase("tick")) {
                kernel.timerTick();
                continue;
            }

            if (command.equalsIgnoreCase("sstf-test")) {
                kernel.testSSTF();
                continue;
            }

            if (command.equalsIgnoreCase("defrag-test")) {
                kernel.testDefragmentation();
                continue;
            }

            kernel.executeCommand(command);
        }

        scanner.close();
    }


    private static void printHelp() {

        System.out.println();
        System.out.println("--- KOMANDE ---");

        System.out.println("mkdir <putanja>");
        System.out.println("touch <putanja>");
        System.out.println("cd <putanja>");
        System.out.println("ls");

        System.out.println("write <fajl> <tekst>");
        System.out.println("cat <fajl>");
        System.out.println("rm <putanja>");

        System.out.println("open <fajl> READ");
        System.out.println("open <fajl> WRITE");

        System.out.println("saveasm <fajl> <assembler>");
        System.out.println("run <fajl>");

        System.out.println("create <ime>");

        System.out.println("ps");
        System.out.println("block <pid>");
        System.out.println("unblock <pid>");
        System.out.println("kill <pid>");

        System.out.println("tick");
        System.out.println("sstf-test");
        System.out.println("defrag-test");

        System.out.println("help");
        System.out.println("exit");

        System.out.println();
    }


    /// test test laptop
}