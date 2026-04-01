package org.PETree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.PETree.MatchType.*;

enum MatchType {
    AND,
    OR,
    SEQUENCE,
    NSEQ
}



interface TreeNode {
    TreeNode getSibling();
    void setSibling(TreeNode sibling);
    TreeNode getParent();
    void setParent(TreeNode parent);
    List<LeafNode> getChildren();
    TreeNode getChild(int index);
    void  clearMatch();
    List<match> getMatches();
}


class LeafNode implements TreeNode {
    private String eventType;
    private List<Event> eventList;
    private TreeNode sibling;
    private TreeNode parent;
    private int j;
    private int index;
    public LeafNode(String eventType) {
        this.eventType = eventType;
        this.eventList = new ArrayList<>();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getEventType() {
        return eventType;
    }


    public List<Event> getEventList() {
        return eventList;
    }

    @Override
    public TreeNode getSibling() {
        return sibling;
    }

    @Override
    public void setSibling(TreeNode sibling) {
        this.sibling = sibling;
    }

    @Override
    public TreeNode getParent() {
        return parent;
    }

    @Override
    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    @Override
    public List<LeafNode> getChildren() {
        return null;
    }

    @Override
    public TreeNode getChild(int index) {
        return null;
    }

    @Override
    public void clearMatch() {

    }

    @Override
    public List<match> getMatches() {
        return Collections.emptyList();
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }
}



class SeqNode implements TreeNode {
   ;
    private List<match> partialmatchs;

    private List<LeafNode> childreds;
    private TreeNode siblingNode;
    private TreeNode parent;
    private int i;

    public SeqNode(List<LeafNode> childreds) {
        this.partialmatchs =  new ArrayList<>();
        this.childreds = childreds;
    }


    @Override
    public TreeNode getSibling() {
        return siblingNode;
    }

    @Override
    public void setSibling(TreeNode sibling) {
        this.siblingNode = sibling;
    }

    @Override
    public TreeNode getParent() {
        return parent;
    }

    @Override
    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    @Override
    public List<LeafNode> getChildren() {
        return childreds;
    }

    public void setChildreds(List<LeafNode> childreds) {
        this.childreds = childreds;
    }

    @Override
    public TreeNode getChild(int index) {
        if (index<0 || index>=childreds.size()) {
            return null;
        } else {
            return childreds.get(index);
        }
    }

    @Override
    public void clearMatch() {
        partialmatchs.clear();
    }

    @Override
    public List<match> getMatches() {
        return partialmatchs;
    }

    public List<match> getPartialmatchs() {
        return partialmatchs;
    }

    public void setPartialmatchs(List<match> partialmatchs) {
        this.partialmatchs = partialmatchs;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }
}

class AndNode implements TreeNode {
    private List<String> partialMatch;
    private List<match> partialmatchs;
    private MatchType type;
    private TreeNode parent;
    private int i;
    private List<LeafNode> childreds;
    private TreeNode siblingNode;

    public AndNode(List<LeafNode> childreds) {
        this.partialMatch =  new ArrayList<>();
        this.childreds = childreds;
    }

    public List<String> getPartialMatch() {
        return partialMatch;
    }

    public MatchType getType() {
        return type;
    }


    @Override
    public TreeNode getSibling() {
        return siblingNode;
    }

    @Override
    public void setSibling(TreeNode sibling) {
        this.siblingNode = sibling;
    }

    @Override
    public TreeNode getParent() {
        return parent;
    }

    @Override
    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    @Override
    public List<LeafNode> getChildren() {
        return childreds;
    }

    public void setChildreds(List<LeafNode> childreds) {
        this.childreds = childreds;
    }



    @Override
    public TreeNode getChild(int index) {
        if (index < 0 || index >= childreds.size()) {
            return null;
        } else {
            return childreds.get(index);
        }
    }

    @Override
    public void clearMatch() {
        partialmatchs.clear();
    }

    @Override
    public List<match> getMatches() {
        return partialmatchs;
    }

    public List<match> getPartialmatchs() {
        return partialmatchs;
    }

    public void setPartialmatchs(List<match> partialmatchs) {
        this.partialmatchs = partialmatchs;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }
}


class NSEQNode implements TreeNode {
    private List<String> partialMatch;
    private List<match> partialmatchs;
    private MatchType type;
    private int i;
    private LeafNode leftNode;
    private LeafNode negationNode;
    private LeafNode rightNode;
    private TreeNode parent;
    private TreeNode siblingNode;


