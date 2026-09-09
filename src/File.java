import java.util.ArrayList;
import java.util.List;

public class File extends FsNode {

    private StringBuilder content;

    // Disk blok u kojem se nalazi indeks
    private int indexBlock;

    // Blokovi koji pripadaju fajlu
    private List<Integer> dataBlocks;

    public File(String name, Directory parent) {
        super(name, parent);

        this.content = new StringBuilder();
        this.indexBlock = -1;
        this.dataBlocks = new ArrayList<>();
    }

    public String read() {
        return content.toString();
    }

    public void write(String data) {
        content.setLength(0);
        content.append(data);
    }

    public void append(String data) {
        content.append(data);
    }

    public int getIndexBlock() {
        return indexBlock;
    }

    public void setIndexBlock(int indexBlock) {
        this.indexBlock = indexBlock;
    }

    public List<Integer> getDataBlocks() {
        return dataBlocks;
    }

    public void addDataBlock(int block) {
        dataBlocks.add(block);
    }

    public void clearDataBlocks() {
        dataBlocks.clear();
    }
}