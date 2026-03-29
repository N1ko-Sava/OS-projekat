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

        int base = 0;

        // sortiraj segmente po base adresi (sigurnost)
        segments.sort((a, b) -> Integer.compare(a.getBase(), b.getBase()));

        for (MemorySegment seg : segments) {

            int gap = seg.getBase() - base;

            if (gap >= size) {
                // našli smo mjesto
                MemorySegment newSeg = new MemorySegment(p, base, size);
                segments.add(newSeg);

                p.setBaseAddress(base);
                p.setLimit(size);

                return true;
            }

            base = seg.getBase() + seg.getLimit();
        }

        // provjeri kraj memorije
        if (ram.getSize() - base >= size) {

            MemorySegment newSeg = new MemorySegment(p, base, size);
            segments.add(newSeg);

            p.setBaseAddress(base);
            p.setLimit(size);

            return true;
        }

        return false; // nema mjesta
    }

    public void free(PCB p) {
        segments.removeIf(seg -> seg.getOwner().equals(p));
    }

    public int read(PCB p, int address) {

        if (address >= p.getLimit()) {
            throw new RuntimeException("Memory access violation");
        }

        int realAddress = p.getBaseAddress() + address;

        return ram.getCells()[realAddress];
    }

    public void write(PCB p, int address, int value) {

        if (address >= p.getLimit()) {
            throw new RuntimeException("Memory access violation");
        }

        int realAddress = p.getBaseAddress() + address;

        ram.getCells()[realAddress] = value;
    }

    public String dumpMemory() {

        StringBuilder sb = new StringBuilder();

        for (MemorySegment seg : segments) {
            sb.append("PID: ")
                    .append(seg.getOwner().getPid())
                    .append(" | Base: ")
                    .append(seg.getBase())
                    .append(" | Size: ")
                    .append(seg.getLimit())
                    .append("\n");
        }

        return sb.toString();
    }
}