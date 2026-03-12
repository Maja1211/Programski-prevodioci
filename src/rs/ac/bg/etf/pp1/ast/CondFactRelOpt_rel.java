// generated with ast extension for cup
// version 0.8
// 5/2/2026 1:45:55


package rs.ac.bg.etf.pp1.ast;

public class CondFactRelOpt_rel extends CondFactRelOpt {

    private Relop Relop;
    private ExprBasic ExprBasic;

    public CondFactRelOpt_rel (Relop Relop, ExprBasic ExprBasic) {
        this.Relop=Relop;
        if(Relop!=null) Relop.setParent(this);
        this.ExprBasic=ExprBasic;
        if(ExprBasic!=null) ExprBasic.setParent(this);
    }

    public Relop getRelop() {
        return Relop;
    }

    public void setRelop(Relop Relop) {
        this.Relop=Relop;
    }

    public ExprBasic getExprBasic() {
        return ExprBasic;
    }

    public void setExprBasic(ExprBasic ExprBasic) {
        this.ExprBasic=ExprBasic;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Relop!=null) Relop.accept(visitor);
        if(ExprBasic!=null) ExprBasic.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Relop!=null) Relop.traverseTopDown(visitor);
        if(ExprBasic!=null) ExprBasic.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Relop!=null) Relop.traverseBottomUp(visitor);
        if(ExprBasic!=null) ExprBasic.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CondFactRelOpt_rel(\n");

        if(Relop!=null)
            buffer.append(Relop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ExprBasic!=null)
            buffer.append(ExprBasic.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CondFactRelOpt_rel]");
        return buffer.toString();
    }
}
