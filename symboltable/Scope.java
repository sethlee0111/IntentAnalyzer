package com.sethlee0111.basicintentactivity.symboltable;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;

import java.io.File;

public class Scope {
    private boolean isBlock = false; // does this contain info on block?
    private File file;
    private BlockStmt block;
    private int scopeNum;
    private static int scopeCount = 0;

    private boolean blockEquals(BlockStmt bs1, BlockStmt bs2) {
        NodeList<Statement> stmts1 = bs1.getStatements();
        NodeList<Statement> stmts2 = bs2.getStatements();

        if(stmts1.size() != stmts2.size())
            return false;
        for(int i = 0; i < stmts1.size() ; i++) {
            if(!stmts1.get(i).equals(stmts2.get(i)))
                return false;
        }
        return true;
    }

    public Scope(File file) {
        isBlock = false;
        this.scopeNum = scopeCount++;
        this.file = file;
        for(Scope scope : GlobalSymbolTable.scopeGraph.keySet()) {
            if(!scope.isBlock && scope.file.equals(file)){
                this.scopeNum = scope.scopeNum;
                scopeCount--;
            }
        }
    }

    public File getFile() {
        return file;
    }

    public Scope(File file, BlockStmt block) {
        this.scopeNum = scopeCount++;
        this.file = file;
        this.block = block;
        isBlock = true;
        for(Scope scope : GlobalSymbolTable.scopeGraph.keySet()) {
            if(scope.isBlock && isBlock && blockEquals(scope.block, block) && scope.file.equals(file)) {
                this.scopeNum = scope.scopeNum;
                scopeCount--;
            }
        }
    }

    public BlockStmt getBlock() {
        return block;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Scope) {
            Scope targetScope = (Scope) obj;
            if (isBlock && targetScope.isBlock) {
                if (targetScope.file.equals(file) && blockEquals(targetScope.block, block))
                    return true;
            } else {
                if (targetScope.file.equals(file))
                    return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        if(!isBlock)
            return "File-" + file.getName();
        else return "B#" + scopeNum + " in " + file.getName();
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
}
