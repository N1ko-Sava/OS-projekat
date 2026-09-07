import java.util.ArrayList;
import java.util.List;

public class BlockedQueue {
    private List<PCB> list;

    public BlockedQueue() {
        this.list = new ArrayList<>();
    }

    // Metoda za blokiranje procesa
    public void block(PCB p) {
        if (!list.contains(p)) {
            list.add(p);
            p.setState(ProcessState.WAITING);
        }
    }

    // Metoda za odblokiranje procesa
    public boolean unblock(PCB p) {
        if (list.remove(p)) {
            p.setState(ProcessState.READY);
            return true;
        }
        return false;
    }


    public List<PCB> findByDevice(IODevice d) {
        List<PCB> result = new ArrayList<>();

        return result;
    }
}