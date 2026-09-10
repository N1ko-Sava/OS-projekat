import java.util.List;

public class FileSystem {

    private Directory root;
    private Directory currentDirectory;

    private DiskDevice disk;

    // Simulacija diska
    private boolean[] freeBlocks;
    private String[] diskBlocks;

    // Koliko znakova može stati u jedan blok
    private static final int BLOCK_SIZE = 100;

    // Ukupan broj blokova na disku
    private static final int BLOCK_COUNT = 100;


    // =========================
    // KONSTRUKTOR
    // =========================

    public FileSystem(DiskDevice disk) {

        this.disk = disk;

        this.root = new Directory("/", null);
        this.currentDirectory = root;

        freeBlocks = new boolean[BLOCK_COUNT];
        diskBlocks = new String[BLOCK_COUNT];

        for (int i = 0; i < BLOCK_COUNT; i++) {

            freeBlocks[i] = true;
            diskBlocks[i] = "";
        }
    }


    // =========================
    // GETTERI
    // =========================

    public Directory getRoot() {
        return root;
    }

    public Directory getCurrentDirectory() {
        return currentDirectory;
    }


    // =========================
    // RAD SA DISK BLOKOVIMA
    // =========================

    private int allocateBlock() {

        for (int i = 0; i < freeBlocks.length; i++) {

            if (freeBlocks[i]) {

                freeBlocks[i] = false;

                return i;
            }
        }

        return -1;
    }


    private void freeBlock(int block) {

        if (block >= 0 && block < freeBlocks.length) {

            freeBlocks[block] = true;
            diskBlocks[block] = "";
        }
    }


    // =========================
    // RESOLVE PUTANJE
    // =========================

    public FsNode resolve(String path) {

        if (path == null || path.isBlank()) {
            return null;
        }

        // Root
        if (path.equals("/")) {
            return root;
        }

        Directory startDirectory;

        // Apsolutna putanja
        if (path.startsWith("/")) {
            startDirectory = root;
        }

        // Relativna putanja
        else {
            startDirectory = currentDirectory;
        }

        String[] parts = path.split("/");

        FsNode current = startDirectory;

        for (String part : parts) {

            if (part.isBlank() || part.equals(".")) {
                continue;
            }

            // cd ..
            if (part.equals("..")) {

                if (current instanceof Directory) {

                    Directory dir = (Directory) current;

                    if (dir.getParent() != null) {
                        current = dir.getParent();
                    }
                }

                continue;
            }

            if (!(current instanceof Directory)) {
                return null;
            }

            Directory directory = (Directory) current;

            current = directory.getChild(part);

            if (current == null) {
                return null;
            }
        }

        return current;
    }


    // =========================
    // KREIRANJE DIREKTORIJUMA
    // =========================

    public Directory createDirectory(String path) {

        if (path == null || path.isBlank()) {
            return null;
        }

        String cleanPath = path;

        // Ukloni završni /
        if (cleanPath.length() > 1 &&
                cleanPath.endsWith("/")) {

            cleanPath =
                    cleanPath.substring(
                            0,
                            cleanPath.length() - 1
                    );
        }

        int lastSlash =
                cleanPath.lastIndexOf("/");

        String directoryName;
        String parentPath;


        // Npr. mkdir folder
        if (lastSlash == -1) {

            directoryName = cleanPath;
            parentPath = ".";
        }

        // Npr. mkdir /folder
        else if (lastSlash == 0) {

            directoryName =
                    cleanPath.substring(1);

            parentPath = "/";
        }

        // Npr. mkdir /home/test
        else {

            directoryName =
                    cleanPath.substring(lastSlash + 1);

            parentPath =
                    cleanPath.substring(0, lastSlash);
        }


        FsNode parentNode =
                resolve(parentPath);


        if (!(parentNode instanceof Directory)) {

            System.out.println(
                    "Roditeljski direktorijum ne postoji: "
                            + parentPath
            );

            return null;
        }


        Directory parent =
                (Directory) parentNode;


        // Provjera da već postoji
        if (parent.getChild(directoryName) != null) {

            System.out.println(
                    "Vec postoji: " + path
            );

            return null;
        }


        Directory newDirectory =
                new Directory(
                        directoryName,
                        parent
                );


        parent.addChild(newDirectory);


        System.out.println(
                "Kreiran direktorijum: " + path
        );


        return newDirectory;
    }


