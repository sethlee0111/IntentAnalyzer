package com.sethlee0111.basicintentactivity.visitor;

import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;

public class AnnotationDeclarationVisitor extends VoidVisitorAdapter<Void> {
    ArrayList<String> aList = new ArrayList<>();

    @Override
    public void visit(AnnotationDeclaration md, Void arg) {
        super.visit(md, arg);
        aList.add(md.getNameAsString());
    }

    public ArrayList<String> getaList() {
        return aList;
    }
}