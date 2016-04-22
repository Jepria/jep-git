package com.technology.jep.jepriatoolkit.creator.module;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

public class ModuleDeclaration {

	private List<FieldDeclaration> fields = new ArrayList<FieldDeclaration>();
	private List<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
	private List<ConstructorDeclaration> constructors = new ArrayList<ConstructorDeclaration>();
	
	public List<FieldDeclaration> getFields() {
		return fields;
	}
	
	public List<MethodDeclaration> getMethods() {
		return methods;
	}
	
	public List<ConstructorDeclaration> getConstructors() {
		return constructors;
	}
}
