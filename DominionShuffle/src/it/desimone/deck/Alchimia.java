package it.desimone.deck;

import java.util.ArrayList;
import java.util.List;

public class Alchimia implements Deck {
	
	private static List<String> cards = new ArrayList<String>();
	
	static{
		cards.add("Alchimista");
		cards.add("Apprendista");
		cards.add("Divinazione");
		cards.add("Erborista");
		cards.add("Famiglio");
		cards.add("Farmacista");
		cards.add("Golem");
		cards.add("Pietra filosofale");
		cards.add("Possessione");
		cards.add("Trasmutazione");
		cards.add("Università");
		cards.add("Vigna");
	}


	public List<String> cards() {
		return cards;
	}
	
	public static boolean nonSiCompraConPozione(String card){
		return !cards.contains(card) || card.equals("Apprendista") || card.equals("Erborista");
	}
	public static boolean isInDeck(String card) {
		return cards.contains(card);
	}

}
