// generated with ast extension for cup
// version 0.8
// 5/2/2026 1:45:55


package rs.ac.bg.etf.pp1.ast;

public class Stmt_simple extends Statement {

    private SimpleStatement SimpleStatement;

    public Stmt_simple (SimpleStatement SimpleStatement) {
        this.SimpleStatement=SimpleStatement;
        if(SimpleStatement!=null) SimpleStatement.setParent(this);
    }

    public SimpleStatement getSimpleStatement() {
        return SimpleStatement;
    }

    public void setSimpleStatement(SimpleStatement SimpleStatement) {
        this.SimpleStatement=SimpleStatement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(SimpleStatement!=null) SimpleStatement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(SimpleStatement!=null) SimpleStatement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(SimpleStatement!=null) SimpleStatement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Stmt_simple(\n");

        if(SimpleStatement!=null)
            buffer.append(SimpleStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Stmt_simple]");
        return buffer.toString();
    }
}
