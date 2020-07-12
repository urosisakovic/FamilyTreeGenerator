package familytree;

import java.util.ArrayList;

public class FamilyNode {
    private final String name;
    private final String gender;
    private final int depth;
    
    private ArrayList<FamilyNode> children = null;
    private FamilyNode parent = null;
    
    private int posX = 0;
    private int posY = 0;
    
    private int areaHeight = 0;

    public int getAreaHeight() {
        return areaHeight;
    }

    public void setAreaHeight(int areaHeight) {
        this.areaHeight = areaHeight;
    }

    public FamilyNode getParent() {
        return parent;
    }

    public void setParent(FamilyNode parent) {
        this.parent = parent;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }
    
    public FamilyNode(String name, String gender, int depth, FamilyNode parent) {
        this.name = name;
        this.gender = gender;
        this.depth = depth;
        this.parent = parent;
        this.children = new ArrayList<>();
    }
    
    public boolean isLastChild() {
        if (parent == null)
            return true;
        ArrayList<FamilyNode> c = parent.getChildren();
        if (this == c.get(c.size() - 1))
            return true;
        return false;
    }
    
    public String getName() {
        return name;
    }
    
    public boolean isMale() {
        return "M".equals(gender) || "m".equals(gender);
    }
    
    public boolean isFemale() {
        return !isMale();
    }
    
    public int getDepth() {
        return depth;
    }
    
    public void addChild(FamilyNode child) {
        children.add(child);
    }
    
    @Override
    public String toString() {
        return name + "[" + gender + "] - " + depth;
    }
    
    public ArrayList<FamilyNode> getChildren() {
       return children; 
    }
}
