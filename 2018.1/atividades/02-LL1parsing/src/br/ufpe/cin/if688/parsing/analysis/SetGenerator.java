package br.ufpe.cin.if688.parsing.analysis;

import java.util.*;

import br.ufpe.cin.if688.parsing.grammar.*;


public final class SetGenerator {
	
	//public static  talvez economizar codigo fazendo recursivamente quando o first for nonterminal

	public static Map<Nonterminal, Set<GeneralSymbol>> getFirst(Grammar g) {
        
    	if (g == null) throw new NullPointerException("g nao pode ser nula.");
        
    	Map<Nonterminal, Set<GeneralSymbol>> first = initializeNonterminalMapping(g);
    	/*
    	 * Implemente aqui o mÃ©todo para retornar o conjunto first
    	 */
    	//System.out.println(g.getProductions());
    	//nonterminal, terminal, specialsymbol
    	Collection<Production> prod = g.getProductions();
    	Iterator<Production> x = prod.iterator();
    	
    	while(x.hasNext()) {
    		Production a = x.next();
    		Set<GeneralSymbol> ss = first.get(a.getNonterminal());
    		List<GeneralSymbol> ls = a.getProduction();
    		for(int i = 0; i < ls.size(); i++) {
    			if(ls.get(i).getClass().getSimpleName().equalsIgnoreCase("terminal")) {
    				ss.add(ls.get(i));
    				break;
    			}else if(ls.get(i).getClass().getSimpleName().equalsIgnoreCase("specialsymbol")) {
    				ss.add(ls.get(i));
    				break;
    			}else if(ls.get(i).getClass().getSimpleName().equalsIgnoreCase("nonterminal")) {
    				Nonterminal aa = (Nonterminal) a.getProduction().get(i);
    				//tenho que achar a produção desse não terminal
    				boolean found = false;
    				while(!found) {
    					Iterator<Production> y = prod.iterator();
    					while(y.hasNext()) {
        					Production k = y.next();
        					if(k.getNonterminal().equals(aa)) { //se ao percorrer novamente as produções acho o nao terminal, vejo o first do mesmo
        						
        					}
        				}
    				}
    				
    				
    			}
    			System.out.println();
    		}
    		first.put(a.getNonterminal(), ss);
    		
    	}  
        return first;
    	
    }

    
    public static Map<Nonterminal, Set<GeneralSymbol>> getFollow(Grammar g, Map<Nonterminal, Set<GeneralSymbol>> first) {
       
        if (g == null || first == null)
            throw new NullPointerException();
                
        Map<Nonterminal, Set<GeneralSymbol>> follow = initializeNonterminalMapping(g);
        Collection<Production> prod = g.getProductions();
        Nonterminal start = g.getStartSymbol();
    		Iterator<Production> x = prod.iterator();
    		while(x.hasNext()) {
    			Production a = x.next();
    			Set<GeneralSymbol> sx = first.get(a.getNonterminal());
    			List<GeneralSymbol> ls = a.getProduction();
    			if(a.getNonterminal().equals(start)) {
    				sx.add(SpecialSymbol.EOF);
    				//ss.clear();
    			}else{
    				
    			}
    			follow.put(a.getNonterminal(), sx);
    		} 
        
        return follow;
    }
    
    //mÃ©todo para inicializar mapeamento nÃ£oterminais -> conjunto de sÃ­mbolos
    private static Map<Nonterminal, Set<GeneralSymbol>>
    initializeNonterminalMapping(Grammar g) {
    Map<Nonterminal, Set<GeneralSymbol>> result = 
        new HashMap<Nonterminal, Set<GeneralSymbol>>();

    for (Nonterminal nt: g.getNonterminals())
        result.put(nt, new HashSet<GeneralSymbol>());

    return result;
}

} 
