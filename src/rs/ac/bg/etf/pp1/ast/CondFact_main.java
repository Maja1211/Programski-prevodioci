// generated with ast extension for cup
// version 0.8
// 5/2/2026 1:45:55


package rs.ac.bg.etf.pp1.ast;

public class CondFact_main extends CondFact {

    private ExprBasic ExprBasic;
    private CondFactRelOpt CondFactRelOpt;

    public CondFact_main (ExprBasic ExprBasic, CondFactRelOpt CondFactRelOpt) {
        this.ExprBasic=ExprBasic;
        if(ExprBasic!=null) ExprBasic.setParent(this);
        this.CondFactRelOpt=CondFactRelOpt;
        if(CondFactRelOpt!=null) CondFactRelOpt.setParent(this);
    }

    public ExprBasic getExprBasic() {
        return ExprBasic;
    }

    public void setExprBasic(ExprBasic ExprBasic) {
        this.ExprBasic=ExprBasic;
    }

    public CondFactRelOpt getCondFactRelOpt() {
        return CondFactRelOpt;
    }

    public void setCondFactRelOpt(CondFactRelOpt CondFactRelOpt) {
        this.CondFactRelOpt=CondFactRelOpt;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ExprBasic!=null) ExprBasic.accept(visitor);
        if(CondFactRelOpt!=null) CondFactRelOpt.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ExprBasic!=null) ExprBasic.traverseTopDown(visitor);
        if(CondFactRelOpt!=null) CondFactRelOpt.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ExprBasic!=null) ExprBasic.traverseBottomUp(visitor);
        if(CondFactRelOpt!=null) CondFactRelOpt.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CondFact_main(\n");

        if(ExprBasic!=null)
            buffer.append(ExprBasic.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CondFactRelOpt!=null)
            buffer.append(CondFactRelOpt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CondFact_main]");
        return buffer.toString();
    }
}
