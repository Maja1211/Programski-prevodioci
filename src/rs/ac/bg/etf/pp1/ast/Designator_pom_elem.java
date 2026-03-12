// generated with ast extension for cup
// version 0.8
// 5/2/2026 1:45:56


package rs.ac.bg.etf.pp1.ast;

public class Designator_pom_elem extends Designator {

    private DesignatorPom DesignatorPom;
    private DesignatorMore DesignatorMore;

    public Designator_pom_elem (DesignatorPom DesignatorPom, DesignatorMore DesignatorMore) {
        this.DesignatorPom=DesignatorPom;
        if(DesignatorPom!=null) DesignatorPom.setParent(this);
        this.DesignatorMore=DesignatorMore;
        if(DesignatorMore!=null) DesignatorMore.setParent(this);
    }

    public DesignatorPom getDesignatorPom() {
        return DesignatorPom;
    }

    public void setDesignatorPom(DesignatorPom DesignatorPom) {
        this.DesignatorPom=DesignatorPom;
    }

    public DesignatorMore getDesignatorMore() {
        return DesignatorMore;
    }

    public void setDesignatorMore(DesignatorMore DesignatorMore) {
        this.DesignatorMore=DesignatorMore;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorPom!=null) DesignatorPom.accept(visitor);
        if(DesignatorMore!=null) DesignatorMore.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorPom!=null) DesignatorPom.traverseTopDown(visitor);
        if(DesignatorMore!=null) DesignatorMore.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorPom!=null) DesignatorPom.traverseBottomUp(visitor);
        if(DesignatorMore!=null) DesignatorMore.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Designator_pom_elem(\n");

        if(DesignatorPom!=null)
            buffer.append(DesignatorPom.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorMore!=null)
            buffer.append(DesignatorMore.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Designator_pom_elem]");
        return buffer.toString();
    }
}
