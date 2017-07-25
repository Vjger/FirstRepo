package it.desimone.rd3analyzer.ui.panels;

import it.desimone.rd3analyzer.Territorio;
import it.desimone.utils.Configurator;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class StatePosition {

	private Map<Territorio, Point> posizioni = new HashMap<Territorio, Point>();
	
	public Map<Confine,PositionAndRotation> confini = new HashMap<Confine,PositionAndRotation>();
	
	public StatePosition(){
		initBIG();
	}
	
	public StatePosition(Configurator.PlanciaSize planciaSize){
		switch (planciaSize) {
		case SMALL:
			initSMALL();
			break;
		case BIG:
			initBIG();
			break;
		default:
			break;
		}
	}
	
	private void initBIG(){
		initPosizioniBig();
		initConfiniBig();
	}
	
	private void initPosizioniBig(){
		posizioni.put(Territorio.ALASKA, new Point(102, 67));
		posizioni.put(Territorio.TERRITORI_DEL_NORD_OVEST, new Point(244, 99));
		posizioni.put(Territorio.ALBERTA, new Point(148, 169));
		posizioni.put(Territorio.GROENLANDIA, new Point(410, 94));
		posizioni.put(Territorio.ONTARIO, new Point(210, 210));		
		posizioni.put(Territorio.QUEBEC, new Point(302, 226));		
		posizioni.put(Territorio.STATI_UNITI_OCCIDENTALI, new Point(138, 271));	
		posizioni.put(Territorio.STATI_UNITI_ORIENTALI, new Point(185, 290));	
		posizioni.put(Territorio.AMERICA_CENTRALE, new Point(79, 387));
		
		posizioni.put(Territorio.VENEZUELA, new Point(194, 455));
		posizioni.put(Territorio.BRASILE, new Point(253, 521));	
		posizioni.put(Territorio.PERU, new Point(63, 543));	
		posizioni.put(Territorio.ARGENTINA, new Point(131, 632));
		
		posizioni.put(Territorio.ISLANDA, new Point(504, 106));	
		posizioni.put(Territorio.SCANDINAVIA, new Point(586, 147));	
		posizioni.put(Territorio.GRAN_BRETAGNA, new Point(448, 239));	
		posizioni.put(Territorio.EUROPA_SETTENTRIONALE, new Point(586, 269));	
		posizioni.put(Territorio.UCRAINA, new Point(651, 245));	
		posizioni.put(Territorio.EUROPA_OCCIDENTALE, new Point(456, 335));	
		posizioni.put(Territorio.EUROPA_MERIDIONALE, new Point(597, 353));	
		
		posizioni.put(Territorio.AFRICA_DEL_NORD, new Point(502, 540));	
		posizioni.put(Territorio.EGITTO, new Point(561, 494));	
		posizioni.put(Territorio.CONGO, new Point(522, 605));	
		posizioni.put(Territorio.AFRICA_ORIENTALE, new Point(625, 607));	
		posizioni.put(Territorio.AFRICA_DEL_SUD, new Point(578, 724));	
		posizioni.put(Territorio.MADAGASCAR, new Point(695, 654));
		
		posizioni.put(Territorio.URALI, new Point(765, 242));	
		posizioni.put(Territorio.SIBERIA, new Point(813, 234));	
		posizioni.put(Territorio.JACUZIA, new Point(852, 64));	
		posizioni.put(Territorio.KAMCHATKA, new Point(953, 166));	
		posizioni.put(Territorio.GIAPPONE, new Point(1017, 139));	
		posizioni.put(Territorio.CITA, new Point(858, 216));	
		posizioni.put(Territorio.MONGOLIA, new Point(884, 259));	
		posizioni.put(Territorio.AFGHANISTAN, new Point(796, 335));	
		posizioni.put(Territorio.CINA, new Point(880, 338));	
		posizioni.put(Territorio.MEDIO_ORIENTE, new Point(761, 429));	
		posizioni.put(Territorio.INDIA, new Point(886, 422));	
		posizioni.put(Territorio.SIAM, new Point(993, 444));
		
		posizioni.put(Territorio.INDONESIA, new Point(1079, 478));	
		posizioni.put(Territorio.NUOVA_GUINEA, new Point(1151, 603));	
		posizioni.put(Territorio.AUSTRALIA_OCCIDENTALE, new Point(1031, 643));	
		posizioni.put(Territorio.AUSTRALIA_ORIENTALE, new Point(1100, 676));	
		
	}
	
	private void initConfiniBig(){
		confini.put(new Confine(Territorio.ALASKA, Territorio.KAMCHATKA), new PositionAndRotation(new Point(32,99), 180));
		confini.put(new Confine(Territorio.ALASKA, Territorio.ALBERTA), new PositionAndRotation(new Point(117,140), 45));
		confini.put(new Confine(Territorio.ALASKA, Territorio.TERRITORI_DEL_NORD_OVEST), new PositionAndRotation(new Point(134,112), 0));
		confini.put(new Confine(Territorio.TERRITORI_DEL_NORD_OVEST, Territorio.ALASKA), new PositionAndRotation(new Point(116,110), 180));
		confini.put(new Confine(Territorio.TERRITORI_DEL_NORD_OVEST, Territorio.ALBERTA), new PositionAndRotation(new Point(150,151), 116));
		confini.put(new Confine(Territorio.TERRITORI_DEL_NORD_OVEST, Territorio.GROENLANDIA), new PositionAndRotation(new Point(232,166), 0));		
		confini.put(new Confine(Territorio.TERRITORI_DEL_NORD_OVEST, Territorio.ONTARIO), new PositionAndRotation(new Point(200,172), 88));
		confini.put(new Confine(Territorio.GROENLANDIA, Territorio.ONTARIO), new PositionAndRotation(new Point(234,190), 135));
		confini.put(new Confine(Territorio.GROENLANDIA, Territorio.QUEBEC), new PositionAndRotation(new Point(387,173), 117));
		confini.put(new Confine(Territorio.GROENLANDIA, Territorio.TERRITORI_DEL_NORD_OVEST), new PositionAndRotation(new Point(219,159), 180));
		confini.put(new Confine(Territorio.GROENLANDIA, Territorio.ISLANDA), new PositionAndRotation(new Point(451,125), 45));
		confini.put(new Confine(Territorio.ALBERTA, Territorio.ALASKA), new PositionAndRotation(new Point(100,139), 255));
		confini.put(new Confine(Territorio.ALBERTA, Territorio.TERRITORI_DEL_NORD_OVEST), new PositionAndRotation(new Point(179,157), -72));
		confini.put(new Confine(Territorio.ALBERTA, Territorio.ONTARIO), new PositionAndRotation(new Point(181,220), 45));
		confini.put(new Confine(Territorio.ALBERTA, Territorio.STATI_UNITI_OCCIDENTALI), new PositionAndRotation(new Point(139,236), 96));
		confini.put(new Confine(Territorio.ONTARIO, Territorio.TERRITORI_DEL_NORD_OVEST), new PositionAndRotation(new Point(187,160), 263));
		confini.put(new Confine(Territorio.ONTARIO, Territorio.ALBERTA), new PositionAndRotation(new Point(166,216), 225));
		confini.put(new Confine(Territorio.ONTARIO, Territorio.GROENLANDIA), new PositionAndRotation(new Point(236,177), -45));
		confini.put(new Confine(Territorio.ONTARIO, Territorio.QUEBEC), new PositionAndRotation(new Point(274,259), 0));
		confini.put(new Confine(Territorio.ONTARIO, Territorio.STATI_UNITI_OCCIDENTALI), new PositionAndRotation(new Point(156,254), 135));
		confini.put(new Confine(Territorio.ONTARIO, Territorio.STATI_UNITI_ORIENTALI), new PositionAndRotation(new Point(195,271), 135));
		confini.put(new Confine(Territorio.QUEBEC, Territorio.GROENLANDIA), new PositionAndRotation(new Point(368,215), -63));
		confini.put(new Confine(Territorio.QUEBEC, Territorio.ONTARIO), new PositionAndRotation(new Point(258,265), 225));
		confini.put(new Confine(Territorio.QUEBEC, Territorio.STATI_UNITI_ORIENTALI), new PositionAndRotation(new Point(256,309), 135));
		confini.put(new Confine(Territorio.STATI_UNITI_OCCIDENTALI, Territorio.ALBERTA), new PositionAndRotation(new Point(110,235), -45));
		confini.put(new Confine(Territorio.STATI_UNITI_OCCIDENTALI, Territorio.ONTARIO), new PositionAndRotation(new Point(166,247), -45));
		confini.put(new Confine(Territorio.STATI_UNITI_OCCIDENTALI, Territorio.STATI_UNITI_ORIENTALI), new PositionAndRotation(new Point(124,303), 45));
		confini.put(new Confine(Territorio.STATI_UNITI_OCCIDENTALI, Territorio.AMERICA_CENTRALE), new PositionAndRotation(new Point(64,312), 93));
		confini.put(new Confine(Territorio.STATI_UNITI_ORIENTALI, Territorio.ONTARIO), new PositionAndRotation(new Point(196,253), -72));
		confini.put(new Confine(Territorio.STATI_UNITI_ORIENTALI, Territorio.QUEBEC), new PositionAndRotation(new Point(266,296), -72));
		confini.put(new Confine(Territorio.STATI_UNITI_ORIENTALI, Territorio.STATI_UNITI_OCCIDENTALI), new PositionAndRotation(new Point(117,295), 225));
		confini.put(new Confine(Territorio.STATI_UNITI_ORIENTALI, Territorio.AMERICA_CENTRALE), new PositionAndRotation(new Point(94,340), 135));
		confini.put(new Confine(Territorio.AMERICA_CENTRALE, Territorio.STATI_UNITI_OCCIDENTALI), new PositionAndRotation(new Point(61,299), -72));
		confini.put(new Confine(Territorio.AMERICA_CENTRALE, Territorio.STATI_UNITI_ORIENTALI), new PositionAndRotation(new Point(105,339), -45));
		confini.put(new Confine(Territorio.AMERICA_CENTRALE, Territorio.VENEZUELA), new PositionAndRotation(new Point(101,436), 82));
		
		confini.put(new Confine(Territorio.VENEZUELA, Territorio.AMERICA_CENTRALE), new PositionAndRotation(new Point(103,428), 225));
		confini.put(new Confine(Territorio.VENEZUELA, Territorio.BRASILE), new PositionAndRotation(new Point(177,490), 45));
		confini.put(new Confine(Territorio.VENEZUELA, Territorio.PERU), new PositionAndRotation(new Point(68,508), 117));
		confini.put(new Confine(Territorio.BRASILE, Territorio.VENEZUELA), new PositionAndRotation(new Point(178,477), 256));
		confini.put(new Confine(Territorio.BRASILE, Territorio.PERU), new PositionAndRotation(new Point(83,527), 180));
		confini.put(new Confine(Territorio.BRASILE, Territorio.ARGENTINA), new PositionAndRotation(new Point(203,603), 135));
		confini.put(new Confine(Territorio.BRASILE, Territorio.AFRICA_DEL_NORD), new PositionAndRotation(new Point(338,524), 0));
		confini.put(new Confine(Territorio.PERU, Territorio.VENEZUELA), new PositionAndRotation(new Point(71,501), -45));
		confini.put(new Confine(Territorio.PERU, Territorio.BRASILE), new PositionAndRotation(new Point(100,535), 0));
		confini.put(new Confine(Territorio.PERU, Territorio.ARGENTINA), new PositionAndRotation(new Point(103,600), 63));
		confini.put(new Confine(Territorio.ARGENTINA, Territorio.PERU), new PositionAndRotation(new Point(97,589), 243));
		confini.put(new Confine(Territorio.ARGENTINA, Territorio.BRASILE), new PositionAndRotation(new Point(207,595), -45));
		
		confini.put(new Confine(Territorio.ISLANDA, Territorio.GROENLANDIA), new PositionAndRotation(new Point(453,116), 225));
		confini.put(new Confine(Territorio.ISLANDA, Territorio.SCANDINAVIA), new PositionAndRotation(new Point(532,162), 45));
		confini.put(new Confine(Territorio.ISLANDA, Territorio.GRAN_BRETAGNA), new PositionAndRotation(new Point(483,178), 104));
		confini.put(new Confine(Territorio.SCANDINAVIA, Territorio.ISLANDA), new PositionAndRotation(new Point(530,156), 243));
		confini.put(new Confine(Territorio.SCANDINAVIA, Territorio.GRAN_BRETAGNA), new PositionAndRotation(new Point(502,210), 135));
		confini.put(new Confine(Territorio.SCANDINAVIA, Territorio.UCRAINA), new PositionAndRotation(new Point(611,197), 45));
		confini.put(new Confine(Territorio.SCANDINAVIA, Territorio.EUROPA_SETTENTRIONALE), new PositionAndRotation(new Point(545,247), 88));
		confini.put(new Confine(Territorio.GRAN_BRETAGNA, Territorio.SCANDINAVIA), new PositionAndRotation(new Point(509,219), -45));
		confini.put(new Confine(Territorio.GRAN_BRETAGNA, Territorio.ISLANDA), new PositionAndRotation(new Point(484,180), -72));
		confini.put(new Confine(Territorio.GRAN_BRETAGNA, Territorio.EUROPA_OCCIDENTALE), new PositionAndRotation(new Point(464,295), 101));
		confini.put(new Confine(Territorio.GRAN_BRETAGNA, Territorio.EUROPA_SETTENTRIONALE), new PositionAndRotation(new Point(506,267), 45));
		confini.put(new Confine(Territorio.EUROPA_SETTENTRIONALE, Territorio.SCANDINAVIA), new PositionAndRotation(new Point(546,248), -89));
		confini.put(new Confine(Territorio.EUROPA_SETTENTRIONALE, Territorio.GRAN_BRETAGNA), new PositionAndRotation(new Point(497,265), 225));
		confini.put(new Confine(Territorio.EUROPA_SETTENTRIONALE, Territorio.EUROPA_OCCIDENTALE), new PositionAndRotation(new Point(479,303), 135));
		confini.put(new Confine(Territorio.EUROPA_SETTENTRIONALE, Territorio.EUROPA_MERIDIONALE), new PositionAndRotation(new Point(552,321), 89));
		confini.put(new Confine(Territorio.EUROPA_SETTENTRIONALE, Territorio.UCRAINA), new PositionAndRotation(new Point(616,292), 0));
		confini.put(new Confine(Territorio.EUROPA_OCCIDENTALE, Territorio.GRAN_BRETAGNA), new PositionAndRotation(new Point(466,291), 266));
		confini.put(new Confine(Territorio.EUROPA_OCCIDENTALE, Territorio.EUROPA_SETTENTRIONALE), new PositionAndRotation(new Point(497,299), -45));
		confini.put(new Confine(Territorio.EUROPA_OCCIDENTALE, Territorio.EUROPA_MERIDIONALE), new PositionAndRotation(new Point(491,334), 0));
		confini.put(new Confine(Territorio.EUROPA_OCCIDENTALE, Territorio.AFRICA_DEL_NORD), new PositionAndRotation(new Point(440,408), 83));
		confini.put(new Confine(Territorio.EUROPA_MERIDIONALE, Territorio.EUROPA_SETTENTRIONALE), new PositionAndRotation(new Point(554,313), 265));
		confini.put(new Confine(Territorio.EUROPA_MERIDIONALE, Territorio.EUROPA_OCCIDENTALE), new PositionAndRotation(new Point(477,332), 180));
		confini.put(new Confine(Territorio.EUROPA_MERIDIONALE, Territorio.UCRAINA), new PositionAndRotation(new Point(623,319), -63));
		confini.put(new Confine(Territorio.EUROPA_MERIDIONALE, Territorio.AFRICA_DEL_NORD), new PositionAndRotation(new Point(500,408), 117));
		confini.put(new Confine(Territorio.EUROPA_MERIDIONALE, Territorio.EGITTO), new PositionAndRotation(new Point(561,446), 45));
		confini.put(new Confine(Territorio.EUROPA_MERIDIONALE, Territorio.MEDIO_ORIENTE), new PositionAndRotation(new Point(619,398), 45));
		confini.put(new Confine(Territorio.UCRAINA, Territorio.SCANDINAVIA), new PositionAndRotation(new Point(605,185), 225));
		confini.put(new Confine(Territorio.UCRAINA, Territorio.EUROPA_SETTENTRIONALE), new PositionAndRotation(new Point(598,288), 180));
		confini.put(new Confine(Territorio.UCRAINA, Territorio.EUROPA_MERIDIONALE), new PositionAndRotation(new Point(614,324), 135));
		confini.put(new Confine(Territorio.UCRAINA, Territorio.URALI), new PositionAndRotation(new Point(696,223), 0));
		confini.put(new Confine(Territorio.UCRAINA, Territorio.AFGHANISTAN), new PositionAndRotation(new Point(693,296), 0));
		confini.put(new Confine(Territorio.UCRAINA, Territorio.MEDIO_ORIENTE), new PositionAndRotation(new Point(720,382), 45));
		
		confini.put(new Confine(Territorio.AFRICA_DEL_NORD, Territorio.EUROPA_OCCIDENTALE), new PositionAndRotation(new Point(437,404), 252));
		confini.put(new Confine(Territorio.AFRICA_DEL_NORD, Territorio.EUROPA_MERIDIONALE), new PositionAndRotation(new Point(499,412), -63));
		confini.put(new Confine(Territorio.AFRICA_DEL_NORD, Territorio.BRASILE), new PositionAndRotation(new Point(347,523), 180));
		confini.put(new Confine(Territorio.AFRICA_DEL_NORD, Territorio.EGITTO), new PositionAndRotation(new Point(538,507), -45));
		confini.put(new Confine(Territorio.AFRICA_DEL_NORD, Territorio.CONGO), new PositionAndRotation(new Point(541,582), 45));
		confini.put(new Confine(Territorio.AFRICA_DEL_NORD, Territorio.AFRICA_ORIENTALE), new PositionAndRotation(new Point(577,562), 40));
		confini.put(new Confine(Territorio.EGITTO, Territorio.AFRICA_DEL_NORD), new PositionAndRotation(new Point(526,518), 135));
		confini.put(new Confine(Territorio.EGITTO, Territorio.EUROPA_MERIDIONALE), new PositionAndRotation(new Point(573,452), 270));
		confini.put(new Confine(Territorio.EGITTO, Territorio.MEDIO_ORIENTE), new PositionAndRotation(new Point(669,477), -45));
		confini.put(new Confine(Territorio.EGITTO, Territorio.AFRICA_ORIENTALE), new PositionAndRotation(new Point(596,541), 45));
		confini.put(new Confine(Territorio.AFRICA_ORIENTALE, Territorio.AFRICA_DEL_NORD), new PositionAndRotation(new Point(566,553), 220));
		confini.put(new Confine(Territorio.AFRICA_ORIENTALE, Territorio.EGITTO), new PositionAndRotation(new Point(607,528), 256));
		confini.put(new Confine(Territorio.AFRICA_ORIENTALE, Territorio.CONGO), new PositionAndRotation(new Point(582,611), 180));
		confini.put(new Confine(Territorio.AFRICA_ORIENTALE, Territorio.AFRICA_DEL_SUD), new PositionAndRotation(new Point(597,665), 117));
		confini.put(new Confine(Territorio.AFRICA_ORIENTALE, Territorio.MADAGASCAR), new PositionAndRotation(new Point(662,648), 45));
		confini.put(new Confine(Territorio.CONGO, Territorio.AFRICA_DEL_NORD), new PositionAndRotation(new Point(531,570), 243));
		confini.put(new Confine(Territorio.CONGO, Territorio.AFRICA_ORIENTALE), new PositionAndRotation(new Point(596,611), 0));
		confini.put(new Confine(Territorio.CONGO, Territorio.AFRICA_DEL_SUD), new PositionAndRotation(new Point(561,657), 85));
		confini.put(new Confine(Territorio.AFRICA_DEL_SUD, Territorio.AFRICA_ORIENTALE), new PositionAndRotation(new Point(602,649), -63));
		confini.put(new Confine(Territorio.AFRICA_DEL_SUD, Territorio.CONGO), new PositionAndRotation(new Point(564,647), 266));
		confini.put(new Confine(Territorio.AFRICA_DEL_SUD, Territorio.MADAGASCAR), new PositionAndRotation(new Point(638,706), 0));
		confini.put(new Confine(Territorio.MADAGASCAR, Territorio.AFRICA_DEL_SUD), new PositionAndRotation(new Point(644,702), 180));
		confini.put(new Confine(Territorio.MADAGASCAR, Territorio.AFRICA_ORIENTALE), new PositionAndRotation(new Point(655,642), 225));
		
		confini.put(new Confine(Territorio.URALI, Territorio.UCRAINA), new PositionAndRotation(new Point(683,221), 180));
		confini.put(new Confine(Territorio.URALI, Territorio.SIBERIA), new PositionAndRotation(new Point(763,201), 0));
		confini.put(new Confine(Territorio.URALI, Territorio.AFGHANISTAN), new PositionAndRotation(new Point(734,263), 79));
		confini.put(new Confine(Territorio.URALI, Territorio.CINA), new PositionAndRotation(new Point(828,303), 45));		
		confini.put(new Confine(Territorio.SIBERIA, Territorio.CINA), new PositionAndRotation(new Point(844,300), 45));
		confini.put(new Confine(Territorio.SIBERIA, Territorio.URALI), new PositionAndRotation(new Point(755,204), 180));
		confini.put(new Confine(Territorio.SIBERIA, Territorio.CITA), new PositionAndRotation(new Point(824,186), 0));
		confini.put(new Confine(Territorio.SIBERIA, Territorio.JACUZIA), new PositionAndRotation(new Point(828,110), 0));
		confini.put(new Confine(Territorio.SIBERIA, Territorio.MONGOLIA), new PositionAndRotation(new Point(851,273), -45));
		confini.put(new Confine(Territorio.JACUZIA, Territorio.SIBERIA), new PositionAndRotation(new Point(812,123),180));
		confini.put(new Confine(Territorio.JACUZIA, Territorio.CITA), new PositionAndRotation(new Point(863,138), 90));
		confini.put(new Confine(Territorio.JACUZIA, Territorio.KAMCHATKA), new PositionAndRotation(new Point(905,121), 0));
		confini.put(new Confine(Territorio.CITA, Territorio.SIBERIA), new PositionAndRotation(new Point(815,179), 180));
		confini.put(new Confine(Territorio.CITA, Territorio.JACUZIA), new PositionAndRotation(new Point(861,129), 270));
		confini.put(new Confine(Territorio.CITA, Territorio.KAMCHATKA), new PositionAndRotation(new Point(921,172), -45));
		confini.put(new Confine(Territorio.CITA, Territorio.MONGOLIA), new PositionAndRotation(new Point(896,218), 45));
		confini.put(new Confine(Territorio.KAMCHATKA, Territorio.CITA), new PositionAndRotation(new Point(907,173), 135));
		confini.put(new Confine(Territorio.KAMCHATKA, Territorio.JACUZIA), new PositionAndRotation(new Point(892,113), 180));
		confini.put(new Confine(Territorio.KAMCHATKA, Territorio.MONGOLIA), new PositionAndRotation(new Point(955,196), 135));
		confini.put(new Confine(Territorio.KAMCHATKA, Territorio.GIAPPONE), new PositionAndRotation(new Point(1000,172), 0));
		confini.put(new Confine(Territorio.KAMCHATKA, Territorio.ALASKA), new PositionAndRotation(new Point(989,92), 0));
		confini.put(new Confine(Territorio.GIAPPONE, Territorio.KAMCHATKA), new PositionAndRotation(new Point(1005,167), 180));
		confini.put(new Confine(Territorio.GIAPPONE, Territorio.MONGOLIA), new PositionAndRotation(new Point(1017,203), 135));
		confini.put(new Confine(Territorio.MONGOLIA, Territorio.SIBERIA), new PositionAndRotation(new Point(835,262), 225));
		confini.put(new Confine(Territorio.MONGOLIA, Territorio.CITA), new PositionAndRotation(new Point(892,207), 225));
		confini.put(new Confine(Territorio.MONGOLIA, Territorio.KAMCHATKA), new PositionAndRotation(new Point(960,188), -45));
		confini.put(new Confine(Territorio.MONGOLIA, Territorio.GIAPPONE), new PositionAndRotation(new Point(1020,211), -45));
		confini.put(new Confine(Territorio.MONGOLIA, Territorio.CINA), new PositionAndRotation(new Point(923,269), 90));
		confini.put(new Confine(Territorio.AFGHANISTAN, Territorio.URALI), new PositionAndRotation(new Point(742,256), 270));
		confini.put(new Confine(Territorio.AFGHANISTAN, Territorio.UCRAINA), new PositionAndRotation(new Point(688,280), 180));
		confini.put(new Confine(Territorio.AFGHANISTAN, Territorio.MEDIO_ORIENTE), new PositionAndRotation(new Point(779,389), 104));
		confini.put(new Confine(Territorio.AFGHANISTAN, Territorio.CINA), new PositionAndRotation(new Point(827,328), 0));
		confini.put(new Confine(Territorio.CINA, Territorio.SIAM), new PositionAndRotation(new Point(947,377), 63));
		confini.put(new Confine(Territorio.CINA, Territorio.INDIA), new PositionAndRotation(new Point(891,382), 117));
		confini.put(new Confine(Territorio.CINA, Territorio.MEDIO_ORIENTE), new PositionAndRotation(new Point(828,383), 135));
		confini.put(new Confine(Territorio.CINA, Territorio.AFGHANISTAN), new PositionAndRotation(new Point(816,317), 180));
		confini.put(new Confine(Territorio.CINA, Territorio.URALI), new PositionAndRotation(new Point(824,298), 225));
		confini.put(new Confine(Territorio.CINA, Territorio.SIBERIA), new PositionAndRotation(new Point(837,289), 225));
		confini.put(new Confine(Territorio.CINA, Territorio.MONGOLIA), new PositionAndRotation(new Point(918,265), 252));
		confini.put(new Confine(Territorio.MEDIO_ORIENTE, Territorio.EGITTO), new PositionAndRotation(new Point(632,439), 135));
		confini.put(new Confine(Territorio.MEDIO_ORIENTE, Territorio.EUROPA_MERIDIONALE), new PositionAndRotation(new Point(617,397), 225));
		confini.put(new Confine(Territorio.MEDIO_ORIENTE, Territorio.UCRAINA), new PositionAndRotation(new Point(721,374), 243));
		confini.put(new Confine(Territorio.MEDIO_ORIENTE, Territorio.AFGHANISTAN), new PositionAndRotation(new Point(782,383), -81));
		confini.put(new Confine(Territorio.MEDIO_ORIENTE, Territorio.CINA), new PositionAndRotation(new Point(830,376), -45));
		confini.put(new Confine(Territorio.MEDIO_ORIENTE, Territorio.INDIA), new PositionAndRotation(new Point(799,446), 0));
		confini.put(new Confine(Territorio.INDIA, Territorio.MEDIO_ORIENTE), new PositionAndRotation(new Point(788,442), 180));
		confini.put(new Confine(Territorio.INDIA, Territorio.CINA), new PositionAndRotation(new Point(899,379), -63));
		confini.put(new Confine(Territorio.INDIA, Territorio.SIAM), new PositionAndRotation(new Point(921,410), 0));
		confini.put(new Confine(Territorio.SIAM, Territorio.INDIA), new PositionAndRotation(new Point(908,406), 180));
		confini.put(new Confine(Territorio.SIAM, Territorio.CINA), new PositionAndRotation(new Point(941,373), 225));
		confini.put(new Confine(Territorio.SIAM, Territorio.INDONESIA), new PositionAndRotation(new Point(1003,480), 45));
		
		confini.put(new Confine(Territorio.INDONESIA, Territorio.SIAM), new PositionAndRotation(new Point(1007,473), 225));
		confini.put(new Confine(Territorio.INDONESIA, Territorio.NUOVA_GUINEA), new PositionAndRotation(new Point(1100,522), 72));
		confini.put(new Confine(Territorio.INDONESIA, Territorio.AUSTRALIA_OCCIDENTALE), new PositionAndRotation(new Point(1024,600), 98));
		confini.put(new Confine(Territorio.NUOVA_GUINEA, Territorio.INDONESIA), new PositionAndRotation(new Point(1106,528), 243));
		confini.put(new Confine(Territorio.NUOVA_GUINEA, Territorio.AUSTRALIA_OCCIDENTALE), new PositionAndRotation(new Point(1065,598), 135));
		confini.put(new Confine(Territorio.NUOVA_GUINEA, Territorio.AUSTRALIA_ORIENTALE), new PositionAndRotation(new Point(1112,612), 101));
		confini.put(new Confine(Territorio.AUSTRALIA_OCCIDENTALE, Territorio.INDONESIA), new PositionAndRotation(new Point(1028,591), -76));
		confini.put(new Confine(Territorio.AUSTRALIA_OCCIDENTALE, Territorio.NUOVA_GUINEA), new PositionAndRotation(new Point(1063,605), -45));
		confini.put(new Confine(Territorio.AUSTRALIA_OCCIDENTALE, Territorio.AUSTRALIA_ORIENTALE), new PositionAndRotation(new Point(1074,687), 45));
		confini.put(new Confine(Territorio.AUSTRALIA_ORIENTALE, Territorio.AUSTRALIA_OCCIDENTALE), new PositionAndRotation(new Point(1057,684), 225));
		confini.put(new Confine(Territorio.AUSTRALIA_ORIENTALE, Territorio.NUOVA_GUINEA), new PositionAndRotation(new Point(1128,628), -63));
	
	}
	
	private void initSMALL(){
		initPosizioniSmall();
		initConfiniSmall();
	}
	
	private void initPosizioniSmall(){
		posizioni.put(Territorio.ALASKA, new Point(80, 50));
		posizioni.put(Territorio.TERRITORI_DEL_NORD_OVEST, new Point(195, 77));
		posizioni.put(Territorio.ALBERTA, new Point(75, 163));
		posizioni.put(Territorio.GROENLANDIA, new Point(344, 72));
		posizioni.put(Territorio.ONTARIO, new Point(179, 175));		
		posizioni.put(Territorio.QUEBEC, new Point(255, 187));		
		posizioni.put(Territorio.STATI_UNITI_OCCIDENTALI, new Point(110, 226));	
		posizioni.put(Territorio.STATI_UNITI_ORIENTALI, new Point(164, 253));	
		posizioni.put(Territorio.AMERICA_CENTRALE, new Point(71, 331));
		
		posizioni.put(Territorio.VENEZUELA, new Point(155, 375));
		posizioni.put(Territorio.BRASILE, new Point(213, 435));	
		posizioni.put(Territorio.PERU, new Point(50, 459));	
		posizioni.put(Territorio.ARGENTINA, new Point(111, 529));
		
		posizioni.put(Territorio.ISLANDA, new Point(425, 87));	
		posizioni.put(Territorio.SCANDINAVIA, new Point(489, 118));	
		posizioni.put(Territorio.GRAN_BRETAGNA, new Point(364, 195));	
		posizioni.put(Territorio.EUROPA_SETTENTRIONALE, new Point(490, 219));	
		posizioni.put(Territorio.UCRAINA, new Point(545, 197));	
		posizioni.put(Territorio.EUROPA_OCCIDENTALE, new Point(371, 274));	
		posizioni.put(Territorio.EUROPA_MERIDIONALE, new Point(500, 294));	
		
		posizioni.put(Territorio.AFRICA_DEL_NORD, new Point(414, 442));	
		posizioni.put(Territorio.EGITTO, new Point(472, 422));	
		posizioni.put(Territorio.CONGO, new Point(435, 515));	
		posizioni.put(Territorio.AFRICA_ORIENTALE, new Point(528, 512));	
		posizioni.put(Territorio.AFRICA_DEL_SUD, new Point(486, 613));	
		posizioni.put(Territorio.MADAGASCAR, new Point(570, 596));
		
		posizioni.put(Territorio.URALI, new Point(646, 202));	
		posizioni.put(Territorio.SIBERIA, new Point(655, 128));	
		posizioni.put(Territorio.JACUZIA, new Point(721, 48));	
		posizioni.put(Territorio.KAMCHATKA, new Point(800, 132));	
		posizioni.put(Territorio.GIAPPONE, new Point(896, 173));	
		posizioni.put(Territorio.CITA, new Point(717, 175));	
		posizioni.put(Territorio.MONGOLIA, new Point(752, 209));	
		posizioni.put(Territorio.AFGHANISTAN, new Point(663, 291));	
		posizioni.put(Territorio.CINA, new Point(743, 278));	
		posizioni.put(Territorio.MEDIO_ORIENTE, new Point(635, 359));	
		posizioni.put(Territorio.INDIA, new Point(739, 351));	
		posizioni.put(Territorio.SIAM, new Point(839, 373));
		
		posizioni.put(Territorio.INDONESIA, new Point(910, 407));	
		posizioni.put(Territorio.NUOVA_GUINEA, new Point(967, 510));	
		posizioni.put(Territorio.AUSTRALIA_OCCIDENTALE, new Point(855, 534));	
		posizioni.put(Territorio.AUSTRALIA_ORIENTALE, new Point(924, 564));	
		
	}
	
	private void initConfiniSmall(){
		confini.put(new Confine(Territorio.ALASKA, Territorio.KAMCHATKA), new PositionAndRotation(new Point(20,88), 180));
		confini.put(new Confine(Territorio.ALASKA, Territorio.ALBERTA), new PositionAndRotation(new Point(83,128), 45));
		confini.put(new Confine(Territorio.ALASKA, Territorio.TERRITORI_DEL_NORD_OVEST), new PositionAndRotation(new Point(103,84), 0));
		confini.put(new Confine(Territorio.TERRITORI_DEL_NORD_OVEST, Territorio.ALASKA), new PositionAndRotation(new Point(93,84), 180));
		confini.put(new Confine(Territorio.TERRITORI_DEL_NORD_OVEST, Territorio.ALBERTA), new PositionAndRotation(new Point(132,136), 100));
		confini.put(new Confine(Territorio.TERRITORI_DEL_NORD_OVEST, Territorio.GROENLANDIA), new PositionAndRotation(new Point(190,130), 0));
		confini.put(new Confine(Territorio.TERRITORI_DEL_NORD_OVEST, Territorio.ONTARIO), new PositionAndRotation(new Point(165,146), 60));
		confini.put(new Confine(Territorio.GROENLANDIA, Territorio.ONTARIO), new PositionAndRotation(new Point(190,165), 120));
		confini.put(new Confine(Territorio.GROENLANDIA, Territorio.QUEBEC), new PositionAndRotation(new Point(316,161), 115));
		confini.put(new Confine(Territorio.GROENLANDIA, Territorio.TERRITORI_DEL_NORD_OVEST), new PositionAndRotation(new Point(180,130), 180));
		confini.put(new Confine(Territorio.GROENLANDIA, Territorio.ISLANDA), new PositionAndRotation(new Point(382,92), 45));
		confini.put(new Confine(Territorio.ALBERTA, Territorio.ALASKA), new PositionAndRotation(new Point(73,118), 225));
		confini.put(new Confine(Territorio.ALBERTA, Territorio.TERRITORI_DEL_NORD_OVEST), new PositionAndRotation(new Point(132,136), 10));
		confini.put(new Confine(Territorio.ALBERTA, Territorio.ONTARIO), new PositionAndRotation(new Point(150,173), 0));
		confini.put(new Confine(Territorio.ALBERTA, Territorio.STATI_UNITI_OCCIDENTALI), new PositionAndRotation(new Point(97,200), 90));
		confini.put(new Confine(Territorio.ONTARIO, Territorio.TERRITORI_DEL_NORD_OVEST), new PositionAndRotation(new Point(160,141), 240));
		confini.put(new Confine(Territorio.ONTARIO, Territorio.ALBERTA), new PositionAndRotation(new Point(135,173), 180));
		confini.put(new Confine(Territorio.ONTARIO, Territorio.GROENLANDIA), new PositionAndRotation(new Point(200,155), 300));
		confini.put(new Confine(Territorio.ONTARIO, Territorio.QUEBEC), new PositionAndRotation(new Point(220,227), 0));
		confini.put(new Confine(Territorio.ONTARIO, Territorio.STATI_UNITI_OCCIDENTALI), new PositionAndRotation(new Point(125,217), 145));
		confini.put(new Confine(Territorio.ONTARIO, Territorio.STATI_UNITI_ORIENTALI), new PositionAndRotation(new Point(173,250), 100));
		confini.put(new Confine(Territorio.QUEBEC, Territorio.GROENLANDIA), new PositionAndRotation(new Point(316,161), 295));
		confini.put(new Confine(Territorio.QUEBEC, Territorio.ONTARIO), new PositionAndRotation(new Point(210,227), 180));
		confini.put(new Confine(Territorio.QUEBEC, Territorio.STATI_UNITI_ORIENTALI), new PositionAndRotation(new Point(216,260), 110));
		confini.put(new Confine(Territorio.STATI_UNITI_OCCIDENTALI, Territorio.ALBERTA), new PositionAndRotation(new Point(97,190), 270));
		confini.put(new Confine(Territorio.STATI_UNITI_OCCIDENTALI, Territorio.ONTARIO), new PositionAndRotation(new Point(135,207), 325));
		confini.put(new Confine(Territorio.STATI_UNITI_OCCIDENTALI, Territorio.STATI_UNITI_ORIENTALI), new PositionAndRotation(new Point(124,247), 0));
		confini.put(new Confine(Territorio.STATI_UNITI_OCCIDENTALI, Territorio.AMERICA_CENTRALE), new PositionAndRotation(new Point(66,270), 100));
		confini.put(new Confine(Territorio.STATI_UNITI_ORIENTALI, Territorio.ONTARIO), new PositionAndRotation(new Point(173,240), 280));
		confini.put(new Confine(Territorio.STATI_UNITI_ORIENTALI, Territorio.QUEBEC), new PositionAndRotation(new Point(216,250), 290));
		confini.put(new Confine(Territorio.STATI_UNITI_ORIENTALI, Territorio.STATI_UNITI_OCCIDENTALI), new PositionAndRotation(new Point(110,247), 180));
		confini.put(new Confine(Territorio.STATI_UNITI_ORIENTALI, Territorio.AMERICA_CENTRALE), new PositionAndRotation(new Point(69,293), 135));
		confini.put(new Confine(Territorio.AMERICA_CENTRALE, Territorio.STATI_UNITI_OCCIDENTALI), new PositionAndRotation(new Point(68,256), 280));
		confini.put(new Confine(Territorio.AMERICA_CENTRALE, Territorio.STATI_UNITI_ORIENTALI), new PositionAndRotation(new Point(79,283), 315));
		confini.put(new Confine(Territorio.AMERICA_CENTRALE, Territorio.VENEZUELA), new PositionAndRotation(new Point(90,360), 60));
		
		confini.put(new Confine(Territorio.VENEZUELA, Territorio.AMERICA_CENTRALE), new PositionAndRotation(new Point(80,350), 240));
		confini.put(new Confine(Territorio.VENEZUELA, Territorio.BRASILE), new PositionAndRotation(new Point(163,400), 45));
		confini.put(new Confine(Territorio.VENEZUELA, Territorio.PERU), new PositionAndRotation(new Point(55,423), 100));
		confini.put(new Confine(Territorio.BRASILE, Territorio.VENEZUELA), new PositionAndRotation(new Point(153,390), 225));
		confini.put(new Confine(Territorio.BRASILE, Territorio.PERU), new PositionAndRotation(new Point(122,466), 135));
		confini.put(new Confine(Territorio.BRASILE, Territorio.ARGENTINA), new PositionAndRotation(new Point(158,512), 135));
		confini.put(new Confine(Territorio.BRASILE, Territorio.AFRICA_DEL_NORD), new PositionAndRotation(new Point(285,437), 0));
		confini.put(new Confine(Territorio.PERU, Territorio.VENEZUELA), new PositionAndRotation(new Point(50,413), 280));
		confini.put(new Confine(Territorio.PERU, Territorio.BRASILE), new PositionAndRotation(new Point(132,460), 315));
		confini.put(new Confine(Territorio.PERU, Territorio.ARGENTINA), new PositionAndRotation(new Point(106,509), 60));
		confini.put(new Confine(Territorio.ARGENTINA, Territorio.PERU), new PositionAndRotation(new Point(106,499), 240));
		confini.put(new Confine(Territorio.ARGENTINA, Territorio.BRASILE), new PositionAndRotation(new Point(163,502), 315));
		
		confini.put(new Confine(Territorio.ISLANDA, Territorio.GROENLANDIA), new PositionAndRotation(new Point(380,80), 230));
		confini.put(new Confine(Territorio.ISLANDA, Territorio.SCANDINAVIA), new PositionAndRotation(new Point(442,127), 45));
		confini.put(new Confine(Territorio.ISLANDA, Territorio.GRAN_BRETAGNA), new PositionAndRotation(new Point(400,147), 95));
		confini.put(new Confine(Territorio.SCANDINAVIA, Territorio.ISLANDA), new PositionAndRotation(new Point(442,117), 225));
		confini.put(new Confine(Territorio.SCANDINAVIA, Territorio.GRAN_BRETAGNA), new PositionAndRotation(new Point(422,170), 135));
		confini.put(new Confine(Territorio.SCANDINAVIA, Territorio.UCRAINA), new PositionAndRotation(new Point(510,160), 60));
		confini.put(new Confine(Territorio.SCANDINAVIA, Territorio.EUROPA_SETTENTRIONALE), new PositionAndRotation(new Point(473,210), 90));
		confini.put(new Confine(Territorio.GRAN_BRETAGNA, Territorio.SCANDINAVIA), new PositionAndRotation(new Point(426,180), 315));
		confini.put(new Confine(Territorio.GRAN_BRETAGNA, Territorio.ISLANDA), new PositionAndRotation(new Point(400,140), 275));
		confini.put(new Confine(Territorio.GRAN_BRETAGNA, Territorio.EUROPA_OCCIDENTALE), new PositionAndRotation(new Point(385,245), 100));
		confini.put(new Confine(Territorio.GRAN_BRETAGNA, Territorio.EUROPA_SETTENTRIONALE), new PositionAndRotation(new Point(425,225), 45));
		confini.put(new Confine(Territorio.EUROPA_SETTENTRIONALE, Territorio.SCANDINAVIA), new PositionAndRotation(new Point(465,205), 270));
		confini.put(new Confine(Territorio.EUROPA_SETTENTRIONALE, Territorio.GRAN_BRETAGNA), new PositionAndRotation(new Point(410,210), 225));
		confini.put(new Confine(Territorio.EUROPA_SETTENTRIONALE, Territorio.EUROPA_OCCIDENTALE), new PositionAndRotation(new Point(410,250), 145));
		confini.put(new Confine(Territorio.EUROPA_SETTENTRIONALE, Territorio.EUROPA_MERIDIONALE), new PositionAndRotation(new Point(471,271), 90));
		confini.put(new Confine(Territorio.EUROPA_SETTENTRIONALE, Territorio.UCRAINA), new PositionAndRotation(new Point(518,235), 0));
		confini.put(new Confine(Territorio.EUROPA_OCCIDENTALE, Territorio.GRAN_BRETAGNA), new PositionAndRotation(new Point(385,238), 280));
		confini.put(new Confine(Territorio.EUROPA_OCCIDENTALE, Territorio.EUROPA_SETTENTRIONALE), new PositionAndRotation(new Point(410,250), 320));
		confini.put(new Confine(Territorio.EUROPA_OCCIDENTALE, Territorio.EUROPA_MERIDIONALE), new PositionAndRotation(new Point(410,286), 0));
		confini.put(new Confine(Territorio.EUROPA_OCCIDENTALE, Territorio.AFRICA_DEL_NORD), new PositionAndRotation(new Point(366,342), 75));
		confini.put(new Confine(Territorio.EUROPA_MERIDIONALE, Territorio.EUROPA_SETTENTRIONALE), new PositionAndRotation(new Point(483,253), 270));
		confini.put(new Confine(Territorio.EUROPA_MERIDIONALE, Territorio.EUROPA_OCCIDENTALE), new PositionAndRotation(new Point(406,286), 180));
		confini.put(new Confine(Territorio.EUROPA_MERIDIONALE, Territorio.UCRAINA), new PositionAndRotation(new Point(526,276), 325));
		confini.put(new Confine(Territorio.EUROPA_MERIDIONALE, Territorio.AFRICA_DEL_NORD), new PositionAndRotation(new Point(405,350), 135));
		confini.put(new Confine(Territorio.EUROPA_MERIDIONALE, Territorio.EGITTO), new PositionAndRotation(new Point(490,363), 90));
		confini.put(new Confine(Territorio.EUROPA_MERIDIONALE, Territorio.MEDIO_ORIENTE), new PositionAndRotation(new Point(526,329), 50));
		confini.put(new Confine(Territorio.UCRAINA, Territorio.SCANDINAVIA), new PositionAndRotation(new Point(503,145), 240));
		confini.put(new Confine(Territorio.UCRAINA, Territorio.EUROPA_SETTENTRIONALE), new PositionAndRotation(new Point(508,234), 180));
		confini.put(new Confine(Territorio.UCRAINA, Territorio.EUROPA_MERIDIONALE), new PositionAndRotation(new Point(510,280), 145));
		confini.put(new Confine(Territorio.UCRAINA, Territorio.URALI), new PositionAndRotation(new Point(582,182), 0));
		confini.put(new Confine(Territorio.UCRAINA, Territorio.AFGHANISTAN), new PositionAndRotation(new Point(593,227), 30));
		confini.put(new Confine(Territorio.UCRAINA, Territorio.MEDIO_ORIENTE), new PositionAndRotation(new Point(600,320), 90));
		
		confini.put(new Confine(Territorio.AFRICA_DEL_NORD, Territorio.EUROPA_OCCIDENTALE), new PositionAndRotation(new Point(370,334), 255));
		confini.put(new Confine(Territorio.AFRICA_DEL_NORD, Territorio.EUROPA_MERIDIONALE), new PositionAndRotation(new Point(427,335), 315));
		confini.put(new Confine(Territorio.AFRICA_DEL_NORD, Territorio.BRASILE), new PositionAndRotation(new Point(287,436), 180));
		confini.put(new Confine(Territorio.AFRICA_DEL_NORD, Territorio.EGITTO), new PositionAndRotation(new Point(454,424), 315));
		confini.put(new Confine(Territorio.AFRICA_DEL_NORD, Territorio.CONGO), new PositionAndRotation(new Point(447,490), 60));
		confini.put(new Confine(Territorio.AFRICA_DEL_NORD, Territorio.AFRICA_ORIENTALE), new PositionAndRotation(new Point(486,467), 0));
		confini.put(new Confine(Territorio.EGITTO, Territorio.AFRICA_DEL_NORD), new PositionAndRotation(new Point(440,434), 135));
		confini.put(new Confine(Territorio.EGITTO, Territorio.EUROPA_MERIDIONALE), new PositionAndRotation(new Point(490,367), 270));
		confini.put(new Confine(Territorio.EGITTO, Territorio.MEDIO_ORIENTE), new PositionAndRotation(new Point(563,392), 345));
		confini.put(new Confine(Territorio.EGITTO, Territorio.AFRICA_ORIENTALE), new PositionAndRotation(new Point(524,437), 60));
		confini.put(new Confine(Territorio.AFRICA_ORIENTALE, Territorio.AFRICA_DEL_NORD), new PositionAndRotation(new Point(470,467), 180));
		confini.put(new Confine(Territorio.AFRICA_ORIENTALE, Territorio.EGITTO), new PositionAndRotation(new Point(516,424), 240));
		confini.put(new Confine(Territorio.AFRICA_ORIENTALE, Territorio.CONGO), new PositionAndRotation(new Point(489,493), 150));
		confini.put(new Confine(Territorio.AFRICA_ORIENTALE, Territorio.AFRICA_DEL_SUD), new PositionAndRotation(new Point(500,551), 115));
		confini.put(new Confine(Territorio.AFRICA_ORIENTALE, Territorio.MADAGASCAR), new PositionAndRotation(new Point(550,535), 60));
		confini.put(new Confine(Territorio.CONGO, Territorio.AFRICA_DEL_NORD), new PositionAndRotation(new Point(445,481), 240));
		confini.put(new Confine(Territorio.CONGO, Territorio.AFRICA_ORIENTALE), new PositionAndRotation(new Point(506,490), 330));
		confini.put(new Confine(Territorio.CONGO, Territorio.AFRICA_DEL_SUD), new PositionAndRotation(new Point(470,550), 80));
		confini.put(new Confine(Territorio.AFRICA_DEL_SUD, Territorio.AFRICA_ORIENTALE), new PositionAndRotation(new Point(505,542), 295));
		confini.put(new Confine(Territorio.AFRICA_DEL_SUD, Territorio.CONGO), new PositionAndRotation(new Point(472,540), 260));
		confini.put(new Confine(Territorio.AFRICA_DEL_SUD, Territorio.MADAGASCAR), new PositionAndRotation(new Point(546,585), 0));
		confini.put(new Confine(Territorio.MADAGASCAR, Territorio.AFRICA_DEL_SUD), new PositionAndRotation(new Point(535,577), 180));
		confini.put(new Confine(Territorio.MADAGASCAR, Territorio.AFRICA_ORIENTALE), new PositionAndRotation(new Point(555,531), 240));
		
		confini.put(new Confine(Territorio.URALI, Territorio.UCRAINA), new PositionAndRotation(new Point(569,182), 180));
		confini.put(new Confine(Territorio.URALI, Territorio.SIBERIA), new PositionAndRotation(new Point(645,178), 335));
		confini.put(new Confine(Territorio.URALI, Territorio.AFGHANISTAN), new PositionAndRotation(new Point(634,222), 90));
		confini.put(new Confine(Territorio.URALI, Territorio.CINA), new PositionAndRotation(new Point(695,255), 50));		
		confini.put(new Confine(Territorio.SIBERIA, Territorio.CINA), new PositionAndRotation(new Point(700,245), 60));
		confini.put(new Confine(Territorio.SIBERIA, Territorio.URALI), new PositionAndRotation(new Point(648,184), 155));
		confini.put(new Confine(Territorio.SIBERIA, Territorio.CITA), new PositionAndRotation(new Point(692,170), 0));
		confini.put(new Confine(Territorio.SIBERIA, Territorio.JACUZIA), new PositionAndRotation(new Point(687,81), 0));
		confini.put(new Confine(Territorio.SIBERIA, Territorio.MONGOLIA), new PositionAndRotation(new Point(712,228), 345));
		confini.put(new Confine(Territorio.JACUZIA, Territorio.SIBERIA), new PositionAndRotation(new Point(679,88),180));
		confini.put(new Confine(Territorio.JACUZIA, Territorio.CITA), new PositionAndRotation(new Point(716,120), 90));
		confini.put(new Confine(Territorio.JACUZIA, Territorio.KAMCHATKA), new PositionAndRotation(new Point(752,94), 0));
		confini.put(new Confine(Territorio.CITA, Territorio.SIBERIA), new PositionAndRotation(new Point(688,173), 180));
		confini.put(new Confine(Territorio.CITA, Territorio.JACUZIA), new PositionAndRotation(new Point(720,111), 270));
		confini.put(new Confine(Territorio.CITA, Territorio.KAMCHATKA), new PositionAndRotation(new Point(764,138), 315));
		confini.put(new Confine(Territorio.CITA, Territorio.MONGOLIA), new PositionAndRotation(new Point(744,179), 45));
		confini.put(new Confine(Territorio.KAMCHATKA, Territorio.CITA), new PositionAndRotation(new Point(762,142), 135));
		confini.put(new Confine(Territorio.KAMCHATKA, Territorio.JACUZIA), new PositionAndRotation(new Point(745,96), 180));
		confini.put(new Confine(Territorio.KAMCHATKA, Territorio.MONGOLIA), new PositionAndRotation(new Point(800,160), 125));
		confini.put(new Confine(Territorio.KAMCHATKA, Territorio.GIAPPONE), new PositionAndRotation(new Point(847,138), 0));
		confini.put(new Confine(Territorio.KAMCHATKA, Territorio.ALASKA), new PositionAndRotation(new Point(849,83), 0));
		confini.put(new Confine(Territorio.GIAPPONE, Territorio.KAMCHATKA), new PositionAndRotation(new Point(844,137), 180));
		confini.put(new Confine(Territorio.GIAPPONE, Territorio.MONGOLIA), new PositionAndRotation(new Point(848,174), 135));
		confini.put(new Confine(Territorio.MONGOLIA, Territorio.SIBERIA), new PositionAndRotation(new Point(692,215), 225));
		confini.put(new Confine(Territorio.MONGOLIA, Territorio.CITA), new PositionAndRotation(new Point(750,169), 225));
		confini.put(new Confine(Territorio.MONGOLIA, Territorio.KAMCHATKA), new PositionAndRotation(new Point(809,148), 295));
		confini.put(new Confine(Territorio.MONGOLIA, Territorio.GIAPPONE), new PositionAndRotation(new Point(858,169), 315));
		confini.put(new Confine(Territorio.MONGOLIA, Territorio.CINA), new PositionAndRotation(new Point(768,229), 60));
		confini.put(new Confine(Territorio.AFGHANISTAN, Territorio.URALI), new PositionAndRotation(new Point(635,210), 270));
		confini.put(new Confine(Territorio.AFGHANISTAN, Territorio.UCRAINA), new PositionAndRotation(new Point(580,220), 210));
		confini.put(new Confine(Territorio.AFGHANISTAN, Territorio.MEDIO_ORIENTE), new PositionAndRotation(new Point(660,324), 100));
		confini.put(new Confine(Territorio.AFGHANISTAN, Territorio.CINA), new PositionAndRotation(new Point(689,279), 0));
		confini.put(new Confine(Territorio.CINA, Territorio.SIAM), new PositionAndRotation(new Point(797,315), 90));
		confini.put(new Confine(Territorio.CINA, Territorio.INDIA), new PositionAndRotation(new Point(732,317), 100));
		confini.put(new Confine(Territorio.CINA, Territorio.MEDIO_ORIENTE), new PositionAndRotation(new Point(685,320), 150));
		confini.put(new Confine(Territorio.CINA, Territorio.AFGHANISTAN), new PositionAndRotation(new Point(685,280), 180));
		confini.put(new Confine(Territorio.CINA, Territorio.URALI), new PositionAndRotation(new Point(689,240), 230));
		confini.put(new Confine(Territorio.CINA, Territorio.SIBERIA), new PositionAndRotation(new Point(705,234), 240));
		confini.put(new Confine(Territorio.CINA, Territorio.MONGOLIA), new PositionAndRotation(new Point(772,220), 240));
		confini.put(new Confine(Territorio.MEDIO_ORIENTE, Territorio.EGITTO), new PositionAndRotation(new Point(555,391), 165));
		confini.put(new Confine(Territorio.MEDIO_ORIENTE, Territorio.EUROPA_MERIDIONALE), new PositionAndRotation(new Point(517,312), 230));
		confini.put(new Confine(Territorio.MEDIO_ORIENTE, Territorio.UCRAINA), new PositionAndRotation(new Point(607,310), 270));
		confini.put(new Confine(Territorio.MEDIO_ORIENTE, Territorio.AFGHANISTAN), new PositionAndRotation(new Point(660,312), 280));
		confini.put(new Confine(Territorio.MEDIO_ORIENTE, Territorio.CINA), new PositionAndRotation(new Point(705,309), 330));
		confini.put(new Confine(Territorio.MEDIO_ORIENTE, Territorio.INDIA), new PositionAndRotation(new Point(664,361), 0));
		confini.put(new Confine(Territorio.INDIA, Territorio.MEDIO_ORIENTE), new PositionAndRotation(new Point(652,360), 180));
		confini.put(new Confine(Territorio.INDIA, Territorio.CINA), new PositionAndRotation(new Point(742,309), 280));
		confini.put(new Confine(Territorio.INDIA, Territorio.SIAM), new PositionAndRotation(new Point(771,341), 0));
		confini.put(new Confine(Territorio.SIAM, Territorio.INDIA), new PositionAndRotation(new Point(756,340), 180));
		confini.put(new Confine(Territorio.SIAM, Territorio.CINA), new PositionAndRotation(new Point(804,308), 260));
		confini.put(new Confine(Territorio.SIAM, Territorio.INDONESIA), new PositionAndRotation(new Point(841,390), 60));
		
		confini.put(new Confine(Territorio.INDONESIA, Territorio.SIAM), new PositionAndRotation(new Point(840,402), 240));
		confini.put(new Confine(Territorio.INDONESIA, Territorio.NUOVA_GUINEA), new PositionAndRotation(new Point(910,452), 40));
		confini.put(new Confine(Territorio.INDONESIA, Territorio.AUSTRALIA_OCCIDENTALE), new PositionAndRotation(new Point(850,503), 100));
		confini.put(new Confine(Territorio.NUOVA_GUINEA, Territorio.INDONESIA), new PositionAndRotation(new Point(905,442), 220));
		confini.put(new Confine(Territorio.NUOVA_GUINEA, Territorio.AUSTRALIA_OCCIDENTALE), new PositionAndRotation(new Point(890,505), 135));
		confini.put(new Confine(Territorio.NUOVA_GUINEA, Territorio.AUSTRALIA_ORIENTALE), new PositionAndRotation(new Point(931,512), 112));
		confini.put(new Confine(Territorio.AUSTRALIA_OCCIDENTALE, Territorio.INDONESIA), new PositionAndRotation(new Point(854,498), 280));
		confini.put(new Confine(Territorio.AUSTRALIA_OCCIDENTALE, Territorio.NUOVA_GUINEA), new PositionAndRotation(new Point(894,505), 315));
		confini.put(new Confine(Territorio.AUSTRALIA_OCCIDENTALE, Territorio.AUSTRALIA_ORIENTALE), new PositionAndRotation(new Point(893,581), 60));
		confini.put(new Confine(Territorio.AUSTRALIA_ORIENTALE, Territorio.AUSTRALIA_OCCIDENTALE), new PositionAndRotation(new Point(878,563), 240));
		confini.put(new Confine(Territorio.AUSTRALIA_ORIENTALE, Territorio.NUOVA_GUINEA), new PositionAndRotation(new Point(945,507), 292));
	}
	
	public Point getPosition(Territorio territorio){
		return posizioni.get(territorio);
	}
	
	public PositionAndRotation getPosizioneERotazione(Territorio territorioDa, Territorio territorioA){
		Confine confine = new Confine(territorioDa, territorioA);
		return confini.get(confine);
	}
	
	public static class Confine{
		public Confine(Territorio territorioDa, Territorio territorioA){
			this.territorioA = territorioA;
			this.territorioDa = territorioDa;
		}
		private Territorio territorioDa;
		private Territorio territorioA;
		public Territorio getTerritorioDa() {
			return territorioDa;
		}
		public void setTerritorioDa(Territorio territorioDa) {
			this.territorioDa = territorioDa;
		}
		public Territorio getTerritorioA() {
			return territorioA;
		}
		public void setTerritorioA(Territorio territorioA) {
			this.territorioA = territorioA;
		}
		public boolean equals2(Object o){
			boolean result = false;
			if (o!= null && o instanceof Confine){
				Confine confine = (Confine) o;
				result = this.getTerritorioDa() == confine.getTerritorioDa() && this.getTerritorioA() == confine.getTerritorioA();
			}
			return result;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((territorioA == null) ? 0 : territorioA.hashCode());
			result = prime * result
					+ ((territorioDa == null) ? 0 : territorioDa.hashCode());
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
			Confine other = (Confine) obj;
			if (territorioA != other.territorioA)
				return false;
			if (territorioDa != other.territorioDa)
				return false;
			return true;
		}
		
	}
	
	public static class PositionAndRotation{
		private Point position;
		private int rotationAngle;
		public PositionAndRotation(Point position, int rotationAngle){
			this.position = position;
			this.rotationAngle = rotationAngle;
		}
		public Point getPosition() {
			return position;
		}
		public void setPosition(Point position) {
			this.position = position;
		}
		public int getRotationAngle() {
			return rotationAngle;
		}
		public void setRotationAngle(int rotationAngle) {
			this.rotationAngle = rotationAngle;
		}
	}
}
