// generated with ast extension for cup
// version 0.8
// 5/2/2026 1:45:55


package rs.ac.bg.etf.pp1.ast;

public class ConVarEnumDecList_enum extends ConVarEnumDecList {

    private ConVarEnumDecList ConVarEnumDecList;
    private EnumDecl EnumDecl;

    public ConVarEnumDecList_enum (ConVarEnumDecList ConVarEnumDecList, EnumDecl EnumDecl) {
        this.ConVarEnumDecList=ConVarEnumDecList;
        if(ConVarEnumDecList!=null) ConVarEnumDecList.setParent(this);
        this.EnumDecl=EnumDecl;
        if(EnumDecl!=null) EnumDecl.setParent(this);
    }

    public ConVarEnumDecList getConVarEnumDecList() {
        return ConVarEnumDecList;
    }

    public void setConVarEnumDecList(ConVarEnumDecList ConVarEnumDecList) {
        this.ConVarEnumDecList=ConVarEnumDecList;
    }

    public EnumDecl getEnumDecl() {
        return EnumDecl;
    }

    public void setEnumDecl(EnumDecl EnumDecl) {
        this.EnumDecl=EnumDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConVarEnumDecList!=null) ConVarEnumDecList.accept(visitor);
        if(EnumDecl!=null) EnumDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConVarEnumDecList!=null) ConVarEnumDecList.traverseTopDown(visitor);
        if(EnumDecl!=null) EnumDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConVarEnumDecList!=null) ConVarEnumDecList.traverseBottomUp(visitor);
        if(EnumDecl!=null) EnumDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConVarEnumDecList_enum(\n");

        if(ConVarEnumDecList!=null)
            buffer.append(ConVarEnumDecList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(EnumDecl!=null)
            buffer.append(EnumDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConVarEnumDecList_enum]");
        return buffer.toString();
    }
}
