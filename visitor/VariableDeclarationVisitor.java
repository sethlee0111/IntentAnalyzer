package com.sethlee0111.basicintentactivity.visitor;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.sethlee0111.basicintentactivity.symboltable.Scope;
import com.sethlee0111.basicintentactivity.symboltable.SymbolTable;
import com.sethlee0111.basicintentactivity.symboltable.VariableInfo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class VariableDeclarationVisitor extends VoidVisitorAdapter<Void> {
    private SymbolTable symbolTable = new SymbolTable();    // HashMap<Integer, HashSet<VariableInfo>>
    private Scope scope;

    public VariableDeclarationVisitor(Scope scope) {
        this.scope = scope;
        symbolTable.put(scope, new HashSet<VariableInfo>());
    }

    /**
     * checks if a String starts with '@'
     * @param an
     * @return
     */
    private boolean isAnnotation(String an) {
        if(an.charAt(0) == '@')
            return true;
        return false;
    }

//    @Override
//    public void visit(MarkerAnnotationExpr md, Void arg) {  // @TODO get general annotation instead of marker annotation
//        super.visit(md, arg);
//        List<Node> cn = md.getChildNodes();
//        for(Node node : cn) {
//            System.out.println("Child of Anot: " + node);
//        }
//        System.out.println("--> Annotation: " + md);
//    }

    @Override
    public void visit(VariableDeclarationExpr md, Void arg) {  // @TODO get general annotation instead of marker annotation
        super.visit(md, arg);
        if(!symbolTable.containsKey(scope)) {
            symbolTable.put(scope, new HashSet<VariableInfo>());
        }
        VariableInfo variableInfo = new VariableInfo();
        NodeList<AnnotationExpr> annotationList= md.getAnnotations();
        if(annotationList.isNonEmpty()) {   // only consider variables declared with security type systems
            for (AnnotationExpr annotationExpr : annotationList) {
                variableInfo.addAnnotation(annotationExpr.getName().toString());
//                System.out.println("Ann added: " + annotationExpr.getName().toString());
            }
            NodeList<VariableDeclarator> variableList = md.getVariables();
            for (VariableDeclarator variableDeclarator : variableList) {
                variableInfo.setName(variableDeclarator.getName().toString());
                variableInfo.setScope(scope);
                variableInfo.setType(variableDeclarator.getType().toString());
//                System.out.println("vars: " + variableDeclarator);
            }
            symbolTable.get(scope).add(variableInfo);
        }
    }

    public SymbolTable getSymbolTable() {
        Iterator<Map.Entry<Scope, HashSet<VariableInfo>>> itr = symbolTable.entrySet().iterator();
        while(itr.hasNext()) {
            Map.Entry<Scope, HashSet<VariableInfo>> e = itr.next();
            Scope key = e.getKey();
            HashSet<VariableInfo> set = e.getValue();
            if(set.isEmpty()) {
                itr.remove();
            }
        }
        return symbolTable;
    }
}