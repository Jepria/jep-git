package com.technology.jep.jepriatoolkit.parser;

import static com.technology.jep.jepriatoolkit.JepRiaToolkitConstant.UTF_8;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ApplicationStructureParserUtil {


	/**
	 * Получение ссылки на модуль компиляции для указанного пути к файлу
	 * 
	 * @param fileNameOrPath	путь до файла	
	 * @return модуль компиляции
	 */
	public static CompilationUnit getCompilationUnit(String fileNameOrPath) {
        try {
            return JavaParser.parse(new InputStreamReader(new FileInputStream(fileNameOrPath), UTF_8), true);
        } catch (Exception e) {
            e.printStackTrace();
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
	public static List<MethodDeclaration> getMethods(String complitionUnitPath){
		CompilationUnit compilationUnit = getCompilationUnit(complitionUnitPath);
		final List<MethodDeclaration> methodBodies = new ArrayList<MethodDeclaration>();
		compilationUnit.accept(new VoidVisitorAdapter() {
			@Override
		    public void visit(MethodDeclaration m, Object arg) {
				if (m.getParentNode().getClass().equals(ClassOrInterfaceDeclaration.class)){
					methodBodies.add(m);
				}
				super.visit(m, arg);
			}
		}, null);
		return methodBodies;
	}
}
