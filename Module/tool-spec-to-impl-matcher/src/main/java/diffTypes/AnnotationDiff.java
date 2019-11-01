package diffTypes;

import com.github.javaparser.ast.Node;

/**
 * Contains diff between annotations
 */
public class AnnotationDiff extends MatchDiff{
    public AnnotationDiff(DiffType type, Node node) {
        super(type, node);
    }
}
