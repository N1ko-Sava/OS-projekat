import java.util.Stack;

public class ZeroAddressAssembler {

    public void executeInstruction(PCB process, String instruction) {

        if (process == null || instruction == null || instruction.isBlank()) {
            return;
        }

        String[] parts = instruction.trim().split("\\s+");
        String command = parts[0].toUpperCase();

        Stack<Integer> stack = process.getStack();

        switch (command) {

            case "PUSH":

                if (parts.length != 2) {
                    System.out.println("GRESKA: PUSH zahtijeva broj.");
                    return;
                }

                try {
                    int value = Integer.parseInt(parts[1]);
                    stack.push(value);
                } catch (NumberFormatException e) {
                    System.out.println("GRESKA: PUSH vrijednost mora biti broj.");
                }

                break;


            case "ADD":

                if (stack.size() < 2) {
                    System.out.println("GRESKA: nema dovoljno vrijednosti na steku.");
                    return;
                }

                int addB = stack.pop();
                int addA = stack.pop();

                stack.push(addA + addB);

                break;


            case "SUB":

                if (stack.size() < 2) {
                    System.out.println("GRESKA: nema dovoljno vrijednosti na steku.");
                    return;
                }

                int subB = stack.pop();
                int subA = stack.pop();

                stack.push(subA - subB);

                break;


            case "MUL":

                if (stack.size() < 2) {
                    System.out.println("GRESKA: nema dovoljno vrijednosti na steku.");
                    return;
                }

                int mulB = stack.pop();
                int mulA = stack.pop();

                stack.push(mulA * mulB);

                break;


            case "DIV":

                if (stack.size() < 2) {
                    System.out.println("GRESKA: nema dovoljno vrijednosti na steku.");
                    return;
                }

                int divB = stack.pop();
                int divA = stack.pop();

                if (divB == 0) {
                    System.out.println("GRESKA: dijeljenje nulom.");

                    // vratimo vrijednosti nazad na stek
                    stack.push(divA);
                    stack.push(divB);

                    return;
                }

                stack.push(divA / divB);

                break;


            case "PRINT":

                if (stack.isEmpty()) {
                    System.out.println("Stek je prazan.");
                    return;
                }

                System.out.println(
                        "OUTPUT PID="
                                + process.getPid()
                                + ": "
                                + stack.peek()
                );

                break;


            case "HALT":

                process.setState(ProcessState.TERMINATED);

                System.out.println(
                        "PID=" + process.getPid()
                                + " zavrsio program."
                );

                break;


            default:

                System.out.println(
                        "Nepoznata asembler instrukcija: " + command
                );
        }
    }
}