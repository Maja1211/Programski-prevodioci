package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class SemanticAnalyzer extends VisitorAdaptor {

    private boolean greskaDetektovana = false;
    Logger log = Logger.getLogger(getClass());

    public void report_error(String message, SyntaxNode info) {
        greskaDetektovana = true;
        StringBuilder msg = new StringBuilder(message);
        int line = (info == null) ? 0 : info.getLine();
        if (line != 0)
            msg.append(" na liniji ").append(line);
        log.error(msg.toString());
    }

    public void report_info(String message, SyntaxNode info) {
        StringBuilder msg = new StringBuilder(message);
        int line = (info == null) ? 0 : info.getLine();
        if (line != 0)
            msg.append(" na liniji ").append(line);
        log.info(msg.toString());
    }

    public boolean passed() {
        return !greskaDetektovana;
    }

    private Obj tekuciProgram = null;

    private Struct tekuciTip = Tab.noType;

    private Obj tekucaMetoda = null;
    private boolean uMetodi = false;

    private boolean mainPronadjen = false;
    
    private int forDepth = 0;
    private int switchDepth = 0;

    private int konstVrednost;
    private Struct konstTip = Tab.noType;
    
    private java.util.IdentityHashMap<SyntaxNode, java.util.List<Struct>> tipoviAktPars = new java.util.IdentityHashMap<>();
    
    private java.util.HashMap<String, java.util.List<Struct>> formalniTipovi = new java.util.HashMap<>();

    private static final int MAX_GLOBAL_VARS = 65536;
    private static final int MAX_LOCAL_VARS = 256;

    private int globalVarCount = 0;
    private int localVarCount = 0;  


    @Override
    public void visit(ProgramName pn) {
        String naziv = pn.getI1();
        tekuciProgram = Tab.insert(Obj.Prog, naziv, Tab.noType);
        Tab.openScope();
        report_info("Program: " + naziv, pn);
    }

    @Override
    public void visit(Program p) {
        if (!mainPronadjen) {
            report_error("Nedostaje void main() bez argumenata.", p);
        }
        Tab.chainLocalSymbols(tekuciProgram);
        Tab.closeScope();
        tekuciProgram = null;
    }
    
    private String formatObj(Obj o) {
        if (o == null) return "null";
        if (o == Tab.noObj) return "noObj";

        String kind;
        switch (o.getKind()) {
            case Obj.Con: kind = "Con"; break;
            case Obj.Var: kind = "Var"; break;
            case Obj.Meth: kind = "Meth"; break;
            case Obj.Type: kind = "Type"; break;
            case Obj.Prog: kind = "Prog"; break;
            case Obj.Elem: kind = "Elem"; break;
            case Obj.Fld: kind = "Fld"; break;
            default: kind = "Kind(" + o.getKind() + ")";
        }

        String type = formatType(o.getType());

        String fp = (o.getFpPos() > 0) ? (", fpPos=" + o.getFpPos()) : "";
        return kind + " " + o.getName() + " : " + type + " (adr=" + o.getAdr() + ", level=" + o.getLevel() + fp + ")";
    }

    private String formatType(Struct t) {
        if (t == null) return "null";
        if (t == Tab.noType) return "noType";
        if (t == Tab.intType) return "int";
        if (t == Tab.charType) return "char";

        Obj b = Tab.find("bool");
        if (b != Tab.noObj && t == b.getType()) return "bool";

        switch (t.getKind()) {
            case Struct.Array:
                return formatType(t.getElemType()) + "[]";
            case Struct.Class:
                return "class";
            case Struct.Bool:
                return "bool";
            default:
                return "typeKind(" + t.getKind() + ")";
        }
    }
    
    private void report_detected(String vrsta, String ime, Obj objekat, SyntaxNode gde) {
        report_info(vrsta + ": " + ime + " -> " + formatObj(objekat), gde);
    }

    @Override
    public void visit(Type t) {
        Obj o = Tab.find(t.getI1());
        if (o == Tab.noObj || o.getKind() != Obj.Type) {
            report_error("Nepoznat tip: " + t.getI1(), t);
            tekuciTip = Tab.noType;
        } else {
            tekuciTip = o.getType();
        }
        t.struct = tekuciTip;
    }
    
    @Override
    public void visit(Constant_n cn) {
        konstVrednost = cn.getN1();
        konstTip = Tab.intType;
    }

    @Override
    public void visit(Constant_c cc) {
        konstVrednost = cc.getC1();
        konstTip = Tab.charType;
    }

    @Override
    public void visit(Constant_b cb) {
        konstVrednost = cb.getB1();
        konstTip = Tab.find("bool").getType();
    }

    @Override
    public void visit(ConDecl cd) {
        String ime = cd.getI1();

        if (Tab.currentScope().findSymbol(ime) != null) {
            report_error("Konstanta je vec definisana: " + ime, cd);
            return;
        }

        if (tekuciTip == Tab.noType || konstTip == Tab.noType || !konstTip.assignableTo(tekuciTip)) {
            report_error("Pogresan tip konstante: " + ime, cd);
            return;
        }

        Obj k = Tab.insert(Obj.Con, ime, tekuciTip);
        k.setAdr(konstVrednost);
        report_info("Definisana konstanta: " + ime, cd);
    }
    
    
    @Override
    public void visit(VarIdent_1 v) {
        String ime = v.getI1(); 
        if (Tab.currentScope().findSymbol(ime) != null) {
            report_error("Promenljiva je vec definisana: " + ime, v);
            return;
        }
        if (tekuciTip == Tab.noType) {
            report_error("Nepoznat tip promenljive: " + ime, v);
            return;
        }
        Tab.insert(Obj.Var, ime, tekuciTip);
        if (!uMetodi) {
            globalVarCount++;
            if (globalVarCount > MAX_GLOBAL_VARS) {
                report_error("Prekoracen maksimalan broj globalnih promenljivih (65536).", v);
            }
        } else {
            localVarCount++;
            if (localVarCount > MAX_LOCAL_VARS) {
                report_error("Prekoracen maksimalan broj lokalnih promenljivih (256).", v);
            }
        }
        report_info("Promenljiva: " + ime, v);
    }

    @Override
    public void visit(VarIdent_more v) {
        String ime = v.getI1(); 
        if (Tab.currentScope().findSymbol(ime) != null) {
            report_error("Promenljiva je vec definisana: " + ime, v);
            return;
        }
        if (tekuciTip == Tab.noType) {
            report_error("Nepoznat tip niza: " + ime, v);
            return;
        }
        Struct nizTip = new Struct(Struct.Array, tekuciTip);
        Tab.insert(Obj.Var, ime, nizTip);
        if (!uMetodi) {
            globalVarCount++;
            if (globalVarCount > MAX_GLOBAL_VARS) {
                report_error("Prekoracen maksimalan broj globalnih promenljivih (65536).", v);
            }
        } else {
            localVarCount++;
            if (localVarCount > MAX_LOCAL_VARS) {
                report_error("Prekoracen maksimalan broj lokalnih promenljivih (256).", v);
            }
        }
        report_info("Niz: " + ime, v);
    }
    
    private boolean returnVidjen = false;
    
    @Override
    public void visit(MethRetAndName_void m) {
    	returnVidjen = false;
    	localVarCount = 0; 

        String ime = m.getI1();

        if (Tab.currentScope().findSymbol(ime) != null) {
            report_error("Metoda je vec definisana: " + ime, m);
            tekucaMetoda = Tab.noObj;
            Tab.openScope();
            return;
        }

        tekucaMetoda = Tab.insert(Obj.Meth, ime, Tab.noType);
        m.obj = tekucaMetoda;
        formalniTipovi.put(ime, new java.util.ArrayList<>());
        tekucaMetoda.setLevel(0);
        uMetodi = true;
        Tab.openScope();
    }

    @Override
    public void visit(MethRetAndName_type m) {
    	returnVidjen = false;
    	localVarCount = 0; 
        String ime = m.getI2();

        if (Tab.currentScope().findSymbol(ime) != null) {
            report_error("Metoda je vec definisana: " + ime, m);
            tekucaMetoda = Tab.noObj;
            Tab.openScope();
            return;
        }

        if (tekuciTip == Tab.noType) {
            report_error("Nepoznat povratni tip metode: " + ime, m);
            tekucaMetoda = Tab.noObj;
            Tab.openScope();
            return;
        }

        tekucaMetoda = Tab.insert(Obj.Meth, ime, tekuciTip);
        m.obj = tekucaMetoda;
        formalniTipovi.put(ime, new java.util.ArrayList<>());
        tekucaMetoda.setLevel(0);
        uMetodi = true;
        Tab.openScope();
    }

    @Override
    public void visit(FormPar_var fp) {
        if (!uMetodi || tekucaMetoda == null || tekucaMetoda == Tab.noObj) {
            report_error("Formalni parametar van metode.", fp);
            return;
        }

        String ime = fp.getI2();
        if (Tab.currentScope().findSymbol(ime) != null) {
            report_error("Duplikat formalnog parametra: " + ime, fp);
            return;
        }
        if (tekuciTip == Tab.noType) {
            report_error("Nepoznat tip formalnog parametra: " + ime, fp);
            return;
        }

        Obj param = Tab.insert(Obj.Var, ime, tekuciTip);
        formalniTipovi.get(tekucaMetoda.getName()).add(tekuciTip);
        //formalniTipovi.computeIfAbsent(tekucaMetoda.getName(), k -> new java.util.ArrayList<>()).add(tekuciTip);
        localVarCount++;
        if (localVarCount > MAX_LOCAL_VARS) {
            report_error("Prekoracen maksimalan broj lokalnih promenljivih (256).", fp);
        }

        int poz = tekucaMetoda.getLevel() + 1;
        param.setFpPos(poz);
        tekucaMetoda.setLevel(poz);
    }

    @Override
    public void visit(FormPar_array fp) {
        if (!uMetodi || tekucaMetoda == null || tekucaMetoda == Tab.noObj) {
            report_error("Formalni parametar van metode.", fp);
            return;
        }

        String ime = fp.getI2();
        if (Tab.currentScope().findSymbol(ime) != null) {
            report_error("Duplikat formalnog parametra: " + ime, fp);
            return;
        }
        if (tekuciTip == Tab.noType) {
            report_error("Nepoznat tip formalnog parametra: " + ime, fp);
            return;
        }

        Struct tipNiza = new Struct(Struct.Array, tekuciTip);
        Obj param = Tab.insert(Obj.Var, ime, tipNiza);
        formalniTipovi.get(tekucaMetoda.getName()).add(tipNiza);
        //formalniTipovi.computeIfAbsent(tekucaMetoda.getName(), k -> new java.util.ArrayList<>()).add(tipNiza);
        localVarCount++;
        if (localVarCount > MAX_LOCAL_VARS) {
            report_error("Prekoracen maksimalan broj lokalnih promenljivih (256).", fp);
        }


        int poz = tekucaMetoda.getLevel() + 1;
        param.setFpPos(poz);
        tekucaMetoda.setLevel(poz);
    }

    @Override
    public void visit(MethodDecl md) {
        if (tekucaMetoda == Tab.noObj) {
            Tab.closeScope();
            tekucaMetoda = null;
            uMetodi = false;
            return;
        }

        if ("main".equals(tekucaMetoda.getName())) {
            boolean voidPovratna = (tekucaMetoda.getType() == Tab.noType);
            boolean bezParam = (tekucaMetoda.getLevel() == 0);

            if (!voidPovratna || !bezParam) {
                report_error("main mora biti void i bez argumenata.", md);
            } else {
                if (mainPronadjen) {
                    report_error("Dozvoljena je samo jedna main metoda.", md);
                } else {
                    mainPronadjen = true;
                }
            }
        }
        
        /*if (tekucaMetoda != null && tekucaMetoda != Tab.noObj && tekucaMetoda.getType() != Tab.noType) {
            if (!returnVidjen) {
                report_error("Metoda sa povratnim tipom mora imati bar jedan return sa izrazom.", md);
            }
        }*/


        Tab.chainLocalSymbols(tekucaMetoda);
        Tab.closeScope();
        tekucaMetoda = null;
        uMetodi = false;
    }

    private static class EnumStavka {
        String ime;
        Integer vrednost; 
        EnumStavka(String ime, Integer vrednost) {
            this.ime = ime;
            this.vrednost = vrednost;
        }
    }

    private java.util.List<EnumStavka> enumStavke = new java.util.ArrayList<>();

    @Override
    public void visit(EnumItem_simple it) {
        enumStavke.add(new EnumStavka(it.getI1(), null));
    }

    @Override
    public void visit(EnumItem_value it) {
        enumStavke.add(new EnumStavka(it.getI1(), it.getN2()));
    }

    @Override
    public void visit(EnumDecl e) {
        String imeEnuma = e.getI1();

        if (Tab.currentScope().findSymbol(imeEnuma) != null) {
            report_error("Ime enuma '" + imeEnuma + "' je vec zauzeto u ovom opsegu.", e);
            enumStavke.clear();
            return;
        }

        Obj enumObj = Tab.insert(Obj.Type, imeEnuma, Tab.intType);

     Tab.openScope();

     int sledeca = 0;
     java.util.HashSet<Integer> zauzeteVrednosti = new java.util.HashSet<>();

     for (EnumStavka s : enumStavke) {

         if (Tab.currentScope().findSymbol(s.ime) != null) {
             report_error("Duplikat imena u enum-u '" + imeEnuma + "': " + s.ime, e);
             continue;
         }

         int vrednost = (s.vrednost != null) ? s.vrednost : sledeca;

         if (zauzeteVrednosti.contains(vrednost)) {
             report_error("Vrednost " + vrednost + " je vec dodeljena u enum-u '" + imeEnuma + "'.", e);
             sledeca = vrednost + 1;
             continue;
         }
         zauzeteVrednosti.add(vrednost);

         Obj c = Tab.insert(Obj.Con, s.ime, Tab.intType);
         c.setAdr(vrednost);

         sledeca = vrednost + 1;

         report_info("Enum stavka: " + s.ime + " = " + vrednost, e);
     }

     Tab.chainLocalSymbols(enumObj);
     Tab.closeScope();

     report_info("Definisan enum: " + imeEnuma, e);
     enumStavke.clear();
    }
    
    private java.util.IdentityHashMap<Expr, Struct> tipIzraza = new java.util.IdentityHashMap<>();

    private Struct dohvatiTipIzraza(Expr izraz) {
        if (izraz == null) return Tab.noType;
        Struct t = tipIzraza.get(izraz);
        if (t != null) return t;
        if (izraz.struct != null) return izraz.struct;  
        return Tab.noType;
    }

    @Override
    public void visit(Expr_basic e) {
        Struct t = e.getExprBasic().struct;
        e.struct = t;              
        tipIzraza.put(e, t);
    }

    @Override
    public void visit(Expr_ternary e) {
        Struct tip1 = dohvatiTipIzraza(e.getExpr());
        Struct tip2 = dohvatiTipIzraza(e.getExpr1());

        if (tip1 == Tab.noType || tip2 == Tab.noType) {
            e.struct = Tab.noType;          
            tipIzraza.put(e, Tab.noType);
            return;
        }
        if (!tip1.assignableTo(tip2) && !tip2.assignableTo(tip1)) {
            report_error("Ternarni operator: nekompatibilni tipovi grana.", e);
            e.struct = Tab.noType;          
            tipIzraza.put(e, Tab.noType);
            return;
        }

        e.struct = tip1;                   
        tipIzraza.put(e, tip1);
    }
    
    /*if (!tip1.equals(tip2)) {
    report_error("Ternarni operator: drugi i treci izraz moraju biti istog tipa.", e);
    e.struct = Tab.noType;
    tipIzraza.put(e, Tab.noType);
    return;
}
e.struct = tip1;
tipIzraza.put(e, tip1);*/


    @Override
    public void visit(ExprBasic_main n) {
        n.struct = n.getAddopTermList().struct;
    }

    @Override
    public void visit(AddopTermList_term n) {
        n.struct = n.getTerm().struct;
    }

    @Override
    public void visit(AddopTermList_add n) {
        Struct leviTip = n.getAddopTermList().struct;
        Struct desniTip = n.getTerm().struct;

        if (leviTip != Tab.intType || desniTip != Tab.intType) {
            report_error("Sabiranje/oduzimanje je dozvoljeno samo nad int.", n);
            n.struct = Tab.noType;
        } else {
            n.struct = Tab.intType;
        }
    }

    @Override
    public void visit(Term n) {
        n.struct = n.getMulopFactorList().struct;
    }

    @Override
    public void visit(MulopFactorList_factor n) {
        n.struct = n.getFactor().struct;
    }

    @Override
    public void visit(MulopFactorList_mul n) {
        Struct leviTip = n.getMulopFactorList().struct;
        Struct desniTip = n.getFactor().struct;

        if (leviTip != Tab.intType || desniTip != Tab.intType) {
            report_error("Mnozenje/deljenje/mod je dozvoljeno samo nad int.", n);
            n.struct = Tab.noType;
        } else {
            n.struct = Tab.intType;
        }
    }

    @Override
    public void visit(Factor n) {
        Struct osnovniTip = n.getFactorSub().struct;

        if (n.getPom() instanceof Pom_m) {
            if (osnovniTip != Tab.intType) {
                report_error("Unarni minus je dozvoljen samo nad int.", n);
                n.struct = Tab.noType;
            } else {
                n.struct = Tab.intType;
            }
        } else {
            n.struct = osnovniTip;
        }
    }

    @Override
    public void visit(Factor_num n) { n.struct = Tab.intType; }

    @Override
    public void visit(Factor_char n) { n.struct = Tab.charType; }

    @Override
    public void visit(Factor_bool n) { n.struct = Tab.find("bool").getType(); }

    @Override
    public void visit(Factor_paren n) {
        n.struct = dohvatiTipIzraza(n.getExpr());
    }

    @Override
    public void visit(Factor_new_arr n) {
        if (tekuciTip == Tab.noType) {
            report_error("Nepoznat tip u new.", n);
            n.struct = Tab.noType;
            return;
        }

        Struct tipIndeksa = dohvatiTipIzraza(n.getExpr());
        if (tipIndeksa != Tab.intType) {
            report_error("Indeks u new T[expr] mora biti int.", n);
        }

        n.struct = new Struct(Struct.Array, tekuciTip);
    }


    private Obj napraviVrednostObj(Struct tip) {
        return new Obj(Obj.Con, "$tmp", tip);
    }

    private Obj napraviElemObj(Struct tipElem) {
        return new Obj(Obj.Elem, "$elem", tipElem);
    }


    @Override
    public void visit(DesignatorArrayName n) {
        Obj o = Tab.find(n.getI1());
        if (o == Tab.noObj) {
            report_error("Nedefinisan identifikator: " + n.getI1(), n);
            n.obj = Tab.noObj;
        } else {
            n.obj = o;
            //report_detected("KORISCENJE", n.getI1(), o, n);//
        }
    }

    @Override
    public void visit(DesignatorName n) {
        Obj o = Tab.find(n.getI1());
        if (o == Tab.noObj) {
            report_error("Nedefinisan identifikator: " + n.getI1(), n);
            n.obj = Tab.noObj;
        } else {
            n.obj = o;
           // report_detected("KORISCENJE", n.getI1(), o, n);
        }
    }

    @Override
    public void visit(DesignatorPomArrayName n) {
        Obj o = Tab.find(n.getI1());
        if (o == Tab.noObj) {
            report_error("Nedefinisan identifikator: " + n.getI1(), n);
            n.obj = Tab.noObj;
        } else {
            n.obj = o;
        }
    }
    
    @Override
    public void visit(Designator_var d) {
        String ime = d.getI1();
        Obj obj = Tab.find(ime);

        if (obj == Tab.noObj) {
            report_error("Nedefinisan identifikator: " + ime, d);
            d.obj = Tab.noObj;
            return;
        }

        d.obj = obj;

        String poruka;
        if (obj.getKind() == Obj.Var && obj.getFpPos() > 0) {
            poruka = "KORISCENJE FORMALNOG PARAMETRA";
        } else if (obj.getKind() == Obj.Con) {
            poruka = "KORISCENJE KONSTANTE";
        } else if (obj.getKind() == Obj.Meth) {
            poruka = "KORISCENJE METODE";
        } else if (obj.getKind() == Obj.Type) {
            poruka = "KORISCENJE TIPA";
        } else {
            poruka = "KORISCENJE";
        }

        report_detected(poruka, ime, obj, d);
    }



    private Obj resiElementNiza(Obj objNiza, Expr izrazIndeksa, SyntaxNode gde) {
        if (objNiza == null || objNiza == Tab.noObj) return Tab.noObj;

        Struct tip = objNiza.getType();
        if (tip.getKind() != Struct.Array) {
            report_error("Indeksiranje je dozvoljeno samo nad nizom.", gde);
            return Tab.noObj;
        }

        if (dohvatiTipIzraza(izrazIndeksa) != Tab.intType) {
            report_error("Indeks niza mora biti int.", gde);
        }

        return napraviElemObj(tip.getElemType());
    }

    @Override
    public void visit(Designator_elem d) {
    	Obj bazniNiz = d.getDesignatorArrayName().obj;
        String imeNiza = d.getDesignatorArrayName().getI1();
        if (bazniNiz != null && bazniNiz != Tab.noObj) {
            report_detected("KORISCENJE NIZA", imeNiza, bazniNiz, d);
        }

        Obj elem = resiElementNiza(bazniNiz, d.getExpr(), d);
        d.obj = elem;

        if (elem != null && elem != Tab.noObj) {
            report_detected("PRISTUP ELEMENTU NIZA", imeNiza + "[...]", elem, d);
        }
    }

    @Override
    public void visit(DesignatorPom d) {
        Obj objNiza = d.getDesignatorArrayName().obj;
        d.obj = resiElementNiza(objNiza, d.getExpr(), d);
    }

    private Obj primeniMore(Obj baza, DesignatorMore more, SyntaxNode gde) {
        if (baza == null || baza == Tab.noObj || more == null) return baza;

        if (more instanceof DesignatorMore_pom_var) {
            Obj pref = primeniMore(baza, ((DesignatorMore_pom_var) more).getDesignatorMore(), gde);
            report_error("Pristup polju (.) nije podržan u fazi A/B.", gde);
            return Tab.noObj;
        }
        if (more instanceof DesignatorMore_pom_elem) {
            Obj pref = primeniMore(baza, ((DesignatorMore_pom_elem) more).getDesignatorMore(), gde);
            report_error("Pristup polju/indeksiranju preko '.' nije podržan u fazi A/B.", gde);
            return Tab.noObj;
        }
        if (more instanceof DesignatorMore_pom_length) {
            Obj pref = primeniMore(baza, ((DesignatorMore_pom_length) more).getDesignatorMore(), gde);
            report_error("Pristup polju (.) nije podržan u fazi A/B.", gde);
            return Tab.noObj;
        }

        if (more instanceof DesignatorMore_length) {
            if (baza.getType().getKind() != Struct.Array) {
                report_error(".length je dozvoljen samo nad nizom.", gde);
                return Tab.noObj;
            }
            return napraviVrednostObj(Tab.intType);
        }

        if (more instanceof DesignatorMore_var) {
            String ime = ((DesignatorMore_var) more).getI1(); 

            if (baza.getKind() == Obj.Type) {
                for (Obj o : baza.getLocalSymbols()) {
                    if (o.getKind() == Obj.Con && o.getName().equals(ime)) {
                        return o; 
                    }
                }
                report_error("Enum '" + baza.getName() + "' nema stavku '" + ime + "'.", gde);
                return Tab.noObj;
            }

            report_error("Pristup polju (.) nije podržan u fazi A/B.", gde);
            return Tab.noObj;
        }

        if (more instanceof DesignatorMore_elem) {
            report_error("Pristup polju/indeksiranju preko '.' nije podržan u fazi A/B.", gde);
            return Tab.noObj;
        }

        return Tab.noObj;
    }

    @Override
    public void visit(Designator_pom d) {
        Obj baza = d.getDesignatorName().obj;
        d.obj = primeniMore(baza, d.getDesignatorMore(), d);
    }

    @Override
    public void visit(Designator_pom_elem d) {
        Obj baza = d.getDesignatorPom().obj; 
        d.obj = primeniMore(baza, d.getDesignatorMore(), d);
    }

    @Override
    public void visit(Factor_design f) {
        Obj des = f.getDesignator().obj;
        if (des == null || des == Tab.noObj) {
            f.struct = Tab.noType;
            return;
        }

        FactorOptActPars opt = f.getFactorOptActPars();

        if (opt instanceof FactorOpt_e) {
            if (des.getKind() == Obj.Meth) {
                report_error("Metoda '" + des.getName() + "' se ne moze koristiti bez poziva.", f);
                f.struct = Tab.noType;
            } else {
                f.struct = des.getType();
            }
            return;
        }

        if (des.getKind() != Obj.Meth) {
            report_error("Poziv funkcije je dozvoljen samo nad metodom.", f);
            f.struct = Tab.noType;
            return;
        }

        report_detected("POZIV FUNKCIJE", des.getName(), des, f);

        if (opt instanceof FactorOpt_call_args) {
            ActPars ap = ((FactorOpt_call_args) opt).getActPars();
            java.util.List<Struct> akt = dohvatiTipoveAktPars(ap);
            proveriPozivMetode(des, akt, f);

        } else if (opt instanceof FactorOpt_call_noargs) {
            proveriPozivMetode(des, new java.util.ArrayList<>(), f);

        } else {
            proveriPozivMetode(des, new java.util.ArrayList<>(), f);
        }
        if (des.getType() == Tab.noType) {
            report_error("Void metoda '" + des.getName() + "' ne moze da se koristi u izrazu.", f);
            f.struct = Tab.noType;
            return;
        }


        f.struct = des.getType();
    }

    private java.util.List<Struct> dohvatiTipoveAktPars(SyntaxNode n) {
        java.util.List<Struct> l = tipoviAktPars.get(n);
        return (l != null) ? l : new java.util.ArrayList<>();
    }

    @Override
    public void visit(ActParsMore_e n) {
        tipoviAktPars.put(n, new java.util.ArrayList<>());
    }

    @Override
    public void visit(ActParsMore_comma n) {
        java.util.List<Struct> lista = new java.util.ArrayList<>();
        lista.add(dohvatiTipIzraza(n.getExpr()));
        lista.addAll(dohvatiTipoveAktPars(n.getActParsMore()));
        tipoviAktPars.put(n, lista);
    }

    @Override
    public void visit(ActPars_one n) {
        java.util.List<Struct> lista = new java.util.ArrayList<>();
        lista.add(dohvatiTipIzraza(n.getExpr()));
        lista.addAll(dohvatiTipoveAktPars(n.getActParsMore()));
        tipoviAktPars.put(n, lista);
    }

    private void proveriPozivMetode(Obj metoda, java.util.List<Struct> akt, SyntaxNode gde) {
        if (metoda == null || metoda == Tab.noObj) return;

        if (metoda.getKind() != Obj.Meth) {
            report_error("Poziv je dozvoljen samo nad metodom.", gde);
            return;
        }
        
        String naziv = metoda.getName();

     if ("chr".equals(naziv)) {
         if (akt.size() != 1) {
             report_error("chr mora imati tacno 1 argument.", gde);
             return;
         }
         if (akt.get(0) != Tab.intType) {
             report_error("chr(int) ocekuje int argument.", gde);
         }
         return;
     }

     if ("ord".equals(naziv)) {
         if (akt.size() != 1) {
             report_error("ord mora imati tacno 1 argument.", gde);
             return;
         }
         if (akt.get(0) != Tab.charType) {
             report_error("ord(char) ocekuje char argument.", gde);
         }
         return;
     }

     if ("len".equals(naziv)) {
         if (akt.size() != 1) {
             report_error("len mora imati tacno 1 argument.", gde);
             return;
         }
         Struct t0 = akt.get(0);
         if (t0 == Tab.noType || t0.getKind() != Struct.Array) {
             report_error("len ocekuje argument koji je niz.", gde);
         }
         return;
     }

     java.util.List<Struct> form = formalniTipovi.get(metoda.getName());
     if (form == null) form = new java.util.ArrayList<>();

        if (akt.size() != form.size()) {
            report_error("Pogresan broj argumenata u pozivu metode '" + metoda.getName() + "'.", gde);
            return;
        }

        for (int i = 0; i < akt.size(); i++) {
            Struct at = akt.get(i);
            Struct ft = form.get(i);   

            if (at == Tab.noType || ft == Tab.noType) continue;

            if (!at.assignableTo(ft)) {
                report_error("Argument #" + (i + 1) + " nije dodeljiv formalnom parametru metode '" + metoda.getName() + "'.", gde);
            }
        }
    }


    private boolean jeLValue(Obj o) {
        return o != null && o != Tab.noObj &&
               (o.getKind() == Obj.Var || o.getKind() == Obj.Elem);
    }

    
    @Override
    public void visit(DesStmt_assign n) {
        Obj levo = n.getDesignator().obj;
        Struct desno = dohvatiTipIzraza(n.getExpr());

        if (levo == null || levo == Tab.noObj) return;

        if (!jeLValue(levo)) {
            report_error("Leva strana dodele mora biti promenljiva ili element niza.", n);
            return;
        }

        Struct tipL = levo.getType();
        if (desno != Tab.noType && tipL != Tab.noType && !desno.assignableTo(tipL)) {
            report_error("Nekompatibilni tipovi u dodeli.", n);
        }
    }

    @Override
    public void visit(DesStmt_inc n) {
        Obj d = n.getDesignator().obj;
        if (d == null || d == Tab.noObj) return;

        if (!jeLValue(d)) {
            report_error("Operator ++ je dozvoljen samo nad promenljivom/elementom niza.", n);
            return;
        }
        if (d.getType() != Tab.intType) {
            report_error("Operator ++ je dozvoljen samo nad int.", n);
        }
    }

    @Override
    public void visit(DesStmt_dec n) {
        Obj d = n.getDesignator().obj;
        if (d == null || d == Tab.noObj) return;

        if (!jeLValue(d)) {
            report_error("Operator -- je dozvoljen samo nad promenljivom/elementom niza.", n);
            return;
        }
        if (d.getType() != Tab.intType) {
            report_error("Operator -- je dozvoljen samo nad int.", n);
        }
    }

    @Override
    public void visit(DesStmt_call_noargs n) {
        Obj m = n.getDesignator().obj;
        if (m == null || m == Tab.noObj) return;

        report_detected("POZIV FUNKCIJE", m.getName(), m, n);
        proveriPozivMetode(m, new java.util.ArrayList<>(), n);
    }

    @Override
    public void visit(DesStmt_call_args n) {
        Obj m = n.getDesignator().obj;
        if (m == null || m == Tab.noObj) return;

        report_detected("POZIV FUNKCIJE", m.getName(), m, n);
        java.util.List<Struct> akt = dohvatiTipoveAktPars(n.getActPars());
        proveriPozivMetode(m, akt, n);
    }
    
    private Struct boolTip() {
        Obj b = Tab.find("bool");
        return (b != Tab.noObj) ? b.getType() : Tab.noType;
    }

    
    @Override
    public void visit(Stmt_return n) { 
        if (!uMetodi || tekucaMetoda == null || tekucaMetoda == Tab.noObj) return;

        if (tekucaMetoda.getType() != Tab.noType) {
            report_error("Metoda sa povratnim tipom mora imati return izraz.", n);
        }
        returnVidjen = true;
    }

    @Override
    public void visit(Stmt_return_expr n) { 
        if (!uMetodi || tekucaMetoda == null || tekucaMetoda == Tab.noObj) return;

        if (tekucaMetoda.getType() == Tab.noType) {
            report_error("Void metoda ne sme vratiti izraz.", n);
            returnVidjen = true;
            return;
        }

        Struct t = dohvatiTipIzraza(n.getExpr());
        if (t != Tab.noType && !t.assignableTo(tekucaMetoda.getType())) {
        	report_error("Tip return izraza nije kompatibilan sa povratnim tipom metode.", n.getExpr());
        }
        returnVidjen = true;
    }

    @Override
    public void visit(Stmt_read n) {
        Obj d = n.getDesignator().obj;
        if (d == null || d == Tab.noObj) return;

        if (!jeLValue(d)) {
            report_error("read argument mora biti promenljiva ili element niza.", n.getDesignator()); 
            return;
        }

        Struct t = d.getType();
        if (t != Tab.intType && t != Tab.charType && t != boolTip()) {
            report_error("read dozvoljen samo za int/char/bool.", n.getDesignator()); 
        }
    }

    @Override
    public void visit(Stmt_print n) {
        Struct t = dohvatiTipIzraza(n.getExpr());
        if (t != Tab.intType && t != Tab.charType && t != boolTip()) {
            report_error("print dozvoljen samo za int/char/bool.", n.getExpr()); 
        }
    }

    @Override
    public void visit(Stmt_print_num n) {
        Struct t = dohvatiTipIzraza(n.getExpr());
        if (t != Tab.intType && t != Tab.charType && t != boolTip()) {
            report_error("print dozvoljen samo za int/char/bool.", n.getExpr()); 
        }
        if (n.getN2() <= 0) {
            report_error("Sirina u print(expr, n) mora biti pozitivna.", n); 
        }
    }

    private java.util.IdentityHashMap<SyntaxNode, Struct> tipUslova = new java.util.IdentityHashMap<>();
    private Struct dohvatiTipUslova(SyntaxNode n) {
        Struct t = tipUslova.get(n);
        return (t != null) ? t : Tab.noType;
    }

    @Override
    public void visit(CondFactRelOpt_e n) {
        tipUslova.put(n, Tab.noType); 
    }

    @Override
    public void visit(CondFactRelOpt_rel n) {
        tipUslova.put(n, boolTip());
    }

    @Override
    public void visit(CondFact_main n) {
        Struct levo = n.getExprBasic().struct;

        if (n.getCondFactRelOpt() instanceof CondFactRelOpt_e) {
            if (levo != boolTip()) {
            	report_error("Uslov bez relacije mora biti bool.", n.getExprBasic());
                tipUslova.put(n, Tab.noType);
            } else {
                tipUslova.put(n, boolTip());
            }
            return;
        }

        CondFactRelOpt_rel rel = (CondFactRelOpt_rel) n.getCondFactRelOpt();
        Struct desno = rel.getExprBasic().struct;

        Relop op = rel.getRelop();
        boolean eqNeq = (op instanceof Relop_eq) || (op instanceof Relop_ne);

        if (eqNeq) {
            if (levo == Tab.noType || desno == Tab.noType || (!levo.assignableTo(desno) && !desno.assignableTo(levo))) {
                report_error("==/!=: nekompatibilni tipovi.", n);
                tipUslova.put(n, Tab.noType);
            } else {
                tipUslova.put(n, boolTip());
            }
        } else {
            boolean ok = (levo == Tab.intType && desno == Tab.intType) || (levo == Tab.charType && desno == Tab.charType);
            if (!ok) {
                report_error("Relacije <,>,<=,>= dozvoljene su samo za int ili char (isti tip).", n);
                tipUslova.put(n, Tab.noType);
            } else {
                tipUslova.put(n, boolTip());
            }
        }
    }

    @Override
    public void visit(CondTerm_cf n) {
        tipUslova.put(n, dohvatiTipUslova(n.getCondFact()));
    }

    @Override
    public void visit(CondTerm_and n) {
        Struct l = dohvatiTipUslova(n.getCondTerm());
        Struct r = dohvatiTipUslova(n.getCondFact());
        if (l != boolTip() || r != boolTip()) {
            report_error("&& zahteva bool uslove.", n);
            tipUslova.put(n, Tab.noType);
        } else tipUslova.put(n, boolTip());
    }

    @Override
    public void visit(Condition_ct n) {
        tipUslova.put(n, dohvatiTipUslova(n.getCondTerm()));
    }

    @Override
    public void visit(Condition_or n) {
        Struct l = dohvatiTipUslova(n.getCondition());
        Struct r = dohvatiTipUslova(n.getCondTerm());
        if (l != boolTip() || r != boolTip()) {
            report_error("|| zahteva bool uslove.", n);
            tipUslova.put(n, Tab.noType);
        } else tipUslova.put(n, boolTip());
    }

    @Override
    public void visit(StmtNoElse_if n) {
        Struct ct = dohvatiTipUslova(n.getCondition());
        if (ct == Tab.noType) return;         
        if (ct != boolTip()) {
            report_error("Uslov u if mora biti bool.", n.getCondition());
        }
    }

    @Override
    public void visit(ForCond_cond n) {
        Struct ct = dohvatiTipUslova(n.getCondition());
        if (ct == Tab.noType) return;               
        if (ct != boolTip()) {
            report_error("Uslov u for mora biti bool.", n.getCondition());  
        }
    }

    private java.util.List<Integer> caseVrednosti = new java.util.ArrayList<>();

    @Override
    public void visit(CaseHdr n) {
        caseVrednosti.add(n.getN1());
    }
    
    @Override
    public void visit(Stmt_switch n) {
        Struct t = dohvatiTipIzraza(n.getExpr());
        if (t != Tab.intType) {
            report_error("switch(expr) zahteva int.", n);
        }

        java.util.HashSet<Integer> vidjeno = new java.util.HashSet<>();
        for (Integer v : caseVrednosti) {
            if (!vidjeno.add(v)) {
                report_error("Duplikat case vrednosti: " + v, n);
            }
        }
        caseVrednosti.clear();
    }
    
    @Override
    public void visit(Stmt_for n) {

    }
    
    @Override
    public void visit(ForBegin n) {
        forDepth++;
    }

    @Override
    public void visit(ForEnd n) {
        forDepth--;
        if (forDepth < 0) forDepth = 0; 
    }

    @Override
    public void visit(SwitchBegin n) {
        switchDepth++;
    }

    @Override
    public void visit(SwitchEnd n) {
        switchDepth--;
        if (switchDepth < 0) switchDepth = 0; 
    }
    
    @Override
    public void visit(Stmt_break n) {
        if (forDepth == 0 && switchDepth == 0) {
            report_error("break je dozvoljen samo unutar for ili switch.", n);
        }
    }

    @Override
    public void visit(Stmt_continue n) {
        if (forDepth == 0) {
            report_error("continue je dozvoljen samo unutar for.", n);
        }
    }
    
    public int getGlobalVarCount() {
        return globalVarCount;
    }

}
