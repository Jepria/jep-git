package diffTypes;

import com.github.javaparser.ast.Node;

public class MatchDiff {
    public enum DiffType {
        CHANGED,
        TYPE_CHANGED,
        DELETED,
        ADDED
    }

    private DiffType type;
    private Node node;

    MatchDiff(Node node) {
        this.node = node;
    }

    MatchDiff(DiffType type, Node node) {
        this.type = type;
        this.node = node;
    }

    public DiffType getType() {
        return type;
    }

    public Node getNode() {
        return node;
    }

    public void setType(DiffType type) {
        this.type = type;
    }
}
