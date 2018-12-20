package com.sethlee0111.basicintentactivity.visitor;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.sethlee0111.basicintentactivity.symboltable.Scope;
import com.sethlee0111.basicintentactivity.symboltable.SymbolTable;

import java.io.File;

public class ScopeVisitor extends VoidVisitorAdapter<Void> {
    private Scope curScope;   // curScope is a hash value of a file name or BlockStmt @TODO think of more reasonable method
    private File curFile;
    private SymbolTable symbolTable = new SymbolTable();

    public ScopeVisitor(Scope scope) {
        this.curFile = scope.getFile();
        this.curScope = scope;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    @Override
    public void visit(BlockStmt md, Void arg) {
        super.visit(md, arg);
        curScope = new Scope(curFile, md);

        VariableDeclarationVisitor vdVisitor = new VariableDeclarationVisitor(curScope);
        NodeList<Statement> statements = md.getStatements();

        ExpressionStmt expressionStmt;
        for(Statement statement : statements) {
            if(statement.isExpressionStmt()) {
                expressionStmt = statement.asExpressionStmt();
                vdVisitor.visit(expressionStmt, null);
                symbolTable.putAll(vdVisitor.getSymbolTable());
            }
        }
    }
}