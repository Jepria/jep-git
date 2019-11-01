import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;

import java.util.ArrayList;
import java.util.List;

// методы считаются одинаковыми, если у них одинаковое значение аннотации @Path и
// у них одинаковая аннотация типа http запроса (например @GET и @Path("/path") - есть у обоих)

public class JerseyMethodDeclaration {

    private JerseyMethodDeclaration(MethodDeclaration methodDeclaration, String endpoint, String type) {
        this.methodDeclaration = methodDeclaration;
        this.endpoint = endpoint;
        this.type = type;
    }

    MethodDeclaration methodDeclaration;

    private static ArrayList<String> TYPES;
    private        String            endpoint;
    private        String            type;

    static {
        TYPES = new ArrayList<>();
        TYPES.add("GET");
        TYPES.add("POST");
        TYPES.add("PUT");
        TYPES.add("DELETE");
    }

    static List<JerseyMethodDeclaration> findJerseyMethods(List<MethodDeclaration> methods) {
        List<JerseyMethodDeclaration> result = new ArrayList<>();

        for (MethodDeclaration method : methods) {
            String type     = null;
            String endpoint = null;
            for (AnnotationExpr annotationExpr : method.getAnnotations()) {
                if (TYPES.contains(annotationExpr.getNameAsString())) {
                    type = annotationExpr.getNameAsString();
                } else if ("Path".equals(annotationExpr.getNameAsString())) {
                    endpoint = annotationExpr.toString();
                }
            }
            if (null != type) {
                result.add(new JerseyMethodDeclaration(method, endpoint, type));
            }
        }

        return result;
    }

    boolean isSameMethod(JerseyMethodDeclaration method) {
        boolean result = false;
        if (type.equals(method.type)) {
            if (null != endpoint) {
                result = endpoint.equals(method.endpoint);
            } else {
                result = (null == method.endpoint);
            }
        }
        return result;
    }

}
