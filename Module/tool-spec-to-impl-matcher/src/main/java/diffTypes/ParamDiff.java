package diffTypes;

import com.github.javaparser.ast.Node;

import java.util.List;

/**
 * Contains diff between params in functions. Also parameter can have annotation.
 */
public class ParamDiff extends MatchDiff{
    private List<AnnotationDiff> annotationsDiff;

    public ParamDiff(Node node) {
        super(node);
    }

    public ParamDiff(DiffType type, Node node) {
        super(type, node);
    }

    public List<AnnotationDiff> getAnnotationsDiff() {
        return annotationsDiff;
    }

    public void setAnnotationsDiff(List<AnnotationDiff> annotationsDiff) {
        this.annotationsDiff = annotationsDiff;
    }
}