    public NSEQNode(LeafNode leftNode, LeafNode negationNode ,LeafNode rightNode) {
        this.partialMatch =  new ArrayList<>();
        this.type = NSEQ;
        this.leftNode = leftNode;
        this.negationNode = negationNode;
        this.rightNode = rightNode;
    }


    public TreeNode getSiblingNode() {
        return siblingNode;
    }

    public void setSiblingNode(TreeNode siblingNode) {
        this.siblingNode = siblingNode;
    }


    public List<String> getPartialMatch() {
        return partialMatch;
    }

    public List<match> getPartialmatchs() {
        return partialmatchs;
    }

    public void setPartialmatchs(List<match> partialmatchs) {
        this.partialmatchs = partialmatchs;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public TreeNode getSibling(int index) {
        return null;
    }

    @Override
    public TreeNode getSibling() {
        return null;
    }

    @Override
    public void setSibling(TreeNode sibling) {

    }

    @Override
    public TreeNode getParent() {
        return null;
    }

    @Override
    public void setParent(TreeNode parent) {

    }

    @Override
    public List<LeafNode> getChildren() {
        return null;
    }

    @Override
    public TreeNode getChild(int index) {
        if (index == 0) {
            return leftNode;
        }
        if (index == 1) {
            return negationNode;
        }
        if (index == 2) {
            return rightNode;
        }
        else {
            return null;
        }
    }

    @Override
    public void clearMatch() {
        partialmatchs.clear();
    }

    @Override
    public List<match> getMatches() {
        return partialmatchs;

    }
}


class TreeRoot implements TreeNode {
    private LeafNode[] leafNodes;
    private List<String> partialMatch;
    private List<match> partialmatchs;
    private MatchType type;
    private TreeNode leftNode;
    private TreeNode rightNode;
    private TreeNode parent;
    private List<TreeNode> operatorLists;

    public TreeRoot(LeafNode[] leafNodes, TreeNode leftNode, TreeNode rightNode) {
        this.leafNodes = leafNodes;
        this.partialMatch = new ArrayList<>();
        this.type = type;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }


    public TreeRoot(List<TreeNode> operatorLists) {
        this.operatorLists = operatorLists;
    }


    public LeafNode[] getLeafNodes() {
        return leafNodes;
    }

    public List<String> getPartialMatch() {
        return partialMatch;
    }


    public TreeNode getLeftNode() {
        return leftNode;
    }

    public TreeNode getRightNode() {
        return rightNode;
    }

    @Override
    public TreeNode getSibling() {

        return null;
    }

    @Override
    public void setSibling(TreeNode sibling) {

    }

    @Override
    public TreeNode getParent() {
        return null;
    }

    @Override
    public void setParent(TreeNode parent) {

    }

    @Override
    public List<LeafNode> getChildren() {
        return null;
    }

    @Override
    public TreeNode getChild(int index) {
        return null;
    }

    @Override
    public void clearMatch() {
        partialmatchs.clear();
    }

    @Override
    public List<match> getMatches() {
        return partialmatchs;
    }

    public List<match> getPartialmatchs() {
        return partialmatchs;
    }

    public void setPartialmatchs(List<match> partialmatchs) {
        this.partialmatchs = partialmatchs;
    }


    public List<TreeNode> getOperatorLists() {
        return operatorLists;
    }

    public void setOperatorLists(List<TreeNode> operatorLists) {
        this.operatorLists = operatorLists;
    }

}



class InternalNode4Connection implements TreeNode {
    private List<String> partialMatch;
    private List<match> partialmatchs;
    private MatchType type;
    private TreeNode leftNode;
    private TreeNode rightNode;
    private TreeNode siblingNode;
    private TreeNode parent;

    public InternalNode4Connection(MatchType type, TreeNode leftNode, TreeNode rightNode) {
        this.partialMatch =  new ArrayList<>();
        this.type = type;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }


    public List<String> getPartialMatch() {
        return partialMatch;
    }

    public MatchType getType() {
        return type;
    }

    public TreeNode getLeftNode() {
        return leftNode;
    }

    public TreeNode getRightNode() {
        return rightNode;
    }

    @Override
    public TreeNode getSibling() {
        return siblingNode;
    }

    @Override
    public void setSibling(TreeNode sibling) {
        this.siblingNode = sibling;
    }

    @Override
    public TreeNode getParent() {
        return parent;
    }

    @Override
    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    @Override
    public List<LeafNode> getChildren() {
        return null;
    }

    @Override
    public TreeNode getChild(int index) {
        return null;
    }

    @Override
    public void clearMatch() {
        partialmatchs.clear();
    }

    @Override
    public List<match> getMatches() {
        return Collections.emptyList();
    }

    public List<match> getPartialmatchs() {
        return partialmatchs;
    }

