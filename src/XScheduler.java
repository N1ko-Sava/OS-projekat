public class XScheduler implements Scheduler {

    @Override
    public PCB chooseNext(ReadyQueue ready) {

        if (ready == null || ready.isEmpty()) {
            return null;
        }

        return ready.removeNext(); // FCFS: prvi proces iz reda
    }
}