package com.sethlee0111.basicintentactivity;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.sethlee0111.basicintentactivity.symboltable.FilterInfo;
import com.sethlee0111.basicintentactivity.symboltable.GlobalSymbolTable;
import com.sethlee0111.basicintentactivity.symboltable.IntentFilterInfo;
import com.sethlee0111.basicintentactivity.symboltable.Scope;
import com.sethlee0111.basicintentactivity.symboltable.SymbolTable;
import com.sethlee0111.basicintentactivity.symboltable.VariableInfo;
import com.sethlee0111.basicintentactivity.visitor.AnnotationDeclarationVisitor;
import com.sethlee0111.basicintentactivity.visitor.BlockGetExtraVisitor;
import com.sethlee0111.basicintentactivity.visitor.BlockGetIntentVisitor;
import com.sethlee0111.basicintentactivity.visitor.BlockIntentVisitor;
import com.sethlee0111.basicintentactivity.visitor.BlockPutExtraVisitor;
import com.sethlee0111.basicintentactivity.visitor.BlockScopeVisitor;
import com.sethlee0111.basicintentactivity.visitor.IntentVisitor;
import com.sethlee0111.basicintentactivity.visitor.ScopeVisitor;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class AnnotationTest {
    private final String manifestPath = "/Users/sethlee/AndroidStudioProjects/BasicIntentActivity/app/src/main/AndroidManifest.xml";
    private final String FILE_PATH = "/Users/sethlee/AndroidStudioProjects/BasicIntentActivity/app/src/main/java/com/sethlee0111/basicintentactivity/MainActivity.java";
    private final String FOLDER_PATH = "/Users/sethlee/AndroidStudioProjects/BasicIntentActivity/app/src/main/java/com/sethlee0111/basicintentactivity/";
    private ArrayList<File> fileArrayList;

    private ArrayList<String> aList = new ArrayList<>();    // list of annotations defined in the package

    @Test
    public void annotation_test() {
        /*
        parse manifest file to get intent filters
         */
        xmlParseTest();

        // get all .java files
        FileGetter.getFiles(new File(FOLDER_PATH));
        fileArrayList = FileGetter.getFileArrayList();

        /**
         * First, gather info on user-defined annotations
         */
        System.out.println("Building AST...");
        CompilationUnit acu = null;
        for(File file : fileArrayList) {
            try {
                acu = JavaParser.parse(file);
                AnnotationDeclarationVisitor vv = new AnnotationDeclarationVisitor();
                vv.visit(acu, null);
                aList.addAll(vv.getaList());
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        // check annotations
        System.out.println("Searching Annotations...");
        int i = 1;
        for(String str : aList) {
            System.out.println("Annotation " + (i++) + ": " + str);
        }
        /**
         * Second, define all scopes
         */

        // make symbol graph
        for(File file : fileArrayList) {
            try {
                acu = JavaParser.parse(file);
                Scope fileScope = new Scope(file);
                // Make Symbol Graph
                BlockScopeVisitor bv = new BlockScopeVisitor(fileScope);
                bv.visit(acu, null);
                // Make Symbol Tables
                ScopeVisitor sv = new ScopeVisitor(fileScope);
                sv.visit(acu, null);

                SymbolTable symbolTable;  // HashMap<Integer, HashSet<VariableInfo>>
                symbolTable = sv.getSymbolTable();
                GlobalSymbolTable.symbolTable.putAll(symbolTable);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        for(File file : fileArrayList) {
            try {
                acu = JavaParser.parse(file);
                // Resolve Intents
                BlockIntentVisitor biv = new BlockIntentVisitor(new Scope(file));
                biv.visit(acu, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        for(File file : fileArrayList) {
            try {
                acu = JavaParser.parse(file);
                // Resolve PutExtras
                BlockPutExtraVisitor bpev = new BlockPutExtraVisitor(new Scope(file));
                bpev.visit(acu, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        for(File file : fileArrayList) {
            try {
                acu = JavaParser.parse(file);
                // Resolve Intents
                BlockGetIntentVisitor bgiv = new BlockGetIntentVisitor(new Scope(file));
                bgiv.visit(acu, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        for(File file : fileArrayList) {
            try {
                acu = JavaParser.parse(file);
                // Resolve Intents
                BlockGetExtraVisitor bgiv = new BlockGetExtraVisitor(new Scope(file));
                bgiv.visit(acu, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        /**
         * resolve security types of all extras
         */
//        GlobalSymbolTable.print();
    }

    public void xmlParseTest() {
        try {
            File manifest = new File(manifestPath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(manifest);

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

//            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            System.out.println("Parsing manifest...");
            String packageName = doc.getDocumentElement().getAttribute("package");
            System.out.println(packageName);

            NodeList nList = doc.getElementsByTagName("activity");

//            System.out.println("----------------------------");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

//                System.out.println("\nCurrent Element :" + nNode.getNodeName());

                // for each activity
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    System.out.println("In activity: " + eElement.getAttribute("android:name"));
                    Class<?> cls = Class.forName(packageName + eElement.getAttribute("android:name"));
//                    System.out.println("Class detected: " + cls);
                    IntentFilterInfo info = new IntentFilterInfo(cls);
                    NodeList iList = eElement.getElementsByTagName("intent-filter"); // intent-filter list
//                    System.out.println(iList.getLength());
                    for(int temp2 = 0; temp2 < iList.getLength(); temp2++) {
                        FilterInfo fInfo = new FilterInfo();
                        Node iNode = iList.item(temp2);
                        Element iElement = (Element) iNode;
                        NodeList aList = iElement.getElementsByTagName("action");
                        for(int i = 0; i < aList.getLength(); i++) {
                            System.out.println("Action#" + (i+1) + ((Element)aList.item(i)).getAttribute("android:name"));
                            fInfo.addAction(((Element)aList.item(i)).getAttribute("android:name"));
                        }
                        NodeList cList = iElement.getElementsByTagName("category");
                        for(int i = 0; i < cList.getLength(); i++) {
                            System.out.println("Category#" + (i+1) + ((Element)cList.item(i)).getAttribute("android:name"));
                            fInfo.addCategory(((Element)cList.item(i)).getAttribute("android:name"));
                        }
                        NodeList dList = iElement.getElementsByTagName("data");
                        for(int i = 0; i < dList.getLength(); i++) {
                            System.out.println("Data#" + (i+1) + ((Element)dList.item(i)).getAttribute("android:mimeType"));
                            fInfo.addData(((Element)dList.item(i)).getAttribute("android:mimeType"));
                        }
                        info.addIntentFilter(fInfo);
                    }
//                    System.out.println("---");

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}