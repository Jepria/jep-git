package diffTypes;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.List;

public class MethodDiff{
    public enum DiffType {
        CHANGED,
        TYPE_CHANGED,
        DELETED,
        ADDED
    }
    private List<AnnotationDiff> annotationsDiff;
    private List<ParamDiff>      paramsDiff;
    private MethodDeclaration    method;
    private DiffType   type;

    public MethodDiff(DiffType type, MethodDeclaration method) {
        this.method = method;
        this.type = type;
    }

    public List<AnnotationDiff> getAnnotationsDiff() {
        return annotationsDiff;
    }

    public List<ParamDiff> getParamsDiff() {
        return paramsDiff;
    }

    public void setAnnotationsDiff(List<AnnotationDiff> annotationsDiff) {
        this.annotationsDiff = annotationsDiff;
    }

    public void setParamsDiff(List<ParamDiff> paramsDiff) {
        this.paramsDiff = paramsDiff;
    }

    public MethodDeclaration getMethod() {
        return method;
    }

    public DiffType getType() {
        return type;
    }
}
