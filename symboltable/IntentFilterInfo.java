package com.sethlee0111.basicintentactivity.symboltable;

import java.util.ArrayList;

public class IntentFilterInfo {
    Class activity;
    ArrayList<FilterInfo> flist = new ArrayList<>();

    public IntentFilterInfo(Class activity) {
        this.activity = activity;
    }

    public void addIntentFilter(FilterInfo fi) {
        flist.add(fi);
    }
}
