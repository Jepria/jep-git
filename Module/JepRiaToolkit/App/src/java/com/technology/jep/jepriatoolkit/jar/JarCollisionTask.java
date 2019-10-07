package com.technology.jep.jepriatoolkit.jar;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.PropertyHelper;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Echo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Checks the two classpaths for collisions (finds intersection between the two file sets)
 */
public class JarCollisionTask extends Task  {

  /**
   * Task input parameter, semicolon-separated classpath roots (relative to the ant project root)
   */
  private String classPathRoots;

  public void setClassPathRoots(String classPathRoots) {
    this.classPathRoots = classPathRoots;
  }

  /**
   * Task input parameter, name of the property to set the output value to (optional).
   * The output property is a boolean: whether or not the classpaths have collisions.
   */
  private String property = null;

  public void setProperty(String property) {
    this.property = property;
  }

  /**
   * Task input parameter, semicolon-separated classpath references (names or any IDs) matching {@link #classPathRoots}
   */
  private String classPathRefs;

  public void setClassPathRefs(String classPathRefs) {
    this.classPathRefs = classPathRefs;
  }

  protected PropertyHelper ph;

  @Override
  public void init() throws BuildException {
    super.init();
    ph = PropertyHelper.getPropertyHelper(getProject());
  }

  protected Set<Path> walkClassPathRoot(Path classPathRoot) {
    try {
      return Files.walk(classPathRoot)
              .filter(path -> Files.isRegularFile(path) && path.getFileName().toString().endsWith(".class"))
              .map(path -> path.subpath(1, path.getNameCount())) // crop the path root (0-th element)
              .collect(Collectors.toSet());
    } catch (IOException e) {
      throw new BuildException("Exception traversing class path root [" + classPathRoot + "]", e);
    }
  }

  @Override
  public void execute() throws BuildException {

    if (classPathRoots == null) {
      throw new BuildException("classPathRoots attribute is mandatory");
    }

    List<String> classPathRootsSplit = Arrays.asList(classPathRoots.split("\\s*;\\s*"));
    List<String> classPathRefsSplit = Arrays.asList(classPathRefs.split("\\s*;\\s*"));

    final Map<Path, Set<Integer>> classpaths = new HashMap<>();

    if (classPathRootsSplit.size() > 1) {
      for (int index = 0; index < classPathRootsSplit.size(); index++) {
        String classPathRoot = classPathRootsSplit.get(index);

        final Path classPathRootPath = Paths.get(classPathRoot); // ant works with paths relative to the ant project root as if they are absolute paths

        if (!Files.isDirectory(classPathRootPath)) {
          throw new BuildException("classPathRoot must be a directory: " + classPathRoot);
        }

        // classes from a single classpath
        Set<Path> classpath = walkClassPathRoot(classPathRootPath);

        for (Path clazz: classpath) {
          Set<Integer> s = classpaths.get(clazz);
          if (s == null) {
            s = new HashSet<>();
            classpaths.put(clazz, s);
          }
          s.add(index);
        }
      }


      boolean hasCollisions = false;

      // log collisions only
      Iterator<Map.Entry<Path, Set<Integer>>> eit = classpaths.entrySet().iterator();
      while (eit.hasNext()) {
        Map.Entry<Path, Set<Integer>> e = eit.next();
        if (e.getValue().size() > 1) {

          hasCollisions = true;

          { // log collisions
            StringBuilder sb = new StringBuilder();

            sb.append("The classpaths");
            for (Integer i : e.getValue()) {
              if (classPathRefsSplit.size() > i) {
                sb.append("\n> ").append(classPathRefsSplit.get(i));
              } else {
                sb.append("\n> #").append(i);
              }
            }
            sb.append("\ncontain collision: ").append(pathToClassname(e.getKey()));
            String msg = sb.toString();


            Echo echo = new Echo();
            echo.setMessage(msg);
            echo.execute();
          }

        }
      }


      if (!hasCollisions) {
        Echo echo = new Echo();
        echo.setMessage("The classpaths do not have collisions");
        echo.execute();
      }

      // set output
      ph.setNewProperty(property, hasCollisions);

    }
  }

  protected String pathToClassname(Path path) {
    String classname = path.toString();
    classname = classname.substring(0, classname.lastIndexOf('.'));
    classname = classname.replaceAll(Pattern.quote(File.separator), ".");
    return classname;
  }
}
