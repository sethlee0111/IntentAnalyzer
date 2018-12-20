package com.sethlee0111.basicintentactivity.visitor;

import android.util.Pair;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.sethlee0111.basicintentactivity.symboltable.GlobalSymbolTable;
import com.sethlee0111.basicintentactivity.symboltable.IntentInfo;
import com.sethlee0111.basicintentactivity.symboltable.Scope;
import com.sethlee0111.basicintentactivity.symboltable.TargetPackage;
import com.sethlee0111.basicintentactivity.symboltable.VariableInfo;

import java.util.ArrayList;
import java.util.Optional;

public class IntentVisitor extends VoidVisitorAdapter<Void> {
    private Scope scope;
    private Class thisActivity;
    private Package thisPackage;

    public IntentVisitor(Scope scope) {
        String fileName = "";
        try {
            fileName = removeExtension(scope.getFile().getName());
            thisActivity = Class.forName(TargetPackage.packageName + "." + fileName);
            thisPackage = thisActivity.getPackage();
        } catch (ClassNotFoundException e) {
            System.out.println("No class Found for " + fileName);
            e.printStackTrace();
        }
        this.scope = scope;
    }

    public static String removeExtension(String file){
        return file.replaceFirst("[.][^.]+$", "");
    }

    @Override
    public void visit(BlockStmt md, Void arg) {
        IntentDeclarationVisitor idv = new IntentDeclarationVisitor(new Scope(scope.getFile(), md));
        idv.visit(md, arg);
    }

    class IntentDeclarationVisitor extends VoidVisitorAdapter<Void> {
        Scope curScope;
        public IntentDeclarationVisitor(Scope scope) {
            curScope = scope;
        }

        /**
         * visit() for Intent declaration "Intent intent = ..."
         * @param md
         * @param arg
         */
        @Override
        public void visit(VariableDeclarator md, Void arg) {
            super.visit(md, arg);
            if(md.getType().toString().equals("Intent")) {  //@TODO check if its "real" Intent in Android Library
                IntentInfo intentInfo = new IntentInfo(md.getName().toString());   // we'll save this IntentInfo
                intentInfo.setIntentScope(curScope);
                Optional<Expression> newIntentExpr = md.getInitializer();   // read the initialization
                ObjectCreationExpr newIntentCreationExpr;
                // for a new intent
                if(newIntentExpr.isPresent() && newIntentExpr.get().isObjectCreationExpr()) {
                    newIntentCreationExpr = newIntentExpr.get().asObjectCreationExpr(); // read init part as ObjectCreationExpr
                    if (newIntentCreationExpr.getType().toString().equals("Intent")) {
                        NodeList<?> argList = newIntentCreationExpr.getArguments();
                        if(argList.get(0).getClass().equals(ThisExpr.class)) {
                            intentInfo.setDepartActivity(thisActivity);
                        }
                        else if(argList.get(0).getClass().equals(ClassExpr.class)) {
                            ClassExpr classExpr = (ClassExpr) argList.get(0);
                            try {
                                Class<?> cls = Class.forName(thisPackage.getName() + "." + classExpr.getType().toString());
                                intentInfo.setDepartActivity(cls);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                        if(argList.get(1).getClass().equals(ClassExpr.class)) {
                            ClassExpr classExpr = (ClassExpr) argList.get(1);
                            try {
                                Class<?> cls = Class.forName(thisPackage.getName() + "." + classExpr.getType().toString());
                                intentInfo.setDestActivity(cls);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    GlobalSymbolTable.intents.add(intentInfo);
                }
                // for intent acquired with getIntent()
            }
        }
    }
}
