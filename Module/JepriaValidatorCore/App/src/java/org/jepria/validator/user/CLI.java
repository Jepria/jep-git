package org.jepria.validator.user;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;

import org.jepria.validator.core.base.Message;
import org.jepria.validator.core.base.MessageCollector;
import org.jepria.validator.core.base.ValidatorRule;
import org.jepria.validator.core.base.resource.Resource;
import org.jepria.validator.core.fs.FileContextValidator;
import org.jepria.validator.core.fs.FileWalker;

/**
 * Command Line Interface: main class for launching validation from command line
 */
public class CLI implements Runnable {

  private static PrintStream out = System.out;
  
  
  private static class ArgParser implements Enumeration<String> {
    
    private final String[] args;
    private int nextIndex;
    
    public ArgParser(String[] args) {
      this.args = args;
      this.nextIndex = 0;
    }

    @Override
    public boolean hasMoreElements() {
      return args != null && nextIndex < args.length;
    }

    @Override
    public String nextElement() {
      if (hasMoreElements()) {
        return args[nextIndex++];
      } else {
        throw new NoSuchElementException();
      }
    }
    
    public boolean contains(String arg) {
      if (args == null) {
        return false;
      }
      for (String arg2: args) {
        if (arg == null && arg2 == null || arg != null && arg2 != null && arg2.equals(arg)) {
          return true;
        }
      }
      return false;
    }
  }
  
  private boolean transformOpt = false;
  private List<String> ruleClassesOpt = null;
  private String ruleListOpt = null;
  private List<String> directoriesOpt = null;
  private List<String> outputOpt = null;
  private List<String> filterMessagesOpt = null;
  
  /**
   * Выводимый в консоль уровень сообщения-исключения
   * (вызванного методом {@link org.jepria.validator.core.base.MessageHandler#handleThrowable(Throwable)})
   */
  public static final String THROWABLE_LEVEL = "/THROWABLE";
  
  /**
   * Выводимый в консоль уровень сообщения,
   * поступившего без указанного в явном виде уровня
   * (вызванного методом {@link org.jepria.validator.core.base.MessageHandler#handleMessage(Message)})
   */
  public static final String VERBOSE_LEVEL = "/VERBOSE";
  
  
  public static void main(String[] args) {
    
    ArgParser argParser = new ArgParser(args);
    
    
    // проверим запрос помощи
    if (argParser.contains("--help")) {
      showHelpFull();
      return;
    }
    if (argParser.contains("-h")) {
      showHelpBrief();
      return;
    }
    
    
    // Проверим обязательные опции
    boolean containsMandatoryOptions = false;
    if (
        (argParser.contains("-d") || argParser.contains("--directories")) &&
        (argParser.contains("-rc") || argParser.contains("--ruleClasses") || argParser.contains("-rl") || argParser.contains("--ruleList"))
        ) {
      containsMandatoryOptions = true;
    }
    if (!containsMandatoryOptions) {
      out.println("ERROR: The following options are mandatory: --directories, (--ruleClasses or --ruleList)");
      return;
    }

    
    
    
    CLI instance = new CLI();
    
    while (argParser.hasMoreElements()) {
      String arg = argParser.nextElement();
      
      switch (arg) {
      case "-d": case "--directories": {
        if (argParser.hasMoreElements()) {
          String value = argParser.nextElement();
          try {
            instance.directoriesOpt = parseSemicolonSeparated(value);
          } catch (ParseException e) {
            invalidSemicolonSeparated(arg, e.getInvalidValue());
            return;
          }
        } else {
          requiredValue(arg);
          return;
        }
        break;
      }
      case "-t": case "--transform": {
        instance.transformOpt = true;
        break;
      }
      case "-rl": case "--ruleList": {
        if (argParser.hasMoreElements()) {
          arg = argParser.nextElement();
          instance.ruleListOpt = arg;
        } else {
          requiredValue(arg);
          return;
        }
        break;
      }
      case "-rc": case "--ruleClasses": {
        if (argParser.contains("-rl") || argParser.contains("--ruleList")) {
          out.println("WARN: " + arg + " option is superseded with the --ruleList option");
        } else {
          if (argParser.hasMoreElements()) {
            arg = argParser.nextElement();
            try {
              instance.ruleClassesOpt = parseSemicolonSeparated(arg);
            } catch (ParseException e) {
              invalidSemicolonSeparated(arg, e.getInvalidValue());
              return;
            }
          } else {
            requiredValue(arg);
            return;
          }
        }
        break;
      }
      case "-o": case "--output": {
        if (!argParser.contains("-t") && !argParser.contains("--transform")) {
          out.println("WARN: " + arg + " option is useless without --transform option");
        }
        if (argParser.hasMoreElements()) {
          arg = argParser.nextElement();
          try {
            instance.outputOpt = parseSemicolonSeparated(arg);
          } catch (ParseException e) {
            invalidSemicolonSeparated(arg, e.getInvalidValue());
            return;
          }
        } else {
          requiredValue(arg);
          return;
        }
        break;
      }
      case "-fm": case "--filterMessages": {
        if (argParser.hasMoreElements()) {
          arg = argParser.nextElement();
          try {
            instance.filterMessagesOpt = parseSemicolonSeparated(arg);
          } catch (ParseException e) {
            invalidSemicolonSeparated(arg, e.getInvalidValue());
            return;
          }
        } else {
          requiredValue(arg);
          return;
        }
        break;
      }
      default: {
      }
      }
    }
    
    instance.run();
  }
  
