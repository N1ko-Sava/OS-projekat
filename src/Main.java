import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        OSKernel kernel = new OSKernel();
        kernel.boot();


        System.out.println("\n==============================");
        System.out.println("1. FCFS RASPOREDJIVANJE");
        System.out.println("==============================");

        kernel.executeCommand("ps");

        kernel.timerTick();
        kernel.timerTick();

        kernel.executeCommand("ps");


        System.out.println("\n==============================");
        System.out.println("2. BLOKIRANJE / ODBLOKIRANJE");
        System.out.println("==============================");

        kernel.executeCommand("block 1");

        kernel.executeCommand("ps");

        kernel.timerTick();

        kernel.executeCommand("ps");

        kernel.executeCommand("unblock 1");

        kernel.executeCommand("ps");


        System.out.println("\n==============================");
        System.out.println("3. FILE SYSTEM");
        System.out.println("==============================");

        kernel.executeCommand("mkdir /home");
        kernel.executeCommand("touch /home/test.txt");

        kernel.executeCommand(
                "write /home/test.txt Pozdrav iz operativnog sistema"
        );

        kernel.executeCommand("cat /home/test.txt");


        System.out.println("\n==============================");
        System.out.println("4. SYSCALL TEST");
        System.out.println("==============================");

        kernel.syscall(
                new Syscall(
                        SyscallType.WRITE,
                        List.of(
                                "/home/test.txt",
                                "Podaci zapisani preko syscall-a"
                        )
                )
        );

        kernel.syscall(
                new Syscall(
                        SyscallType.READ,
                        List.of("/home/test.txt")
                )
        );


        System.out.println("\n==============================");
        System.out.println("5. NOVI PROCES");
        System.out.println("==============================");

        kernel.syscall(
                new Syscall(
                        SyscallType.CREATE_PROCESS,
                        List.of("assembler_program", "0")
                )
        );

        PCB p = kernel.findProcess(3);

        p.setProgram(Arrays.asList(
                "PUSH 5",
                "PUSH 3",
                "ADD",
                "PUSH 2",
                "MUL",
                "PRINT",
                "HALT"
        ));

        kernel.executeCommand("ps");


        System.out.println("\n==============================");
        System.out.println("6. NULA-ADRESNI ASEMBLER");
        System.out.println("==============================");

        // Zavrsimo PID 2 da FCFS dodje do PID 3
        PCB p2 = kernel.findProcess(2);

        if (p2 != null) {
            kernel.terminateProcess(p2);
        }

        // PID 1 je READY nakon unblock-a, pa ga takodje zavrsimo
        PCB p1 = kernel.findProcess(1);

        if (p1 != null) {
            kernel.terminateProcess(p1);
        }

        while (p.getState() != ProcessState.TERMINATED) {
            kernel.timerTick();
        }


        System.out.println("\n==============================");
        System.out.println("7. DEFRAGMENTACIJA MEMORIJE");
        System.out.println("==============================");

        kernel.defragmentMemory();


        System.out.println("\n==============================");
        System.out.println("8. BRISANJE FAJLA");
        System.out.println("==============================");

        kernel.executeCommand("rm /home/test.txt");

        kernel.executeCommand("cat /home/test.txt");


        System.out.println("\n==============================");
        System.out.println("KRAJ DEMONSTRACIJE");
        System.out.println("==============================");
    }
}