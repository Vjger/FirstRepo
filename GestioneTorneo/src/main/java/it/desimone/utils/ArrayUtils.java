package it.desimone.utils;

import it.desimone.risiko.torneo.dto.Partita;

public class ArrayUtils {

	public static String fromPartiteToString(Partita[] partite){
		StringBuilder buffer = new StringBuilder(); 
		if (partite != null){
			for (Partita partita: partite){
				if (partita != null){
					buffer.append(partita.toString()+"\n");
				}
			}
		}
		
		return buffer.toString();
	}
	
	
	
	public static Partita[] clonaPartite(Partita[] partite){
		Partita[] clone = null;
		if (partite != null){
			clone = new Partita[partite.length];
			for (int index = 0; index < partite.length; index++){
				clone[index] = (Partita) partite[index].clone();
			}
		}
		return clone;
	}
	
}
