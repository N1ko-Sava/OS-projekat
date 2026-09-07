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

        scheduler = new XScheduler(5);

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

    public void terminateProcess(int pid) {
        // TODO
    }

    public void timerTick() {
        // TODO
    }

    public void handleIOCompletion(IODevice device) {
        // TODO
    }

    public void syscall(Syscall request) {
        // TODO
    }
}