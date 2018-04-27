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
import br.ufpe.cin.if688.symboltable.IntAndTable;
import br.ufpe.cin.if688.symboltable.Table;

public class Interpreter implements IVisitor<Table> {
	private IntAndTableVisitor iatv;
	//a=8;b=80;a=7;
	// a->7 ==> b->80 ==> a->8 ==> NIL
	private Table t;
	
	public Interpreter(Table t) {
		this.t = t;
	}

	@Override
	public Table visit(Stm s) {
		s.accept(this);
		return t;
	}
	

	@Override
	public Table visit(AssignStm s) {
		iatv = new IntAndTableVisitor(this.t);
		IntAndTable res = s.getExp().accept(iatv);
		t = new Table(s.getId(), res.result, this.t);
		return t; //EseqExp / ProgEseqExp precisam do return da table no assign
	}

	@Override
	public Table visit(CompoundStm s) {
		s.getStm1().accept(this);
		s.getStm2().accept(this);
		return null;
	}

	@Override
	public Table visit(PrintStm s) {
		ExpList el = s.getExps();
		iatv = new IntAndTableVisitor(this.t);
		while(true) {
			if(el instanceof PairExpList) { //se houver mais de um parametro, a tail contem outra explist
				IntAndTable aux = iatv.visit(((PairExpList) el).getHead());
				el = ((PairExpList) el).getTail();
				System.out.println(aux.result);
			}else { //se for o ultimo item imprimo e saio do laço pois nao tem tail no lastexplist
				IntAndTable aux = iatv.visit(((LastExpList) el).getHead());
				System.out.println(aux.result);
				break;
			}
		}
		return t;
	}

	@Override
	public Table visit(Exp e) { //quando o intandtablevisitor chama no visit do EseqExp o statement
		return e.accept(this); 
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
