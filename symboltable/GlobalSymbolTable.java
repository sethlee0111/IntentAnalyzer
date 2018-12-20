package com.sethlee0111.basicintentactivity.symboltable;

import com.sethlee0111.basicintentactivity.visitor.IntentVisitor;

import java.util.ArrayList;
import java.util.HashSet;


public class GlobalSymbolTable {
    public static ArrayList<Scope> allScopes = new ArrayList<>();
    public static ScopeGraph scopeGraph = new ScopeGraph();
    public static SymbolTable symbolTable = new SymbolTable();
    public static ArrayList<IntentInfo> intents = new ArrayList<>();
    public static ArrayList<IntentFilterInfo> filters = new ArrayList<>();

    public static VariableInfo findVariable(String name, Scope scope) {
        HashSet<VariableInfo> set = symbolTable.get(scope);
        for(VariableInfo info : set) {
            if(info.getName().equals(name))
                return info;
        }
        return null;
    }

    public static IntentInfo findDestIntent(String name, Scope scope) {
        for(IntentInfo info : intents) {
            if(info.getDestNames().contains(name)) {
                if(info.getDestScopes().get(info.getDestNames().indexOf(name)).equals(scope))
                    return info;
            }
        }
        return null;
    }

    public static void print() {
        System.out.println("<Graph>");
        for(Scope key : scopeGraph.keySet()) {
            System.out.println("" + key + " : " + scopeGraph.get(key));
        }
        System.out.println("<ST>");
        for(Scope key : symbolTable.keySet()) {
            System.out.println("" + key + " : " + symbolTable.get(key));
        }
        printIntent();
    }
    public static void printIntent() {
        System.out.println("<Intents>");
        for(IntentInfo info : intents) {
            System.out.println(info);
        }
    }
}
