package com.technology.jep.jrcompiler;

import java.io.File;
import java.io.FilenameFilter;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;

/**
 * ���������� ������� <I>JasperReports</I>.
 * <p>
 * ������������ ��� �������������� ��������� ����� ����������� ������ � ������� XML (��� �������, �������� ���������� <code>*.jrxml</code>)
 * � �������������� ����, ���������� ��������������� ��� ���������� �������������. �������������� ���� ����� ���������� <code>*.jasper</code>,
 * ����������� � ��� �� ����������, ��� �������� ����, � ����� ����� �� ���.
 * 
 * <p>
 * <u>������� ������:</u><br>
 * <p>
 * <I><code>java -cp &lt;ScriptletAndDataSourceClasses&gt;;jrcompiler.jar com.technology.jep.jrcompiler.JRCompiler Report.jrxml</code></I>
 * <I><code>java -cp &lt;ScriptletAndDataSourceClasses&gt;;jrcompiler.jar com.technology.jep.jrcompiler.JRCompiler report_directory</code></I>
 * 
 * <p>
 * <u>������� �������������:</u>
 * <p>
 * ���������� ������ ������ <I>&lt;ModuleName&gt;</I> �� <I>make.bat</I><br>
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
 * 
 */
public class JRCompiler {

	/**
	 * @param args ��������� �������
	 */
	public static void main(String[] args) {
		if(args.length < 1) {
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
			// �������� 1 ����
			String srcFileName = srcFile.getAbsolutePath();
			try {
				JasperCompileManager.compileReportToFile(srcFileName);
			} catch (JRException e) {
				showError(e.getMessage());
			}
		} else { // �������� ����������
			// �������� ������ ������, ��������� �� ���������� ������� ���������� � ��������� ����������
			File[] files = srcFile.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					String path = dir + "/" + name;
					return new File(path).isDirectory() || name.endsWith(".jrxml") ;
				}
			});
			
			if(files != null) {
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
