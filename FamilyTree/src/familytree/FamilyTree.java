package familytree;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FamilyTree {
    
    private FamilyNode root = null;
    
    private String readFile(String filepath) throws Exception {
        File file = new File(filepath); 
        BufferedReader br = new BufferedReader(new FileReader(file)); 
        StringBuilder xml = new StringBuilder();
        
        String st; 
        while ((st = br.readLine()) != null) {
          xml.append(st).append("\n");
        }
        
        return xml.toString();
    }
    
    private class BFSNode {
        Node node;
        int depth;
        FamilyNode parent;
        
        public BFSNode(Node node, int depth, FamilyNode parent) {
            this.node = node;
            this.depth = depth;
            this.parent = parent;
        }
    }
    
    private void BFSTraversal(Node elementRoot) {
        String name = null;
        String gender = null;
        
        Queue<BFSNode> queue = new LinkedList<>();
        queue.add(new BFSNode(elementRoot, 0, null));
        
        BFSNode front;
        Node node;
        int depth;
        FamilyNode parent;
        while (!queue.isEmpty()) {
            front = queue.remove();
            
            node = front.node;
            depth = front.depth;
            parent = front.parent;
                        
            NodeList childNodes = node.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                var child = childNodes.item(i);

                if (child.getNodeName().equals("name"))
                    name = child.getTextContent();

                if (child.getNodeName().equals("gender"))
                    gender = child.getTextContent();
            }
            
            FamilyNode newNode;
            
            if (name == null) {
                newNode = null;
            }
            else if (parent == null) {
                root = new FamilyNode(name, gender, depth, null);
                newNode = root;
            } else {
                newNode = new FamilyNode(name, gender, depth, parent);
                parent.addChild(newNode);
            }
                    
            for (int i = 0; i < childNodes.getLength(); i++) {
                var child = childNodes.item(i);
                if (child.getNodeName().equals("node"))
                    queue.add(new BFSNode(child, depth + 1, newNode));
            }   
        }
    }
    
    public void print() {
        Queue<FamilyNode> queue = new LinkedList<>();
        queue.add(root);
        
        FamilyNode front;
        while (!queue.isEmpty()) {
            front = queue.remove();
            System.out.println(front.toString());
        
            for (int i = 0; i < front.getChildren().size(); i++) {
                var child = front.getChildren().get(i);
                queue.add(child);
            }   
        }
    }
    
    public void build(String xmlFilePath) throws Exception {
        
        String xmlFileContent = readFile(xmlFilePath);
        
        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        
        ByteArrayInputStream input = new ByteArrayInputStream(
                xmlFileContent.getBytes("UTF-8")
        );
        Document doc = builder.parse(input);
        Element elementRoot = doc.getDocumentElement();
        
        BFSTraversal(elementRoot);
    }
    
    private void computeAreaHeightRec(FamilyNode node, int boxWidth, int boxHeight, 
            int boxPaddingHor, int boxPaddingVer) {
        
        if (node.getChildren().isEmpty()) {
            node.setAreaHeight(boxHeight + boxPaddingVer);
            return;
        }
       
        for (int i = 0; i < node.getChildren().size(); i++) {
            computeAreaHeightRec(node.getChildren().get(i), boxWidth, 
                    boxHeight, boxPaddingHor, boxPaddingVer);
        }
        
        int currAreaHeight = 0;
        for (int i = 0; i < node.getChildren().size(); i++) {
            currAreaHeight += node.getChildren().get(i).getAreaHeight();
        }
        
        node.setAreaHeight(currAreaHeight);
    }
    
    /*public void computeDrawPositions(int boxWidth, int boxHeight,
    int boxPaddingHor, int boxPaddingVer) {
    
    computeAreaHeightRec(root, boxWidth, boxHeight, boxPaddingHor, boxPaddingVer);
    
    Queue<FamilyNode> queue = new LinkedList<>();
    queue.add(root);
    root.setPosY(root.getAreaHeight() / 2);
    root.setPosX(0);
    
    FamilyNode front;
    int currY;
    while (!queue.isEmpty()) {
    front = queue.remove();
    
    currY = front.getPosY() - front.getAreaHeight() / 2;
    for (int i = 0; i < front.getChildren().size(); i++) {
    var child = front.getChildren().get(i);
    child.setPosY(currY + child.getAreaHeight() / 2);
    currY += child.getAreaHeight();
    child.setPosX(front.getPosX() + boxWidth + boxPaddingHor);
    queue.add(child);
    }
    }
    }*/
    
    public void computeDrawPositions(int boxWidth, int boxHeight,
        int boxPaddingHor, int boxPaddingVer) {

        Queue<FamilyNode> queue = new LinkedList<>();
        queue.add(root);

        int[] depthPtr = new int[20];
        int[] depthMean = new int[20];
        int[] depthCnt = new int[20];
        for (int i = 0; i < 20; i++) {
            depthPtr[i] = 0;
            depthMean[i] = 0;
            depthCnt[i] = 0;
        }

        FamilyNode front;
        while (!queue.isEmpty()) {
            front = queue.remove();
            front.setPosX((front.getDepth() - 1) * (boxWidth + boxPaddingHor));
            front.setPosY(depthPtr[front.getDepth()]);
            
            depthMean[front.getDepth()] += depthPtr[front.getDepth()];
            depthCnt[front.getDepth()]++;
            depthPtr[front.getDepth()] += boxHeight + boxPaddingVer;

            if (front.isLastChild()) {
                depthPtr[front.getDepth()] += boxHeight;
            }

            for (int i = 0; i < front.getChildren().size(); i++) {
                var child = front.getChildren().get(i);
                queue.add(child);
            }
        }
        
        int maxDepthMean = 0;
        for (int i = 0; i < 20; i++) {
            if (depthCnt[i] > 0) {
                depthMean[i] = depthMean[i] / depthCnt[i];
                if (depthMean[i] > maxDepthMean)
                    maxDepthMean = depthMean[i];
            }
        }
        
        float[] scaleFactor = new float[20];
        for (int i = 0; i < 20; i++) {
            scaleFactor[i] = 1f;
        }
        
        for (int i = 1; i < 20 && depthCnt[i] > 0; i++) {
            if (depthPtr[i] < depthPtr[i - 1]) {
                scaleFactor[i] = (float)depthPtr[i - 1] / (float)depthPtr[i];
                depthMean[i] = depthMean[i - 1];
            }
        }
        
        for (int i = 0; i < 20; i++) {
            depthMean[i] = maxDepthMean - depthMean[i];
        }
        
        for (int i = 0; i < 10; i++)
            System.out.println(scaleFactor[i]);
        
        queue.add(root);
        while (!queue.isEmpty()) {
            front = queue.remove();
            front.setPosY((int)(front.getPosY() * scaleFactor[front.getDepth()]));
            front.setPosY(front.getPosY() + depthMean[front.getDepth()]);
            
            for (int i = 0; i < front.getChildren().size(); i++) {
                var child = front.getChildren().get(i);
                queue.add(child);
            }
        }
    }
    
    public ArrayList<DrawNode> getDrawArray() {
        ArrayList<DrawNode> drawArray = new ArrayList<>();
        
        Queue<FamilyNode> queue = new LinkedList<>();
        queue.add(root);
        
        FamilyNode front;
        while (!queue.isEmpty()) {
            front = queue.remove();
            DrawNode newDrawNode = 
                    new DrawNode(front.getName(), front.isMale(), front.getPosX(), front.getPosY());
        
            drawArray.add(newDrawNode);
            
            for (int i = 0; i < front.getChildren().size(); i++) {
                var child = front.getChildren().get(i);
                queue.add(child);
            }   
        }
        
        return drawArray;
    }
    
    public ArrayList<FamilyLine> getDrawLines(int boxHeight, int boxWidth) {
        ArrayList<FamilyLine> drawLines = new ArrayList<>();
        
        Queue<FamilyNode> queue = new LinkedList<>();
        queue.add(root);
        
        FamilyNode front;
        while (!queue.isEmpty()) {
            front = queue.remove();
            
            for (int i = 0; i < front.getChildren().size(); i++) {
                var child = front.getChildren().get(i);
                
                FamilyLine newLine = new FamilyLine(
                        front.getPosX() + boxWidth,
                        front.getPosY() + boxHeight / 2,
                        child.getPosX(),
                        child.getPosY() + boxHeight / 2
                );
                drawLines.add(newLine);
                
                queue.add(child);
            }   
        }
        
        return drawLines;
    }
    
}
