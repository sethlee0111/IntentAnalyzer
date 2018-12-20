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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GetExtraVisitor extends VoidVisitorAdapter<Void>{
    private Scope curScope;

    public GetExtraVisitor(Scope scope) {
        curScope = scope;
    }

    // @TODO resolve getExtras() call
    /**
     * visit Method Calls and find putExtra() calls
     * @param md
     * @param arg
     */
    @Override
    public void visit(MethodCallExpr md, Void arg) {
        super.visit(md, arg);
        VariableInfo var = new VariableInfo();
        IntentInfo intent = new IntentInfo();
        if(md.getName().toString().contains("Extra") && md.getName().toString().contains("get")) {
            Optional<Node> parentNode = md.getParentNode();
            if(parentNode.isPresent()) {
                List<Node> nl = parentNode.get().getChildNodes();
                // which variable does getExtra() output?
                for(Node n : nl) {
                    var = GlobalSymbolTable.findVariable(n.toString(), curScope);
                    if(var != null)
                        break;
                }
                // which intent does getExtra() called on?
                Optional<Expression> expr = md.getScope();
                if(expr.isPresent()) {
                    String nn = expr.get().toString();
                    intent = GlobalSymbolTable.findDestIntent(nn, curScope);
                }
            }
            System.out.println("getExtra() called : " + md.getName());
            // which extra inside the intent called?
            NodeList<?> argList = md.getArguments();
            Node keyNode = argList.get(0);
            VariableInfo variableInfo = null;
            if(intent != null)
                variableInfo = intent.getExtra(keyNode.toString());
            if(var != null && !var.getName().equals("null")) {
                if(!var.isSameAnnotation(variableInfo)) {
                    System.out.println("WARNING");
                }
            }
        }
    }
}