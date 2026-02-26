public class CPU {

    private PCB current;
    private long cycleCount;

    public void executeOneStep()
    {
        //TODO
    }

    public void contextSwitch(PCB next)
    {
        //TODO

    }

    public PCB getCurrent()
    {
        return current;
    }

    public CPU(PCB current, long cycleCount) {
        this.current = current;
        this.cycleCount = cycleCount;
    }


    public void setCurrent(PCB current) {
        this.current = current;
    }

    public long getCycleCount() {
        return cycleCount;
    }

    public void setCycleCount(long cycleCount) {
        this.cycleCount = cycleCount;
    }
}
