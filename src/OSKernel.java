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
        // TODO: implement boot logic
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