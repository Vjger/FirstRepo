package it.desimone.deck;

import java.util.ArrayList;
import java.util.List;

public class Intrigo implements Deck {
	
	private static List<String> cards = new ArrayList<String>();
	
	static{
		cards.add("Ballo in maschera");
		cards.add("Baraccopoli");
		cards.add("Barone");
		cards.add("Battirame");
		cards.add("Carceriere");
		cards.add("Cortile di Campagna");
		cards.add("Cospiratori");
		cards.add("Duca");
		cards.add("Fonderia");
		cards.add("Grande Salone");
		cards.add("Harem");
		cards.add("Maggiordomo");
		cards.add("Nobili");
		cards.add("Pedina");
		cards.add("Ponte");
		cards.add("Potenziamento");
		cards.add("Pozzo dei desideri");
		cards.add("Sabotatore");
		cards.add("Scout");
		cards.add("Stanza segreta");
		cards.add("Stazione commerciale");
		cards.add("Tirapiedi");
		cards.add("Tributo");
		cards.add("Truffatore");
		cards.add("Villaggio minerario");
	}

	public List<String> cards() {
		return cards;
	}
	public static boolean isInDeck(String card) {
		return cards.contains(card);
	}
}
