package br.ufpe.cin.if688.parsing.analysis;

import java.lang.reflect.Field;
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
		boolean ssp = false;
		Set<GeneralSymbol> ends = new HashSet<GeneralSymbol>();
		while(x.hasNext()) {
			Production p1 = x.next();
			Nonterminal nt = p1.getNonterminal();
			if(nt.equals(g.getStartSymbol())) { //se for o símbolo de start da gramática, então follow = $				
				GeneralSymbol end = SpecialSymbol.EOF;
				ends.add(end);
				Iterator<Production> ff = prod.iterator();
				Nonterminal nts = g.getStartSymbol();
				while(ff.hasNext() && !ssp) {
					Production check = ff.next();
					List<GeneralSymbol> cls = check.getProduction();
					Iterator<GeneralSymbol> gsc = cls.iterator();
					while(gsc.hasNext()) {
						GeneralSymbol cs = gsc.next();
						if(nts.equals(cs)) {
							if(gsc.hasNext()) {
								cs = gsc.next();
								ends.add(cs);
							}
						}
						
					}
				}
				ssp = true;
				if(!ends.isEmpty()) follow.put(g.getStartSymbol(), ends);
				p1 = x.next();
				nt = p1.getNonterminal();
			}

			Iterator<Production> f = prod.iterator();
			Boolean found = false;
			
			while(f.hasNext() && !found) {
				GeneralSymbol ntt = nt;
				Set<GeneralSymbol> ans = new HashSet<GeneralSymbol>();
				Production seek = f.next();
				List<GeneralSymbol> prodf = seek.getProduction();
				Iterator<GeneralSymbol> gs = prodf.iterator();
				int ct = 0;
				boolean ee = true, e1 = false;
				while(gs.hasNext()) {
					GeneralSymbol sym = prodf.get(ct);
					if(sym.equals(ntt)){
						if(ct+1 < prodf.size()) {
							if(ee) {sym = prodf.get(ct+1);}	
							String sw = sym.getClass().getSimpleName().toLowerCase();
							switch(sw) {
							case "nonterminal":									
								ans.addAll(first.get(sym));
								boolean eps = ans.remove(SpecialSymbol.EPSILON);
								if(eps) {
									ans.addAll(follow.get(seek.getNonterminal()));
									if(ee) {
										if(ct+2 < prodf.size()) {
											if (prodf.get(ct+2).getClass().getSimpleName().equalsIgnoreCase("nonterminal")) {
												ntt = (Nonterminal) prodf.get(ct+2);
												ee = false;
											}else if(prodf.get(ct+2).getClass().getSimpleName().equalsIgnoreCase("terminal")) {
												ans.add(prodf.get(ct+2));
												ee = false;
												if(ct+2 == prodf.size()-1) {
													ans.removeAll(follow.get(seek.getNonterminal()));
												}
												
											}	
										}
									}else {
										if(ct+1 < prodf.size()) {
											if (prodf.get(ct+1).getClass().getSimpleName().equalsIgnoreCase("nonterminal")) {
												ntt = (Nonterminal) prodf.get(ct+1);
												ee = false;
											}else if(prodf.get(ct+1).getClass().getSimpleName().equalsIgnoreCase("terminal")) {
												ans.add(prodf.get(ct+1));
												ee = false;
											}
										}
									}
										
								}else {
									found = true;
									if(gs.hasNext()) {ans.removeAll(follow.get(seek.getNonterminal()));}
									
								}
								break;
							case "terminal":
								ans.add(sym);
								found = true;
								break;
							default:
								break;
							}
							
						}else{
								ans.addAll(follow.get(seek.getNonterminal()));
								found = true;
								//fim da produção
							break;
						}
					}
					ct++;
					gs.next();
					if (found) {
						break;
					}
				}
				if(!ans.isEmpty()) follow.put(nt, ans);
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
