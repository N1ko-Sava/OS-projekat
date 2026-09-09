public class FileSystem {

    private Directory root;
    private DiskDevice disk;
    private boolean[] freeBlocks;
    private String[] diskBlocks;

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

        parent.removeChild(node.getName());

        System.out.println("Obrisano: " + path);
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
