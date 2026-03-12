// generated with ast extension for cup
// version 0.8
// 5/2/2026 1:45:55


package rs.ac.bg.etf.pp1.ast;

public class CaseList_pom extends CaseList {

    private CaseList CaseList;
    private CaseBlock CaseBlock;

    public CaseList_pom (CaseList CaseList, CaseBlock CaseBlock) {
        this.CaseList=CaseList;
        if(CaseList!=null) CaseList.setParent(this);
        this.CaseBlock=CaseBlock;
        if(CaseBlock!=null) CaseBlock.setParent(this);
    }

    public CaseList getCaseList() {
        return CaseList;
    }

    public void setCaseList(CaseList CaseList) {
        this.CaseList=CaseList;
    }

    public CaseBlock getCaseBlock() {
        return CaseBlock;
    }

    public void setCaseBlock(CaseBlock CaseBlock) {
        this.CaseBlock=CaseBlock;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(CaseList!=null) CaseList.accept(visitor);
        if(CaseBlock!=null) CaseBlock.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(CaseList!=null) CaseList.traverseTopDown(visitor);
        if(CaseBlock!=null) CaseBlock.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(CaseList!=null) CaseList.traverseBottomUp(visitor);
        if(CaseBlock!=null) CaseBlock.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CaseList_pom(\n");

        if(CaseList!=null)
            buffer.append(CaseList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CaseBlock!=null)
            buffer.append(CaseBlock.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CaseList_pom]");
        return buffer.toString();
    }
}
