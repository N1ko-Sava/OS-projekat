import java.util.Queue;
import java.util.LinkedList;

public class ReadyQueue {

    private Queue<PCB> queue;

    public ReadyQueue() {
        queue = new LinkedList<>();
    }

    public void add(PCB p) {
        queue.add(p);
    }

    public void remove(PCB p) {
        queue.remove(p);
    }

    public PCB removeNext() {

        if (queue.isEmpty()) {
            return null;
        }

        return queue.remove();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}