    // =========================
    // KREIRANJE FAJLA
    // =========================

    public File createFile(String path) {

        if (path == null || path.isBlank()) {
            return null;
        }

        int lastSlash =
                path.lastIndexOf("/");

        String fileName;
        String parentPath;


        // touch program.asm
        if (lastSlash == -1) {

            fileName = path;
            parentPath = ".";
        }

        // touch /program.asm
        else if (lastSlash == 0) {

            fileName =
                    path.substring(1);

            parentPath = "/";
        }

        // touch /home/program.asm
        else {

            fileName =
                    path.substring(lastSlash + 1);

            parentPath =
                    path.substring(0, lastSlash);
        }


        FsNode parentNode =
                resolve(parentPath);


        if (!(parentNode instanceof Directory)) {

            System.out.println(
                    "Roditeljski direktorijum ne postoji: "
                            + parentPath
            );

            return null;
        }


        Directory parent =
                (Directory) parentNode;


        if (parent.getChild(fileName) != null) {

            System.out.println(
                    "Vec postoji: " + path
            );

            return null;
        }


        // Kreiramo fajl
        File newFile =
                new File(
                        fileName,
                        parent
                );


        // Kod indeksirane alokacije
        // fajl dobija poseban indeksni blok.
        int indexBlock =
                allocateBlock();


        if (indexBlock == -1) {

            System.out.println(
                    "Nema slobodnih blokova na disku."
            );

            return null;
        }


        newFile.setIndexBlock(
                indexBlock
        );


        parent.addChild(newFile);


        System.out.println(
                "Kreiran fajl: "
                        + path
                        + " | index blok: "
                        + indexBlock
        );


        return newFile;
    }


    // =========================
    // PROMJENA DIREKTORIJUMA
    // =========================

    public boolean changeDirectory(String path) {

        FsNode node =
                resolve(path);


        if (node == null) {

            System.out.println(
                    "Direktorijum ne postoji: "
                            + path
            );

            return false;
        }


        if (!(node instanceof Directory)) {

            System.out.println(
                    "Putanja nije direktorijum: "
                            + path
            );

            return false;
        }


        currentDirectory =
                (Directory) node;


        System.out.println(
                "Trenutni direktorijum: "
                        + currentDirectory.getName()
        );


        return true;
    }


    // =========================
    // LS
    // =========================

    public void listCurrentDirectory() {

        List<FsNode> nodes =
                currentDirectory.list();


        if (nodes.isEmpty()) {

            System.out.println(
                    "Direktorijum je prazan."
            );

            return;
        }


        System.out.println(
                "Sadrzaj direktorijuma "
                        + currentDirectory.getName()
                        + ":"
        );


        for (FsNode node : nodes) {

            if (node instanceof Directory) {

                System.out.println(
                        "[DIR]  "
                                + node.getName()
                );

            } else if (node instanceof File) {

                File file =
                        (File) node;

                System.out.println(
                        "[FILE] "
                                + file.getName()
                                + " | index blok="
                                + file.getIndexBlock()
                );
            }
        }
    }


    // =========================
    // OTVARANJE FAJLA
    // =========================

    public OpenFileHandle openFile(
            String path,
            FileMode mode,
            PCB process) {

        FsNode node =
                resolve(path);


        if (node == null) {

            System.out.println(
                    "Fajl ne postoji: "
                            + path
            );

            return null;
        }


        if (!(node instanceof File)) {

            System.out.println(
                    "Putanja nije fajl: "
                            + path
            );

            return null;
        }


        File file =
                (File) node;


        System.out.println(
                "Otvaram fajl "
                        + path
                        + " u modu "
                        + mode
        );


        /*
         * Fajl se nalazi na simuliranom disku.
         *
         * Pristup fajlu generise disk zahtjev.
         * DiskDevice zatim koristi SSTF.
         */

        if (disk != null &&
                process != null) {

            IOType type;

            if (mode == FileMode.READ) {
                type = IOType.READ;
            } else {
                type = IOType.WRITE;
            }


            IOOperation operation =
                    new IOOperation(
                            type,
                            "",
                            1,
                            file.getIndexBlock()
                    );


            disk.startOperation(
                    operation,
                    process
            );
        }


        return new OpenFileHandle(
                file,
                mode
        );
    }


