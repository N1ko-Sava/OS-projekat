public abstract class IODevice {

    protected String name;
    protected boolean busy;
    protected PCB currentProcess;
    protected IOOperation currentOperation;

    public IODevice(String name) {
        this.name = name;
        this.busy = false;
    }

    public abstract void startOperation(IOOperation op, PCB p);

    public boolean isBusy() {
        return busy;
    }

    public String getName() {
        return name;
    }

    public PCB getCurrentProcess() {
        return currentProcess;
    }

    public IOOperation getCurrentOperation() {
        return currentOperation;
    }

    public void completeOperation() {
        busy = false;
        currentProcess = null;
        currentOperation = null;
    }
}