public class ConsoleDevice extends IODevice {

    public ConsoleDevice(String name) {
        super(name);
    }

    @Override
    public void startOperation(IOOperation op, PCB p) {

        if (busy) {
            return;
        }

        busy = true;
        currentProcess = p;
        currentOperation = op;

        System.out.println(
                "Proces PID=" + p.getPid()
                        + " je zapoceo Console IO: "
                        + op.getType()
        );
    }
}