package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor {

    private int mainPC = 0;
    public int getMainPc() { return mainPC; }

    public CodeGenerator() {
        inicijalizujUgradjeneMetode();
    }

    private void inicijalizujUgradjeneMetode() {
        Obj ord = Tab.find("ord");
        Obj chr = Tab.find("chr");

        if (ord != Tab.noObj) ord.setAdr(Code.pc);
        if (chr != Tab.noObj) chr.setAdr(Code.pc);

        Code.put(Code.enter);
        Code.put(1);
        Code.put(1);
        Code.put(Code.load_n);
        Code.put(Code.exit);
        Code.put(Code.return_);

        Obj len = Tab.find("len");
        if (len != Tab.noObj) len.setAdr(Code.pc);

        Code.put(Code.enter);
        Code.put(1);
        Code.put(1);
        Code.put(Code.load_n);
        Code.put(Code.arraylength);
        Code.put(Code.exit);
        Code.put(Code.return_);
    }
    
    private boolean termJeNaVrhu(CondTerm n) {
        SyntaxNode p = n.getParent();
        return (p instanceof Condition_ct) || (p instanceof Condition_or);
    }
    
    private boolean returnFound = false;

    private boolean jeChar(Struct t) { return t == Tab.charType; }
    /*private boolean jeChar(Struct t) { 
    return t != null && t.getKind() == Struct.Char; 
}*/
    
    private boolean generisemForKorak = false;

    private boolean jeUnutarForStep(SyntaxNode n) {
        for (SyntaxNode p = n; p != null; p = p.getParent()) {
            if (p instanceof ForStep_stmt) return true;
        }
        return false;
    }


    private void ucitajKonstantu(int v) { Code.loadConst(v); }
    private void ucitajVrednost(Obj o) { Code.load(o); }
    private void upisiVrednost(Obj o) { Code.store(o); }

    private void pozoviMetodu(Obj m) {
        int pomeraj = m.getAdr() - Code.pc;
        Code.put(Code.call);
        Code.put2(pomeraj);
    }
    
    private Obj currentMethod = null;

    @Override
    public void visit(MethRetAndName_void n) {
        Obj metoda = n.obj;
        if (metoda == null || metoda == Tab.noObj) return;

        currentMethod = metoda;
        returnFound = false;

        metoda.setAdr(Code.pc);
        if ("main".equalsIgnoreCase(metoda.getName())) mainPC = Code.pc;

        int brojParam = metoda.getLevel();
        int brojVar = 0;
        for (Obj o : metoda.getLocalSymbols()) if (o.getKind() == Obj.Var) brojVar++;

        Code.put(Code.enter);
        Code.put(brojParam);
        Code.put(brojVar);
    }

    @Override
    public void visit(MethRetAndName_type n) {
        Obj metoda = n.obj;
        if (metoda == null || metoda == Tab.noObj) return;

        currentMethod = metoda;
        returnFound = false;

        metoda.setAdr(Code.pc);

        int brojParam = metoda.getLevel();
        int brojVar = 0;
        for (Obj o : metoda.getLocalSymbols()) if (o.getKind() == Obj.Var) brojVar++;

        Code.put(Code.enter);
        Code.put(brojParam);
        Code.put(brojVar);
    }

    @Override
    public void visit(MethodDecl n) {
        if (currentMethod != null && currentMethod != Tab.noObj) {
            if (currentMethod.getType() != Tab.noType) {
                Code.put(Code.trap);
                Code.put(1);
            } else {
                Code.put(Code.exit);
                Code.put(Code.return_);
            }
        }

        currentMethod = null;
        returnFound = false;
    }
    @Override public void visit(Factor_num n)  { ucitajKonstantu(n.getN1()); }
    @Override public void visit(Factor_char n) { ucitajKonstantu(n.getC1()); }
    @Override public void visit(Factor_bool n) { ucitajKonstantu(n.getB1()); }

    @Override
    public void visit(Factor_new_arr n) {
        Code.put(Code.newarray);
        Struct tip = (n.getType() != null) ? n.getType().struct : Tab.noType;
        if (tip == Tab.charType) Code.put(0);
        else Code.put(1);
    }

    @Override
    public void visit(DesignatorArrayName n) {
        Obj niz = n.obj;
        if (niz != null && niz != Tab.noObj) Code.load(niz); 
    }

    @Override
    public void visit(Designator_elem n) {
    }

    @Override
    public void visit(DesignatorName n) {
        Obj baza = n.obj;
        if (baza == null || baza == Tab.noObj) return;
        if (baza.getKind() == Obj.Type) return;
        if (baza.getKind() == Obj.Var && baza.getType() != null && baza.getType().getKind() == Struct.Array) {
            Code.load(baza); 
        }
    }

    @Override
    public void visit(DesignatorMore_length n) {
        Code.put(Code.arraylength);
    }

    private boolean krajJeLength(DesignatorMore more) {
        if (more == null) return false;
        if (more instanceof DesignatorMore_length) return true;
        if (more instanceof DesignatorMore_pom_length) {
            return krajJeLength(((DesignatorMore_pom_length) more).getDesignatorMore());
        }
        return false;
    }

    private boolean designatorJeLength(Designator d) {
        if (d instanceof Designator_pom) {
            return krajJeLength(((Designator_pom) d).getDesignatorMore());
        }
        if (d instanceof Designator_pom_elem) {
            return krajJeLength(((Designator_pom_elem) d).getDesignatorMore());
        }
        return false;
    }

    @Override
    public void visit(Factor_design n) {
        Obj d = n.getDesignator().obj;
        if (d == null || d == Tab.noObj) return;
        if (n.getFactorOptActPars() instanceof FactorOpt_e && designatorJeLength(n.getDesignator())) {
            return;
        }

        if (n.getFactorOptActPars() instanceof FactorOpt_e) {
            ucitajVrednost(d);
            return;
        }
        if (d.getKind() == Obj.Meth) {
            pozoviMetodu(d);
        }
    }

    @Override
    public void visit(AddopTermList_add n) {
        if (n.getAddop() instanceof Addop_plus) Code.put(Code.add);
        else Code.put(Code.sub);
    }

    @Override
    public void visit(MulopFactorList_mul n) {
        if (n.getMulop() instanceof Mulop_mul) Code.put(Code.mul);
        else if (n.getMulop() instanceof Mulop_div) Code.put(Code.div);
        else Code.put(Code.rem);
    }

    @Override
    public void visit(Factor n) {
        if (n.getPom() instanceof Pom_m) Code.put(Code.neg);
    }
    
    @Override
    public void visit(DesStmt_assign n) {
        if (jeUnutarForStep(n) && !generisemForKorak) return;

        Obj levo = n.getDesignator().obj;
        if (levo == null || levo == Tab.noObj) return;
        upisiVrednost(levo);
    }

    @Override
    public void visit(DesStmt_inc n) {
        if (jeUnutarForStep(n) && !generisemForKorak) return;

        Obj d = n.getDesignator().obj;
        if (d == null || d == Tab.noObj) return;
        if (d.getKind() == Obj.Elem) Code.put(Code.dup2);
        ucitajVrednost(d);
        ucitajKonstantu(1);
        Code.put(Code.add);
        upisiVrednost(d);
    }

    @Override
    public void visit(DesStmt_dec n) {
        if (jeUnutarForStep(n) && !generisemForKorak) return;

        Obj d = n.getDesignator().obj;
        if (d == null || d == Tab.noObj) return;

        if (d.getKind() == Obj.Elem) Code.put(Code.dup2);

        ucitajVrednost(d);
        ucitajKonstantu(1);
        Code.put(Code.sub);
        upisiVrednost(d);
    }

    @Override
    public void visit(DesStmt_call_noargs n) {
        if (jeUnutarForStep(n) && !generisemForKorak) return;

        Obj m = n.getDesignator().obj;
        if (m != null && m != Tab.noObj && m.getKind() == Obj.Meth) {
            pozoviMetodu(m);
            if (m.getType() != Tab.noType) Code.put(Code.pop);
        }
    }

    @Override
    public void visit(DesStmt_call_args n) {
        if (jeUnutarForStep(n) && !generisemForKorak) return;

        Obj m = n.getDesignator().obj;
        if (m != null && m != Tab.noObj && m.getKind() == Obj.Meth) {
            pozoviMetodu(m);
            if (m.getType() != Tab.noType) Code.put(Code.pop);
        }
    }

    @Override
    public void visit(Stmt_return n) {
        returnFound = true;
        Code.put(Code.exit);
        Code.put(Code.return_);
    }

    @Override
    public void visit(Stmt_return_expr n) {
        returnFound = true;
        Code.put(Code.exit);
        Code.put(Code.return_);
    }

    @Override
    public void visit(Stmt_read n) {
        Obj d = n.getDesignator().obj;
        if (d == null || d == Tab.noObj) return;
        if (jeChar(d.getType())) Code.put(Code.bread);
        else Code.put(Code.read);

        upisiVrednost(d);
    }

    @Override
    public void visit(Stmt_print n) {
        Struct t = n.getExpr().struct;
        if (t == null) t = Tab.intType;

        if (t == Tab.charType) {
            ucitajKonstantu(1);
            Code.put(Code.bprint);
        } else {
            ucitajKonstantu(5);
            Code.put(Code.print);
        }
    }

    @Override
    public void visit(Stmt_print_num n) {
        Struct t = n.getExpr().struct;
        if (t == null) t = Tab.intType;

        ucitajKonstantu(n.getN2());
        if (t == Tab.charType) Code.put(Code.bprint);
        else Code.put(Code.print);
    }

    private static class UslovKontekst {
        List<Integer> skokoviNaFalse = new ArrayList<>(); 
        List<Integer> tacniTermovi = new ArrayList<>(); 
    }

    private Stack<UslovKontekst> ifUslovi = new Stack<>();
    private Stack<UslovKontekst> forUslovi = new Stack<>();
    private Stack<UslovKontekst> ternUslovi = new Stack<>();

    private Stack<Integer> ifSkokNaElse = new Stack<>();  
    private Stack<Integer> ifSkokNaKraj = new Stack<>();  

    private Stack<Integer> ternSkokNaFalseGranu = new Stack<>(); 
    private Stack<Integer> ternSkokNaKraj = new Stack<>();       

    private boolean gradimIfUslov = false;
    private boolean gradimForUslov = false;

    private boolean insideTernary(SyntaxNode n) {
        for (SyntaxNode p = n; p != null; p = p.getParent()) {
            if (p instanceof Expr_ternary) return true;
        }
        return false;
    }

    private UslovKontekst trenutniUslov(SyntaxNode gde) {
        if (insideTernary(gde)) {
            if (ternUslovi.isEmpty()) ternUslovi.push(new UslovKontekst());
            return ternUslovi.peek();
        }
        if (gradimForUslov) {
            if (forUslovi.isEmpty()) forUslovi.push(new UslovKontekst());
            return forUslovi.peek();
        }
        if (gradimIfUslov) {
            if (ifUslovi.isEmpty()) ifUslovi.push(new UslovKontekst());
            return ifUslovi.peek();
        }
        if (ifUslovi.isEmpty()) ifUslovi.push(new UslovKontekst());
        return ifUslovi.peek();
    }

    private int relopKod(Relop r) {
        if (r instanceof Relop_eq) return Code.eq;
        if (r instanceof Relop_ne) return Code.ne;
        if (r instanceof Relop_gt) return Code.gt;
        if (r instanceof Relop_ge) return Code.ge;
        if (r instanceof Relop_lt) return Code.lt;
        return Code.le;
    }

    @Override
    public void visit(CondFactRelOpt_rel n) {
        UslovKontekst k = trenutniUslov(n);
        int op = relopKod(n.getRelop());
        Code.putFalseJump(op, 0);
        k.skokoviNaFalse.add(Code.pc - 2);
    }

    @Override
    public void visit(CondFact_main n) {
        if (n.getCondFactRelOpt() instanceof CondFactRelOpt_e) {
            UslovKontekst k = trenutniUslov(n);
            Code.loadConst(0);
            Code.putFalseJump(Code.ne, 0); 
            k.skokoviNaFalse.add(Code.pc - 2);
        }
    }

    @Override
    public void visit(CondTerm_cf n) {
        if (!termJeNaVrhu(n)) return;

        UslovKontekst k = trenutniUslov(n);

        Code.putJump(0);
        k.tacniTermovi.add(Code.pc - 2);

        for (int adr : k.skokoviNaFalse) {
            Code.fixup(adr);
        }
        k.skokoviNaFalse.clear();
    }

    @Override
    public void visit(CondTerm_and n) {
        if (!termJeNaVrhu(n)) return;

        UslovKontekst k = trenutniUslov(n);

        Code.putJump(0);
        k.tacniTermovi.add(Code.pc - 2);

        for (int adr : k.skokoviNaFalse) {
            Code.fixup(adr);
        }
        k.skokoviNaFalse.clear();
    }
    
    @Override
    public void visit(IfBegin n) {
        gradimIfUslov = true;
        ifUslovi.push(new UslovKontekst());
        ifSkokNaElse.push(-1);
    }

    @Override
    public void visit(IfAfterCond n) {
        gradimIfUslov = false;
        UslovKontekst k = ifUslovi.peek();
        Code.putJump(0);
        ifSkokNaElse.pop();
        ifSkokNaElse.push(Code.pc - 2);
        for (int adr : k.tacniTermovi) Code.fixup(adr);
        k.tacniTermovi.clear();
    }

    @Override
    public void visit(IfEnd n) {
        Code.putJump(0);
        ifSkokNaKraj.push(Code.pc - 2);
        int adrElse = ifSkokNaElse.peek();
        if (adrElse != -1) Code.fixup(adrElse);
    }

    @Override
    public void visit(StmtElse_simple n) {
        if (!ifSkokNaKraj.isEmpty()) Code.fixup(ifSkokNaKraj.pop());
    }

    @Override
    public void visit(StmtNoElse_if n) {
        if (n.getStatementElse() instanceof StmtElse_e) {
            int adrElse = ifSkokNaElse.pop();
            if (adrElse != -1) Code.fixup(adrElse);
            if (!ifSkokNaKraj.isEmpty()) Code.fixup(ifSkokNaKraj.pop());
        } else {
            ifSkokNaElse.pop();
        }

        if (!ifUslovi.isEmpty()) ifUslovi.pop();
    }

    @Override
    public void visit(TernaryQ n) {
        if (ternUslovi.isEmpty()) ternUslovi.push(new UslovKontekst());
        UslovKontekst k = ternUslovi.peek();

        Code.putJump(0);
        ternSkokNaFalseGranu.push(Code.pc - 2);

        for (int adr : k.tacniTermovi) Code.fixup(adr);
        k.tacniTermovi.clear();
        k.skokoviNaFalse.clear();
    }

 @Override
 public void visit(TernaryColon n) {
     Code.putJump(0);
     ternSkokNaKraj.push(Code.pc - 2);
     int adrFalse = ternSkokNaFalseGranu.pop();
     Code.fixup(adrFalse);
 }

 @Override
 public void visit(TernaryEnd n) {
     int adrKraj = ternSkokNaKraj.pop();
     Code.fixup(adrKraj);
     if (!ternUslovi.isEmpty()) ternUslovi.pop();
 }
 private Stack<Integer> forAdrUslov = new Stack<>();
 private Stack<Integer> forAdrKoraka = new Stack<>();
 private Stack<Integer> forSkokNaIzlaz = new Stack<>();
 private Stack<List<Integer>> forBreakLista = new Stack<>();
 private Stack<List<Integer>> forContinueLista = new Stack<>();
 private Stack<DesignatorStatement> forKorakCvor = new Stack<>();
 private Stack<Boolean> forImaUslov = new Stack<>();
 private Stack<String> breakKontekst = new Stack<>();

 @Override
 public void visit(ForCondBegin n) {
     forAdrUslov.push(Code.pc);
     gradimForUslov = true;
     forUslovi.push(new UslovKontekst());
 }

 @Override
 public void visit(ForCond_cond n) {
     forImaUslov.push(true);
     gradimForUslov = false;
 }

 @Override
 public void visit(ForCond_e n) {
     forImaUslov.push(false);
     gradimForUslov = false;

     if (!forUslovi.isEmpty()) {
         forUslovi.pop();
     }
 }

 @Override
 public void visit(ForStep_stmt n) {
     forKorakCvor.push(n.getDesignatorStatement());
 }

 @Override
 public void visit(ForStep_e n) {
     forKorakCvor.push(null);
 }

 @Override
 public void visit(ForBegin n) {
     boolean imaUslov = !forImaUslov.isEmpty() && forImaUslov.pop();

     forBreakLista.push(new ArrayList<>());
     forContinueLista.push(new ArrayList<>());
     breakKontekst.push("for");

     if (imaUslov) {
         UslovKontekst k = forUslovi.peek();

         Code.putJump(0);
         forSkokNaIzlaz.push(Code.pc - 2);

         for (int adr : k.tacniTermovi) {
             Code.fixup(adr);
         }
         k.tacniTermovi.clear();

         k.skokoviNaFalse.clear();
     } else {
         forSkokNaIzlaz.push(-1);
     }
 }
 @Override
 public void visit(ForEnd n) {
     int adrKoraka = Code.pc;
     forAdrKoraka.push(adrKoraka);

     if (!forContinueLista.isEmpty()) {
         for (int adr : forContinueLista.peek()) {
             Code.fixup(adr);
         }
         forContinueLista.pop();
     }

     if (!forKorakCvor.isEmpty()) {
    	    DesignatorStatement korak = forKorakCvor.pop();
    	    if (korak != null) {
    	        generisemForKorak = true;
    	        korak.traverseBottomUp(this);
    	        generisemForKorak = false;
    	    }
    	}

     int adrUslova = (!forAdrUslov.isEmpty()) ? forAdrUslov.pop() : -1;
     if (adrUslova != -1) {
         Code.putJump(adrUslova);
     }

     int izlaz = (!forSkokNaIzlaz.isEmpty()) ? forSkokNaIzlaz.pop() : -1;
     if (izlaz != -1) {
         Code.fixup(izlaz);
     }

     if (!forBreakLista.isEmpty()) {
         for (int adr : forBreakLista.peek()) {
             Code.fixup(adr);
         }
         forBreakLista.pop();
     }

     if (!forAdrKoraka.isEmpty()) {
         forAdrKoraka.pop();
     }

     if (!forUslovi.isEmpty()) {
         forUslovi.pop();
     }

     if (!breakKontekst.isEmpty() && "for".equals(breakKontekst.peek())) {
         breakKontekst.pop();
     }
 }

 @Override
 public void visit(Stmt_continue n) {
     Code.putJump(0);
     int adr = Code.pc - 2;
     if (!forContinueLista.isEmpty()) {
         forContinueLista.peek().add(adr);
     }
 }
    private Stack<Integer> switchSkokNaDispatcher = new Stack<>();
    private Stack<Integer> switchSkokPreskociDispatcher = new Stack<>();
    private Stack<List<Integer>> switchCaseVrednosti = new Stack<>();
    private Stack<List<Integer>> switchCaseAdreseTela = new Stack<>();
    private Stack<List<Integer>> switchBreakListe = new Stack<>();

    @Override
    public void visit(SwitchBegin n) {
        Code.putJump(0);
        switchSkokNaDispatcher.push(Code.pc - 2);

        switchCaseVrednosti.push(new ArrayList<>());
        switchCaseAdreseTela.push(new ArrayList<>());
        switchBreakListe.push(new ArrayList<>());

        breakKontekst.push("switch");
    }

    @Override
    public void visit(CaseHdr h) {
        switchCaseVrednosti.peek().add(h.getN1());
        switchCaseAdreseTela.peek().add(Code.pc); 
    }

    @Override
    public void visit(CaseBlock c) {
    }

    @Override
    public void visit(SwitchEnd n) {
        Code.putJump(0);
        switchSkokPreskociDispatcher.push(Code.pc - 2);
    }

    @Override
    public void visit(Stmt_switch n) {
        int adrSkok = switchSkokNaDispatcher.pop();
        Code.fixup(adrSkok);
        List<Integer> vrednosti = switchCaseVrednosti.pop();
        List<Integer> adreseTela = switchCaseAdreseTela.pop();
        for (int i = 0; i < vrednosti.size(); i++) {
            int v = vrednosti.get(i);
            int adrTela = adreseTela.get(i);
            Code.put(Code.dup);
            Code.loadConst(v);
            Code.putFalseJump(Code.eq, 0);
            int adrNije = Code.pc - 2;
            Code.put(Code.pop);     
            Code.putJump(adrTela);   
            Code.fixup(adrNije);
        }
        Code.put(Code.pop);

        if (!switchSkokPreskociDispatcher.isEmpty()) {
            int adrPreskoci = switchSkokPreskociDispatcher.pop();
            Code.fixup(adrPreskoci);
        }

        for (int adr : switchBreakListe.pop()) Code.fixup(adr);

        if (!breakKontekst.isEmpty() && "switch".equals(breakKontekst.peek())) breakKontekst.pop();
    }

    @Override
    public void visit(Stmt_break n) {
        Code.putJump(0);
        int adr = Code.pc - 2;

        if (!breakKontekst.isEmpty() && "switch".equals(breakKontekst.peek())) {
            if (!switchBreakListe.isEmpty()) switchBreakListe.peek().add(adr);
        } else {
            if (!forBreakLista.isEmpty()) forBreakLista.peek().add(adr);
        }
    }
    
    
}