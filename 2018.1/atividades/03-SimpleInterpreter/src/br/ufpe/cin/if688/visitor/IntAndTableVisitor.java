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

public class IntAndTableVisitor implements IVisitor<IntAndTable> {
	private Table t;
	//private boolean print = false;

	public IntAndTableVisitor(Table t) {
		this.t = t;
	}

	@Override
	public IntAndTable visit(Stm s) {
		return null;
	}

	@Override
	public IntAndTable visit(AssignStm s) {
		return null;
	}

	@Override
	public IntAndTable visit(CompoundStm s) {
		return null;
	}

	@Override
	public IntAndTable visit(PrintStm s) {
		return null;
	}

	@Override
	public IntAndTable visit(Exp e) {
		return e.accept(this);
	}

	@Override
	public IntAndTable visit(EseqExp e) {
		t = e.getStm().accept(new Interpreter(this.t));
		IntAndTable exp = e.getExp().accept(this);
		return new IntAndTable(exp.result, t);
	}

	@Override
	public IntAndTable visit(IdExp e) {
		Table aux = this.t;
		while(aux != null) {
			if(aux.id.equals(e.getId())) {
				break;
			}else {
				aux = aux.tail;
			}
		}
		return new IntAndTable(aux.value, this.t);
	}

	@Override
	public IntAndTable visit(NumExp e) {
		return new IntAndTable(e.getNum(), this.t);
	}

	@Override
	public IntAndTable visit(OpExp e) {
		IntAndTable esq = e.getLeft().accept(this);
		IntAndTable dir = e.getRight().accept(this);
		double a = esq.result;
		double b = dir.result;
		double answ = -99999;
		switch(e.getOper()) {
		case 1:
			answ = a + b;
			break;
		case 2:
			answ = a - b;
			break;
		case 3:
			answ = a * b;
			break;
		case 4: 
			answ = a / b;
			break;
		default:
			throw new RuntimeException("Operacao invalida.");
		}
		//System.out.println((double)answ);
		//this.t.value = answ;
		return new IntAndTable(answ, this.t);
	}

	@Override
	public IntAndTable visit(ExpList el) {
		return null;
	}

	@Override
	public IntAndTable visit(PairExpList el) {
		return null;
	}

	@Override
	public IntAndTable visit(LastExpList el) {
		return null;
	}


}