    public void setPartialmatchs(List<match> partialmatchs) {
        this.partialmatchs = partialmatchs;
    }


    public TreeRoot petree1(){
        LeafNode AleafNode = new LeafNode("A");
        LeafNode bleafNode = new LeafNode("B");
        LeafNode cleafNode = new LeafNode("C");
        List<LeafNode> leafNodes = new ArrayList<>();
        leafNodes.add(AleafNode);
        leafNodes.add(bleafNode);
        leafNodes.add(cleafNode);
        SeqNode seq = new SeqNode(leafNodes);
        List<TreeNode> operatorLists = new ArrayList<>();
        operatorLists.add(seq);
        TreeRoot root = new TreeRoot(operatorLists);
        return root;
    }


    public TreeRoot petree2(){
        LeafNode AleafNode = new LeafNode("A");
        LeafNode bleafNode = new LeafNode("B");
        LeafNode cleafNode = new LeafNode("C");
        LeafNode dleafNode = new LeafNode("D");
        List<LeafNode> leafNodes = new ArrayList<>();
        leafNodes.add(AleafNode);
        leafNodes.add(bleafNode);
        leafNodes.add(cleafNode);
        leafNodes.add(dleafNode);
        SeqNode seq = new SeqNode(leafNodes);
        List<TreeNode> operatorLists = new ArrayList<>();
        operatorLists.add(seq);
        TreeRoot root = new TreeRoot(operatorLists);
        return root;
    }


    public TreeRoot petree3(){
        LeafNode AleafNode = new LeafNode("A");
        LeafNode bleafNode = new LeafNode("B");
        LeafNode cleafNode = new LeafNode("C");
        LeafNode dleafNode = new LeafNode("D");
        LeafNode eleafNode = new LeafNode("E");
        List<LeafNode> leafNodes = new ArrayList<>();
        leafNodes.add(AleafNode);
        leafNodes.add(bleafNode);
        leafNodes.add(cleafNode);
        leafNodes.add(dleafNode);
        leafNodes.add(eleafNode);
        SeqNode seq = new SeqNode(leafNodes);
        List<TreeNode> operatorLists = new ArrayList<>();
        operatorLists.add(seq);
        TreeRoot root = new TreeRoot(operatorLists);
        return root;
    }

    public TreeRoot petreen4(){
        LeafNode AleafNode = new LeafNode("A");
        LeafNode bleafNode = new LeafNode("B");
        LeafNode cleafNode = new LeafNode("C");
        LeafNode dleafNode = new LeafNode("D");
        LeafNode eleafNode = new LeafNode("E");
        LeafNode fleafNode = new LeafNode("F");
        List<LeafNode> leafNodes = new ArrayList<>();
        leafNodes.add(AleafNode);
        leafNodes.add(bleafNode);
        leafNodes.add(cleafNode);
        leafNodes.add(dleafNode);
        leafNodes.add(eleafNode);
        leafNodes.add(fleafNode);
        SeqNode seq = new SeqNode(leafNodes);
        List<TreeNode> operatorLists = new ArrayList<>();
        operatorLists.add(seq);
        TreeRoot root = new TreeRoot(operatorLists);
        return root;
    }


    public TreeRoot petree5(){
        LeafNode AleafNode = new LeafNode("A");
        LeafNode bleafNode = new LeafNode("B");
        LeafNode cleafNode = new LeafNode("C");
        List<LeafNode> leafNodes = new ArrayList<>();
        leafNodes.add(AleafNode);
        leafNodes.add(bleafNode);
        leafNodes.add(cleafNode);
        AndNode and = new AndNode(leafNodes);
        List<TreeNode> operatorLists = new ArrayList<>();
        operatorLists.add(and);
        TreeRoot root = new TreeRoot(operatorLists);
        return root;
    }


    public TreeRoot petree6(){
        LeafNode AleafNode = new LeafNode("A");
        LeafNode bleafNode = new LeafNode("B");
        LeafNode cleafNode = new LeafNode("C");
        LeafNode dleafNode = new LeafNode("D");
        List<LeafNode> leafNodes = new ArrayList<>();
        leafNodes.add(AleafNode);
        leafNodes.add(bleafNode);
        leafNodes.add(cleafNode);
        leafNodes.add(dleafNode);
        AndNode and = new AndNode(leafNodes);
        List<TreeNode> operatorLists = new ArrayList<>();
        operatorLists.add(and);
        TreeRoot root = new TreeRoot(operatorLists);
        return root;
    }


