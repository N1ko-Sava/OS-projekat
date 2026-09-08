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

    public boolean requestIO(PCB p, String deviceName, IOOperation op) {

        if (p == null || op == null) {
            return false;
        }

        for (IODevice d : devices) {

            if (d.getName().equals(deviceName)) {

                if (d instanceof DiskDevice) {
                    d.startOperation(op, p);
                    return true;
                }

                if (d.isBusy()) {
                    System.out.println(
                            "Uredjaj " + deviceName + " je trenutno zauzet."
                    );
                    return false;
                }

                d.startOperation(op, p);
                return true;
            }
        }

        System.out.println("Uredjaj nije pronadjen: " + deviceName);
        return false;
    }

    public PCB completeIO(IODevice device) {

        if (device == null || !device.isBusy()) {
            return null;
        }



        // Zapamti proces prije nego sto oslobodimo uredjaj
        PCB process = device.getCurrentProcess();

        device.completeOperation();

        System.out.println(
                "IO zavrsen na uredjaju: " + device.getName()
        );

        return process;
    }
}