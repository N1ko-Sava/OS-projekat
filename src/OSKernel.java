import java.util.ArrayList;
import java.util.List;

public class OSKernel {

    private List<PCB> processTable;
    private ReadyQueue readyQueue;
    private BlockedQueue blockedQueue;
    private CPU cpu;
    private Scheduler scheduler;
    private MemoryManager memoryManager;
    private FileSystem fileSystem;
    private IOManager ioManager;
    private int nextPid;

    public OSKernel() {
        processTable = new ArrayList<>();
        readyQueue = new ReadyQueue();
        blockedQueue = new BlockedQueue();
        nextPid = 1;
    }

    public void boot() {
        System.out.println("\t --- Booting OS... --- \t");

        processTable = new ArrayList<>();

        readyQueue = new ReadyQueue();
        blockedQueue = new BlockedQueue();

        RAM ram = new RAM(1024);

        memoryManager = new MemoryManager(ram);

        cpu = new CPU(null, 0);

        scheduler = new XScheduler();

        fileSystem = new FileSystem();

        ioManager = new IOManager();



        ioManager.addDevice(new DiskDevice("disk"));

        createProcess("idle",0);
        createProcess("system_task",0);

        System.out.println("\t --- OS booted  --- \t");
    }

    public int createProcess(String programName, int priority) {
        PCB pcb = new PCB();

        pcb.setPid(nextPid++);
        pcb.setState(ProcessState.READY);
        pcb.setPriority(priority);
        pcb.setProgramCounter(0);
        pcb.setLimit(64);

        boolean allocated = memoryManager.allocate(pcb, 64);

        if (!allocated) {
            System.out.println("Not enough memory for process " + programName);
            return -1;
        }

        processTable.add(pcb);
        readyQueue.add(pcb);

        System.out.println("Process created: PID=" + pcb.getPid() + " (" + programName + ")");

        return pcb.getPid();

    }

    public void terminateProcess(PCB pcb) {

        if (pcb == null) {
            return;
        }

        pcb.setState(ProcessState.TERMINATED);

        if (cpu.getCurrent() == pcb) {
            cpu.setCurrent(null);
        }

        memoryManager.free(pcb);

        processTable.remove(pcb);

        System.out.println("Proces PID=" + pcb.getPid() + " je zavrsen.");
    }


    public void blockProcess(PCB pcb) {
        if (pcb == null) {
            return;
        }

        // Ako je ovaj proces trenutno na CPU-u, ukloni ga
        if (cpu.getCurrent() == pcb) {
            cpu.setCurrent(null);
        }

        // Prebaci proces u BlockedQueue
        blockedQueue.block(pcb);

        System.out.println(
                "Proces PID=" + pcb.getPid() + " je blokiran."
        );
    }


    public void unblockProcess(PCB pcb) {
        if (pcb == null) {
            return;
        }

        if (blockedQueue.unblock(pcb)) {
            readyQueue.add(pcb);

            System.out.println(
                    "Proces PID=" + pcb.getPid() + " je odblokiran."
            );
        }
    }


    public void timerTick() {


        if (cpu.getCurrent() == null) {

            PCB next = scheduler.chooseNext(readyQueue);

            if (next != null) {
                cpu.contextSwitch(next);
            } else {
                System.out.println("Nema procesa spremnih za izvrsavanje.");
                return;
            }
        }


        cpu.executeOneStep();
    }

    public void handleIOCompletion(PCB pcb) {

        if (pcb == null) {
            return;
        }

        if (pcb.getState() == ProcessState.WAITING) {
            unblockProcess(pcb);

            System.out.println(
                    "IO zavrsen za proces PID=" + pcb.getPid()
            );
        }
    }

    public void completeIO(IODevice device) {

        PCB pcb = ioManager.completeIO(device);

        if (pcb != null) {
            handleIOCompletion(pcb);
        }
    }
    public void requestIO(PCB pcb, String deviceName, IOOperation op) {

        if (pcb == null || op == null) {
            return;
        }

        boolean started = ioManager.requestIO(
                pcb,
                deviceName,
                op
        );

        if (started) {

            blockProcess(pcb);

            System.out.println(
                    "Proces PID=" + pcb.getPid()
                            + " je zatrazio IO na uredjaju: "
                            + deviceName
            );
        }
    }
    public PCB findProcess(int pid) {
        for (PCB pcb : processTable) {
            if (pcb.getPid() == pid) {
                return pcb;
            }
        }

        return null;
    }


    public void syscall(Syscall request) {
        // TODO
    }
}