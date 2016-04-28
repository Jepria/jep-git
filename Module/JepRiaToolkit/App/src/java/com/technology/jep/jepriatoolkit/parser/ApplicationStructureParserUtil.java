package com.technology.jep.jepriatoolkit.parser;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.ERROR_PREFIX;
import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.UTF_8;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.echoMessage;
import static com.technology.jep.jepriatoolkit.util.JepRiaToolkitUtil.multipleConcat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.technology.jep.jepriatoolkit.creator.module.ModuleDeclaration;

public class ApplicationStructureParserUtil {

	/**
	 * Получение ссылки на модуль компиляции для указанного пути к файлу
	 * 
	 * @param fileNameOrPath	путь до файла	
	 * @return модуль компиляции
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 */
	public static CompilationUnit getCompilationUnit(String fileNameOrPath) {
        try {
            return JavaParser.parse(new InputStreamReader(new FileInputStream(fileNameOrPath), UTF_8), true);
        } catch (ParseException e) {
            echoMessage(multipleConcat(ERROR_PREFIX, "Check the file '", fileNameOrPath, "'! It contains compilation errors!"));
        } catch (FileNotFoundException e) {
            echoMessage(multipleConcat(ERROR_PREFIX, "File '", fileNameOrPath, "' is not found!"));
        } catch (UnsupportedEncodingException e) {
            echoMessage(multipleConcat(ERROR_PREFIX, e.getLocalizedMessage()));
        } 
        return null;
    }
	
	/**
	 * Получение списка методов и их тел для указанного по входному параметру пути модуля компиляци
	 * 
	 * @param complitionUnitPath		путь до файла модуля компиляции
	 * @return список методов модуля компиляции
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ModuleDeclaration getModuleDeclaration(String complitionUnitPath){
		if (complitionUnitPath == null) return null;
		CompilationUnit compilationUnit = getCompilationUnit(complitionUnitPath);
		if (compilationUnit == null) return null;
		
		final ModuleDeclaration module = new ModuleDeclaration();
		compilationUnit.accept(new VoidVisitorAdapter() {
			@Override
		    public void visit(MethodDeclaration m, Object arg) {
				if (m.getParentNode().getClass().equals(ClassOrInterfaceDeclaration.class)){
					module.getMethods().add(m);
				}
				super.visit(m, arg);
			}
			@Override
		    public void visit(FieldDeclaration f, Object arg) {
				if (f.getParentNode().getClass().equals(ClassOrInterfaceDeclaration.class)){
					module.getFields().add(f);
				}
				super.visit(f, arg);
			}
			@Override
		    public void visit(ConstructorDeclaration f, Object arg) {
				if (f.getParentNode().getClass().equals(ClassOrInterfaceDeclaration.class)){
					module.getConstructors().add(f);
				}
				super.visit(f, arg);
			}
		}, null);
		return module;
	}
}
