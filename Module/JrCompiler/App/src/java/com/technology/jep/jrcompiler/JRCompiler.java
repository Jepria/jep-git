package com.technology.jep.jrcompiler;

import java.io.File;
import java.io.FilenameFilter;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;

/**
 * Компилятор отчётов <I>JasperReports</I>.
 * <p>
 * Предназначен для преобразования исходного файла определения отчёта в формате XML (как правило, имеющего расширение <code>*.jrxml</code>)
 * в результирующий файл, содержащий сериализованный вид объектного представления. Результирующий файл имеет расширение <code>*.jasper</code>,
 * размещается в той же директории, что исходный файл, и имеет такое же имя.
 * <p>
 * <p>
 * <u>Правила вызова:</u><br>
 * <p>
 * <I><code>java -cp &lt;ScriptletAndDataSourceClasses&gt;;jrcompiler.jar com.technology.jep.jrcompiler.JRCompiler Report.jrxml</code></I>
 * <I><code>java -cp &lt;ScriptletAndDataSourceClasses&gt;;jrcompiler.jar com.technology.jep.jrcompiler.JRCompiler report_directory</code></I>
 * <p>
 * <p>
 * <u>Примеры использования:</u>
 * <p>
 * Компиляция отчёта модуля <I>&lt;ModuleName&gt;</I> из <I>make.bat</I><br>
 * <I><code>
 * ...<br>
 * %JAVA_HOME%\bin\java -cp %CLASSES%;%JEP_REPORT_COMPILER%\lib\jrcompiler.jar com.technology.jep.jrcompiler.JRCompiler %RESOURCE_HOME%\%MODULE_PACKAGE%\report\%MODULE_NAME%Report.jrxml<br>
 * ...<br>
 * </code></I>
 * <I><code>
 * ...<br>
 * %JAVA_HOME%\bin\java -cp %CLASSES%;%JEP_REPORT_COMPILER%\lib\jrcompiler.jar com.technology.jep.jrcompiler.JRCompiler %RESOURCE_HOME%\%MODULE_PACKAGE%\report<br>
 * ...<br>
 * </code></I>
 */
public class JRCompiler {

	/**
	 * @param args параметры запуска
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			showUsageError("Wrong parameter number.");
		}

		try {
			compile(new File(args[0]));
		} catch (JRException e) {
			showError(e.getMessage());
		}
	}

	private static void compile(File srcFile) throws JRException {
		if (srcFile.isFile()) {
			// Передали 1 файл
			String srcFileName = srcFile.getAbsolutePath();
			// название берем не из отчета, а такое же как исходное имя шаблона
			String destFileName = srcFileName.substring(0, srcFileName.lastIndexOf(".") + 1) + "jasper";
			try {
				JasperCompileManager.compileReportToFile(srcFileName, destFileName);
			} catch (JRException e) {
				showError(e.getMessage());
			}
		} else { // передали директорию
			// Создание списка файлов, состоящих из исходников текущей директории и вложенных директорий
			File[] files = srcFile.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					String path = dir + "/" + name;
					return new File(path).isDirectory() || name.endsWith(".jrxml");
				}
			});

			if (files != null) {
				for (File file : files) {
					compile(file);
				}
			}
		}
	}

	private static void showError(String message) {
		System.out.println("ERROR: " + message);
	}

	private static void showUsageError(String message) {
		System.out.println(message);
		System.out.println("Usage:");
		System.out.println("java -jar jrcompiler <sourceFilePath>");
		System.out.println("where:");
		System.out.println("sourceFilePath:\t\tFull path of the file containing a report definition");

		System.exit(-1);
	}

}
