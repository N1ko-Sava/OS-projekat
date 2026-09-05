public class RAM {
    private int size;
    private int[] cells;

    public RAM(int size) {
        this.size = size;
        this.cells = new int[size];
    }

    public int read(int address) {
        if (address >= 0 && address < size) {
            return cells[address];
        }
        throw new IndexOutOfBoundsException("Adresa van opsega RAM-a: " + address);
    }

    public void write(int address, int value) {
        if (address >= 0 && address < size) {
            cells[address] = value;
        } else {
            throw new IndexOutOfBoundsException("Adresa van opsega RAM-a: " + address);
        }
    }

    public int getSize() {
        return size;
    }

    public int[] getCells() {
        return cells;
    }
}