package it.desimone.deck;

import java.util.ArrayList;
import java.util.List;

public class Seaside implements Deck {
	
	private static List<String> cards = new ArrayList<String>();
	
	static{
		cards.add("Avamposto");
		cards.add("Avventuriero");
		cards.add("Bazar");
		cards.add("Borseggiatore");
		cards.add("Carovana");
		cards.add("Contrabbandieri");
		cards.add("Embargo");
		cards.add("Faro");
		cards.add("Isola");
		cards.add("Magazzino");
		cards.add("Mappa del Tesoro");
		cards.add("Megera dei Mari");
		cards.add("Molo");
		cards.add("Nave Fantasma");
		cards.add("Nave Mercantile");
		cards.add("Nave Pirata");
		cards.add("Navigatore");
		cards.add("Pescatore di perle");
		cards.add("Rada");
		cards.add("Recupero");
		cards.add("Stratega");
		cards.add("Tesoro");
		cards.add("Vedetta");
		cards.add("Villaggio di pescatori");
		cards.add("Villaggio indigeno");
	}


	public List<String> cards() {
		return cards;
	}
	
	public static boolean isInDeck(String card) {
		return cards.contains(card);
	}

}
