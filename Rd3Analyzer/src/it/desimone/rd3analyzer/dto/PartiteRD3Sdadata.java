package it.desimone.rd3analyzer.dto;

import it.desimone.rd3analyzer.azioni.Sdadata;

public class PartiteRD3Sdadata extends PartiteRD3CommonData{


	private Integer dadiChiusura;
	private Integer dadiSdadata;
	
	public PartiteRD3Sdadata(Long idPartita, Sdadata sdadata){
		this.idPartita = idPartita;
		this.logId = sdadata.getLogId();
		this.coloreGiocatore = sdadata.getGiocatoreCheAgisce().getColore();
		this.time = sdadata.getTime();
		this.dadiChiusura = (int) sdadata.getChiusura();
		if (sdadata.getSdadata() != null){
			this.dadiSdadata = (int) sdadata.getSdadata();
		}
	}

	public Integer getDadiChiusura() {
		return dadiChiusura;
	}

	public void setDadiChiusura(Integer dadiChiusura) {
		this.dadiChiusura = dadiChiusura;
	}

	public Integer getDadiSdadata() {
		return dadiSdadata;
	}

	public void setDadiSdadata(Integer dadiSdadata) {
		this.dadiSdadata = dadiSdadata;
	}


	
}
