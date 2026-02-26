public interface Scheduler {

    public PCB chooseNext(ReadyQueue ready);

}
