// generated with ast extension for cup
// version 0.8
// 5/2/2026 1:45:56


package rs.ac.bg.etf.pp1.ast;

public class DesignatorMore_pom_elem extends DesignatorMore {

    private DesignatorMore DesignatorMore;
    private DesignatorPomArrayName DesignatorPomArrayName;
    private Expr Expr;

    public DesignatorMore_pom_elem (DesignatorMore DesignatorMore, DesignatorPomArrayName DesignatorPomArrayName, Expr Expr) {
        this.DesignatorMore=DesignatorMore;
        if(DesignatorMore!=null) DesignatorMore.setParent(this);
        this.DesignatorPomArrayName=DesignatorPomArrayName;
        if(DesignatorPomArrayName!=null) DesignatorPomArrayName.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
    }

    public DesignatorMore getDesignatorMore() {
        return DesignatorMore;
    }

    public void setDesignatorMore(DesignatorMore DesignatorMore) {
        this.DesignatorMore=DesignatorMore;
    }

    public DesignatorPomArrayName getDesignatorPomArrayName() {
        return DesignatorPomArrayName;
    }

    public void setDesignatorPomArrayName(DesignatorPomArrayName DesignatorPomArrayName) {
        this.DesignatorPomArrayName=DesignatorPomArrayName;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorMore!=null) DesignatorMore.accept(visitor);
        if(DesignatorPomArrayName!=null) DesignatorPomArrayName.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorMore!=null) DesignatorMore.traverseTopDown(visitor);
        if(DesignatorPomArrayName!=null) DesignatorPomArrayName.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorMore!=null) DesignatorMore.traverseBottomUp(visitor);
        if(DesignatorPomArrayName!=null) DesignatorPomArrayName.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorMore_pom_elem(\n");

        if(DesignatorMore!=null)
            buffer.append(DesignatorMore.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorPomArrayName!=null)
            buffer.append(DesignatorPomArrayName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorMore_pom_elem]");
        return buffer.toString();
    }
}
