package com.sethlee0111.basicintentactivity.visitor;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.sethlee0111.basicintentactivity.symboltable.GlobalSymbolTable;
import com.sethlee0111.basicintentactivity.symboltable.Scope;
import com.sethlee0111.basicintentactivity.symboltable.SymbolTable;
import com.sethlee0111.basicintentactivity.symboltable.VariableInfo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class MatchVariableVisitor extends VoidVisitorAdapter<Void> {
    private Scope scope;
    private VariableInfo foundInfo;
    private boolean found = false;

    public MatchVariableVisitor(Scope scope) {
        this.scope = scope;
    }

    @Override
    public void visit(VariableDeclarationExpr md, Void arg) {  // @TODO get general annotation instead of marker annotation
        super.visit(md, arg);
        NodeList<VariableDeclarator> variableList = md.getVariables();

        for (VariableDeclarator variableDeclarator : variableList) {
            VariableInfo varInfo = new VariableInfo(variableDeclarator.getName().toString(), scope);
            for(Map.Entry<Scope, HashSet<VariableInfo>> entry : GlobalSymbolTable.symbolTable.entrySet()) {
                for(VariableInfo var : entry.getValue()) {
                    if(var.equals(varInfo)) {
                        foundInfo = var;
                        found = true;
                    }
                }
            }
        }
    }

    public boolean isFound() {
        return found;
    }

    public VariableInfo getFoundInfo() {
        return foundInfo;
    }
}