    public TreeRoot petree7(){
        LeafNode AleafNode = new LeafNode("A");
        LeafNode bleafNode = new LeafNode("B");
        LeafNode cleafNode = new LeafNode("C");
        LeafNode dleafNode = new LeafNode("D");
        LeafNode eleafNode = new LeafNode("E");
        List<LeafNode> leafNodes = new ArrayList<>();
        leafNodes.add(AleafNode);
        leafNodes.add(bleafNode);
        leafNodes.add(cleafNode);
        leafNodes.add(dleafNode);
        leafNodes.add(eleafNode);
        AndNode and = new AndNode(leafNodes);
        List<TreeNode> operatorLists = new ArrayList<>();
        operatorLists.add(and);
        TreeRoot root = new TreeRoot(operatorLists);
        return root;
    }

    public TreeRoot petreen8(){
        LeafNode AleafNode = new LeafNode("A");
        LeafNode bleafNode = new LeafNode("B");
        LeafNode cleafNode = new LeafNode("C");
        LeafNode dleafNode = new LeafNode("D");
        LeafNode eleafNode = new LeafNode("E");
        LeafNode fleafNode = new LeafNode("F");
        List<LeafNode> leafNodes = new ArrayList<>();
        leafNodes.add(AleafNode);
        leafNodes.add(bleafNode);
        leafNodes.add(cleafNode);
        leafNodes.add(dleafNode);
        leafNodes.add(eleafNode);
        leafNodes.add(fleafNode);
        AndNode and = new AndNode(leafNodes);
        List<TreeNode> operatorLists = new ArrayList<>();
        operatorLists.add(and);
        TreeRoot root = new TreeRoot(operatorLists);
        return root;
    }




    //fig9b   seq seq
    public TreeRoot petree9(){
        LeafNode AleafNode = new LeafNode("A");
        LeafNode bleafNode = new LeafNode("B");
        LeafNode cleafNode = new LeafNode("C");


        LeafNode dleafNode = new LeafNode("D");
        LeafNode eleafNode = new LeafNode("E");
        LeafNode fleafNode = new LeafNode("F");

        List<LeafNode> leafNodes1 = new ArrayList<>();
        leafNodes1.add(AleafNode);
        leafNodes1.add(bleafNode);
        leafNodes1.add(cleafNode);


        List<LeafNode> leafNodes2 = new ArrayList<>();
        leafNodes2.add(dleafNode);
        leafNodes2.add(eleafNode);
        leafNodes2.add(fleafNode);



        eqNode seqNode1 = new SeqNode(leafNodes1);
        seqNode1.setI(0);
        SeqNode seqNode2 = new SeqNode(leafNodes2);
        seqNode2.setI(1);

        List<TreeNode> operatorLists = new ArrayList<>();
        operatorLists.add(seqNode1);
        operatorLists.add(seqNode2);

        TreeRoot root = new TreeRoot(operatorLists);
        return root;
    }
  // seq and
    public TreeRoot petree10(){
        LeafNode AleafNode = new LeafNode("A");
        LeafNode bleafNode = new LeafNode("B");
        LeafNode cleafNode = new LeafNode("C");


        LeafNode dleafNode = new LeafNode("D");
        LeafNode eleafNode = new LeafNode("E");
        LeafNode fleafNode = new LeafNode("F");

        List<LeafNode> leafNodes1 = new ArrayList<>();
        leafNodes1.add(AleafNode);
        leafNodes1.add(bleafNode);
        leafNodes1.add(cleafNode);


        List<LeafNode> leafNodes2 = new ArrayList<>();
        leafNodes2.add(dleafNode);
        leafNodes2.add(eleafNode);
        leafNodes2.add(fleafNode);



        eqNode seqNode = new SeqNode(leafNodes1);
        seqNode.setI(0);
        AndNode andNode = new AndNode(leafNodes2);
        andNode.setI(1);

        List<TreeNode> operatorLists = new ArrayList<>();
        operatorLists.add(seqNode);
        operatorLists.add(andNode);

        TreeRoot root = new TreeRoot(operatorLists);
        return root;
    }

    //   seq nseq
    public TreeRoot petree11(){
        LeafNode AleafNode = new LeafNode("A");
        LeafNode bleafNode = new LeafNode("B");
        LeafNode cleafNode = new LeafNode("C");


        LeafNode dleafNode = new LeafNode("D");
        LeafNode eleafNode = new LeafNode("E");
        LeafNode fleafNode = new LeafNode("F");

        List<LeafNode> leafNodes1 = new ArrayList<>();
        leafNodes1.add(AleafNode);
        leafNodes1.add(bleafNode);
        leafNodes1.add(cleafNode);

        NSEQNode nseqNode = new NSEQNode(dleafNode,eleafNode,fleafNode);

        eqNode seqNode = new SeqNode(leafNodes1);
        seqNode.setI(0);
        nseqNode.setI(1);

        List<TreeNode> operatorLists = new ArrayList<>();
        operatorLists.add(seqNode);
        operatorLists.add(nseqNode);

        TreeRoot root = new TreeRoot(operatorLists);
        return root;
    }

