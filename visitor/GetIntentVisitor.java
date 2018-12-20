package com.sethlee0111.basicintentactivity.visitor;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.sethlee0111.basicintentactivity.symboltable.GlobalSymbolTable;
import com.sethlee0111.basicintentactivity.symboltable.IntentInfo;
import com.sethlee0111.basicintentactivity.symboltable.Scope;
import com.sethlee0111.basicintentactivity.symboltable.TargetPackage;

import java.util.Optional;

public class GetIntentVisitor extends VoidVisitorAdapter<Void> {
    private Scope curScope;
    private Class thisActivity;

    public GetIntentVisitor(Scope scope) {
        curScope = scope;
        try {
            thisActivity = Class.forName(TargetPackage.packageName + "." + IntentVisitor.removeExtension(scope.getFile().getName()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("ERR: " + TargetPackage.packageName + "." + scope.getFile().getName());
        }
    }

    /**
     * visit() for Intent declaration "Intent intent = ..."
     * @param md
     * @param arg
     */
    @Override
    public void visit(VariableDeclarator md, Void arg) {
        super.visit(md, arg);
        Optional<Expression> newIntentExpr = md.getInitializer();   // read the initialization

        if(newIntentExpr.isPresent() && newIntentExpr.get().isMethodCallExpr()) {
            MethodCallExpr expr = newIntentExpr.get().asMethodCallExpr();
            if(expr.getName().toString().equals("getIntent")) {
                System.out.println("getIntent() called");
                for(IntentInfo info : GlobalSymbolTable.intents) {
                    if(info.getDestActivity().contains(thisActivity) && !info.getDestScopes().contains(curScope)) {
                        info.addDestIntent(md.getName().toString(), curScope);
                    }
                }
            }
        }
    }
}
