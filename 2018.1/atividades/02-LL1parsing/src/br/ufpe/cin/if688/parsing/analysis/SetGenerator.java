package br.ufpe.cin.if688.parsing.analysis;

import java.util.*;

import br.ufpe.cin.if688.parsing.grammar.*;


public final class SetGenerator {

	public static Map<Nonterminal, Set<GeneralSymbol>> getFirst(Grammar g) {
        
    	if (g == null) throw new NullPointerException("g nao pode ser nula.");
        
    	Map<Nonterminal, Set<GeneralSymbol>> first = initializeNonterminalMapping(g);
    	/*
    	 * Implemente aqui o método para retornar o conjunto first
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
    			}
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
    
    //método para inicializar mapeamento nãoterminais -> conjunto de símbolos
    private static Map<Nonterminal, Set<GeneralSymbol>>
    initializeNonterminalMapping(Grammar g) {
    Map<Nonterminal, Set<GeneralSymbol>> result = 
        new HashMap<Nonterminal, Set<GeneralSymbol>>();

    for (Nonterminal nt: g.getNonterminals())
        result.put(nt, new HashSet<GeneralSymbol>());

    return result;
}

} 
