package com.sethlee0111.basicintentactivity.symboltable;

import java.util.ArrayList;
import java.util.HashMap;

public class IntentInfo {
    private boolean isExplicit = true;
    private String intentName;
    private Class departActivity;
    private ArrayList<Class> destActivity = new ArrayList<>();
    private HashMap<String, VariableInfo> extraInfos = new HashMap<>();
    private Scope declaredScope;
    private ArrayList<Scope> destScopes = new ArrayList<>();  // scopes of destination
    private ArrayList<String> destNames = new ArrayList<>();  // names in the corresponding scopes

    public IntentInfo() {this.intentName = "null"; }    // @TODO make sense

    public IntentInfo(String intentName) {
        this.intentName = intentName;
    }

    public String getIntentName() {
        return intentName;
    }

    public IntentInfo setIntentName(String intentName) {
        this.intentName = intentName;
        return this;
    }

    public void addDestIntent(String name, Scope scope) {
        destNames.add(name);
        destScopes.add(scope);
    }

    public ArrayList<Scope> getDestScopes() {
        return destScopes;
    }

    public Scope getScope() {
        return declaredScope;
    }

    public void setScope(Scope scope) {
        this.declaredScope = scope;
    }

    public IntentInfo setIntentScope(Scope scope) {
        this.declaredScope = scope;
        return this;
    }

    public ArrayList<String> getDestNames() {
        return destNames;
    }

    public Class getDepartActivity() {
        return departActivity;
    }

    public IntentInfo setDepartActivity(Class departActivity) {
        this.departActivity = departActivity;
        return this;
    }

    public ArrayList<Class> getDestActivity() {
        return destActivity;
    }

    public IntentInfo setDestActivity(Class destActivity) {
        this.destActivity.add(destActivity);
        return this;
    }

    public VariableInfo getExtra(String key) {
        return extraInfos.get(key);
    }

    public void putExtraInfo(String str, VariableInfo var) {
        extraInfos.put(str, GlobalSymbolTable.findVariable(var.getName(), var.getScope()));
    }

    @Override
    public String toString() {
        String res = "";
        if(isExplicit)
            res += "[Explicit Intent]";
        else
            res += "[Implicit Intent]";
        return res + "\nName: " + intentName + "\nin scope: " + declaredScope + "\nDeparts: " + departActivity +
                "\nDestinations: " + destActivity + "\nExtras: " + extraInfos + "\nDestScopes: " + destScopes +
                "\nDestNames: " + destNames;
    }
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof IntentInfo) {
            IntentInfo intentInfo = (IntentInfo) obj;
            if(intentInfo.getIntentName().equals(intentName) && intentInfo.getScope().equals(declaredScope))
                return true;
        }
        return false;
    }
    @Override
    public int hashCode() {
        return this.getIntentName().hashCode();
    }
}
