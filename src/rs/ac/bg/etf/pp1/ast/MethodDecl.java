// generated with ast extension for cup
// version 0.8
// 5/2/2026 1:45:55


package rs.ac.bg.etf.pp1.ast;

public class MethodDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private MethRetAndName MethRetAndName;
    private FormParList FormParList;
    private VarDeclListPom VarDeclListPom;
    private StatementList StatementList;

    public MethodDecl (MethRetAndName MethRetAndName, FormParList FormParList, VarDeclListPom VarDeclListPom, StatementList StatementList) {
        this.MethRetAndName=MethRetAndName;
        if(MethRetAndName!=null) MethRetAndName.setParent(this);
        this.FormParList=FormParList;
        if(FormParList!=null) FormParList.setParent(this);
        this.VarDeclListPom=VarDeclListPom;
        if(VarDeclListPom!=null) VarDeclListPom.setParent(this);
        this.StatementList=StatementList;
        if(StatementList!=null) StatementList.setParent(this);
    }

    public MethRetAndName getMethRetAndName() {
        return MethRetAndName;
    }

    public void setMethRetAndName(MethRetAndName MethRetAndName) {
        this.MethRetAndName=MethRetAndName;
    }

    public FormParList getFormParList() {
        return FormParList;
    }

    public void setFormParList(FormParList FormParList) {
        this.FormParList=FormParList;
    }

    public VarDeclListPom getVarDeclListPom() {
        return VarDeclListPom;
    }

    public void setVarDeclListPom(VarDeclListPom VarDeclListPom) {
        this.VarDeclListPom=VarDeclListPom;
    }

    public StatementList getStatementList() {
        return StatementList;
    }

    public void setStatementList(StatementList StatementList) {
        this.StatementList=StatementList;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MethRetAndName!=null) MethRetAndName.accept(visitor);
        if(FormParList!=null) FormParList.accept(visitor);
        if(VarDeclListPom!=null) VarDeclListPom.accept(visitor);
        if(StatementList!=null) StatementList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethRetAndName!=null) MethRetAndName.traverseTopDown(visitor);
        if(FormParList!=null) FormParList.traverseTopDown(visitor);
        if(VarDeclListPom!=null) VarDeclListPom.traverseTopDown(visitor);
        if(StatementList!=null) StatementList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethRetAndName!=null) MethRetAndName.traverseBottomUp(visitor);
        if(FormParList!=null) FormParList.traverseBottomUp(visitor);
        if(VarDeclListPom!=null) VarDeclListPom.traverseBottomUp(visitor);
        if(StatementList!=null) StatementList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodDecl(\n");

        if(MethRetAndName!=null)
            buffer.append(MethRetAndName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FormParList!=null)
            buffer.append(FormParList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclListPom!=null)
            buffer.append(VarDeclListPom.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(StatementList!=null)
            buffer.append(StatementList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodDecl]");
        return buffer.toString();
    }
}
