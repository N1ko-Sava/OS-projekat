import java.util.Queue;

public class ReadyQueue {

    private Queue<PCB> queue;

    public void add(PCB p) {
        queue.add(p);
    }

    public PCB  removeNext(){
        return queue.remove();
    }
    public boolean isEmpty() {
        return queue.isEmpty();
    }

}
