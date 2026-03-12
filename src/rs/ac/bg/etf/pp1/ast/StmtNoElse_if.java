// generated with ast extension for cup
// version 0.8
// 5/2/2026 1:45:55


package rs.ac.bg.etf.pp1.ast;

public class StmtNoElse_if extends SimpleStatement {

    private IfBegin IfBegin;
    private Condition Condition;
    private IfAfterCond IfAfterCond;
    private Statement Statement;
    private IfEnd IfEnd;
    private StatementElse StatementElse;

    public StmtNoElse_if (IfBegin IfBegin, Condition Condition, IfAfterCond IfAfterCond, Statement Statement, IfEnd IfEnd, StatementElse StatementElse) {
        this.IfBegin=IfBegin;
        if(IfBegin!=null) IfBegin.setParent(this);
        this.Condition=Condition;
        if(Condition!=null) Condition.setParent(this);
        this.IfAfterCond=IfAfterCond;
        if(IfAfterCond!=null) IfAfterCond.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.IfEnd=IfEnd;
        if(IfEnd!=null) IfEnd.setParent(this);
        this.StatementElse=StatementElse;
        if(StatementElse!=null) StatementElse.setParent(this);
    }

    public IfBegin getIfBegin() {
        return IfBegin;
    }

    public void setIfBegin(IfBegin IfBegin) {
        this.IfBegin=IfBegin;
    }

    public Condition getCondition() {
        return Condition;
    }

    public void setCondition(Condition Condition) {
        this.Condition=Condition;
    }

    public IfAfterCond getIfAfterCond() {
        return IfAfterCond;
    }

    public void setIfAfterCond(IfAfterCond IfAfterCond) {
        this.IfAfterCond=IfAfterCond;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public IfEnd getIfEnd() {
        return IfEnd;
    }

    public void setIfEnd(IfEnd IfEnd) {
        this.IfEnd=IfEnd;
    }

    public StatementElse getStatementElse() {
        return StatementElse;
    }

    public void setStatementElse(StatementElse StatementElse) {
        this.StatementElse=StatementElse;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(IfBegin!=null) IfBegin.accept(visitor);
        if(Condition!=null) Condition.accept(visitor);
        if(IfAfterCond!=null) IfAfterCond.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(IfEnd!=null) IfEnd.accept(visitor);
        if(StatementElse!=null) StatementElse.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(IfBegin!=null) IfBegin.traverseTopDown(visitor);
        if(Condition!=null) Condition.traverseTopDown(visitor);
        if(IfAfterCond!=null) IfAfterCond.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(IfEnd!=null) IfEnd.traverseTopDown(visitor);
        if(StatementElse!=null) StatementElse.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(IfBegin!=null) IfBegin.traverseBottomUp(visitor);
        if(Condition!=null) Condition.traverseBottomUp(visitor);
        if(IfAfterCond!=null) IfAfterCond.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(IfEnd!=null) IfEnd.traverseBottomUp(visitor);
        if(StatementElse!=null) StatementElse.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StmtNoElse_if(\n");

        if(IfBegin!=null)
            buffer.append(IfBegin.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Condition!=null)
            buffer.append(Condition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(IfAfterCond!=null)
            buffer.append(IfAfterCond.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(IfEnd!=null)
            buffer.append(IfEnd.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(StatementElse!=null)
            buffer.append(StatementElse.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StmtNoElse_if]");
        return buffer.toString();
    }
}
