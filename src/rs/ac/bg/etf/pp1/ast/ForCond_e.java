// generated with ast extension for cup
// version 0.8
// 5/2/2026 1:45:55


package rs.ac.bg.etf.pp1.ast;

public class ForCond_e extends ForCond {

    public ForCond_e () {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ForCond_e(\n");

        buffer.append(tab);
        buffer.append(") [ForCond_e]");
        return buffer.toString();
    }
}
