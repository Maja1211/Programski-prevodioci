// generated with ast extension for cup
// version 0.8
// 5/2/2026 1:45:55


package rs.ac.bg.etf.pp1.ast;

public class Stmt_for extends SimpleStatement {

    private ForInit ForInit;
    private ForCondBegin ForCondBegin;
    private ForCond ForCond;
    private ForStep ForStep;
    private ForBegin ForBegin;
    private Statement Statement;
    private ForEnd ForEnd;

    public Stmt_for (ForInit ForInit, ForCondBegin ForCondBegin, ForCond ForCond, ForStep ForStep, ForBegin ForBegin, Statement Statement, ForEnd ForEnd) {
        this.ForInit=ForInit;
        if(ForInit!=null) ForInit.setParent(this);
        this.ForCondBegin=ForCondBegin;
        if(ForCondBegin!=null) ForCondBegin.setParent(this);
        this.ForCond=ForCond;
        if(ForCond!=null) ForCond.setParent(this);
        this.ForStep=ForStep;
        if(ForStep!=null) ForStep.setParent(this);
        this.ForBegin=ForBegin;
        if(ForBegin!=null) ForBegin.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.ForEnd=ForEnd;
        if(ForEnd!=null) ForEnd.setParent(this);
    }

    public ForInit getForInit() {
        return ForInit;
    }

    public void setForInit(ForInit ForInit) {
        this.ForInit=ForInit;
    }

    public ForCondBegin getForCondBegin() {
        return ForCondBegin;
    }

    public void setForCondBegin(ForCondBegin ForCondBegin) {
        this.ForCondBegin=ForCondBegin;
    }

    public ForCond getForCond() {
        return ForCond;
    }

    public void setForCond(ForCond ForCond) {
        this.ForCond=ForCond;
    }

    public ForStep getForStep() {
        return ForStep;
    }

    public void setForStep(ForStep ForStep) {
        this.ForStep=ForStep;
    }

    public ForBegin getForBegin() {
        return ForBegin;
    }

    public void setForBegin(ForBegin ForBegin) {
        this.ForBegin=ForBegin;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public ForEnd getForEnd() {
        return ForEnd;
    }

    public void setForEnd(ForEnd ForEnd) {
        this.ForEnd=ForEnd;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ForInit!=null) ForInit.accept(visitor);
        if(ForCondBegin!=null) ForCondBegin.accept(visitor);
        if(ForCond!=null) ForCond.accept(visitor);
        if(ForStep!=null) ForStep.accept(visitor);
        if(ForBegin!=null) ForBegin.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(ForEnd!=null) ForEnd.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ForInit!=null) ForInit.traverseTopDown(visitor);
        if(ForCondBegin!=null) ForCondBegin.traverseTopDown(visitor);
        if(ForCond!=null) ForCond.traverseTopDown(visitor);
        if(ForStep!=null) ForStep.traverseTopDown(visitor);
        if(ForBegin!=null) ForBegin.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(ForEnd!=null) ForEnd.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ForInit!=null) ForInit.traverseBottomUp(visitor);
        if(ForCondBegin!=null) ForCondBegin.traverseBottomUp(visitor);
        if(ForCond!=null) ForCond.traverseBottomUp(visitor);
        if(ForStep!=null) ForStep.traverseBottomUp(visitor);
        if(ForBegin!=null) ForBegin.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(ForEnd!=null) ForEnd.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Stmt_for(\n");

        if(ForInit!=null)
            buffer.append(ForInit.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForCondBegin!=null)
            buffer.append(ForCondBegin.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForCond!=null)
            buffer.append(ForCond.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForStep!=null)
            buffer.append(ForStep.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForBegin!=null)
            buffer.append(ForBegin.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForEnd!=null)
            buffer.append(ForEnd.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Stmt_for]");
        return buffer.toString();
    }
}
