package com.sethlee0111.basicintentactivity.visitor;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ActivityVisitor extends VoidVisitorAdapter<Void> {
    private boolean isActivity = false;

    @Override
    public void visit(ClassOrInterfaceDeclaration md, Void arg) {
        super.visit(md, arg);
        for(Node node : md.getExtendedTypes()) {
            if(node.toString().equals("AppCompatActivity")) {
                    isActivity = true;
                    break;
            }
        }
    }

    public boolean isActivity() {
        return isActivity;
    }
}

