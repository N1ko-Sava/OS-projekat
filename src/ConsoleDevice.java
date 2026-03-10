public class ConsoleDevice extends IODevice {

    private boolean busy;

    public ConsoleDevice(String name) {
        super(name);
        this.busy = false;
    }

    @Override
    public void startOperation(IOOperation op, PCB p) {
        busy = true;
        System.out.println("Process " + p.getPid() + " started Console IO: " + op.getType());
        busy = false;
    }

    @Override
    public boolean isBusy() {
        return busy;
    }
}