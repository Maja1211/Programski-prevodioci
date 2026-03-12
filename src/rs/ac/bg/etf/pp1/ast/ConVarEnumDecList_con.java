// generated with ast extension for cup
// version 0.8
// 5/2/2026 1:45:55


package rs.ac.bg.etf.pp1.ast;

public class ConVarEnumDecList_con extends ConVarEnumDecList {

    private ConVarEnumDecList ConVarEnumDecList;
    private ConDecList ConDecList;

    public ConVarEnumDecList_con (ConVarEnumDecList ConVarEnumDecList, ConDecList ConDecList) {
        this.ConVarEnumDecList=ConVarEnumDecList;
        if(ConVarEnumDecList!=null) ConVarEnumDecList.setParent(this);
        this.ConDecList=ConDecList;
        if(ConDecList!=null) ConDecList.setParent(this);
    }

    public ConVarEnumDecList getConVarEnumDecList() {
        return ConVarEnumDecList;
    }

    public void setConVarEnumDecList(ConVarEnumDecList ConVarEnumDecList) {
        this.ConVarEnumDecList=ConVarEnumDecList;
    }

    public ConDecList getConDecList() {
        return ConDecList;
    }

    public void setConDecList(ConDecList ConDecList) {
        this.ConDecList=ConDecList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConVarEnumDecList!=null) ConVarEnumDecList.accept(visitor);
        if(ConDecList!=null) ConDecList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConVarEnumDecList!=null) ConVarEnumDecList.traverseTopDown(visitor);
        if(ConDecList!=null) ConDecList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConVarEnumDecList!=null) ConVarEnumDecList.traverseBottomUp(visitor);
        if(ConDecList!=null) ConDecList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConVarEnumDecList_con(\n");

        if(ConVarEnumDecList!=null)
            buffer.append(ConVarEnumDecList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConDecList!=null)
            buffer.append(ConDecList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConVarEnumDecList_con]");
        return buffer.toString();
    }
}