  private CLI() {}
    
  @Override
  public void run() {
    
    List<File> dirList = new ArrayList<>();
    
    for (String dirStr: directoriesOpt) {
      File dir = new File(dirStr);
      if (!dir.exists()) {
        out.println("WARN: " + dir + " does not exist. Skipping...");
      } else if (!dir.isDirectory()) {
        out.println("WARN: " + dir + " is not a directory. Skipping...");
      } else {
        dirList.add(dir);
      }
    }
      
    for (int i = 0; i < dirList.size(); i++) {
      File dir = dirList.get(i);
      
      MessageCollector validationMessageCollector = new MessageCollector() {
        @Override
        public void collect(ValidatorRule rule, Resource resource, Throwable e) {
          if (filterMessagesOpt == null || filterMessagesOpt.contains(THROWABLE_LEVEL)) {
            out.println(THROWABLE_LEVEL + ": " + rule.getClass().getSimpleName() 
                + " validating the file " + resource.getPathName() + ": " 
                + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace(out);
          }
        }
        @Override
        public void collect(ValidatorRule rule, Resource resource, Message message) {
          if (message != null) {
            String levelAsString = message.level == null ? VERBOSE_LEVEL : message.level.toString();
            if (filterMessagesOpt == null || filterMessagesOpt.contains(levelAsString)) {
              out.println(
                  levelAsString + ": "
                  + rule.getClass().getSimpleName() 
                  + " validating the file " + resource.getPathName() + ": "
                  + (message.markSpan == null ? "" : ("at " + Integer.toString(message.markSpan.lineBegin) + ":" + Integer.toString(message.markSpan.colBegin + 1) + ": ")) +
                  message.text);
            }
          }
        }
      };
      
      MessageCollector transformMessageCollector = null;
      
      if (transformOpt) {
        transformMessageCollector = new MessageCollector() {
          @Override
          public void collect(ValidatorRule rule, Resource resource, Throwable e) {
            out.println(THROWABLE_LEVEL + ": " + rule.getClass().getSimpleName() 
                + " on transformation for the resource " + resource.getPathName() + ": " 
                + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace(out);
          }
          @Override
          public void collect(ValidatorRule rule, Resource resource, Message message) {
            if (message != null) {
              String levelAsString = message.level == null ? VERBOSE_LEVEL : message.level.toString();
              if (filterMessagesOpt == null || filterMessagesOpt.contains(levelAsString)) {
                out.println(
                    levelAsString + ": " 
                    + rule.getClass().getSimpleName() 
                    + " on transformation for the resource " + resource.getPathName() + ": "
                    + (message.markSpan == null ? "" : ("at " + Integer.toString(message.markSpan.lineBegin) + ":" + Integer.toString(message.markSpan.colBegin + 1) + ": ")) +
                    message.text);
              }
            }
          }
        };
      }
      
      final File rootDir;
      
      final File outputDir;
          
      // если задана опция output directory (соответствующая по счёту валидируемой директории),
      // скопируем исходную директорию в целевую локацию и запустим валидацию на копии
      if (transformOpt && outputOpt != null && !outputOpt.isEmpty() && outputOpt.size() > i) {
        outputDir = new File(outputOpt.get(i));
        
        if (!outputDir.exists()) {
          outputDir.mkdirs();
        }
        
        Path dirPath = dir.toPath();
        
        FileWalker.walk(dir, file -> {
          Path path = file.toPath();
          File fileCopyTo = new File(outputDir, dirPath.relativize(path).toString()); 
          if (file.isDirectory()) {
            fileCopyTo.mkdirs();
          }
          if (file.isFile()) {
            try {
              Files.copy(path, fileCopyTo.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          }
        });
        
        rootDir = outputDir;
      } else {
        outputDir = null;
        
        rootDir = dir;
      }
      
      List<ValidatorRule> rules = null;
      
      try {
        if (ruleListOpt != null) {
          rules = reflectRulesFromList(ruleListOpt);
        } else if (ruleClassesOpt != null) {
          rules = reflectRulesFromClassnames(ruleClassesOpt);
        }
      } catch (ClassNotFoundException | InstantiationException | 
          IllegalAccessException | ClassCastException e) {
        out.println("ERROR: " + e.getClass().getName() + ": " + e.getMessage());
      }
      
      if (rules == null || rules.isEmpty()) {
        out.print("No rules for validation found.");
        return;
      }
      
      FileContextValidator fileValidationContext = new FileContextValidator(rootDir);
      
      // Некоторое логирование перед запуском
      out.print("Validating directory: " + dir.getAbsolutePath());
      if (transformOpt) {
        if (outputDir != null) {
          out.print(" with transforming to directory: " + outputDir.getAbsolutePath() + " ...");
        } else {
          out.print(" with transforming to itself...");
        }
      } else {
        out.print(" with no transforming...");
      }
      out.println();
      
      fileValidationContext.validate(rules, null, validationMessageCollector, transformOpt, transformMessageCollector);
      
    }
    
  }    
  
  private static class ParseException extends Exception {
    private static final long serialVersionUID = 3345298173160009998L;
    
    private final String invalidValue;

    public ParseException(String invalidValue) {
      this.invalidValue = invalidValue;
    }

    public String getInvalidValue() {
      return invalidValue;
    }
    
    
  }
  
  private static List<String> parseSemicolonSeparated(String value) throws ParseException {
    if (!value.matches("[^;]+(;[^;]+)*")) {
      throw new ParseException(value);
    } else {
      return Arrays.asList(value.split(";")); 
    }
  }
  
  private static void invalidSemicolonSeparated(String option, String value) {
    out.println("ERROR: Value [" + value + "] is invalid for option " + option + ": expected semicolon-separated list");
  }
  
  private static void requiredValue(String option) {
    out.println("ERROR: Option " + option + " requires a value");
  }
  
  private static void showHelpBrief() {
    out.println("usage: jeval");
    out.println(" -d,--directories <arg>   semicolon-separated list of directories (projects) for validation");
    out.println(" -t,--transform           whether to transform files after validation or to validate only");
    out.println(" -o,--output <arg>        semicolon-separated list of output directories (used with -t option only),");
    out.println("                          corresponds to the list of directories (-d option)");
    out.println(" -rl,--ruleList <arg>     rule List classname");
    out.println(" -rc,--ruleClasses <arg>  semicolon-separated list of rule classnames, in necessary order.");
    out.println("                          When used together with -rl option, has no effect");
    out.println(" -fm,--filterMessages <level>;<level>;... output message level filtering");
    
    out.println(" -h                       print brief help (this screen)");
    out.println(" --help                   print full help");
  }
  
  private static void showHelpFull() {
    out.println("usage: jeval");
    out.println(" -d,--directories <arg>   semicolon-separated list of directories (projects) for validation.");
    out.println("                          Each directory will be validated independently");
    out.println(" -t,--transform           whether to transform files after validation or to validate only");
    out.println(" -o,--output <arg>        semicolon-separated list of output directories (used with -t option only),");
    out.println("                          corresponds to the list of directories (-d option).");
    out.println("                          If the corresponding output directory is absent (or the option is missing at all,");
    out.println("                          the original directory contents are modified.");
    out.println("                          For example, the command with options:");
    out.println("                          ` --directories dir1;dir2;dir3 --output dir4;dir5 --transform `");
    out.println("                          will validate and transform dir1 into dir4, dir2 into dir5, dir3 into itself,");
    out.println("                          leaving dir1 and dir2 unchanged, but having dir3 modified.");
    out.println(" -rl,--ruleList <arg>     rule List classname.");
    out.println("                          A single classname that denotes a class implementing " + List.class.getCanonicalName());
    out.println("                          and whose instance contains rules of type");
    out.println("                          " + ValidatorRule.class.getCanonicalName() + ", in necessary order");
    out.println(" -rc,--ruleClasses <arg>  semicolon-separated list of rule classnames, in necessary order.");
    out.println("                          When used together with --ruleList option, has no effect.");
    out.println("                          Each classname must denote a class that extends");
    out.println("                          " + ValidatorRule.class.getCanonicalName());
    out.println(" -fm,--filterMessages <level>;<level>;... output message level filtering.");
    out.println("                          Prints the messages of listed levels only, the others are swallowed.");
    out.println("                          The value '" + THROWABLE_LEVEL + "' as a <level> refers to throwable messages.");
    out.println("                          The value '" + VERBOSE_LEVEL + "' as a <level> refers to regular messages,");
    out.println("                          which come without any explicit level.");
    out.println("                          If the option is missing, messages of all levels are printed.");

    out.println(" -h                       print brief help");
    out.println(" --help                   print full help (this screen)");
  }
  
  private static List<ValidatorRule> reflectRulesFromClassnames(List<String> ruleClassnames) 
      throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    
    List<ValidatorRule> rules = new ArrayList<>();
    
    if (ruleClassnames != null) {
      for (String ruleClassname: ruleClassnames) {
        ValidatorRule rule = (ValidatorRule)Class.forName(ruleClassname).newInstance();
        rules.add(rule);
      }
    }
    
    return rules;
  }
  
  @SuppressWarnings("unchecked")// допустимо
  private static List<ValidatorRule> reflectRulesFromList(String classname) 
      throws InstantiationException, IllegalAccessException, ClassNotFoundException {
    return (List<ValidatorRule>)Class.forName(classname).newInstance();
  }
}
