package it.desimone.risiko.torneo.dto;

public class GiocatoreDTO implements Comparable{
	
	public static final GiocatoreDTO FITTIZIO;

	static{
		FITTIZIO = new GiocatoreDTO();
		FITTIZIO.setId(-1);
		FITTIZIO.setNome("The");
		FITTIZIO.setCognome("Ghost");
		FITTIZIO.setEmail("TheGhost@theghost.it");
	}
	
	private static final long serialVersionUID = 1;
	
	private static final String SEPARATOR="\t";
	
	private Integer id = 0;
	
	private String nome;
	private String cognome;
	private String email;
	private String nick;
	private RegioneDTO regioneProvenienza;
	private ClubDTO clubProvenienza;
	private Boolean presenteTorneo;
	
	private Boolean fissoAlTipoDiTavolo = Boolean.FALSE;
	
	public GiocatoreDTO(){}
	
	public String toString(){
		StringBuilder buffer = new StringBuilder();
		buffer.append(nome);
		buffer.append(" "+cognome);
		buffer.append(" [");
		if (clubProvenienza != null){
			buffer.append(clubProvenienza);
		}
		buffer.append("]");
//		if (nick != null){
//			buffer.append(" ("+nick+")");
//		}
//		if (clubProvenienza != null){
//			buffer.append(" del club "+clubProvenienza);			
//		}

		return buffer.toString();
	}
	
	public boolean equals(Object o){
		GiocatoreDTO giocatore = (GiocatoreDTO) o;
		return giocatore.getId().equals(this.getId());
	}

	public int hashCode(){
		return id;
	}
	
	public String toExcel(){
		StringBuilder buffer = new StringBuilder();
		buffer.append(nome+SEPARATOR);
		buffer.append(cognome+SEPARATOR);
		buffer.append(email+SEPARATOR);
		buffer.append(nick!=null?nick:""+SEPARATOR);
		buffer.append(regioneProvenienza+SEPARATOR);
		buffer.append(clubProvenienza!=null?clubProvenienza:""+SEPARATOR);
		buffer.append(presenteTorneo+SEPARATOR);
		return buffer.toString();
	}

	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getCognome() {
		return cognome;
	}


	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNick() {
		return nick;
	}


	public void setNick(String nick) {
		this.nick = nick;
	}

	public RegioneDTO getRegioneProvenienza() {
		return regioneProvenienza;
	}


	public void setRegioneProvenienza(RegioneDTO regioneProvenienza) {
		this.regioneProvenienza = regioneProvenienza;
	}


	public ClubDTO getClubProvenienza() {
		return clubProvenienza;
	}


	public void setClubProvenienza(ClubDTO club) {
		this.clubProvenienza = club;
	}


	public Boolean getPresenteTorneo() {
		return presenteTorneo;
	}


	public void setPresenteTorneo(Boolean presenteTorneo) {
		this.presenteTorneo = presenteTorneo;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	

	public Boolean isFissoAlTipoDiTavolo() {
		return fissoAlTipoDiTavolo;
	}

	public void setFissoAlTipoDiTavolo(Boolean fissoAlTipoDiTavolo) {
		this.fissoAlTipoDiTavolo = fissoAlTipoDiTavolo;
	}

	public int compareTo(Object arg0) {
		GiocatoreDTO giocatore = (GiocatoreDTO) arg0;
		return giocatore.getId().compareTo(this.id);
	}
	
	

}
