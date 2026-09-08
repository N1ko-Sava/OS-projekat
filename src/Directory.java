import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Directory extends FsNode {

    private Map<String, FsNode> children;

    public Directory(String name, Directory parent) {
        super(name, parent);
        children = new LinkedHashMap<>();
    }

    public void addChild(FsNode node) {

        if (node == null) {
            return;
        }

        children.put(node.getName(), node);
        node.setParent(this);
    }

    public FsNode getChild(String name) {
        return children.get(name);
    }

    public List<FsNode> list() {
        return new ArrayList<>(children.values());
    }

    public void removeChild(String name) {
        children.remove(name);
    }
}