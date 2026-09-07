public class CPU {

    private PCB current;
    private long cycleCount;

    public void executeOneStep() {

        if (current == null) {
            System.out.println("CPU: nema procesa za izvrsavanje.");
            return;
        }

        if (current.getState() != ProcessState.RUNNING) {
            System.out.println("CPU: proces PID=" + current.getPid()
                    + " nije u RUNNING stanju.");
            return;
        }


        current.setProgramCounter(current.getProgramCounter() + 1);


        cycleCount++;

        System.out.println(
                "CPU: PID=" + current.getPid()
                        + " izvrsava instrukciju, PC="
                        + current.getProgramCounter()
                        + ", cycle=" + cycleCount
        );
    }

    public void contextSwitch(PCB next) {

        current = next;

        if (current != null) {
            current.setState(ProcessState.RUNNING);

            System.out.println(
                    "CPU: context switch -> PID=" + current.getPid()
            );
        } else {
            System.out.println("CPU: nema procesa za izvrsavanje.");
        }
    }


    public PCB getCurrent()
    {
        return current;
    }

    public CPU(PCB current, long cycleCount) {
        this.current = current;
        this.cycleCount = cycleCount;
    }


    public void setCurrent(PCB current) {
        this.current = current;
    }

    public long getCycleCount() {
        return cycleCount;
    }

    public void setCycleCount(long cycleCount) {
        this.cycleCount = cycleCount;
    }
}
