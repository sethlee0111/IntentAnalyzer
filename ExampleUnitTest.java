package com.sethlee0111.basicintentactivity;

import com.github.javaparser.ast.expr.Expression;
import com.sethlee0111.basicintentactivity.symboltable.FilterInfo;
import com.sethlee0111.basicintentactivity.symboltable.GlobalSymbolTable;
import com.sethlee0111.basicintentactivity.symboltable.IntentFilterInfo;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    final String manifestPath = "/Users/sethlee/AndroidStudioProjects/BasicIntentActivity/app/src/main/AndroidManifest.xml";

    @Test
    public void xmlParseTest() {
        try {
            File manifest = new File(manifestPath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(manifest);

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            String packageName = doc.getDocumentElement().getAttribute("package");
            System.out.println(packageName);

            NodeList nList = doc.getElementsByTagName("activity");

            System.out.println("----------------------------");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                System.out.println("\nCurrent Element :" + nNode.getNodeName());

                // for each activity
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    System.out.println("In activity: " + eElement.getAttribute("android:name"));
                    Class<?> cls = Class.forName(packageName + eElement.getAttribute("android:name"));
                    System.out.println("Class detected: " + cls);
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
                    System.out.println("---");

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}