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
    	 * Implemente aqui o mÃ©todo para retornar o conjunto first
    	 */
    	Collection<Production> prod = g.getProductions();
    	Iterator<Production> x = prod.iterator();
    	while(x.hasNext()) {
    		boolean firstFound = false, notfound = true;
    		Production a = x.next();
    		Set<GeneralSymbol> ss = new HashSet<GeneralSymbol>();
    		
    		List<GeneralSymbol> ls = a.getProduction();
    		//System.out.println(ls + " " + a.getNonterminal());
    			for(GeneralSymbol gs : ls) { //gs quando um terminal symbol for achado precisa ir pro proximo da produção
    				if(!notfound) {break;}
        			switch(gs.getClass().getSimpleName().toLowerCase()) {
        			case "terminal":
        				if(!firstFound) {
        					ss.add(gs);
        					firstFound = true;
        				}
        				break;
        			case "nonterminal":
        				if(firstFound) {break;} //se o first da produção já for um terminal, ignorar o resto da produção
        				Nonterminal fprod = (Nonterminal) gs; //nterminal gerado pela produção, achar oq esse nterminal produz agora
    					Iterator<Production> az = prod.iterator();
        				while(az.hasNext()) {
        					Production z = az.next();
        					List<GeneralSymbol> lz = z.getProduction();
        					Nonterminal seek = z.getNonterminal();
        					if(seek.equals(fprod)) { //se for o terminal que a produção gerou
        						for(GeneralSymbol gst : lz) {
        							if(gst.getClass().getSimpleName().equalsIgnoreCase("nonterminal")) {
        								//Se o first da minha produção for um nao terminal, buscar novamente oq gera
        								fprod = (Nonterminal) gst; //se manter no loop até ser um terminal o obj encontrado
        							}else if(gst.getClass().getSimpleName().equalsIgnoreCase("specialsymbol")) { //se a produção for um terminal e e mesma produzir um epsilon, ignorar o epsilon e continuar a leitura da produção
        								int aux = ls.indexOf(seek)+1;
        								if(ls.get(aux).getClass().getSimpleName().equalsIgnoreCase("nonterminal")) {
        									fprod = (Nonterminal) ls.get(aux);
        								}
        							}	
        							else { 
        								ss.add(gst);
        							}
        							break;
        						}
        						
        					}
        				}
            			notfound = false;
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
		Iterator<Production> x = prod.iterator();
		while(x.hasNext()) {
			Production p1 = x.next();
			Nonterminal nt = p1.getNonterminal();
			Set<GeneralSymbol> ans = new HashSet<GeneralSymbol>();
			if(nt.equals(g.getStartSymbol())) { //se for o símbolo de start da gramática, então follow = $
				Set<GeneralSymbol> ends = new HashSet<GeneralSymbol>();
				GeneralSymbol end = SpecialSymbol.EOF;
				ends.add(end);
				follow.put(nt, ends);
			}else{
				Iterator<Production> f = prod.iterator();
				while(f.hasNext()) {
					boolean found = false;
					Production seek = f.next();
					List<GeneralSymbol> prodf = seek.getProduction();
					Iterator<GeneralSymbol> gs = prodf.iterator();
					while(gs.hasNext() && !found) {
						GeneralSymbol sp = gs.next();
						if(sp.equals(nt)) {
							if(gs.hasNext() && !found) { 
								GeneralSymbol sp1 = gs.next();
								if(sp1.getClass().getSimpleName().equalsIgnoreCase("nonterminal")) {
									follow.put((Nonterminal) sp, first.get(sp1));
								}else if(sp1.getClass().getSimpleName().equalsIgnoreCase("terminal")) {
									Set<GeneralSymbol> answ = new HashSet<GeneralSymbol>();
									answ.add(sp1);
									follow.put((Nonterminal) sp, answ);
								}
							}else {
								ans = follow.get(seek.getNonterminal());
								follow.put(nt, ans);
							}
						}
					}
					if(found) {
						break;
					}	
				}
			}
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
