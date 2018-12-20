package com.sethlee0111.basicintentactivity.visitor;

import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.sethlee0111.basicintentactivity.symboltable.Scope;

public class BlockGetExtraVisitor extends VoidVisitorAdapter<Void> {
    private Scope scope;

    public BlockGetExtraVisitor(Scope scope) {
        this.scope = scope;
    }

    @Override
    public void visit(BlockStmt md, Void arg) {
        Scope curScope = new Scope(scope.getFile(), md);
        GetExtraVisitor intentVisitor = new GetExtraVisitor(curScope);
        intentVisitor.visit(md, arg);
    }
}
