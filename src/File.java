public class File {

    private StringBuilder content;

    public String read(){
        return content.toString();
        //PROVJERI
    }

    public void write(String data){

        content.append(data);
        //PROVJERI
    }

    public void append(String data){

        content.append(data);
        //PROVJERI
    }

    public File(StringBuilder content) {
        this.content = content;
    }
}
