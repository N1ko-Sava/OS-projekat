public class Main {

    public static void main(String[] args) {

        ZeroAddressAssembler assembler =
                new ZeroAddressAssembler();

        String source =
                """
                PUSH 5
                PUSH 3
                ADD
                PUSH 2
                MUL
                PRINT
                HALT
                """;

        System.out.println("--- ASEMBLER ---");
        System.out.println(source);

        String binary =
                assembler.assemble(source);

        System.out.println("--- BINARNI ZAPIS ---");
        System.out.println(binary);

        String decoded =
                assembler.disassemble(binary);

        System.out.println("--- DEKODIRANO ---");
        System.out.println(decoded);
    }
}