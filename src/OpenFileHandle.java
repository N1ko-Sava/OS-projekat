

public class OpenFileHandle {



    private File file;
    private int position;
    private FileMode mode;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public FileMode getMode() {
        return mode;
    }

    public void setMode(FileMode mode) {
        this.mode = mode;
    }
}
