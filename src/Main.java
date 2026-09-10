public class Main {

    public static void main(String[] args) {

        OSKernel kernel = new OSKernel();
        kernel.boot();


        System.out.println("\n==============================");
        System.out.println("1. KREIRANJE DIREKTORIJUMA");
        System.out.println("==============================");

        kernel.executeCommand("mkdir /programi");

        kernel.executeCommand("ls");


        System.out.println("\n==============================");
        System.out.println("2. ULAZAK U DIREKTORIJUM");
        System.out.println("==============================");

        kernel.executeCommand("cd /programi");

        kernel.executeCommand("ls");


        System.out.println("\n==============================");
        System.out.println("3. KREIRANJE PROGRAMSKOG FAJLA");
        System.out.println("==============================");

        kernel.executeCommand("touch program.asm");

        kernel.executeCommand("ls");


        System.out.println("\n==============================");
        System.out.println("4. OTVARANJE FAJLA ZA PISANJE");
        System.out.println("==============================");

        // Potreban nam je RUNNING proces
        kernel.timerTick();

        kernel.executeCommand(
                "open program.asm WRITE"
        );


        System.out.println("\n==============================");
        System.out.println("5. UPIS ASEMBLERSKOG PROGRAMA");
        System.out.println("==============================");

        kernel.executeCommand(
                "saveasm program.asm " +
                        "PUSH 5\\n" +
                        "PUSH 3\\n" +
                        "ADD\\n" +
                        "PUSH 2\\n" +
                        "MUL\\n" +
                        "PRINT\\n" +
                        "HALT"
        );
        System.out.println("\n==============================");
        System.out.println("6. SADRZAJ FAJLA NA DISKU");
        System.out.println("==============================");

        kernel.executeCommand(
                "cat program.asm"
        );


        System.out.println("\n==============================");
        System.out.println("7. POKRETANJE PROGRAMA");
        System.out.println("==============================");

        kernel.executeCommand(
                "run program.asm"
        );

        for (int i = 0; i < 15; i++) {
            kernel.timerTick();
        }


        System.out.println("\n==============================");
        System.out.println("8. TABELA PROCESA");
        System.out.println("==============================");

        kernel.executeCommand("ps");


        kernel.testSSTF();


        System.out.println("\n==============================");
        System.out.println("KRAJ TESTA");
        System.out.println("==============================");
    }
}