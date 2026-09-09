public class CPU {

    private PCB current;
    private long cycleCount;

    private ZeroAddressAssembler assembler;

    public void executeOneStep() {

        if (current == null) {
            System.out.println("CPU: nema procesa za izvrsavanje.");
            return;
        }

        if (current.getState() != ProcessState.RUNNING) {
            System.out.println(
                    "CPU: PID=" + current.getPid()
                            + " nije u RUNNING stanju."
            );
            return;
        }


        if (current.getProgram() == null ||
                current.getProgram().isEmpty()) {

            current.setProgramCounter(
                    current.getProgramCounter() + 1
            );

            cycleCount++;

            System.out.println(
                    "CPU: PID=" + current.getPid()
                            + " izvrsava instrukciju, PC="
                            + current.getProgramCounter()
                            + ", cycle=" + cycleCount
            );

            return;
        }

        int pc = current.getProgramCounter();


        if (pc >= current.getProgram().size()) {

            current.setState(ProcessState.TERMINATED);

            System.out.println(
                    "PID=" + current.getPid()
                            + " je dosao do kraja programa."
            );

            return;
        }

        String instruction =
                current.getProgram().get(pc);

        System.out.println(
                "PID=" + current.getPid()
                        + " | PC=" + pc
                        + " | " + instruction
        );


        assembler.executeInstruction(
                current,
                instruction
        );

        cycleCount++;


        if (current.getState() == ProcessState.TERMINATED) {

            System.out.println(
                    "CPU: proces PID="
                            + current.getPid()
                            + " vise nije aktivan."
            );

            return;
        }


        current.setProgramCounter(pc + 1);
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
        this.assembler = new ZeroAddressAssembler();
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
