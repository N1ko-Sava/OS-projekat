import java.util.ArrayList;
import java.util.List;

public class OSKernel {

    private List<PCB> processTable;
    private ReadyQueue readyQueue;
    private BlockedQueue blockedQueue;
    private CPU cpu;
    private Scheduler scheduler;
    private MemoryManager memoryManager;
    private FileSystem fileSystem;
    private IOManager ioManager;
    private int nextPid;

    public OSKernel() {
        processTable = new ArrayList<>();
        readyQueue = new ReadyQueue();
        blockedQueue = new BlockedQueue();
        nextPid = 1;
    }

    public void boot() {
        System.out.println("\t --- Booting OS... --- \t");

        processTable = new ArrayList<>();

        readyQueue = new ReadyQueue();
        blockedQueue = new BlockedQueue();

        RAM ram = new RAM(1024);

        memoryManager = new MemoryManager(ram);

        cpu = new CPU(null, 0);

        scheduler = new XScheduler();

        ioManager = new IOManager();

        DiskDevice disk = new DiskDevice("disk");
        ioManager.addDevice(disk);

        fileSystem = new FileSystem(disk);


        ioManager = new IOManager();



        ioManager.addDevice(new DiskDevice("disk"));

        createProcess("idle",0);
        createProcess("system_task",0);

        System.out.println("\t --- OS booted  --- \t");
    }

    public int createProcess(String programName, int priority) {
        PCB pcb = new PCB();

        pcb.setPid(nextPid++);
        pcb.setState(ProcessState.READY);
        pcb.setPriority(priority);
        pcb.setProgramCounter(0);
        pcb.setLimit(64);

        boolean allocated = memoryManager.allocate(pcb, 64);

        if (!allocated) {
            System.out.println("Not enough memory for process " + programName);
            return -1;
        }

        processTable.add(pcb);
        readyQueue.add(pcb);

        System.out.println("Process created: PID=" + pcb.getPid() + " (" + programName + ")");

        return pcb.getPid();

    }

    public void terminateProcess(PCB pcb) {

        if (pcb == null) {
            return;
        }

        pcb.setState(ProcessState.TERMINATED);

        if (cpu.getCurrent() == pcb) {
            cpu.setCurrent(null);
        }

        memoryManager.free(pcb);

        processTable.remove(pcb);

        System.out.println("Proces PID=" + pcb.getPid() + " je zavrsen.");
    }


    public void blockProcess(PCB pcb) {
        if (pcb == null) {
            return;
        }

        // Ako je ovaj proces trenutno na CPU-u, ukloni ga
        if (cpu.getCurrent() == pcb) {
            cpu.setCurrent(null);
        }

        // Prebaci proces u BlockedQueue
        blockedQueue.block(pcb);

        System.out.println(
                "Proces PID=" + pcb.getPid() + " je blokiran."
        );
    }


    public void unblockProcess(PCB pcb) {
        if (pcb == null) {
            return;
        }

        if (blockedQueue.unblock(pcb)) {
            readyQueue.add(pcb);

            System.out.println(
                    "Proces PID=" + pcb.getPid() + " je odblokiran."
            );
        }
    }


    public void timerTick() {


        if (cpu.getCurrent() == null) {

            PCB next = scheduler.chooseNext(readyQueue);

            if (next != null) {
                cpu.contextSwitch(next);
            } else {
                System.out.println("Nema procesa spremnih za izvrsavanje.");
                return;
            }
        }


        cpu.executeOneStep();
    }

    public void handleIOCompletion(PCB pcb) {

        if (pcb == null) {
            return;
        }

        if (pcb.getState() == ProcessState.WAITING) {
            unblockProcess(pcb);

            System.out.println(
                    "IO zavrsen za proces PID=" + pcb.getPid()
            );
        }
    }

    public void completeIO(IODevice device) {

        PCB pcb = ioManager.completeIO(device);

        if (pcb != null) {
            handleIOCompletion(pcb);
        }
    }
    public void requestIO(PCB pcb, String deviceName, IOOperation op) {

        if (pcb == null || op == null) {
            return;
        }

        boolean started = ioManager.requestIO(
                pcb,
                deviceName,
                op
        );

        if (started) {

            blockProcess(pcb);

            System.out.println(
                    "Proces PID=" + pcb.getPid()
                            + " je zatrazio IO na uredjaju: "
                            + deviceName
            );
        }
    }
    public PCB findProcess(int pid) {
        for (PCB pcb : processTable) {
            if (pcb.getPid() == pid) {
                return pcb;
            }
        }

        return null;
    }

    public IODevice getDevice(String name) {
        return ioManager.getDevice(name);
    }


