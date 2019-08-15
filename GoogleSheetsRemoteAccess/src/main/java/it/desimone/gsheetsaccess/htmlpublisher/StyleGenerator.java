package it.desimone.gsheetsaccess.htmlpublisher;

import it.desimone.risiko.torneo.dto.SchedaTorneo.TipoTorneo;

public class StyleGenerator {

	public static String getStyle(String descrTipoTorneo){
		String style = "";
		TipoTorneo tipoTorneo = TipoTorneo.parseTipoTorneo(descrTipoTorneo);
		switch (tipoTorneo) {
		case CAMPIONATO_NAZIONALE:
			style = "color: #ffffff; text-shadow: 1px 1px 0 #000000; background-color: #3333cc;";
			break;
		case RADUNO_NAZIONALE:
			style = "color: #ffffff; text-shadow: 1px 1px 0 #000000; background-color: #cc0000;";
			break;
		case MASTER:
			style = "color: #ffffff; text-shadow: 1px 1px 0 #000000; background-color: #b33c00;";
			break;
		case OPEN:
			style = "color: #000000; background-color: #ffad33;";
			break;
		case INTERCLUB:
			style = "color: #000000; text-shadow: 1px 1px 0 #ffffff; background-color: #ffff00;";
			break;
		case CAMPIONATO:
			style = "color: #ffffff; background-color: #00b300;";
			break;
		default:
			style = "color: #000000; text-shadow: 1px 1px 0 #ffffff; background-color: #00ffff;";
			break;
		}
		return style;
	}
	
}
