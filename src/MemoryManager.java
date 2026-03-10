import java.util.List;
import java.util.ArrayList;

public class MemoryManager {

    private RAM ram;
    private List<MemorySegment> segments;

    public MemoryManager(RAM ram) {
        this.ram = ram;
        this.segments = new ArrayList<>();
    }

    public MemoryManager(RAM ram, List<MemorySegment> segments) {
        this.ram = ram;
        this.segments = segments;
    }

    public boolean allocate(PCB p, int size) {
        return false;
    }

    public void free(PCB p) {
    }

    public int read(PCB p, int address) {
        return 0;
    }

    public void write(PCB p, int address, int value) {
    }

    public String dumpMemory() {
        return null;
    }
}