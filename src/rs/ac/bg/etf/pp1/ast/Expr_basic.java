// generated with ast extension for cup
// version 0.8
// 5/2/2026 1:45:55


package rs.ac.bg.etf.pp1.ast;

public class Expr_basic extends Expr {

    private ExprBasic ExprBasic;

    public Expr_basic (ExprBasic ExprBasic) {
        this.ExprBasic=ExprBasic;
        if(ExprBasic!=null) ExprBasic.setParent(this);
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
        if(ExprBasic!=null) ExprBasic.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ExprBasic!=null) ExprBasic.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ExprBasic!=null) ExprBasic.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Expr_basic(\n");

        if(ExprBasic!=null)
            buffer.append(ExprBasic.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Expr_basic]");
        return buffer.toString();
    }
}
