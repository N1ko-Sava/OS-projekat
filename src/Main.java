import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        OSKernel kernel = new OSKernel();
        kernel.boot();

        PCB p = kernel.findProcess(1);

        p.setProgram(Arrays.asList(
                "PUSH 5",
                "PUSH 3",
                "ADD",
                "PUSH 2",
                "MUL",
                "PRINT",
                "HALT"
        ));

        System.out.println("\n--- NULA-ADRESNI ASEMBLER TEST ---");

        while (p.getState() != ProcessState.TERMINATED) {
            kernel.timerTick();
        }

        System.out.println("\n--- KRAJ TESTA ---");
    }
}