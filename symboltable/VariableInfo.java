package com.sethlee0111.basicintentactivity.symboltable;

import java.util.ArrayList;

/**
 * keeps track of an annotated variable
 */
public class VariableInfo {
    private ArrayList<String> annotations = new ArrayList<>();    // security types
    private Scope scope;
    private String name;
    private String type;

    public VariableInfo() {
        this.name = "null";
    }

    public VariableInfo(String name, Scope scope) {
        this.scope = scope;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void addAnnotation(String annotation) {
        annotations.add(annotation);
    }

    public ArrayList<String> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(ArrayList<String> annotations) {
        this.annotations = annotations;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean equals(VariableInfo variableInfo) {
        return variableInfo.name.equals(name) && variableInfo.scope == scope && variableInfo.annotations.equals(annotations);
    }

    public boolean isSameAnnotation(VariableInfo variableInfo) {
        return annotations.equals(variableInfo.annotations);    // @TODO regardless of the order
    }

    @Override
    public String toString() {
        return "" + annotations + " var (" + name + ") of type (" + type + ") in scope :" + scope;
    }
}
