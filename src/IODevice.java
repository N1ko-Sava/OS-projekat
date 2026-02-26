public abstract class IODevice {

    protected String name;

    public IODevice(String name) {
        this.name = name;
    }

    public abstract void startOperation(IOOperation op, PCB p);

    public abstract boolean isBusy();

    public String getName() {
        return name;
    }
}