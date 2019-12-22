package it.desimone.gsheetsaccess.dto;

import it.desimone.gsheetsaccess.gsheets.dto.AnagraficaGiocatoreRow;
import it.desimone.risiko.torneo.dto.SchedaTorneo;
import it.desimone.risiko.torneo.dto.SchedaTorneo.TipoTorneo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class ScorePlayer implements Comparable<ScorePlayer>{

	private AnagraficaGiocatoreRow anagraficaGiocatore;
	private BigDecimal scoreRanking = BigDecimal.ZERO;
	private Set<TabellinoPlayer> tabelliniPlayer = new TreeSet<ScorePlayer.TabellinoPlayer>(/*TabellinoPlayer.comparatorPerIdTorneo*/);
	private Map<TipoTorneo, TabellinoPerTipoTorneo> tabelliniPerTipoTorneo = new HashMap<SchedaTorneo.TipoTorneo, ScorePlayer.TabellinoPerTipoTorneo>();
	private Integer partiteGiocate = 0;
	private Integer partiteVinte = 0;
	
	public ScorePlayer(AnagraficaGiocatoreRow anagraficaGiocatore) {
		this.anagraficaGiocatore = anagraficaGiocatore;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((anagraficaGiocatore == null) ? 0 : anagraficaGiocatore
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScorePlayer other = (ScorePlayer) obj;
		if (anagraficaGiocatore == null) {
			if (other.anagraficaGiocatore != null)
				return false;
		} else if (!anagraficaGiocatore.equals(other.anagraficaGiocatore))
			return false;
		return true;
	}
	
	

	@Override
	public String toString() {
		return "ScorePlayer [anagraficaGiocatore=" + anagraficaGiocatore + "]";
	}



	public static class TabellinoPlayer implements Comparable<TabellinoPlayer>{
		private TorneoPubblicato torneo;
		private Integer posizioneRaggiunta;
		private BigDecimal scoreRanking;
		
		public TabellinoPlayer(TorneoPubblicato torneo,
				Integer posizioneRaggiunta, BigDecimal scoreRanking) {
			super();
			this.torneo = torneo;
			this.posizioneRaggiunta = posizioneRaggiunta;
			this.scoreRanking = scoreRanking;
		}
		public TorneoPubblicato getTorneo() {
			return torneo;
		}
		public void setTorneo(TorneoPubblicato torneo) {
			this.torneo = torneo;
		}
		public Integer getPosizioneRaggiunta() {
			return posizioneRaggiunta;
		}
		public void setPosizioneRaggiunta(Integer posizioneRaggiunta) {
			this.posizioneRaggiunta = posizioneRaggiunta;
		}
		public BigDecimal getScoreRanking() {
			return scoreRanking.setScale(1, RoundingMode.HALF_DOWN);
		}
		public void setScoreRanking(BigDecimal scoreRanking) {
			this.scoreRanking = scoreRanking;
		}
		
		public static Comparator<TabellinoPlayer> comparatorPerIdTorneo = new Comparator<TabellinoPlayer>() {
			@Override
			public int compare(TabellinoPlayer o1, TabellinoPlayer o2) {
				return o1.getTorneo().getTorneoRow().getIdTorneo().compareTo(o2.getTorneo().getTorneoRow().getIdTorneo());
			}
		};

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((torneo == null) ? 0 : torneo.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TabellinoPlayer other = (TabellinoPlayer) obj;
			if (torneo == null) {
				if (other.torneo != null)
					return false;
			} else if (!torneo.equals(other.torneo))
				return false;
			return true;
		}
		@Override
		public int compareTo(TabellinoPlayer o) {
			String thisIdTorneo = this.torneo.getIdTorneo();
			String otherIdTorneo = o.getTorneo().getIdTorneo();
			int result = thisIdTorneo.compareTo(otherIdTorneo);
			return result;
		}
		
		
	}
	
	public void addTabellinoPlayer(TorneoPubblicato torneo, Integer posizioneRaggiunta, BigDecimal scoreRanking){
		TabellinoPlayer tabellinoPlayer = new TabellinoPlayer(torneo, posizioneRaggiunta, scoreRanking);
		tabelliniPlayer.add(tabellinoPlayer);
	}
	
	public static class TabellinoPerTipoTorneo{
		private Integer numeroTorneiDisputati = 0;
		private List<BigDecimal> scoreRankings = new ArrayList<BigDecimal>();
		private BigDecimal scoreRanking = BigDecimal.ZERO;
		
		public TabellinoPerTipoTorneo(Integer numeroTorneiDisputati,
				BigDecimal scoreRanking) {
			super();
			this.numeroTorneiDisputati = numeroTorneiDisputati;
			//this.scoreRanking = scoreRanking;
			addScoreRanking(scoreRanking);
		}
		public Integer getNumeroTorneiDisputati() {
			return numeroTorneiDisputati;
		}
		public void addNumeroTorneiDisputati() {
			numeroTorneiDisputati++;
		}
		public void setNumeroTorneiDisputati(Integer numeroTorneiDisputati) {
			this.numeroTorneiDisputati = numeroTorneiDisputati;
		}
		public BigDecimal getScoreRanking() {
			return scoreRanking.setScale(1, RoundingMode.HALF_DOWN);
		}
		public void addScoreRanking(BigDecimal score) {
			scoreRanking = scoreRanking.add(score);
			scoreRankings.add(score);
		}
		public void setScoreRanking(BigDecimal scoreRanking) {
			this.scoreRanking = scoreRanking;
		}
		public List<BigDecimal> getScoreRankings() {
			return scoreRankings;
		}
	}

	public void addTabellinoPerTipoTorneo(BigDecimal scoreRanking, TipoTorneo tipoTorneo){
		TabellinoPerTipoTorneo tabellinoPerTipoTorneo = tabelliniPerTipoTorneo.get(tipoTorneo);
		if (tabellinoPerTipoTorneo == null){
			tabellinoPerTipoTorneo = new TabellinoPerTipoTorneo(1, scoreRanking);
		}else{
			tabellinoPerTipoTorneo.addNumeroTorneiDisputati();
			tabellinoPerTipoTorneo.addScoreRanking(scoreRanking);
		}
		tabelliniPerTipoTorneo.put(tipoTorneo, tabellinoPerTipoTorneo);
	}
	
	public TabellinoPerTipoTorneo getTabellino(TipoTorneo tipoTorneo){
		TabellinoPerTipoTorneo result = tabelliniPerTipoTorneo.get(tipoTorneo);
		if (result == null){
			result = new TabellinoPerTipoTorneo(0, BigDecimal.ZERO);
		}
		return result;
	}
	
	public TabellinoPerTipoTorneo getTabellinoRaduniNazionali(){
		TabellinoPerTipoTorneo result = getTabellino(TipoTorneo.RADUNO_NAZIONALE);
		return result;
	}
	public TabellinoPerTipoTorneo getTabellinoTorneiMaster(){
		TabellinoPerTipoTorneo result = getTabellino(TipoTorneo.MASTER);
		return result;
	}
	public TabellinoPerTipoTorneo getTabellinoTorneiOpen(){
		TabellinoPerTipoTorneo result = getTabellino(TipoTorneo.OPEN);
		return result;
	}
	public TabellinoPerTipoTorneo getTabellinoTorneiTorneiInterclub(){
		TabellinoPerTipoTorneo result = getTabellino(TipoTorneo.INTERCLUB);
		return result;
	}
	public TabellinoPerTipoTorneo getTabellinoCampionatiPeriodici(){
		TabellinoPerTipoTorneo result = getTabellino(TipoTorneo.CAMPIONATO);
		return result;
	}
	
	public BigDecimal getScoreRanking() {
		return scoreRanking.setScale(1, RoundingMode.HALF_DOWN);
	}

	public void setScoreRanking(BigDecimal scoreRanking) {
		this.scoreRanking = scoreRanking;
	}

	public void addScoreRanking(BigDecimal scoreRanking) {
		this.scoreRanking = this.scoreRanking.add(scoreRanking);
	}
	
	public AnagraficaGiocatoreRow getAnagraficaGiocatore() {
		return anagraficaGiocatore;
	}

	public void setAnagraficaGiocatore(AnagraficaGiocatoreRow anagraficaGiocatore) {
		this.anagraficaGiocatore = anagraficaGiocatore;
	}

	public Set<TabellinoPlayer> getTabelliniPlayer() {
		return tabelliniPlayer;
	}

	public void setTabelliniPlayer(Set<TabellinoPlayer> tabelliniPlayer) {
		this.tabelliniPlayer = tabelliniPlayer;
	}
	
	public Map<TipoTorneo, TabellinoPerTipoTorneo> getTabelliniPerTipoTorneo() {
		return tabelliniPerTipoTorneo;
	}

	public void setTabelliniPerTipoTorneo(
			Map<TipoTorneo, TabellinoPerTipoTorneo> tabelliniPerTipoTorneo) {
		this.tabelliniPerTipoTorneo = tabelliniPerTipoTorneo;
	}

	public void addPartiteGiocate() {
		partiteGiocate++;
	}
	
	public Integer getPartiteGiocate() {
		return partiteGiocate;
	}

	public void setPartiteGiocate(Integer partiteGiocate) {
		this.partiteGiocate = partiteGiocate;
	}

	public void addPartiteVinte() {
		partiteVinte++;
	}
	
	public Integer getPartiteVinte() {
		return partiteVinte;
	}

	public void setPartiteVinte(Integer partiteVinte) {
		this.partiteVinte = partiteVinte;
	}

	public int compareTo(ScorePlayer o) {
		int compare = 0;
		if (scoreRanking != null){
			compare = scoreRanking.compareTo(o.getScoreRanking());
		}
		return compare;
	}

	
	
}
