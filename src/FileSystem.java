public class FileSystem {

    private Directory root;
    private DiskDevice disk;
    private boolean[] freeBlocks;
    private String[] diskBlocks;

    private static final int BLOCK_SIZE = 100;
    private static final int BLOCK_COUNT = 100;

    public FileSystem(DiskDevice disk) {
        this.disk = disk;
        this.root = new Directory("/", null);

        freeBlocks = new boolean[BLOCK_COUNT];
        diskBlocks = new String[BLOCK_COUNT];

        for (int i = 0; i < BLOCK_COUNT; i++) {
            freeBlocks[i] = true;
            diskBlocks[i] = "";
        }
    }



    public File createFile(String path) {

        if (path == null || path.isEmpty()) {
            return null;
        }

        if (!path.startsWith("/")) {
            return null;
        }

        if (resolve(path) != null) {
            return null;
        }

        int lastSlash = path.lastIndexOf('/');

        String parentPath;

        if (lastSlash == 0) {
            parentPath = "/";
        } else {
            parentPath = path.substring(0, lastSlash);
        }

        String fileName = path.substring(lastSlash + 1);

        if (fileName.isEmpty()) {
            return null;
        }

        FsNode parentNode = resolve(parentPath);

        if (!(parentNode instanceof Directory)) {
            return null;
        }

        Directory parent = (Directory) parentNode;

        File newFile = new File(fileName, parent);

        int indexBlock = allocateBlock();

        if (indexBlock == -1) {
            System.out.println("Nema slobodnih blokova na disku.");
            return null;
        }

        newFile.setIndexBlock(indexBlock);

        parent.addChild(newFile);

        System.out.println(
                "Kreiran fajl: " + path
                        + " | index blok: " + indexBlock
        );

        return newFile;
    }

    public Directory createDirectory(String path) {

        if (path == null || path.isEmpty() || path.equals("/")) {
            return null;
        }

        if (!path.startsWith("/")) {
            return null;
        }


        if (resolve(path) != null) {
            return null;
        }

        int lastSlash = path.lastIndexOf('/');

        String parentPath;

        if (lastSlash == 0) {
            parentPath = "/";
        } else {
            parentPath = path.substring(0, lastSlash);
        }

        String directoryName = path.substring(lastSlash + 1);

        if (directoryName.isEmpty()) {
            return null;
        }

        FsNode parentNode = resolve(parentPath);

        if (!(parentNode instanceof Directory)) {
            return null;
        }

        Directory parent = (Directory) parentNode;

        Directory newDirectory =
                new Directory(directoryName, parent);

        parent.addChild(newDirectory);

        System.out.println(
                "Kreiran direktorijum: " + path
        );

        return newDirectory;
    }

    public OpenFileHandle open(String path) {

        FsNode node = resolve(path);

        if (!(node instanceof File)) {
            return null;
        }

        File file = (File) node;

        OpenFileHandle handle = new OpenFileHandle();

        handle.setFile(file);
        handle.setPosition(0);
        handle.setMode(FileMode.READ);

        System.out.println(
                "Otvoren fajl: " + path
        );

        return handle;
    }

    public void delete(String path) {

        if (path == null || path.isEmpty() || path.equals("/")) {
            return;
        }

        FsNode node = resolve(path);

        if (node == null) {
            System.out.println("Putanja ne postoji: " + path);
            return;
        }

        Directory parent = node.getParent();

        if (parent == null) {
            return;
        }

        if (node instanceof File) {

            File file = (File) node;

            for (int block : file.getDataBlocks()) {
                freeBlock(block);
            }

            freeBlock(file.getIndexBlock());

            file.clearDataBlocks();

            System.out.println(
                    "Oslobodjeni disk blokovi fajla: " + path
            );
        }

        parent.removeChild(node.getName());

        System.out.println("Obrisano: " + path);
    }

    public boolean writeFile(String path, String data) {

        FsNode node = resolve(path);

        if (!(node instanceof File)) {
            System.out.println("Fajl ne postoji: " + path);
            return false;
        }

        File file = (File) node;

        if (data == null) {
            data = "";
        }


        for (int block : file.getDataBlocks()) {
            freeBlock(block);
        }

        file.clearDataBlocks();

        int blocksNeeded =
                (data.length() + BLOCK_SIZE - 1) / BLOCK_SIZE;

        for (int i = 0; i < blocksNeeded; i++) {

            int block = allocateBlock();

            if (block == -1) {

                System.out.println("Nema dovoljno prostora na disku.");

                for (int allocated : file.getDataBlocks()) {
                    freeBlock(allocated);
                }

                file.clearDataBlocks();
                return false;
            }

            int start = i * BLOCK_SIZE;
            int end = Math.min(start + BLOCK_SIZE, data.length());

            String part = data.substring(start, end);

            diskBlocks[block] = part;

            file.addDataBlock(block);
        }

        diskBlocks[file.getIndexBlock()] =
                file.getDataBlocks().toString();

        file.write(data);

        System.out.println(
                "Upisano u fajl: " + path
                        + " | index blok: "
                        + file.getIndexBlock()
                        + " | data blokovi: "
                        + file.getDataBlocks()
        );

        return true;
    }

    public String readFile(String path) {

        FsNode node = resolve(path);

        if (!(node instanceof File)) {
            return null;
        }

        File file = (File) node;

        StringBuilder result = new StringBuilder();

        for (int block : file.getDataBlocks()) {
            result.append(diskBlocks[block]);
        }

        return result.toString();
    }




    public FsNode resolve(String path) {

        if (path == null || path.isEmpty()) {
            return null;
        }

        if (path.equals("/")) {
            return root;
        }


        if (!path.startsWith("/")) {
            return null;
        }

        String[] parts = path.split("/");

        FsNode current = root;

        for (String part : parts) {


            if (part.isEmpty()) {
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


}