    //fig 10c  nseq
    public TreeRoot petree12(){
        LeafNode AleafNode = new LeafNode("A");
        LeafNode bleafNode = new LeafNode("B");
        LeafNode cleafNode = new LeafNode("C");
        NSEQNode nseqNode = new NSEQNode(AleafNode,bleafNode,cleafNode);
        List<TreeNode> operatorLists = new ArrayList<>();
        operatorLists.add(nseqNode);
        TreeRoot root = new TreeRoot(operatorLists);
        return root;
    }


    //fig.10d    nseq seq
    public TreeRoot petree13(){
        LeafNode AleafNode = new LeafNode("A");
        LeafNode bleafNode = new LeafNode("B");
        LeafNode cleafNode = new LeafNode("C");


        LeafNode dleafNode = new LeafNode("D");
        LeafNode eleafNode = new LeafNode("E");
        LeafNode fleafNode = new LeafNode("F");

        List<LeafNode> leafNodes1 = new ArrayList<>();
        leafNodes1.add(AleafNode);
        leafNodes1.add(bleafNode);
        leafNodes1.add(cleafNode);

        NSEQNode nseqNode = new NSEQNode(dleafNode,eleafNode,fleafNode);

        SeqNode seqNode = new SeqNode(leafNodes1);
        nseqNode.setI(0);
        seqNode.setI(1);
        List<TreeNode> operatorLists = new ArrayList<>();
        operatorLists.add(nseqNode);
        operatorLists.add(seqNode);

        TreeRoot root = new TreeRoot(operatorLists);
        return root;
    }

    //fig.10d
    public TreeRoot petree14(){
        LeafNode AleafNode = new LeafNode("A");

        LeafNode bleafNode = new LeafNode("B");
        LeafNode cleafNode = new LeafNode("C");
        LeafNode dleafNode = new LeafNode("D");

        LeafNode eleafNode = new LeafNode("E");

        List<LeafNode> leafNodes1 = new ArrayList<>();
        leafNodes1.add(AleafNode);


        NSEQNode nseqNode = new NSEQNode(bleafNode,cleafNode,dleafNode);

        List<LeafNode> leafNodes2 = new ArrayList<>();
        leafNodes2.add(eleafNode);


        SeqNode seqNode1 = new SeqNode(leafNodes1);
        SeqNode seqNode2 = new SeqNode(leafNodes2);
        seqNode1.setI(0);
        nseqNode.setI(1);
        seqNode2.setI(2);
        List<TreeNode> operatorLists = new ArrayList<>();
        operatorLists.add(seqNode1);
        operatorLists.add(nseqNode);
        operatorLists.add(seqNode3);
        TreeRoot root = new TreeRoot(operatorLists);
        return root;
    }

    //   seq(ab) nseq
    public TreeRoot petree15(){
        LeafNode AleafNode = new LeafNode("A");
        LeafNode bleafNode = new LeafNode("B");

        LeafNode cleafNode = new LeafNode("C");
        LeafNode dleafNode = new LeafNode("D");
        LeafNode eleafNode = new LeafNode("E");


        List<LeafNode> leafNodes1 = new ArrayList<>();
        leafNodes1.add(AleafNode);
        leafNodes1.add(bleafNode);

        NSEQNode nseqNode = new NSEQNode(cleafNode,dleafNode,eleafNode);

        eqNode seqNode = new SeqNode(leafNodes1);
        seqNode.setI(0);
        nseqNode.setI(1);

        List<TreeNode> operatorLists = new ArrayList<>();
        operatorLists.add(seqNode);
        operatorLists.add(nseqNode);

        TreeRoot root = new TreeRoot(operatorLists);
        return root;
    }


    public TreeRoot petree16(){
        LeafNode AleafNode = new LeafNode("A");
        LeafNode bleafNode = new LeafNode("B");
        List<LeafNode> leafNodes = new ArrayList<>();
        leafNodes.add(AleafNode);
        leafNodes.add(bleafNode);
        AndNode and = new AndNode(leafNodes);
        List<TreeNode> operatorLists = new ArrayList<>();
        operatorLists.add(and);
        TreeRoot root = new TreeRoot(operatorLists);
        return root;
    }

}


