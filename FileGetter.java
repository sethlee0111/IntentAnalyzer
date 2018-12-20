package com.sethlee0111.basicintentactivity;

import java.io.File;
import java.util.ArrayList;

public class FileGetter {
    private static ArrayList<File> fileArrayList = new ArrayList<>();

    public static ArrayList<File> getFileArrayList() {
        return fileArrayList;
    }

    /**
     * get all files under folder
     * @param folder
     */
    public static void getFiles(File folder) {
        for (final File fileEntry : folder.listFiles()) {
            System.out.println(fileEntry);
            if(fileEntry == null)
                return;
            if (fileEntry.isDirectory()) {
                getFiles(fileEntry);
            } else {
                fileArrayList.add(fileEntry);
            }
        }
    }
}
