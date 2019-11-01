import diffTypes.AnnotationDiff;
import diffTypes.MatchDiff;
import diffTypes.MethodDiff;
import diffTypes.ParamDiff;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

import static diffTypes.MatchDiff.DiffType.TYPE_CHANGED;
import static diffTypes.MethodDiff.DiffType.ADDED;
import static diffTypes.MethodDiff.DiffType.CHANGED;
import static diffTypes.MethodDiff.DiffType.DELETED;

public class ParseMatchingResult {
    BufferedWriter outWriter;

    private enum SeverityLevel {
        INFO,
        WARNING,
        ERROR
    }

    static SeverityLevel methodAddedSeverityLevel       = SeverityLevel.ERROR;
    static SeverityLevel methodDeletedSeverityLevel     = SeverityLevel.ERROR;
    static SeverityLevel methodChangedSeverityLevel     = SeverityLevel.ERROR;
    static SeverityLevel paramsChangedSeverityLevel     = SeverityLevel.ERROR;
    static SeverityLevel paramsAddedSeverityLevel       = SeverityLevel.ERROR;
    static SeverityLevel paramsDeletedSeverityLevel     = SeverityLevel.ERROR;
    static SeverityLevel annotationAddedSeverityLevel   = SeverityLevel.ERROR;
    static SeverityLevel annotationDeletedSeverityLevel = SeverityLevel.ERROR;
    static SeverityLevel annotationChangedSeverityLevel = SeverityLevel.ERROR;

    private static SeverityLevel currentSeverityLevel = SeverityLevel.ERROR;

    static {
    }

    public ParseMatchingResult(OutputStream outStream) {
        this.outWriter = new BufferedWriter(new OutputStreamWriter(outStream));
    }

    void parse(List<MethodDiff> matchingResult) throws IOException {
        outWriter.write("Parsing results: ");
        outWriter.newLine();
        for (MethodDiff methodDiff : matchingResult) {
            if (DELETED.equals(methodDiff.getType()) && !isHide(methodDeletedSeverityLevel)) {
                outWriter.write(methodDiff.getMethod().getParentNode().get().getBegin().get() + ": ");
                outWriter.write(methodDeletedSeverityLevel.toString() + ": Method diff " + methodDiff.getMethod().getNameAsString() + " " + "method deleted");
                outWriter.newLine();
                outWriter.newLine();
            }
            if (ADDED.equals(methodDiff.getType()) && !isHide(methodAddedSeverityLevel)) {
                outWriter.write(methodDiff.getMethod().getParentNode().get().getBegin().get() + ": ");
                outWriter.write(methodAddedSeverityLevel.toString() + ": Need implement new method " + methodDiff.getMethod().getNameAsString() + ".");
                outWriter.newLine();
                outWriter.newLine();
            }
            if (CHANGED.equals(methodDiff.getType()) && !isHide(methodChangedSeverityLevel)) {
                outWriter.write(methodDiff.getMethod().getParentNode().get().getBegin().get() + ": ");
                outWriter.write(methodChangedSeverityLevel.toString() + ": method changed " + methodDiff.getMethod().getDeclarationAsString());
                outWriter.newLine();
                if (null != methodDiff.getAnnotationsDiff() && !methodDiff.getAnnotationsDiff().isEmpty()) {
                    for (AnnotationDiff annotationDiff : methodDiff.getAnnotationsDiff()) {
                        if (MatchDiff.DiffType.ADDED.equals(annotationDiff.getType()) && !isHide(annotationDeletedSeverityLevel)) {
                            outWriter.write(annotationAddedSeverityLevel.toString() + ": need add new annotation " + annotationDiff.getNode().toString());
                            outWriter.newLine();
                        }
                        if (MatchDiff.DiffType.DELETED.equals(annotationDiff.getType()) && !isHide(annotationAddedSeverityLevel)) {
                            outWriter.write(annotationAddedSeverityLevel.toString() + ": found extra annotation " + annotationDiff.getNode().toString());
                            outWriter.newLine();
                        }
                        if (MatchDiff.DiffType.CHANGED.equals(annotationDiff.getType()) && !isHide(annotationChangedSeverityLevel)) {
                            outWriter.write(annotationChangedSeverityLevel.toString() + ": maybe wrong annotation " + annotationDiff.getNode().toString());
                            outWriter.newLine();
                        }
                    }
                }
                if (null != methodDiff.getParamsDiff() && !methodDiff.getParamsDiff().isEmpty()) {
                    for (ParamDiff paramDiff : methodDiff.getParamsDiff()) {
                        if (MatchDiff.DiffType.ADDED.equals(paramDiff.getType()) && !isHide(paramsAddedSeverityLevel)) {
                            outWriter.write(paramsAddedSeverityLevel.toString() + ": need add new parameter in method " + paramDiff.getNode().toString());
                            outWriter.newLine();
                        }
                        if (MatchDiff.DiffType.DELETED.equals(paramDiff.getType()) && !isHide(paramsDeletedSeverityLevel)) {
                            outWriter.write(paramsDeletedSeverityLevel.toString() + ": found extra parameter " + paramDiff.getNode().toString());
                            outWriter.newLine();
                        }
                        if (TYPE_CHANGED.equals(paramDiff.getType()) && !isHide(paramsChangedSeverityLevel)) {
                            outWriter.write(paramsChangedSeverityLevel.toString() + ": parameter type changed " + paramDiff.getNode().toString());
                            outWriter.newLine();
                        }
//                        if (DELETED.equals(paramDiff.getType()) && currentSeverityLevel.equals(annotationAdded)) {
//                            outWriter.write(annotationAdded.toString() + ": Method diff " + paramDiff.getNode().toString() + " " + "parameter deleted");
//                            outWriter.newLine();
//                        }
                    }
                }
                outWriter.newLine();
            }
        }
        outWriter.flush();
    }

    void printResult(MethodDiff diff) {

    }

    void printResult() {

    }

    boolean isHide(SeverityLevel level) {
        return (level.ordinal() < currentSeverityLevel.ordinal());
    }

    public void setCurrentSeverityLevel(SeverityLevel currentSeverityLevel) {
        this.currentSeverityLevel = currentSeverityLevel;
    }
}
