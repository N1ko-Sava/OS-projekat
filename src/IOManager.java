import java.util.List;
import java.util.ArrayList;

public class IOManager {

    private List<IODevice> devices;

    public IOManager() {
        devices = new ArrayList<>();
    }

    public void addDevice(IODevice device) {
        devices.add(device);
    }

    public void requestIO(PCB p, String deviceName, IOOperation op) {

        if (p == null || op == null) {
            return;
        }

        for (IODevice d : devices) {

            if (d.getName().equals(deviceName)) {

                if (d.isBusy()) {
                    System.out.println(
                            "Uredjaj " + deviceName + " je trenutno zauzet."
                    );
                    return;
                }

                d.startOperation(op, p);
                return;
            }
        }

        System.out.println("Uredjaj nije pronadjen: " + deviceName);
    }

    public void completeIO(IODevice device)
    {
        System.out.println("IO completed on device: " + device.getName());
    }
}