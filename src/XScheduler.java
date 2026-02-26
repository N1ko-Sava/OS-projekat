public class XScheduler implements Scheduler {

    private int timeQuantum;

    @Override
    public PCB chooseNext(ReadyQueue ready) {
        return null;
        //TODO First come first served
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
