public class XScheduler implements Scheduler {

    private int timeQuantum;

    @Override
    public PCB chooseNext(ReadyQueue ready) {

        if (ready == null || ready.isEmpty()) {
            return null;
        }

        return ready.removeNext(); // FCFS: prvi proces iz reda
    }


    public XScheduler(int timeQuantum) {
        this.timeQuantum = timeQuantum;
    }


    public int getTimeQuantum() {
        return timeQuantum;
    }

    public void setTimeQuantum(int timeQuantum) {
        this.timeQuantum = timeQuantum;
    }
}
