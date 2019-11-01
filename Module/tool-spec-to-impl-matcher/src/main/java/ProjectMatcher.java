import diffTypes.MethodDiff;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * 1-м аргументом передавать измененный руками java файл
 * 2-й аргумент - выход из генератора
 */

public class ProjectMatcher {
    public static void main(String[] args) throws IOException {

        String oldFileName = "C:\\temp\\showcase\\src\\gen\\java\\io\\swagger\\api\\FeatureApi1.java";
        String newFileName = "C:\\temp\\showcase\\src\\gen\\java\\io\\swagger\\api\\FeatureApi.java";

        if (args.length == 2) {
            oldFileName = args[0];
            newFileName = args[1];
        }

        FileInputStream oldStream = new FileInputStream(oldFileName);
        FileInputStream newStream = new FileInputStream(newFileName);
        System.out.println("Start matching...");
        YamlJavaMatcher     matcher  = new YamlJavaMatcher();
        List<MethodDiff>    diffList = matcher.matchFiles(oldStream, newStream);
        ParseMatchingResult parser   = new ParseMatchingResult(System.out);
        parser.parse(diffList);
        oldStream.close();
        newStream.close();
        System.out.println("Matching finished.");
        FileWriter fileWriter = new FileWriter("D:\\temp.txt"); // TODO
        fileWriter.write("asdasd");
        fileWriter.close();
    }
}
