
 class Main {

    public static void main(String[] args) {

        OSKernel kernel = new OSKernel();
        kernel.boot();

        System.out.println("\n--- Pokrenut sistem ---\n");

        int pid = kernel.createProcess("test_program", 0);

        PCB p = kernel.findProcess(pid);


        System.out.println("\n--- CPU TEST ---");

        kernel.timerTick();
        kernel.timerTick();
        kernel.timerTick();


        IOOperation op = new IOOperation(
                IOType.READ,
                "test podaci",
                3
        );

        System.out.println("\n--- IO REQUEST ---");

        kernel.requestIO(
                p,
                "disk",
                op
        );

        System.out.println("\n--- CPU NAKON BLOKIRANJA ---");

        kernel.timerTick();
        kernel.timerTick();
    }
}