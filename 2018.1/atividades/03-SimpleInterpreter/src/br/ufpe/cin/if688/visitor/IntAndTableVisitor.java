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
	private Interpreter i;
	private boolean print = false;
	
	public IntAndTableVisitor(Object c) {
		
	}
	
	public IntAndTableVisitor(Table t, Interpreter I) {
		this.t = t;
		this.i = I;
	}

	@Override
	public IntAndTable visit(Stm s) {
		s.accept(this);
		return null;
	}

	@Override
	public IntAndTable visit(AssignStm s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntAndTable visit(CompoundStm s) {
		s.getStm1().accept(this);
		s.getStm2().accept(this);
		return null;
	}

	@Override
	public IntAndTable visit(PrintStm s) {
		s.getExps().accept(this);
		return null;
	}

	@Override
	public IntAndTable visit(Exp e) {
		e.accept(this);
		return null;
	}

	@Override
	public IntAndTable visit(EseqExp e) {
		e.getExp().accept(this);
		if(e.getStm() instanceof PrintStm) {
			print = true;
			e.getStm().accept(this);
		}
		return null;
	}

	@Override
	public IntAndTable visit(IdExp e) {
		Table tt = this.t;
		while(tt.tail != null) {
			if(tt.id.equals(e.getId())) {
				return new IntAndTable(tt.value, this.t);
			}
			else {
				tt = tt.tail;
			}
		}
		if(tt.id.equals(e.getId())) {
			if(print) {
				t = new Table(tt.id, tt.value, this.t);
				return new IntAndTable(tt.value, this.t);
			}else {
				return new IntAndTable(tt.value, this.t);
			}

		}
		return null;
	}

	@Override
	public IntAndTable visit(NumExp e) {
		return new IntAndTable(e.getNum(), this.t);
	}

	@Override
	public IntAndTable visit(OpExp e) {
		IntAndTable esq = e.getLeft().accept(this);
		IntAndTable dir = e.getRight().accept(this);
		int answ = -1;
		int a = esq.result;
		int b = dir.result;
		switch(e.getOper()){ //public final static int Plus = 1, Minus = 2, Times = 3, Div = 4;
		case 1:
			answ = a+b;
			break;
		case 2:
			answ = a - b;
			break;
		case 3:
			answ = a * b;
			break;
		case 4:
			if(b == 0) { throw new RuntimeException("Divisao por zero.");}
			else {
				answ = a / b;
			}
			break;
		default:
			break;
		}
		if(answ == -1) {throw new RuntimeException("Operacao invalida.");}
		this.t.value = answ;
		if(print)i.newEntry(t.id, t.value);
		return null;
	}

	@Override
	public IntAndTable visit(ExpList el) {
		el.accept(this);
		return null;
	}

	@Override
	public IntAndTable visit(PairExpList el) {
		el.getHead().accept(this);
		el.getTail().accept(this);
		return null;
	}

	@Override
	public IntAndTable visit(LastExpList el) {
		el.getHead().accept(this);
		return null;
	}


}
