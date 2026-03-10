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

    public void requestIO(PCB p, String deviceName, IOOperation op)
    {
        for (IODevice d : devices) {

            if (d.getName().equals(deviceName)) {
                //d.performIO(p, op);
                return;
            }

        }

        System.out.println("Device not found: " + deviceName);
    }

    public void completeIO(IODevice device)
    {
        System.out.println("IO completed on device: " + device.getName());
    }
}