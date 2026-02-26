import java.util.List;

public class MemoryManager {

    private RAM ram;
    private List<MemorySegment> segments;

    public boolean allocate(PCB p, int size)
    {

        //TODO dinamicko particionisanje
        return false;
    }


    public void free(PCB p)
    {
        //TODO dinamicko
    }


    public int read(PCB p, int address)
    {
        return 0;
        //TODO dinamicko
    }

    public void write(PCB p, int address, int value)
    {
        //TODO dinamicko
    }

    public String dumpMemory()
    {
        return null;
        //TODO
    }

}
