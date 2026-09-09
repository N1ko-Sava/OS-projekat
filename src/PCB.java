import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class PCB {

    private int pid;
    private ProcessState state;
    private int priority;
    private int programCounter;

    private Map<String, Integer> registers;

    private int baseAddress;
    private int limit;

    private List<OpenFileHandle> openFiles;

    private Stack<Integer> stack;
    private List<String> program;


    public PCB() {

        this.programCounter = 0;

        this.registers = new HashMap<>();
        this.openFiles = new ArrayList<>();

        this.stack = new Stack<>();
        this.program = new ArrayList<>();
    }


    public PCB(int pid,
               ProcessState state,
               int priority,
               int programCounter,
               Map<String, Integer> registers,
               int baseAddress,
               int limit,
               List<OpenFileHandle> openFiles,
               Stack<Integer> stack,
               List<String> program) {

        this.pid = pid;
        this.state = state;
        this.priority = priority;
        this.programCounter = programCounter;

        this.registers = registers;

        this.baseAddress = baseAddress;
        this.limit = limit;

        this.openFiles = openFiles;

        this.stack = stack;
        this.program = program;
    }


    public List<OpenFileHandle> getOpenFiles() {
        return openFiles;
    }

    public void setOpenFiles(List<OpenFileHandle> openFiles) {
        this.openFiles = openFiles;
    }


    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }


    public int getBaseAddress() {
        return baseAddress;
    }

    public void setBaseAddress(int baseAddress) {
        this.baseAddress = baseAddress;
    }


    public Map<String, Integer> getRegisters() {
        return registers;
    }

    public void setRegisters(Map<String, Integer> registers) {
        this.registers = registers;
    }


    public int getProgramCounter() {
        return programCounter;
    }

    public void setProgramCounter(int programCounter) {
        this.programCounter = programCounter;
    }


    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }


    public ProcessState getState() {
        return state;
    }

    public void setState(ProcessState state) {
        this.state = state;
    }


    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }


    public Stack<Integer> getStack() {
        return stack;
    }

    public void setStack(Stack<Integer> stack) {
        this.stack = stack;
    }


    public List<String> getProgram() {
        return program;
    }

    public void setProgram(List<String> program) {
        this.program = program;
    }


    @Override
    public String toString() {
        return "PCB{" +
                "pid=" + pid +
                ", state=" + state +
                ", priority=" + priority +
                ", programCounter=" + programCounter +
                ", registers=" + registers +
                ", baseAddress=" + baseAddress +
                ", limit=" + limit +
                ", openFiles=" + openFiles +
                ", stack=" + stack +
                ", program=" + program +
                '}';
    }
}