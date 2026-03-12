// generated with ast extension for cup
// version 0.8
// 5/2/2026 1:45:56


package rs.ac.bg.etf.pp1.ast;

public class Factor_design extends FactorSub {

    private Designator Designator;
    private FactorOptActPars FactorOptActPars;

    public Factor_design (Designator Designator, FactorOptActPars FactorOptActPars) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.FactorOptActPars=FactorOptActPars;
        if(FactorOptActPars!=null) FactorOptActPars.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public FactorOptActPars getFactorOptActPars() {
        return FactorOptActPars;
    }

    public void setFactorOptActPars(FactorOptActPars FactorOptActPars) {
        this.FactorOptActPars=FactorOptActPars;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(FactorOptActPars!=null) FactorOptActPars.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(FactorOptActPars!=null) FactorOptActPars.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(FactorOptActPars!=null) FactorOptActPars.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Factor_design(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FactorOptActPars!=null)
            buffer.append(FactorOptActPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Factor_design]");
        return buffer.toString();
    }
}
