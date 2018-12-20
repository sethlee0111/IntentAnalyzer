package com.sethlee0111.basicintentactivity;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.sethlee0111.basicintentactivity.visitor.ActivityVisitor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Filter out certain .java files
 */
public class JavaFilter {

    /**
     * filter out Android activity files
     * @param fileList
     * @return
     */
    public static ArrayList<File> activityFilter(ArrayList<File> fileList) {
        ArrayList<File> res = new ArrayList<>();

        for(File file : fileList) {
            ActivityVisitor activityVisitor = new ActivityVisitor();
            CompilationUnit cu = null;
            try {
                cu = JavaParser.parse(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            activityVisitor.visit(cu, null);
            if(activityVisitor.isActivity())
                res.add(file);
        }

        return res;
    }
}
