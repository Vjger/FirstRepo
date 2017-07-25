package it.desimone.deck;

public class DeckUtils {

	public static String getSymbol(String card){
		String symbol = "";
		if (Base.isInDeck(card)){
			symbol = "<B>";
		}else if (Intrigo.isInDeck(card)){
			symbol = "<I>";
		}else if (Prosperita.isInDeck(card)){
			symbol = "<P>";
		}else if (Alchimia.isInDeck(card)){
			symbol = "<A>";
		}else if (NuoviOrizzonti.isInDeck(card)){
			symbol = "<N>";
		}else if (Seaside.isInDeck(card)){
			symbol = "<S>";
		}
		return symbol;
	}
}
