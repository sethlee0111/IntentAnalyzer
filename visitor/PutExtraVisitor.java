package com.sethlee0111.basicintentactivity.visitor;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.sethlee0111.basicintentactivity.symboltable.GlobalSymbolTable;
import com.sethlee0111.basicintentactivity.symboltable.IntentInfo;
import com.sethlee0111.basicintentactivity.symboltable.Scope;
import com.sethlee0111.basicintentactivity.symboltable.VariableInfo;

import java.util.Optional;

public class PutExtraVisitor extends VoidVisitorAdapter<Void>{
    private Scope curScope;

    public PutExtraVisitor(Scope scope) {
        curScope = scope;
    }

    /**
     * visit Method Calls and find putExtra() calls
     * @param md
     * @param arg
     */
    @Override
    public void visit(MethodCallExpr md, Void arg) {
        super.visit(md, arg);
        if(md.getName().toString().equals("putExtra")) {
            VariableInfo var = new VariableInfo();  //@TODO put some info in variableInfo
            NodeList<?> argList = md.getArguments();
            Node keyNode = argList.get(0);
            var.setName(argList.get(1).toString());
            var.setScope(curScope);

            Optional<Expression> expr = md.getScope();
            if(expr.isPresent()) {
                String nn = expr.get().toString();
                IntentInfo i = new IntentInfo(nn).setIntentScope(curScope);
                if(GlobalSymbolTable.intents.contains(i)) {   // if putExtra() is called to an intent that is declared
                    GlobalSymbolTable.intents.get(GlobalSymbolTable.intents.indexOf(i)).putExtraInfo(keyNode.toString(), var);
                }
            }
        }
    }
}
