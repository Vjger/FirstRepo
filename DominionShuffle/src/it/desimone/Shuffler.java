package it.desimone;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.desimone.deck.Alchimia;
import it.desimone.deck.Base;
import it.desimone.deck.Deck;
import it.desimone.deck.DeckUtils;
import it.desimone.deck.Intrigo;
import it.desimone.deck.NuoviOrizzonti;
import it.desimone.deck.Prosperita;
import it.desimone.deck.Seaside;

public class Shuffler {
	
	private static short numeroTavoli = 6;
	private static short numeroSetBase = 3;

	public static void main(String[] args) throws IOException {
		
		if (args != null && args.length >= 1 && args[0] != null) numeroSetBase = Short.valueOf(args[0].trim());
		if (args != null && args.length  > 1 && args[1] != null) numeroTavoli = Short.valueOf(args[1].trim());
		
		Deck base = new Base();
		
		List<String> mazzoCarteBlu = new ArrayList<String>();
		for (short indexBase = 1; indexBase <= numeroSetBase; indexBase++){
			mazzoCarteBlu.addAll(base.cards());
		}
		
		if (args != null && args.length > 2){
			for (int i = 2; i < args.length; i++){
				mazzoCarteBlu = aggiungiEspansione(mazzoCarteBlu, args[i]);
			}			
		}else{
			Deck intrigo = new Intrigo();
			Deck prosperita = new Prosperita();
			Deck alchimia = new Alchimia();
			Deck nuoviOrizzonti = new NuoviOrizzonti();
			Deck seaside = new Seaside();
			mazzoCarteBlu.addAll(intrigo.cards());
			mazzoCarteBlu.addAll(prosperita.cards());
			mazzoCarteBlu.addAll(alchimia.cards());
			mazzoCarteBlu.addAll(nuoviOrizzonti.cards());
			mazzoCarteBlu.addAll(seaside.cards());
		}
		
		Collections.shuffle(mazzoCarteBlu);
		//System.out.println(mazzoCarteBlu);
		
		 List<List<String>> mazziTavoli = generaMazziTavoli(mazzoCarteBlu);
		 
		 File file = new File("TavoliDominion.txt");
		 BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
		 bufferedWriter.write("********************************************************************************************************", 0, 104);
		 bufferedWriter.newLine();
		 bufferedWriter.write("***  LEGENDA: <B> BASE - <I> INTRIGO - <S> SEASIDE - <P> PROSPERITA - <A> ALCHIMIA - <N> NUOVI ORIZZONTI  ***", 0, 104);
		 bufferedWriter.newLine();
		 bufferedWriter.write("********************************************************************************************************", 0, 104);
		 bufferedWriter.newLine();
		 bufferedWriter.newLine();
		 short numeroTavolo = 0;
		 for (List<String> mazzo: mazziTavoli){
			 numeroTavolo++;
			 //System.out.println("TAVOLO n° "+numeroTavolo);
			 String tavolo = "TAVOLO n° "+numeroTavolo;
			 bufferedWriter.write(tavolo, 0, tavolo.length());
			 bufferedWriter.newLine();
			 for (String card: mazzo){
				 String carte = card+DeckUtils.getSymbol(card)+" ";
				 //System.out.print(carte);
				 bufferedWriter.write(carte, 0, carte.length());
			 }
			 bufferedWriter.newLine();
			 //System.out.println("");
		 }
		 bufferedWriter.close();
	}
	
	private static List<String> aggiungiEspansione(List<String> mazzoCarteBlu, String espansione){
		if (espansione != null){
			if (espansione.trim().equalsIgnoreCase("I")) mazzoCarteBlu.addAll(new Intrigo().cards());
			if (espansione.trim().equalsIgnoreCase("P")) mazzoCarteBlu.addAll(new Prosperita().cards());
			if (espansione.trim().equalsIgnoreCase("A")) mazzoCarteBlu.addAll(new Alchimia().cards());
			if (espansione.trim().equalsIgnoreCase("N")) mazzoCarteBlu.addAll(new NuoviOrizzonti().cards());
			if (espansione.trim().equalsIgnoreCase("S")) mazzoCarteBlu.addAll(new Seaside().cards());
		}
		return mazzoCarteBlu;
	}
	
	private static List<List<String>> generaMazziTavoli(List<String> mazzoCarteBlu){
		List<List<String>> mazziTavoli = new ArrayList<List<String>>();
		
		short tavoloConPozione = 0;
		short tavoloConPunti = 0;
		
		short indexTavolo = 1;
			List<String> mazzoTavolo = new ArrayList<String>();
			for (String card: mazzoCarteBlu){
				
				if (!mazzoTavolo.contains(card) 
				&& (!Prosperita.usaPuntiVittoria(card) || tavoloConPunti == 0 || tavoloConPunti == indexTavolo)
				&& (Alchimia.nonSiCompraConPozione(card) || tavoloConPozione == 0 || tavoloConPozione == indexTavolo)
				){
					mazzoTavolo.add(card);
					if (Prosperita.usaPuntiVittoria(card)){
						tavoloConPunti = indexTavolo;
					}else if (!Alchimia.nonSiCompraConPozione(card)){
						tavoloConPozione = indexTavolo;
					}
					
				}
				
				if (mazzoTavolo.size() == 10){
					mazziTavoli.add(mazzoTavolo);
					if (mazziTavoli.size() == numeroTavoli){break;}
					mazzoTavolo =  new ArrayList<String>();
					indexTavolo++;
				}
			}
		
		return mazziTavoli;
	}

}
