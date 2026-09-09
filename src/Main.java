public class Main {

    public static void main(String[] args) {

        OSKernel kernel = new OSKernel();
        kernel.boot();

        FileSystem fs = new FileSystem(
                (DiskDevice) kernel.getDevice("disk")
        );

        System.out.println("\n--- FILE SYSTEM TEST ---");

        fs.createDirectory("/home");

        fs.createFile("/home/test.txt");

        String data =
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                        + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                        + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                        + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                        + "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        System.out.println("\n--- WRITE ---");

        fs.writeFile("/home/test.txt", data);

        System.out.println("\n--- READ ---");

        String result = fs.readFile("/home/test.txt");

        System.out.println(result);

        System.out.println("\n--- DELETE ---");

        fs.delete("/home/test.txt");

        System.out.println("\n--- RESOLVE NAKON BRISANJA ---");

        FsNode node = fs.resolve("/home/test.txt");

        if (node == null) {
            System.out.println("Fajl je uspjesno obrisan.");
        } else {
            System.out.println("GRESKA: fajl jos postoji.");
        }
    }
}