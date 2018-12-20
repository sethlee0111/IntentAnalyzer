package com.sethlee0111.basicintentactivity.visitor;

import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.sethlee0111.basicintentactivity.symboltable.GlobalSymbolTable;
import com.sethlee0111.basicintentactivity.symboltable.Scope;

import java.util.HashSet;

public class BlockScopeVisitor extends VoidVisitorAdapter<Void> {
    private Scope curScope;

    public BlockScopeVisitor(Scope scope) {
        this.curScope = scope;
        if(!GlobalSymbolTable.scopeGraph.containsKey(curScope))
            GlobalSymbolTable.scopeGraph.put(curScope, new HashSet<Scope>());
    }

    @Override
    public void visit(BlockStmt md, Void arg) {  // @TODO get general annotation instead of marker annotation
        super.visit(md, arg);
        Scope visitingScope = new Scope(curScope.getFile(), md);
        BlockScopeVisitor blockVisitor = new BlockScopeVisitor(visitingScope);
        blockVisitor.visit(md.getStatements(), null);
        GlobalSymbolTable.scopeGraph.get(curScope).add(visitingScope);
    }
}