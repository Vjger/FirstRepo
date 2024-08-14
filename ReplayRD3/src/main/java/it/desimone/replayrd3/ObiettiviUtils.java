package it.desimone.replayrd3;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ObiettiviUtils {

	private final static Set<Territorio>[] obiettivi = new Set[16];
	
	static{
		Set<Territorio> obiettivo1 = new HashSet<Territorio>();
		obiettivo1.addAll(Territorio.getTerritoriNordAmerica());
		obiettivo1.addAll(Territorio.getTerritoriSudAmerica());
		obiettivo1.addAll(Territorio.getTerritoriAfrica());
		obiettivo1.remove(Territorio.AFRICA_DEL_SUD);
		obiettivo1.remove(Territorio.MADAGASCAR);
		obiettivo1.add(Territorio.MEDIO_ORIENTE);
		obiettivo1.add(Territorio.INDIA);
		obiettivo1.add(Territorio.SIAM);
		obiettivo1.addAll(Territorio.getTerritoriOceania());
		obiettivo1.remove(Territorio.AUSTRALIA_ORIENTALE);
		
		Set<Territorio> obiettivo2 = new HashSet<Territorio>();
		obiettivo2.add(Territorio.GROENLANDIA);
		obiettivo2.add(Territorio.ONTARIO);
		obiettivo2.add(Territorio.QUEBEC);
		obiettivo2.add(Territorio.STATI_UNITI_ORIENTALI);
		obiettivo2.addAll(Territorio.getTerritoriEuropa());
		obiettivo2.addAll(Territorio.getTerritoriAfrica());
		obiettivo2.add(Territorio.MEDIO_ORIENTE);
		obiettivo2.add(Territorio.AFGHANISTAN);
		obiettivo2.add(Territorio.URALI);
		
		Set<Territorio> obiettivo3 = new HashSet<Territorio>();
		obiettivo3.addAll(Territorio.getTerritoriEuropa());
		obiettivo3.addAll(Territorio.getTerritoriAfrica());
		obiettivo3.add(Territorio.MEDIO_ORIENTE);
		obiettivo3.add(Territorio.AFGHANISTAN);
		obiettivo3.add(Territorio.URALI);
		obiettivo3.add(Territorio.INDIA);
		obiettivo3.add(Territorio.SIAM);
		obiettivo3.addAll(Territorio.getTerritoriOceania());
		
		Set<Territorio> obiettivo4 = new HashSet<Territorio>();
		obiettivo4.addAll(Territorio.getTerritoriEuropa());
		obiettivo4.remove(Territorio.EUROPA_OCCIDENTALE);
		obiettivo4.addAll(Territorio.getTerritoriAfrica());
		obiettivo4.addAll(Territorio.getTerritoriNordAmerica());

		Set<Territorio> obiettivo5 = new HashSet<Territorio>();
		obiettivo5.addAll(Territorio.getTerritoriNordAmerica());
		obiettivo5.addAll(Territorio.getTerritoriOceania());
		obiettivo5.add(Territorio.ISLANDA);
		obiettivo5.add(Territorio.SCANDINAVIA);
		obiettivo5.add(Territorio.UCRAINA);
		obiettivo5.add(Territorio.URALI);
		obiettivo5.add(Territorio.AFGHANISTAN);
		obiettivo5.add(Territorio.MEDIO_ORIENTE);
		obiettivo5.add(Territorio.INDIA);
		obiettivo5.add(Territorio.SIAM);
		obiettivo5.add(Territorio.CINA);
		
		Set<Territorio> obiettivo6 = new HashSet<Territorio>();
		obiettivo6.addAll(Territorio.getTerritoriSudAmerica());
		obiettivo6.addAll(Territorio.getTerritoriOceania());
		obiettivo6.addAll(Territorio.getTerritoriEuropa());
		obiettivo6.add(Territorio.AFRICA_DEL_NORD);
		obiettivo6.add(Territorio.EGITTO);
		obiettivo6.add(Territorio.AFRICA_ORIENTALE);
		obiettivo6.add(Territorio.AFGHANISTAN);
		obiettivo6.add(Territorio.MEDIO_ORIENTE);
		obiettivo6.add(Territorio.INDIA);
		obiettivo6.add(Territorio.SIAM);
		
		Set<Territorio> obiettivo7 = new HashSet<Territorio>();
		obiettivo7.addAll(Territorio.getTerritoriSudAmerica());
		obiettivo7.addAll(Territorio.getTerritoriAfrica());
		obiettivo7.addAll(Territorio.getTerritoriAsia());
		
		Set<Territorio> obiettivo8 = new HashSet<Territorio>();
		obiettivo8.addAll(Territorio.getTerritoriSudAmerica());
		obiettivo8.addAll(Territorio.getTerritoriNordAmerica());
		obiettivo8.addAll(Territorio.getTerritoriEuropa());
		obiettivo8.add(Territorio.KAMCHATKA);
		obiettivo8.add(Territorio.GIAPPONE);
		
		Set<Territorio> obiettivo9 = new HashSet<Territorio>();
		obiettivo9.addAll(Territorio.getTerritoriEuropa());
		obiettivo9.addAll(Territorio.getTerritoriAsia());
		obiettivo9.add(Territorio.INDONESIA);

		Set<Territorio> obiettivo10 = new HashSet<Territorio>();
		obiettivo10.addAll(Territorio.getTerritoriEuropa());
		obiettivo10.addAll(Territorio.getTerritoriNordAmerica());
		obiettivo10.add(Territorio.URALI);
		obiettivo10.add(Territorio.SIBERIA);
		obiettivo10.add(Territorio.JACUZIA);
		obiettivo10.add(Territorio.KAMCHATKA);
		obiettivo10.add(Territorio.GIAPPONE);
		
		Set<Territorio> obiettivo11 = new HashSet<Territorio>();
		obiettivo11.addAll(Territorio.getTerritoriEuropa());
		obiettivo11.addAll(Territorio.getTerritoriSudAmerica());
		obiettivo11.addAll(Territorio.getTerritoriAfrica());
		obiettivo11.add(Territorio.URALI);
		obiettivo11.add(Territorio.AFGHANISTAN);
		obiettivo11.add(Territorio.MEDIO_ORIENTE);
		obiettivo11.add(Territorio.SIBERIA);

		Set<Territorio> obiettivo12 = new HashSet<Territorio>();
		obiettivo12.addAll(Territorio.getTerritoriAsia());
		obiettivo12.addAll(Territorio.getTerritoriAfrica());
		obiettivo12.add(Territorio.UCRAINA);
		obiettivo12.add(Territorio.EUROPA_MERIDIONALE);
		
		Set<Territorio> obiettivo13 = new HashSet<Territorio>();
		obiettivo13.addAll(Territorio.getTerritoriAsia());
		obiettivo13.addAll(Territorio.getTerritoriNordAmerica());
		
		Set<Territorio> obiettivo14 = new HashSet<Territorio>();
		obiettivo14.addAll(Territorio.getTerritoriSudAmerica());
		obiettivo14.addAll(Territorio.getTerritoriAfrica());
		obiettivo14.add(Territorio.EUROPA_OCCIDENTALE);
		obiettivo14.add(Territorio.EUROPA_MERIDIONALE);
		obiettivo14.add(Territorio.MEDIO_ORIENTE);
		obiettivo14.add(Territorio.INDIA);
		obiettivo14.add(Territorio.SIAM);
		obiettivo14.add(Territorio.CINA);
		obiettivo14.add(Territorio.MONGOLIA);
		obiettivo14.add(Territorio.CITA);
		obiettivo14.add(Territorio.GIAPPONE);
		obiettivo14.addAll(Territorio.getTerritoriOceania());
		
		Set<Territorio> obiettivo15 = new HashSet<Territorio>();
		obiettivo15.addAll(Territorio.getTerritoriAsia());
		obiettivo15.addAll(Territorio.getTerritoriOceania());
		obiettivo15.addAll(Territorio.getTerritoriAfrica());
		obiettivo15.remove(Territorio.AFRICA_DEL_NORD);
		obiettivo15.add(Territorio.ALASKA);
		obiettivo15.add(Territorio.ALBERTA);
		
		Set<Territorio> obiettivo16 = new HashSet<Territorio>();
		obiettivo16.addAll(Territorio.getTerritoriNordAmerica());
		obiettivo16.addAll(Territorio.getTerritoriSudAmerica());
		obiettivo16.addAll(Territorio.getTerritoriAfrica());
		obiettivo16.add(Territorio.EUROPA_OCCIDENTALE);
		obiettivo16.add(Territorio.EUROPA_MERIDIONALE);
		obiettivo16.add(Territorio.UCRAINA);
		
		obiettivi[0] = obiettivo1;
		obiettivi[1] = obiettivo2;
		obiettivi[2] = obiettivo3;
		obiettivi[3] = obiettivo4;
		obiettivi[4] = obiettivo5;
		obiettivi[5] = obiettivo6;
		obiettivi[6] = obiettivo7;
		obiettivi[7] = obiettivo8;
		obiettivi[8] = obiettivo9;
		obiettivi[9] = obiettivo10;
		obiettivi[10] = obiettivo11;
		obiettivi[11] = obiettivo12;
		obiettivi[12] = obiettivo13;
		obiettivi[13] = obiettivo14;
		obiettivi[14] = obiettivo15;
		obiettivi[15] = obiettivo16;
	}
	
	
	public static int determinaNumeroObiettivo(Collection<Territorio> territoriInObiettivo){
		int result = 0;
		boolean found = false;		
		for (int index = 0; index < obiettivi.length && !found; index++){
			found = territoriInObiettivo.containsAll(obiettivi[index]);
			if (found){result = index+1;}
		}
		return result;
	}
	
	public static void main(String[] args) {
		for (int index = 0; index < obiettivi.length; index++){
			int valore = 0;
			for (Territorio territorio: obiettivi[index]){
				valore += territorio.getValore();
			}
			System.out.println("[Obiettivo n.ro "+(index+1)+"]: "+valore);
		}
	}
	
}
