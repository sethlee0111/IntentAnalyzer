package com.sethlee0111.basicintentactivity.visitor;

import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.sethlee0111.basicintentactivity.symboltable.Scope;

public class BlockGetIntentVisitor extends VoidVisitorAdapter<Void> {
    private Scope scope;

    public BlockGetIntentVisitor(Scope scope) {
        this.scope = scope;
    }

    @Override
    public void visit(BlockStmt md, Void arg) {
        Scope curScope = new Scope(scope.getFile(), md);
        GetIntentVisitor intentVisitor = new GetIntentVisitor(curScope);
        intentVisitor.visit(md, arg);
    }
}
