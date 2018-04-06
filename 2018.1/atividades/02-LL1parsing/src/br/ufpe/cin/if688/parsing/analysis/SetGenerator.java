package br.ufpe.cin.if688.parsing.analysis;

import java.util.*;

import br.ufpe.cin.if688.parsing.grammar.*;


public final class SetGenerator {
	
	//public static  talvez economizar codigo fazendo recursivamente quando o first for nonterminal

	public static List<GeneralSymbol> nonter(GeneralSymbol nonterminal, Grammar g) {
		Collection<Production> prod = g.getProductions();
		Iterator<Production> it = prod.iterator();
		while(it.hasNext()) {
			Production a = it.next();
			if(a.getNonterminal().equals(nonterminal)) {
				List<GeneralSymbol> ls = a.getProduction();
				for(GeneralSymbol gs : ls) {
					if(gs.toString().charAt(0) == '<') {
						nonter(gs, g);
					}
				}
			}
		}
		return null;
	}
	
	
	public static Map<Nonterminal, Set<GeneralSymbol>> getFirst(Grammar g) {
        
    	if (g == null) throw new NullPointerException("g nao pode ser nula.");
        
    	Map<Nonterminal, Set<GeneralSymbol>> first = initializeNonterminalMapping(g);
    	/*
    	 * Implemente aqui o método para retornar o conjunto first
    	 */
    	Collection<Production> prod = g.getProductions();
    	Iterator<Production> x = prod.iterator();
    	while(x.hasNext()) {
    		boolean firstFound = false;
    		Production a = x.next();
    		Set<GeneralSymbol> ss = new HashSet<GeneralSymbol>();
    		
    		List<GeneralSymbol> ls = a.getProduction();
    		//System.out.println(ls + " " + a.getNonterminal());
    			for(GeneralSymbol gs : ls) {
        			switch(gs.getClass().getSimpleName().toLowerCase()) {
        			case "terminal":
        				if(!firstFound) {
        					ss.add(gs);
        					firstFound = true;
        				}
        				break;
        			case "nonterminal":
        				Iterator<Production> az = prod.iterator();
        				while(az.hasNext()) {
        					Production z = az.next();
        					List<GeneralSymbol> lz = z.getProduction();
        					Nonterminal seek = z.getNonterminal();
        					if(seek.equals(a.getNonterminal())) {
        						
        					}
        				}
        				break;
        			case "specialsymbol":
        				ss.add(gs);
        				break;
        			default:
        				break;
        			}
    		}
    			if(first.get(a.getNonterminal()).isEmpty()){
    				first.put(a.getNonterminal(), ss);
    			}else{
    				Object[] arr = ss.toArray();
    				if(!arr[0].getClass().getSimpleName().equals("Terminal")) {
    					SpecialSymbol sp = (SpecialSymbol) arr[0];
            			first.get(a.getNonterminal()).add(sp);
    				}else{
    					Terminal t = (Terminal) arr[0];
    					first.get(a.getNonterminal()).add(t);
    				}
    			} 		
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
