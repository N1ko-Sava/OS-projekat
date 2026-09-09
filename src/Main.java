import java.util.List;

public class Main {

    public static void main(String[] args) {

        OSKernel kernel = new OSKernel();
        kernel.boot();

        kernel.executeCommand("mkdir /home");
        kernel.executeCommand("touch /home/test.txt");

        System.out.println("\n--- WRITE SYSCALL ---");

        kernel.syscall(
                new Syscall(
                        SyscallType.WRITE,
                        List.of(
                                "/home/test.txt",
                                "Pozdrav preko syscall-a"
                        )
                )
        );

        System.out.println("\n--- READ SYSCALL ---");

        kernel.syscall(
                new Syscall(
                        SyscallType.READ,
                        List.of("/home/test.txt")
                )
        );

        System.out.println("\n--- CREATE PROCESS SYSCALL ---");

        kernel.syscall(
                new Syscall(
                        SyscallType.CREATE_PROCESS,
                        List.of("novi_program", "0")
                )
        );

        kernel.executeCommand("ps");
    }
}