    /*
     * Ovu metodu ostavljamo zbog syscall-a
     * koji smo ranije napravili.
     */
    public OpenFileHandle open(String path) {

        FsNode node =
                resolve(path);


        if (!(node instanceof File)) {

            System.out.println(
                    "Fajl ne postoji: "
                            + path
            );

            return null;
        }


        File file =
                (File) node;


        return new OpenFileHandle(
                file,
                FileMode.READ
        );
    }


    // =========================
    // UPIS U FAJL
    // =========================

    public boolean writeFile(
            String path,
            String data) {

        FsNode node =
                resolve(path);


        if (!(node instanceof File)) {

            System.out.println(
                    "Fajl ne postoji: "
                            + path
            );

            return false;
        }


        File file =
                (File) node;


        /*
         * Ako fajl već ima data blokove,
         * prvo ih oslobodimo.
         */

        for (Integer block :
                file.getDataBlocks()) {

            freeBlock(block);
        }


        file.clearDataBlocks();


        // Koliko blokova je potrebno
        int blocksNeeded =
                (data.length()
                        + BLOCK_SIZE - 1)
                        / BLOCK_SIZE;


        for (int i = 0;
             i < blocksNeeded;
             i++) {

            int block =
                    allocateBlock();


            if (block == -1) {

                System.out.println(
                        "Nema dovoljno prostora na disku."
                );

                return false;
            }


            int start =
                    i * BLOCK_SIZE;

            int end =
                    Math.min(
                            start + BLOCK_SIZE,
                            data.length()
                    );


            String part =
                    data.substring(
                            start,
                            end
                    );


            diskBlocks[block] =
                    part;


            file.addDataBlock(
                    block
            );
        }


        /*
         * Indeksni blok sadrži listu
         * data blokova fajla.
         */

        diskBlocks[file.getIndexBlock()] =
                file.getDataBlocks()
                        .toString();


        file.write(data);


        System.out.println(
                "Upisano u fajl: "
                        + path
                        + " | index blok: "
                        + file.getIndexBlock()
                        + " | data blokovi: "
                        + file.getDataBlocks()
        );


        return true;
    }


    // =========================
    // CITANJE FAJLA
    // =========================

    public String readFile(String path) {

        FsNode node =
                resolve(path);


        if (!(node instanceof File)) {

            System.out.println(
                    "Fajl ne postoji: "
                            + path
            );

            return null;
        }


        File file =
                (File) node;


        StringBuilder result =
                new StringBuilder();


        for (Integer block :
                file.getDataBlocks()) {

            if (block >= 0 &&
                    block < diskBlocks.length) {

                result.append(
                        diskBlocks[block]
                );
            }
        }


        return result.toString();
    }


    // =========================
    // BRISANJE
    // =========================

    public boolean delete(String path) {

        FsNode node =
                resolve(path);


        if (node == null) {

            System.out.println(
                    "Ne postoji: "
                            + path
            );

            return false;
        }


        // Ne dozvoljavamo brisanje root-a
        if (node == root) {

            System.out.println(
                    "Root direktorijum se ne moze obrisati."
            );

            return false;
        }


        Directory parent =
                node.getParent();


        if (parent == null) {
            return false;
        }


        // Ako je fajl, oslobodi njegove blokove
        if (node instanceof File) {

            File file =
                    (File) node;


            for (Integer block :
                    file.getDataBlocks()) {

                freeBlock(block);
            }


            file.clearDataBlocks();


            freeBlock(
                    file.getIndexBlock()
            );


            System.out.println(
                    "Oslobodjeni disk blokovi fajla: "
                            + path
            );
        }


        // Za sada ne brišemo neprazan direktorijum
        if (node instanceof Directory) {

            Directory directory =
                    (Directory) node;


            if (!directory.list().isEmpty()) {

                System.out.println(
                        "Direktorijum nije prazan."
                );

                return false;
            }
        }


        parent.removeChild(
                node.getName()
        );


        System.out.println(
                "Obrisano: " + path
        );


        return true;
    }


    // =========================
    // PRIKAZ BLOKOVA - ZA TEST
    // =========================

    public void printDiskBlocks() {

        System.out.println(
                "--- ZAUZETI DISK BLOKOVI ---"
        );


        for (int i = 0;
             i < BLOCK_COUNT;
             i++) {

            if (!freeBlocks[i]) {

                System.out.println(
                        "Blok "
                                + i
                                + ": "
                                + diskBlocks[i]
                );
            }
        }
    }
}