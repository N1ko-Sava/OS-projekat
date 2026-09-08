public class IOOperation {


    public IOOperation(IOType type, String data, int duration) {
        this.type = type;
        this.data = data;
        this.duration = duration;
    }
    public IOOperation(IOType type, String data, int duration, int diskPosition) {
        this.type = type;
        this.data = data;
        this.duration = duration;
        this.diskPosition = diskPosition;
    }

    private int diskPosition;
    private IOType type;
    private String data;
    private int duration;

    public IOType getType() {
        return type;
    }

    public void setType(IOType type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDiskPosition() {
        return diskPosition;
    }

    public void setDiskPosition(int diskPosition) {
        this.diskPosition = diskPosition;
    }

    public class DiskRequest {

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
}
