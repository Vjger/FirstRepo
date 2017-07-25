package it.desimone.deck;

import java.util.ArrayList;
import java.util.List;

public class Prosperita implements Deck {
	
	private static List<String> cards = new ArrayList<String>();
	
	static{
		cards.add("Ambulante");
		cards.add("Ampliamento");
		cards.add("Banca");
		cards.add("Cantiere");
		cards.add("Cava");
		cards.add("Ciarlatano");
		cards.add("Città");
		cards.add("Contabilità");
		cards.add("Contrabbando");
		cards.add("Corte");
		cards.add("Incastonare");
		cards.add("Mercante");
		cards.add("Mercato di lusso");
		cards.add("Monumento");
		cards.add("Mucchio");
		cards.add("Nascondiglio");
		cards.add("Prestito");
		cards.add("Scagnozzi");
		cards.add("Sigillo reale");
		cards.add("Soldo");
		cards.add("Talismano");
		cards.add("Torre di guardia");
		cards.add("Tumulto");
		cards.add("Vescovo");
		cards.add("Zecca");
	}

	public List<String> cards() {
		return cards;
	}
	
	public static boolean usaPuntiVittoria(String card){
		return card.equals("Monumento") || card.equals("Scagnozzi") || card.equals("Vescovo");
	}
	public static boolean isInDeck(String card) {
		return cards.contains(card);
	}
}
