package it.desimone.deck;

import java.util.ArrayList;
import java.util.List;

public class NuoviOrizzonti implements Deck {
	
	private static List<String> cards = new ArrayList<String>();
	
	static{
		cards.add("Campo nomadi");
		cards.add("Cartografo");
		cards.add("Commerciante");
		cards.add("Contrattazione");
		cards.add("Diplomatico");
		cards.add("Duchessa");
		cards.add("Forziere");
		cards.add("Galleria");
		cards.add("Incrocio");
		cards.add("Infame Profitto");
		cards.add("Ladro gentiluomo");
		cards.add("Locanda");
		cards.add("Macchinazione");
		cards.add("Mandarino");
		cards.add("Margravio");
		cards.add("Mercante di spezie");
		cards.add("Oasi");
		cards.add("Oracolo");
		cards.add("Oro dello stolto");
		cards.add("Stalla");
		cards.add("Strada maestra");
		cards.add("Sviluppo");
		cards.add("Terra coltivata");
		cards.add("Tuttofare");
		cards.add("Via della seta");
		cards.add("Villaggio di frontiera");
	}


	public List<String> cards() {
		return cards;
	}

	public static boolean isInDeck(String card) {
		return cards.contains(card);
	}
}
