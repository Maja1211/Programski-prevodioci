// generated with ast extension for cup
// version 0.8
// 5/2/2026 1:45:55


package rs.ac.bg.etf.pp1.ast;

public class ConVarEnumDecList_var extends ConVarEnumDecList {

    private ConVarEnumDecList ConVarEnumDecList;
    private VarDeclList VarDeclList;

    public ConVarEnumDecList_var (ConVarEnumDecList ConVarEnumDecList, VarDeclList VarDeclList) {
        this.ConVarEnumDecList=ConVarEnumDecList;
        if(ConVarEnumDecList!=null) ConVarEnumDecList.setParent(this);
        this.VarDeclList=VarDeclList;
        if(VarDeclList!=null) VarDeclList.setParent(this);
    }

    public ConVarEnumDecList getConVarEnumDecList() {
        return ConVarEnumDecList;
    }

    public void setConVarEnumDecList(ConVarEnumDecList ConVarEnumDecList) {
        this.ConVarEnumDecList=ConVarEnumDecList;
    }

    public VarDeclList getVarDeclList() {
        return VarDeclList;
    }

    public void setVarDeclList(VarDeclList VarDeclList) {
        this.VarDeclList=VarDeclList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConVarEnumDecList!=null) ConVarEnumDecList.accept(visitor);
        if(VarDeclList!=null) VarDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConVarEnumDecList!=null) ConVarEnumDecList.traverseTopDown(visitor);
        if(VarDeclList!=null) VarDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConVarEnumDecList!=null) ConVarEnumDecList.traverseBottomUp(visitor);
        if(VarDeclList!=null) VarDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConVarEnumDecList_var(\n");

        if(ConVarEnumDecList!=null)
            buffer.append(ConVarEnumDecList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclList!=null)
            buffer.append(VarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConVarEnumDecList_var]");
        return buffer.toString();
    }
}
