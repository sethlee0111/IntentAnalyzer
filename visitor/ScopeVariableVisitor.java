package com.sethlee0111.basicintentactivity.visitor;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.sethlee0111.basicintentactivity.symboltable.Scope;
import com.sethlee0111.basicintentactivity.symboltable.SymbolTable;

/**
 * visits all scopes to make a symbol table
 */
public class ScopeVariableVisitor extends VoidVisitorAdapter<Void> {
    private Scope curScope;   // curScope is a hash value of a file name or BlockStmt @TODO think of more reasonable method
    private SymbolTable symbolTable = new SymbolTable();

    public ScopeVariableVisitor(Scope curScope) {
        this.curScope = curScope;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    @Override
    public void visit(BlockStmt md, Void arg) {
        super.visit(md, arg);
        curScope = new Scope(curScope.getFile(), md);

        VariableDeclarationVisitor vdVisitor = new VariableDeclarationVisitor(curScope);

        NodeList<Statement> statements = md.getStatements();
        ExpressionStmt expressionStmt;
        for(Statement statement : statements) {
            if(statement.isExpressionStmt()) {
                expressionStmt = statement.asExpressionStmt();
                vdVisitor.visit(expressionStmt, null);
                symbolTable.putAll(vdVisitor.getSymbolTable());
            }
            else if(statement.isBlockStmt()) {

            }
        }
        System.out.println("______________END OF SCOPE_______________\n");
    }

}