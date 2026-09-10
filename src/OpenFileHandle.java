public class OpenFileHandle {

    private File file;
    private FileMode mode;

    public OpenFileHandle(File file, FileMode mode) {
        this.file = file;
        this.mode = mode;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public FileMode getMode() {
        return mode;
    }

    public void setMode(FileMode mode) {
        this.mode = mode;
    }
}