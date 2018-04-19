package br.ufpe.cin.if688.visitor;

import br.ufpe.cin.if688.ast.AssignStm;
import br.ufpe.cin.if688.ast.CompoundStm;
import br.ufpe.cin.if688.ast.EseqExp;
import br.ufpe.cin.if688.ast.Exp;
import br.ufpe.cin.if688.ast.ExpList;
import br.ufpe.cin.if688.ast.IdExp;
import br.ufpe.cin.if688.ast.LastExpList;
import br.ufpe.cin.if688.ast.NumExp;
import br.ufpe.cin.if688.ast.OpExp;
import br.ufpe.cin.if688.ast.PairExpList;
import br.ufpe.cin.if688.ast.PrintStm;
import br.ufpe.cin.if688.ast.Stm;
import br.ufpe.cin.if688.symboltable.Table;

public class Interpreter implements IVisitor<Table> {
	private IntAndTableVisitor iatv = new IntAndTableVisitor(null);
	//a=8;b=80;a=7;
	// a->7 ==> b->80 ==> a->8 ==> NIL
	private Table t;
	
	public Interpreter(Table t) {
		this.iatv = new IntAndTableVisitor(t, this);
		this.t = t;
	}

	@Override
	public Table visit(Stm s) {
		s.accept(this);
		/*Table tt = this.t;
		if(tt == null) {return null;}
		for(; tt.tail != null; tt = tt.tail) {
			System.out.print(tt.id + "->" + tt.value + " ==> ");
		}
		System.out.println(tt.id + "->" + tt.value + " ==> " + "NIL");*/
		return t;
	}
	
	public void newEntry(String id, int val) {
		t = new Table(id, val, this.t);
	}

	@Override
	public Table visit(AssignStm s) {
		t = new Table(s.getId(), 0, this.t);
		iatv = new IntAndTableVisitor(this.t, this);
		iatv.visit(s.getExp());
		return null;
	}

	@Override
	public Table visit(CompoundStm s) {
		s.getStm1().accept(this);
		s.getStm2().accept(this);
		return null;
	}

	@Override
	public Table visit(PrintStm s) {
		return null;
	}

	@Override
	public Table visit(Exp e) {
		return null;
	}

	@Override
	public Table visit(EseqExp e) {
		
		return null;
	}

	@Override
	public Table visit(IdExp e) {

		return null;
	}

	@Override
	public Table visit(NumExp e) {

		return null;
	}

	@Override
	public Table visit(OpExp e) {
		
		return null;
	}

	@Override
	public Table visit(ExpList el) {
		
		return null;
	}

	@Override
	public Table visit(PairExpList el) {

		return null;
	}

	@Override
	public Table visit(LastExpList el) {
		
		return null;
	}


}
