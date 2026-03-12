// generated with ast extension for cup
// version 0.8
// 5/2/2026 1:45:55


package rs.ac.bg.etf.pp1.ast;

public class Stmt_switch extends SimpleStatement {

    private Expr Expr;
    private SwitchBegin SwitchBegin;
    private CaseList CaseList;
    private SwitchEnd SwitchEnd;

    public Stmt_switch (Expr Expr, SwitchBegin SwitchBegin, CaseList CaseList, SwitchEnd SwitchEnd) {
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.SwitchBegin=SwitchBegin;
        if(SwitchBegin!=null) SwitchBegin.setParent(this);
        this.CaseList=CaseList;
        if(CaseList!=null) CaseList.setParent(this);
        this.SwitchEnd=SwitchEnd;
        if(SwitchEnd!=null) SwitchEnd.setParent(this);
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public SwitchBegin getSwitchBegin() {
        return SwitchBegin;
    }

    public void setSwitchBegin(SwitchBegin SwitchBegin) {
        this.SwitchBegin=SwitchBegin;
    }

    public CaseList getCaseList() {
        return CaseList;
    }

    public void setCaseList(CaseList CaseList) {
        this.CaseList=CaseList;
    }

    public SwitchEnd getSwitchEnd() {
        return SwitchEnd;
    }

    public void setSwitchEnd(SwitchEnd SwitchEnd) {
        this.SwitchEnd=SwitchEnd;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Expr!=null) Expr.accept(visitor);
        if(SwitchBegin!=null) SwitchBegin.accept(visitor);
        if(CaseList!=null) CaseList.accept(visitor);
        if(SwitchEnd!=null) SwitchEnd.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(SwitchBegin!=null) SwitchBegin.traverseTopDown(visitor);
        if(CaseList!=null) CaseList.traverseTopDown(visitor);
        if(SwitchEnd!=null) SwitchEnd.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(SwitchBegin!=null) SwitchBegin.traverseBottomUp(visitor);
        if(CaseList!=null) CaseList.traverseBottomUp(visitor);
        if(SwitchEnd!=null) SwitchEnd.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Stmt_switch(\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(SwitchBegin!=null)
            buffer.append(SwitchBegin.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CaseList!=null)
            buffer.append(CaseList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(SwitchEnd!=null)
            buffer.append(SwitchEnd.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Stmt_switch]");
        return buffer.toString();
    }
}
