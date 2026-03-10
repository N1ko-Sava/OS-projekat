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

    public class Main {
        public static void main(String[] args) {
            OSKernel kernel = new OSKernel();
            kernel.boot();
        }
    }

    public void boot() {
        System.out.println("Booting OS...");

        processTable = new ArrayList<>();

        readyQueue = new ReadyQueue();
        blockedQueue = new BlockedQueue();

        int[] cells = new int[1024];
        RAM ram = new RAM(1024, cells);

        memoryManager = new MemoryManager(ram);

        cpu = new CPU(null, 0);

        scheduler = new XScheduler(5);

        fileSystem = new FileSystem();

        ioManager = new IOManager();

        System.out.println("OS boot completed.");

        ioManager.addDevice(new DiskDevice("disk"));

        createProcess("idle",0);
        createProcess("system_task",0);

        System.out.println("OS boot completed.");
    }

    public int createProcess(String programName, int priority) {
        // TODO: implement process creation
        return -1;
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