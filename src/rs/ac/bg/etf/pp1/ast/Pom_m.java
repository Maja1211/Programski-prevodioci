// generated with ast extension for cup
// version 0.8
// 5/2/2026 1:45:56


package rs.ac.bg.etf.pp1.ast;

public class Pom_m extends Pom {

    public Pom_m () {
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
        buffer.append("Pom_m(\n");

        buffer.append(tab);
        buffer.append(") [Pom_m]");
        return buffer.toString();
    }
}
