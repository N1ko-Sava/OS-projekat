public class Main {

    public static void main(String[] args) {

        OSKernel kernel = new OSKernel();
        kernel.boot();

        System.out.println("\n--- PROCESI NAKON BOOT-a ---");
        kernel.executeCommand("ps");


        System.out.println("\n--- POKRENI PRVI PROCES ---");
        kernel.timerTick();

        kernel.executeCommand("ps");


        System.out.println("\n--- BLOKIRAJ PID 1 ---");
        kernel.executeCommand("block 1");

        kernel.executeCommand("ps");


        System.out.println("\n--- FCFS UZIMA PID 2 ---");
        kernel.timerTick();

        kernel.executeCommand("ps");


        System.out.println("\n--- ODBLOKIRAJ PID 1 ---");
        kernel.executeCommand("unblock 1");

        kernel.executeCommand("ps");


        System.out.println("\n--- ZAVRSI PID 2 ---");
        kernel.executeCommand("kill 2");

        kernel.executeCommand("ps");
    }
}