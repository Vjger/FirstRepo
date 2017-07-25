package it.desimone.deck;

import java.util.ArrayList;
import java.util.List;

public class Base implements Deck {
	
	private static List<String> cards = new ArrayList<String>();
	
	static{
		cards.add("Banchetto");
		cards.add("Biblioteca");
		cards.add("Burocrate");
		cards.add("Cancelliere");
		cards.add("Cappella");
		cards.add("Esploratore");
		cards.add("Fiera");
		cards.add("Fossato");
		cards.add("Fucina");
		cards.add("Giardini");
		cards.add("Laboratorio");
		cards.add("Ladro");
		cards.add("Mercato");
		cards.add("Miglioria");
		cards.add("Milizia");
		cards.add("Miniera");
		cards.add("Officina");
		cards.add("Sala del Consiglio");
		cards.add("Sala del Trono");
		cards.add("Sotterraneo");
		cards.add("Spia");
		cards.add("Strega");
		cards.add("Taglialegna");
		cards.add("Usuraio");
		cards.add("Villaggio");
	}


	public List<String> cards() {
		return cards;
	}

	public static boolean isInDeck(String card) {
		return cards.contains(card);
	}
}
