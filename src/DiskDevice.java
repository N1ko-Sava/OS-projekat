import java.util.ArrayList;
import java.util.List;

public class DiskDevice extends IODevice {


    private static class DiskRequest {

        private PCB process;
        private IOOperation operation;

        public DiskRequest(PCB process, IOOperation operation) {
            this.process = process;
            this.operation = operation;
        }

        public PCB getProcess() {
            return process;
        }

        public IOOperation getOperation() {
            return operation;
        }
    }

    private int headPosition;
    private List<DiskRequest> requests;

    public DiskDevice(String name) {
        super(name);

        headPosition = 0;
        requests = new ArrayList<>();
    }

    @Override
    public void startOperation(IOOperation op, PCB p) {

        DiskRequest request = new DiskRequest(p, op);

        requests.add(request);

        System.out.println(
                "Disk zahtjev: PID=" + p.getPid()
                        + ", pozicija="
                        + op.getDiskPosition()
        );

        // Ako disk trenutno nije zauzet,
        // pokreni sljedeci SSTF zahtjev
        if (!busy) {
            startNextRequest();
        }
    }

    private void startNextRequest() {

        if (requests.isEmpty()) {
            busy = false;
            currentProcess = null;
            currentOperation = null;
            return;
        }

        DiskRequest best = null;
        int smallestDistance = Integer.MAX_VALUE;

        for (DiskRequest request : requests) {

            int position =
                    request.getOperation().getDiskPosition();

            int distance =
                    Math.abs(position - headPosition);

            if (distance < smallestDistance) {
                smallestDistance = distance;
                best = request;
            }
        }

        requests.remove(best);

        currentProcess = best.getProcess();
        currentOperation = best.getOperation();

        busy = true;

        System.out.println(
                "SSTF bira PID="
                        + currentProcess.getPid()
                        + " | "
                        + headPosition
                        + " -> "
                        + currentOperation.getDiskPosition()
        );
    }

    @Override
    public void completeOperation() {

        if (currentOperation != null) {
            headPosition =
                    currentOperation.getDiskPosition();
        }

        busy = false;
        currentProcess = null;
        currentOperation = null;

        startNextRequest();
    }
}