    public void executeCommand(String command) {

        if (command == null || command.isBlank()) {
            return;
        }

        String[] parts = command.trim().split("\\s+", 3);

        String cmd = parts[0];

        switch (cmd) {

            case "mkdir":
                if (parts.length < 2) {
                    System.out.println("Upotreba: mkdir putanja");
                    return;
                }

                fileSystem.createDirectory(parts[1]);
                break;

            case "touch":
                if (parts.length < 2) {
                    System.out.println("Upotreba: touch putanja");
                    return;
                }

                fileSystem.createFile(parts[1]);
                break;

            case "write":
                if (parts.length < 3) {
                    System.out.println("Upotreba: write putanja tekst");
                    return;
                }

                fileSystem.writeFile(parts[1], parts[2]);
                break;

            case "cat":
                if (parts.length < 2) {
                    System.out.println("Upotreba: cat putanja");
                    return;
                }

                String content = fileSystem.readFile(parts[1]);

                if (content != null) {
                    System.out.println(content);
                } else {
                    System.out.println("Fajl ne postoji.");
                }

                break;

            case "rm":
                if (parts.length < 2) {
                    System.out.println("Upotreba: rm putanja");
                    return;
                }

                fileSystem.delete(parts[1]);
                break;

            case "ps":

                System.out.println("--- TABELA PROCESA ---");

                for (PCB pcb : processTable) {
                    System.out.println(
                            "PID=" + pcb.getPid()
                                    + " | stanje=" + pcb.getState()
                                    + " | PC=" + pcb.getProgramCounter()
                    );
                }

                break;


            case "block":

                if (parts.length < 2) {
                    System.out.println("Upotreba: block PID");
                    return;
                }

                try {
                    int pid = Integer.parseInt(parts[1]);

                    PCB pcb = findProcess(pid);

                    if (pcb == null) {
                        System.out.println("Proces PID=" + pid + " ne postoji.");
                        return;
                    }

                    if (pcb.getState() == ProcessState.WAITING) {
                        System.out.println("Proces je vec blokiran.");
                        return;
                    }

                    blockProcess(pcb);

                } catch (NumberFormatException e) {
                    System.out.println("PID mora biti broj.");
                }

                break;


            case "unblock":

                if (parts.length < 2) {
                    System.out.println("Upotreba: unblock PID");
                    return;
                }

                try {
                    int pid = Integer.parseInt(parts[1]);

                    PCB pcb = findProcess(pid);

                    if (pcb == null) {
                        System.out.println("Proces PID=" + pid + " ne postoji.");
                        return;
                    }

                    if (pcb.getState() != ProcessState.WAITING) {
                        System.out.println("Proces PID=" + pid + " nije blokiran.");
                        return;
                    }

                    unblockProcess(pcb);

                } catch (NumberFormatException e) {
                    System.out.println("PID mora biti broj.");
                }

                break;


            case "kill":

                if (parts.length < 2) {
                    System.out.println("Upotreba: kill PID");
                    return;
                }

                try {
                    int pid = Integer.parseInt(parts[1]);

                    PCB pcb = findProcess(pid);

                    if (pcb == null) {
                        System.out.println("Proces PID=" + pid + " ne postoji.");
                        return;
                    }

                    terminateProcess(pcb);

                } catch (NumberFormatException e) {
                    System.out.println("PID mora biti broj.");
                }

                break;

            default:
                System.out.println("Nepoznata komanda: " + cmd);


        }
    }




    public void syscall(Syscall request) {

        if (request == null) {
            return;
        }

        switch (request.getType()) {

            case CREATE_PROCESS: {

                if (request.getArgs().isEmpty()) {
                    System.out.println("CREATE_PROCESS zahtijeva naziv programa.");
                    return;
                }

                String programName = request.getArgs().get(0);

                int priority = 0;

                if (request.getArgs().size() > 1) {
                    try {
                        priority = Integer.parseInt(
                                request.getArgs().get(1)
                        );
                    } catch (NumberFormatException e) {
                        System.out.println("Prioritet mora biti broj.");
                        return;
                    }
                }

                createProcess(programName, priority);

                break;
            }


            case EXIT: {

                PCB current = cpu.getCurrent();

                if (current == null) {
                    System.out.println("Nema aktivnog procesa.");
                    return;
                }

                terminateProcess(current);

                break;
            }


            case OPEN: {

                if (request.getArgs().isEmpty()) {
                    System.out.println("OPEN zahtijeva putanju.");
                    return;
                }

                String path = request.getArgs().get(0);

                OpenFileHandle handle =
                        fileSystem.open(path);

                if (handle == null) {
                    System.out.println(
                            "Fajl nije moguce otvoriti: " + path
                    );
                    return;
                }

                PCB current = cpu.getCurrent();

                if (current != null) {
                    current.getOpenFiles().add(handle);
                }

                break;
            }


            case READ: {

                if (request.getArgs().isEmpty()) {
                    System.out.println("READ zahtijeva putanju.");
                    return;
                }

                String path = request.getArgs().get(0);

                String data = fileSystem.readFile(path);

                if (data == null) {
                    System.out.println("Fajl ne postoji: " + path);
                    return;
                }

                System.out.println(
                        "READ " + path + ": " + data
                );

                break;
            }


            case WRITE: {

                if (request.getArgs().size() < 2) {
                    System.out.println(
                            "WRITE zahtijeva putanju i podatke."
                    );
                    return;
                }

                String path = request.getArgs().get(0);
                String data = request.getArgs().get(1);

                fileSystem.writeFile(path, data);

                break;
            }


            case SLEEP: {

                PCB current = cpu.getCurrent();

                if (current == null) {
                    System.out.println("Nema aktivnog procesa.");
                    return;
                }

                blockProcess(current);

                break;
            }


            case YIELD: {

                /*
                 * Kod FCFS-a proces se normalno ne prekida
                 * dok ne zavrsi ili se blokira.
                 *
                 * YIELD predstavlja dobrovoljno odricanje CPU-a.
                 */

                PCB current = cpu.getCurrent();

                if (current == null) {
                    return;
                }

                current.setState(ProcessState.READY);

                readyQueue.add(current);

                cpu.setCurrent(null);

                System.out.println(
                        "Proces PID=" + current.getPid()
                                + " je dobrovoljno oslobodio CPU."
                );

                break;
            }
        }
    }
}