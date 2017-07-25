package it.desimone.risiko.torneo.utils;

import it.desimone.risiko.torneo.dto.GiocatoreDTO;
import it.desimone.risiko.torneo.dto.Partita;
import it.desimone.risiko.torneo.dto.ScorePlayer;
import it.desimone.utils.MyException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TorneiUtils {
	
	public static boolean vincitoriUnici = Boolean.FALSE;
	
	public static List<GiocatoreDTO> fromScorePlayersToPlayers(List<ScorePlayer> scorePlayers){
		List<GiocatoreDTO> players = null;
		if (scorePlayers != null){
			players = new ArrayList<GiocatoreDTO>();
			for (ScorePlayer scorePlayer: scorePlayers){
				if (scorePlayer != null){
					players.add(scorePlayer.getGiocatore());
				}
			}
		}
		return players;
	}	
	
	private static List<Partita> trovaPartiteConPiuVincitori(List<Partita> partite){
		List<Partita> result = new ArrayList<Partita>(partite);
		Iterator<Partita> iterator = result.iterator();
		while (iterator.hasNext()){
			Partita partita = iterator.next();
			if (partita == null || partita.numeroVincitori() == 1){
				iterator.remove();
			}
		}
		return result;
	}
	
	
	public static void checkPartiteConPiuVincitori(List<Partita> partite){
		if (!vincitoriUnici) return;
		if (partite != null){
			List<Partita> partiteConPiuVincitori = trovaPartiteConPiuVincitori(partite);
			if (partiteConPiuVincitori != null && !partiteConPiuVincitori.isEmpty()){
				StringBuilder buffer = new StringBuilder("C'è più di un vincitore nelle partite");
				for (Partita partita: partiteConPiuVincitori){
					buffer.append("\n"+partita);
				}
				throw new MyException(buffer.toString());
			}
		}
	}
	
	public static void checksPartiteConPiuVincitori(List<Partita[]> listaPartiteArray){
		if (!vincitoriUnici) return;
		if (listaPartiteArray != null && !listaPartiteArray.isEmpty()){
			for (Partita[] partite: listaPartiteArray){
				if (partite != null){
					checkPartiteConPiuVincitori(Arrays.asList(partite));
				}
			}
		}
	}
	
	public static void checksPartiteConPiuVincitori(Partita[] partiteArray){
		if (!vincitoriUnici) return;
		if (partiteArray != null){
			checkPartiteConPiuVincitori(Arrays.asList(partiteArray));
		}
	}
}
