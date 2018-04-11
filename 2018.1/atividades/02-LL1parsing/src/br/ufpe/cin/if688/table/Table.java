package br.ufpe.cin.if688.table;


import br.ufpe.cin.if688.parsing.analysis.*;
import br.ufpe.cin.if688.parsing.grammar.*;
import java.util.*;


public final class Table {
	private Table() {    }

	public static Map<LL1Key, List<GeneralSymbol>> createTable(Grammar g) throws NotLL1Exception {
        if (g == null) throw new NullPointerException();

        Map<Nonterminal, Set<GeneralSymbol>> first =
            SetGenerator.getFirst(g);
        Map<Nonterminal, Set<GeneralSymbol>> follow =
            SetGenerator.getFollow(g, first);

        Map<LL1Key, List<GeneralSymbol>> parsingTable = 
            new HashMap<LL1Key, List<GeneralSymbol>>();
       
        Collection<Production> prod = g.getProductions();
        for(Production p : prod) {
        	boolean cont = false;
        	LL1Key key;
        		for(GeneralSymbol gs : p.getProduction()) {
        			String cl = gs.getClass().getSimpleName().toLowerCase();
        			if(cl.equals("terminal")) {
        				if(first.get(p.getNonterminal()).contains(gs)) {
        					key = new LL1Key(p.getNonterminal(), gs);
        					parsingTable.put(key, p.getProduction());
        				}
        			}else if(cl.equals("specialsymbol")) {
        				cont = true;
        			}
        			else{
        				break;
        			}
        			if(cont){
        				boolean EOF = follow.get(p.getNonterminal()).contains(SpecialSymbol.EOF);
            			boolean EPS = first.get(p.getNonterminal()).contains(SpecialSymbol.EPSILON);
            			if(EPS && !EOF) {
            				for(GeneralSymbol fl : follow.get(p.getNonterminal())) {
                				key = new LL1Key(p.getNonterminal(), fl); //<K>,d - d follow K
            					String s = fl.getClass().getSimpleName().toLowerCase();
            					if(s.equals("terminal")) {
            						parsingTable.put(key, p.getProduction());
            					}
            				}
            			}else if(EPS && EOF) {
            				
            			}
        			}
        		}

        }

        /*
         * Implemente aqui o método para retornar a parsing table
         */
        
        return parsingTable;
    }
}
