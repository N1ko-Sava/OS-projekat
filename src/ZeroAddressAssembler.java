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

    public String assemble(String sourceCode) {

        if (sourceCode == null || sourceCode.isBlank()) {
            return "";
        }

        StringBuilder binary = new StringBuilder();

        String[] lines = sourceCode.split("\\R");

        for (String line : lines) {

            line = line.trim();

            if (line.isBlank()) {
                continue;
            }

            String[] parts = line.split("\\s+");
            String command = parts[0].toUpperCase();

            switch (command) {

                case "PUSH":

                    if (parts.length != 2) {
                        throw new IllegalArgumentException(
                                "PUSH mora imati argument."
                        );
                    }

                    int value = Integer.parseInt(parts[1]);

                    if (value < 0 || value > 255) {
                        throw new IllegalArgumentException(
                                "PUSH podrzava vrijednosti od 0 do 255."
                        );
                    }

                    String valueBinary =
                            String.format(
                                    "%8s",
                                    Integer.toBinaryString(value)
                            ).replace(' ', '0');

                    binary.append("0001 ")
                            .append(valueBinary);

                    break;


                case "ADD":
                    binary.append("0010");
                    break;

                case "SUB":
                    binary.append("0011");
                    break;

                case "MUL":
                    binary.append("0100");
                    break;

                case "DIV":
                    binary.append("0101");
                    break;

                case "PRINT":
                    binary.append("0110");
                    break;

                case "HALT":
                    binary.append("0111");
                    break;

                default:
                    throw new IllegalArgumentException(
                            "Nepoznata instrukcija: " + command
                    );
            }

            binary.append("\n");
        }

        return binary.toString();
    }
    public String disassemble(String binaryCode) {

        if (binaryCode == null || binaryCode.isBlank()) {
            return "";
        }

        StringBuilder source = new StringBuilder();

        String[] lines = binaryCode.split("\\R");

        for (String line : lines) {

            line = line.trim();

            if (line.isBlank()) {
                continue;
            }

            String[] parts = line.split("\\s+");

            String opcode = parts[0];

            switch (opcode) {

                case "0001":

                    if (parts.length != 2) {
                        throw new IllegalArgumentException(
                                "Neispravan PUSH binarni zapis."
                        );
                    }

                    int value =
                            Integer.parseInt(
                                    parts[1],
                                    2
                            );

                    source.append("PUSH ")
                            .append(value);

                    break;


                case "0010":
                    source.append("ADD");
                    break;

                case "0011":
                    source.append("SUB");
                    break;

                case "0100":
                    source.append("MUL");
                    break;

                case "0101":
                    source.append("DIV");
                    break;

                case "0110":
                    source.append("PRINT");
                    break;

                case "0111":
                    source.append("HALT");
                    break;

                default:
                    throw new IllegalArgumentException(
                            "Nepoznat opcode: " + opcode
                    );
            }

            source.append("\n");
        }

        return source.toString();
    }
}