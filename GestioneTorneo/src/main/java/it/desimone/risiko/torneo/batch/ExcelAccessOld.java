package it.desimone.risiko.torneo.batch;

import it.desimone.risiko.torneo.dto.ClubDTO;
import it.desimone.risiko.torneo.dto.GiocatoreDTO;
import it.desimone.risiko.torneo.dto.Partita;
import it.desimone.risiko.torneo.dto.RegioneDTO;
import it.desimone.risiko.torneo.dto.ScorePlayer;
import it.desimone.risiko.torneo.dto.ScorePlayerCampionatoGufo;
import it.desimone.risiko.torneo.dto.ScorePlayerOpen;
import it.desimone.risiko.torneo.dto.ScorePlayerQualificazioniNazionale;
import it.desimone.risiko.torneo.dto.ScorePlayerRaduno;
import it.desimone.risiko.torneo.dto.ScorePlayerTorneoColoni;
import it.desimone.risiko.torneo.dto.ScorePlayerTorneoDominion;
import it.desimone.risiko.torneo.dto.ScorePlayerTorneoGufo;
import it.desimone.risiko.torneo.dto.ScorePlayerTorneoStoneAge;
import it.desimone.risiko.torneo.utils.ClubLoader;
import it.desimone.risiko.torneo.utils.RegioniLoader;
import it.desimone.risiko.torneo.utils.ScoreCampionatoComparator;
import it.desimone.risiko.torneo.utils.ScoreQualificazioniNazionaleComparator;
import it.desimone.risiko.torneo.utils.ScoreRadunoComparator;
import it.desimone.risiko.torneo.utils.ScoreSemifinalistiRadunoComparator;
import it.desimone.risiko.torneo.utils.ScoreTorneoColoniComparator;
import it.desimone.risiko.torneo.utils.ScoreTorneoDominionComparator;
import it.desimone.risiko.torneo.utils.ScoreTorneoOpenComparator;
import it.desimone.risiko.torneo.utils.ScoreTorneoStoneAgeComparator;
import it.desimone.risiko.torneo.utils.TipoTorneo;
import it.desimone.utils.MyException;
import it.desimone.utils.MyLogger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;


//import org.apache.commons.lang.exception.NestableException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.hssf.usermodel.contrib.HSSFCellUtil;
//import org.apache.poi.hssf.usermodel.contrib.HSSFRegionUtil;
import org.apache.poi.hssf.util.HSSFCellUtil;
import org.apache.poi.hssf.util.HSSFRegionUtil;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;

public class ExcelAccessOld {

	private final ClubLoader clubLoader = new ClubLoader();
	private final RegioniLoader regioniLoader = new RegioniLoader();
	
	public static final String SCHEDA_ISCRITTI 		= "Iscritti";
	public static final String SCHEDA_PRIMO_TURNO 	= "1° Turno";
	public static final String SCHEDA_SECONDO_TURNO = "2° Turno";
	public static final String SCHEDA_TERZO_TURNO 	= "3° Turno";
	public static final String SCHEDA_QUARTO_TURNO 	= "4° Turno";
	public static final String SCHEDA_SEMIFINALI	= "Semifinali";
	public static final String STAMPA_PRIMO_TURNO 	= "Stampa 1° Turno";
	public static final String STAMPA_SECONDO_TURNO = "Stampa 2° Turno";
	public static final String STAMPA_TERZO_TURNO 	= "Stampa 3° Turno";
	public static final String STAMPA_QUARTO_TURNO 	= "Stampa 4° Turno";
	public static final String SCHEDA_LOG 			= "Log";
	public static final String SCHEDA_CLASSIFICA	= "Classifica";

	
	private final String PATH_FILE_PROPERTIES = ".\\Properties\\";
	private Properties properties;
	
	String pathFileExcel;
	short posizioneId 			= 0;
	short posizioneNome 		= 1;
	short posizioneCognome 		= 2;
	short posizioneNick 		= 3;
	short posizioneClub 		= 4;
	short posizionePresenza 	= 5;
	short posizioneRegione 		= 6;
	short posizioneProvincia 	= 7;

	private HSSFWorkbook foglioTorneo;
	private HSSFCellStyle styleCell;
	private HSSFCellStyle styleCellId;
	private HSSFCellStyle styleIntestazione;
	private HSSFCellStyle styleIntestazioneStampa;
	private HSSFCellStyle styleCellStampa;
	
	private static short indiceFormatTreDecimali = -1;
	
	private int indexPicture;
	
	public ExcelAccessOld(){}
		
//	public ExcelAccess(){
//		try {
//			readProperties(PATH_FILE_PROPERTIES+"Properties.txt");
//		}catch(FileNotFoundException fe){
//			throw new MyException("Non trovato il file di Properties: "+fe.getMessage());
//		}catch(IOException ioe){
//			throw new MyException("Impossibile accedere al file di Properties: "+ioe.getMessage());
//		}
//	}

	public ExcelAccessOld(File fileExcel){
		try {
			pathFileExcel = fileExcel.getPath();
//			readProperties(PATH_FILE_PROPERTIES+"Properties.txt");
//		}catch(FileNotFoundException fe){
//			throw new MyException("Non trovato il file di Properties: "+fe.getMessage());
//		}catch(IOException ioe){
//			throw new MyException("Impossibile accedere al file di Properties: "+ioe.getMessage());
		}finally{}
	}
	
	public ExcelAccessOld(String pathFile){
		try {
			readProperties(pathFile);
		}catch(FileNotFoundException fe){
			throw new MyException("Non trovato il file di Properties: "+fe.getMessage());
		}catch(IOException ioe){
			throw new MyException("Impossibile accedere al file di Properties: "+ioe.getMessage());
		}
	}
	
	private void readProperties(String pathFile) throws FileNotFoundException, IOException{
		File fileProps = new File(pathFile);
		properties = new Properties();
		properties.load(new FileInputStream(fileProps));
		//pathFileExcel    		= properties.getProperty("PATH_FILE");
		posizioneId    		= (short)(Short.valueOf(properties.getProperty("POSIZIONE_ID"))  		- 1);
		posizioneNome    	= (short)(Short.valueOf(properties.getProperty("POSIZIONE_NOME"))  		- 1);
		posizioneCognome 	= (short)(Short.valueOf(properties.getProperty("POSIZIONE_COGNOME"))  	- 1);
		posizioneNick 		= (short)(Short.valueOf(properties.getProperty("POSIZIONE_NICK"))  		- 1);
		posizioneRegione 	= (short)(Short.valueOf(properties.getProperty("POSIZIONE_REGIONE"))  	- 1);
		posizioneProvincia 	= (short)(Short.valueOf(properties.getProperty("POSIZIONE_PROVINCIA"))  - 1);
		posizioneClub 		= (short)(Short.valueOf(properties.getProperty("POSIZIONE_CLUB"))  		- 1);
		posizionePresenza 	= (short)(Short.valueOf(properties.getProperty("POSIZIONE_PRESENZA"))	- 1);
	}
	
	public void openFileExcel(){
		if (pathFileExcel != null){
			try{
				POIFSFileSystem fs = null;
				fs = new POIFSFileSystem(new FileInputStream(pathFileExcel));
				foglioTorneo = new HSSFWorkbook(fs);
				HSSFDataFormat df = foglioTorneo.createDataFormat();
				short formato = df.getFormat("0.000");
				if (formato != -1){
					indiceFormatTreDecimali = formato;
					MyLogger.getLogger().finer("Trovato indice per formato 0.000: "+String.valueOf(df.getFormat("0.000")));
				}
				creaStili();
				//indexPicture = loadImage(PATH_FILE_PROPERTIES);
			}catch(FileNotFoundException fe){
				throw new MyException("Non trovato il file Excel "+pathFileExcel+": "+fe.getMessage());
			}catch(IOException ioe){
				throw new MyException("Impossibile accedere al file Excel "+pathFileExcel+": "+ioe.getMessage());
			}
		}
	}
	
	private void creaStili(){
		styleCellId = foglioTorneo.createCellStyle();
		//styleCellId.setHidden(true);
		//styleCellId.setLocked(true);
		styleCellId.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);

		styleCell = foglioTorneo.createCellStyle();
		styleCell.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		HSSFFont font = foglioTorneo.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setColor(HSSFColor.BLACK.index);
		styleCell.setFont(font);
		styleCell.setWrapText(true);
		styleCell.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
		styleCell.setFillForegroundColor(HSSFColor.LIGHT_ORANGE.index);
		styleCell.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		styleCellStampa = foglioTorneo.createCellStyle();
		styleCellStampa.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		HSSFFont fontStampa = foglioTorneo.createFont();
		fontStampa.setFontName(HSSFFont.FONT_ARIAL);
		fontStampa.setFontHeight((short)100);
		//font.setColor(HSSFColor.BLACK.index);
		styleCellStampa.setFont(fontStampa);
		styleCellStampa.setWrapText(true);
		styleCellStampa.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
		
		styleIntestazione = foglioTorneo.createCellStyle();		
		styleIntestazione.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleIntestazione.setBorderBottom(HSSFCellStyle.BORDER_THICK);
		styleIntestazione.setBorderTop(HSSFCellStyle.BORDER_THICK);
		styleIntestazione.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		styleIntestazione.setBorderRight(HSSFCellStyle.BORDER_THIN);
		HSSFFont fontIntestazione = foglioTorneo.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		styleIntestazione.setFont(fontIntestazione);
		styleIntestazione.setWrapText(false);
		styleIntestazione.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		styleIntestazione.setFillForegroundColor(HSSFColor.ORANGE.index);
		styleIntestazione.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		
		styleIntestazioneStampa = foglioTorneo.createCellStyle();		
		styleIntestazioneStampa.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont fontIntestazioneStampa = foglioTorneo.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		styleIntestazione.setFont(fontIntestazioneStampa);
		styleIntestazione.setWrapText(false);
		styleIntestazione.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
	}
	
	private int loadImage(String pathFile) throws IOException{
		int pictureIndex;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try
        {
            fis = new FileInputStream(pathFile+File.separator+"image004_rid.png");
            bos = new ByteArrayOutputStream( );
            int c;
            while ( (c = fis.read()) != -1)
                bos.write( c );
            pictureIndex = foglioTorneo.addPicture(bos.toByteArray(),HSSFWorkbook.PICTURE_TYPE_PNG); 
        }
        finally
        {
            if (fis != null)
                fis.close();
            if (bos != null)
                bos.close();
        }
        System.out.println("pictureIndex: "+pictureIndex);
        return pictureIndex;
	}
	
	public List<GiocatoreDTO> getListaGiocatori(boolean partecipanti){
		//List<GiocatoreDTO> listaGiocatori = new ArrayList<GiocatoreDTO>();
		Set<GiocatoreDTO> listaGiocatori = new HashSet<GiocatoreDTO>();
		HSSFSheet sheet = foglioTorneo.getSheet(SCHEDA_ISCRITTI);
		
		//int ultimaRiga = sheet.getLastRowNum();
		//System.out.println("Ultima riga: "+ultimaRiga);
		for (int i = 3; i<=sheet.getLastRowNum(); i++){
			HSSFRow row = sheet.getRow(i);
			GiocatoreDTO giocatore = null;
			try{
				giocatore = getGiocatoreFromRow(row);
			}catch(Exception me){
				throw new MyException(me,"Riga n° "+(i+1)+" della scheda "+SCHEDA_ISCRITTI);
			}
			if (giocatore != null && giocatore.getId() != null && giocatore.getNome() != null && giocatore.getNome().length() >0){
				if (!partecipanti || giocatore.getPresenteTorneo()){
					boolean nonGiaPresente = listaGiocatori.add(giocatore);
					if (!nonGiaPresente){
						throw new MyException("E' presente l'ID "+giocatore.getId()+ " duplicato sulla scheda "+SCHEDA_ISCRITTI);
					}
				}
			}
		}
		
		return new ArrayList(listaGiocatori);
	}
	
	public GiocatoreDTO getGiocatore(Integer id){
		HSSFSheet sheet = foglioTorneo.getSheet(SCHEDA_ISCRITTI);
		
		for (int i = 3; i<sheet.getLastRowNum(); i++){
			HSSFRow row = sheet.getRow(i);
			GiocatoreDTO giocatore = null;
			try{
				giocatore = getGiocatoreFromRow(row);
			}catch(Exception me){
				throw new MyException(me,"Riga n° "+(i+1)+" della scheda "+SCHEDA_ISCRITTI);
			}
			if (giocatore != null && giocatore.getId() != null && giocatore.getId().equals(id)){
				return giocatore;
			}
		}
		
		return null;
	}
	
	private String determinaValoreCella(HSSFRow row, short posizioneCella){
		HSSFCell cella   = row.getCell(posizioneCella);
		String result 		= "";
		int tipoCella   = cella.getCellType();
		if (tipoCella == Cell.CELL_TYPE_STRING){
			result 		    = cella.getRichStringCellValue().getString();
		}else if (tipoCella == Cell.CELL_TYPE_NUMERIC){
			result			= Double.toString(cella.getNumericCellValue());
		}else if (tipoCella != Cell.CELL_TYPE_BLANK){
			MyLogger.getLogger().info("Impossibile leggere la cella della colonna in posizione "+posizioneCella+ " della riga "+ (row.getRowNum()+1)+" perchè di tipo imprevisto: "+tipoCella);
		}
		return result;
	}
	
	private GiocatoreDTO getGiocatoreFromRow(HSSFRow row){
		GiocatoreDTO giocatore = new GiocatoreDTO();
		Short id = null;
		try{
			id 			= (short)row.getCell(posizioneId).getNumericCellValue();
		}catch(NumberFormatException nfe){
			throw new MyException(nfe,"Colonna ID con valore non numerico");
		}
		//String nome 		= row.getCell(posizioneNome).getRichStringCellValue().getString();
		//String cognome 		= row.getCell(posizioneCognome).getRichStringCellValue().getString();
		//String nick 		= row.getCell(posizioneNick).getRichStringCellValue().getString();
		
		String nome = determinaValoreCella(row, posizioneNome);
		String cognome = determinaValoreCella(row, posizioneCognome);
		String nick = determinaValoreCella(row, posizioneNick);
		
		String regione 		= row.getCell(posizioneRegione).getRichStringCellValue().getString();
		//String provincia 	= row.getCell(posizioneProvincia).getRichStringCellValue().getString();
		String club 		= row.getCell(posizioneClub).getRichStringCellValue().getString();
		String presenza		= row.getCell(posizionePresenza).getRichStringCellValue().getString();
		giocatore.setId(id.intValue());
		giocatore.setNome(nome);
		giocatore.setCognome(cognome);
		giocatore.setNick(nick);
		if (regione != null && regione.length() >0){
			try{
				Field field = RegioniLoader.class.getField(regione.trim());
				RegioneDTO regioneDTO = (RegioneDTO) field.get(regioniLoader);
				giocatore.setRegioneProvenienza(regioneDTO);
			}catch(NoSuchFieldException nfe){
				RegioneDTO regioneDTO = new RegioneDTO(regione);
				giocatore.setRegioneProvenienza(regioneDTO);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
//		if (provincia != null && provincia.length() >0){
//			provincia = provincia.replaceAll("\t", "");
//			try{
//				Field field = ProvincieLoader.class.getField(provincia);
//				ProvinciaDTO provinciaDTO = (ProvinciaDTO) field.get(provincieLoader);
//				giocatore.setProvinciaProvenienza(provinciaDTO);
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//		}
		if (club != null && club.length() >0){
			try{
				Field field = ClubLoader.class.getField(club.trim());
				ClubDTO clubDTO = (ClubDTO) field.get(clubLoader);
				giocatore.setClubProvenienza(clubDTO);
			}catch(NoSuchFieldException nfe){
				ClubDTO clubDTO = new ClubDTO(club);
				giocatore.setClubProvenienza(clubDTO);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		giocatore.setPresenteTorneo(presenza!=null&&presenza.equalsIgnoreCase("SI"));		
		return giocatore;
	}
	
	private HSSFSheet creaSchedaTurno(String nomeScheda, boolean hidden){
		HSSFSheet result = foglioTorneo.getSheet(nomeScheda);
		if (result == null){
			result = foglioTorneo.createSheet(nomeScheda);
//			result.autoSizeColumn((short)1);
//			result.autoSizeColumn((short)4);
//			result.autoSizeColumn((short)7);
//			result.autoSizeColumn((short)10);
			if (hidden){
				result.setColumnHidden((short)0,true);
				result.setColumnHidden((short)3,true);
				result.setColumnHidden((short)6,true);
				result.setColumnHidden((short)9,true);
				result.setColumnHidden((short)12,true);
			}else{
				result.setColumnWidth((short)0,(short)1000);
				result.setColumnWidth((short)3,(short)1000);
				result.setColumnWidth((short)6,(short)1000);
				result.setColumnWidth((short)9,(short)1000);
				result.setColumnWidth((short)12,(short)1000);
			}
			result.setColumnWidth((short)1,(short)6000);
			result.setColumnWidth((short)4,(short)6000);
			result.setColumnWidth((short)7,(short)6000);
			result.setColumnWidth((short)10,(short)6000);
			result.setColumnWidth((short)13,(short)6000);
			result.setColumnWidth((short)2,(short)2000);
			result.setColumnWidth((short)5,(short)2000);
			result.setColumnWidth((short)8,(short)2000);
			result.setColumnWidth((short)11,(short)2000);
			result.setColumnWidth((short)14,(short)2000);
		}
//		try{
//			result = schedaTorneo.createSheet(nomeScheda);
//		}catch(IllegalArgumentException ie){
//			//int pos = schedaTorneo.getSheetIndex(nomeScheda);
//			//schedaTorneo.removeSheetAt(pos);
//			//result = schedaTorneo.createSheet(nomeScheda);
//			result = schedaTorneo.getSheet(nomeScheda);
//		}
		return result;
	}
	
	private HSSFSheet creaSchedaTurnoDominion(String nomeScheda){
		int numeroDati = 4;
		HSSFSheet result = foglioTorneo.getSheet(nomeScheda);
		if (result == null){
			result = foglioTorneo.createSheet(nomeScheda);
			for (short numeroGiocatori = 1; numeroGiocatori <=5; numeroGiocatori++){
				result.setColumnHidden((short)((numeroDati+2)*(numeroGiocatori - 1)),true);
				result.setColumnWidth((short)((numeroDati+2)*(numeroGiocatori - 1)+1),(short)6000);
				result.setColumnWidth((short)((numeroDati+2)*(numeroGiocatori - 1)+2),(short)1000);
				result.setColumnWidth((short)((numeroDati+2)*(numeroGiocatori - 1)+3),(short)1000);
				result.setColumnWidth((short)((numeroDati+2)*(numeroGiocatori - 1)+4),(short)1000);
				result.setColumnWidth((short)((numeroDati+2)*(numeroGiocatori - 1)+5),(short)1000);
			}
		}
		return result;
	}
	
	private HSSFSheet creaSchedaTurnoStoneAge(String nomeScheda){
		int numeroDati = 5;
		HSSFSheet result = foglioTorneo.getSheet(nomeScheda);
		if (result == null){
			result = foglioTorneo.createSheet(nomeScheda);
			for (short numeroGiocatori = 1; numeroGiocatori <=5; numeroGiocatori++){
				result.setColumnHidden((short)((numeroDati+2)*(numeroGiocatori - 1)),true);
				result.setColumnWidth((short)((numeroDati+2)*(numeroGiocatori - 1)+1),(short)6000);
				result.setColumnWidth((short)((numeroDati+2)*(numeroGiocatori - 1)+2),(short)1000);
				result.setColumnWidth((short)((numeroDati+2)*(numeroGiocatori - 1)+3),(short)1000);
				result.setColumnWidth((short)((numeroDati+2)*(numeroGiocatori - 1)+4),(short)1000);
				result.setColumnWidth((short)((numeroDati+2)*(numeroGiocatori - 1)+5),(short)1000);
				result.setColumnWidth((short)((numeroDati+2)*(numeroGiocatori - 1)+5),(short)1000);
			}
		}
		return result;
	}
	
	public void scriviPartite(String nomeTurno, Partita partita){
		HSSFSheet schedaTurno = creaSchedaTurno(nomeTurno, true);
		int prossimaRiga = schedaTurno.getLastRowNum()+(schedaTurno.getLastRowNum()==0?0:1);
		/* TEST quinta scheda */
		//int prossimaRiga = schedaTurno.getLastRowNum()+1;
		HSSFRow rowIntestazione = schedaTurno.createRow(prossimaRiga);
		HSSFCell cellIntestazione = rowIntestazione.createCell((short)0);
		cellIntestazione.setCellStyle(styleIntestazione);
		cellIntestazione.setCellValue("Tavolo N°"+partita.getNumeroTavolo());
		Region region = new Region(prossimaRiga,(short)0,prossimaRiga,(short)(partita.getNumeroGiocatori()*3-1));
		try {
			HSSFRegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN,region,schedaTurno,foglioTorneo);
			HSSFRegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN,region,schedaTurno,foglioTorneo);
			HSSFRegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN,region,schedaTurno,foglioTorneo);
			HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN,region,schedaTurno,foglioTorneo);
		} catch (/*Nestable*/Exception e) {
			throw new MyException("Errore nell'utilizzo della classe HSSFRegionUtil: "+e.getMessage());
		}
		schedaTurno.addMergedRegion(region);
		HSSFRow row = schedaTurno.createRow(prossimaRiga+1);
		short counterCell = 0;
		for (GiocatoreDTO giocatore: partita.getGiocatori()){
			HSSFCell cellId = row.createCell(counterCell++);
			cellId.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cellId.setCellValue(giocatore.getId());
			cellId.setCellStyle(styleCellId);
			HSSFCell cellNominativo = row.createCell(counterCell++);
			cellNominativo.setCellStyle(styleCell);
			String nominativo = giocatore.getNome()+" "+giocatore.getCognome();
//			if (giocatore.getNick() != null){
//				nominativo +=" ("+giocatore.getNick()+")";
//			}
			if (giocatore.getClubProvenienza() != null){
				nominativo +="\n"+giocatore.getClubProvenienza();
			}
			cellNominativo.setCellValue(new HSSFRichTextString(nominativo));
			HSSFCell cellPunteggio = row.createCell(counterCell++);
			cellPunteggio.setCellStyle(styleCell);
			cellPunteggio.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cellPunteggio.setCellValue(partita.getTavolo().get(giocatore));
		}
	}
	
	public void scriviPartiteFaseFinaleQualificazioneRisiko(String nomeTurno, Partita partita){
		HSSFSheet schedaTurno = creaSchedaTurno(nomeTurno, false);
		int prossimaRiga = schedaTurno.getLastRowNum()+(schedaTurno.getLastRowNum()==0?0:1);
		/* TEST quinta scheda */
		//int prossimaRiga = schedaTurno.getLastRowNum()+1;
		HSSFRow rowIntestazione = schedaTurno.createRow(prossimaRiga);
		HSSFCell cellIntestazione = rowIntestazione.createCell((short)0);
		cellIntestazione.setCellStyle(styleIntestazione);
		cellIntestazione.setCellValue("Tavolo N°"+partita.getNumeroTavolo());
		Region region = new Region(prossimaRiga,(short)0,prossimaRiga,(short)(partita.getNumeroGiocatori()*3-1));
		try {
			HSSFRegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN,region,schedaTurno,foglioTorneo);
			HSSFRegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN,region,schedaTurno,foglioTorneo);
			HSSFRegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN,region,schedaTurno,foglioTorneo);
			HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN,region,schedaTurno,foglioTorneo);
		} catch (/*Nestable*/Exception e) {
			throw new MyException("Errore nell'utilizzo della classe HSSFRegionUtil: "+e.getMessage());
		}
		schedaTurno.addMergedRegion(region);
		HSSFRow row = schedaTurno.createRow(prossimaRiga+1);
		short counterCell = 0;
		HSSFSheet sheetIscritti = foglioTorneo.getSheet(SCHEDA_ISCRITTI);
		int numeroIscritti = sheetIscritti == null?0:sheetIscritti.getLastRowNum()+1;
		for (GiocatoreDTO giocatore: partita.getGiocatori()){
			HSSFCell cellId = row.createCell(counterCell++);
			cellId.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cellId.setCellValue(giocatore.getId());
			cellId.setCellStyle(styleCellId);
			HSSFCell cellNominativo = row.createCell(counterCell++);
			cellNominativo.setCellStyle(styleCell);
			String nominativo = giocatore.getNome()+" "+giocatore.getCognome();
			if (giocatore.getClubProvenienza() != null){
				nominativo +="\n"+giocatore.getClubProvenienza();
			}
			char indiceColonnaId = trascodificaIndiceColonna(cellId.getCellNum());
			int  indiceRigaId = prossimaRiga+2;
			String formula = "VLOOKUP("+indiceColonnaId+indiceRigaId+",Iscritti!A4:E"+numeroIscritti+",2,FALSE) & \" \" & VLOOKUP("+indiceColonnaId+indiceRigaId+",Iscritti!A4:E"+numeroIscritti+",3,FALSE) & \" \" & VLOOKUP("+indiceColonnaId+indiceRigaId+",Iscritti!A4:E"+numeroIscritti+",5,FALSE)";
			cellNominativo.setCellFormula(formula);
			cellNominativo.setCellValue(new HSSFRichTextString(nominativo));

			HSSFCell cellPunteggio = row.createCell(counterCell++);
			cellPunteggio.setCellStyle(styleCell);
			cellPunteggio.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cellPunteggio.setCellValue(partita.getTavolo().get(giocatore));
		}
	}
	
	private char trascodificaIndiceColonna(int index){
		char result = ' ';
		switch (index) {
		case 0:
			result = 'A';
			break;
		case 1:
			result = 'B';
			break;
		case 2:
			result = 'C';
			break;
		case 3:
			result = 'D';
			break;
		case 4:
			result = 'E';
			break;
		case 5:
			result = 'F';
			break;
		case 6:
			result = 'G';
			break;
		case 7:
			result = 'H';
			break;
		case 8:
			result = 'I';
			break;
		case 9:
			result = 'J';
			break;
		case 10:
			result = 'K';
			break;
		case 11:
			result = 'L';
			break;
		case 12:
			result = 'M';
			break;
		case 13:
			result = 'N';
			break;
		case 14:
			result = 'O';
			break;
		case 15:
			result = 'P';
			break;
		case 16:
			result = 'Q';
			break;
		case 17:
			result = 'R';
			break;
		case 18:
			result = 'S';
			break;
		case 19:
			result = 'T';
			break;
		case 20:
			result = 'U';
			break;
		case 21:
			result = 'V';
			break;
		case 22:
			result = 'W';
			break;
		case 23:
			result = 'X';
			break;
		case 24:
			result = 'Y';
			break;
		case 25:
			result = 'Z';
			break;
		default:
			break;
		}
		
		return result;
	}
	
	public void scriviPartiteDominion(String nomeTurno, Partita partita){
		HSSFSheet schedaTurno = creaSchedaTurnoDominion(nomeTurno);
		int prossimaRiga = schedaTurno.getLastRowNum()+(schedaTurno.getLastRowNum()==0?0:1);
		/* TEST quinta scheda */
		//int prossimaRiga = schedaTurno.getLastRowNum()+1;
		HSSFRow rowIntestazione = schedaTurno.createRow(prossimaRiga);
		HSSFCell cellIntestazione = rowIntestazione.createCell((short)0);
		cellIntestazione.setCellStyle(styleIntestazione);
		cellIntestazione.setCellValue("Tavolo N°"+partita.getNumeroTavolo());
		Region region = new Region(prossimaRiga,(short)0,prossimaRiga,(short)(partita.getNumeroGiocatori()*6-1));
		try {
			HSSFRegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN,region,schedaTurno,foglioTorneo);
			HSSFRegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN,region,schedaTurno,foglioTorneo);
			HSSFRegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN,region,schedaTurno,foglioTorneo);
			HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN,region,schedaTurno,foglioTorneo);
		} catch (/*Nestable*/Exception e) {
			throw new MyException("Errore nell'utilizzo della classe HSSFRegionUtil: "+e.getMessage());
		}
		schedaTurno.addMergedRegion(region);
		HSSFRow row = schedaTurno.createRow(prossimaRiga+1);
		short counterCell = 0;
		for (GiocatoreDTO giocatore: partita.getGiocatori()){
			HSSFCell cellId = row.createCell(counterCell++);
			cellId.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cellId.setCellValue(giocatore.getId());
			cellId.setCellStyle(styleCellId);
			HSSFCell cellNominativo = row.createCell(counterCell++);
			cellNominativo.setCellStyle(styleCell);
			String nominativo = giocatore.getNome()+" "+giocatore.getCognome();
//			if (giocatore.getNick() != null){
//				nominativo +=" ("+giocatore.getNick()+")";
//			}
			if (giocatore.getClubProvenienza() != null){
				nominativo +="\n"+giocatore.getClubProvenienza();
			}
			cellNominativo.setCellValue(new HSSFRichTextString(nominativo));
			HSSFCell cellPunteggio = row.createCell(counterCell++);
			cellPunteggio.setCellStyle(styleCell);
			cellPunteggio.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cellPunteggio.setCellValue(partita.getTavolo().get(giocatore));
			
			HSSFCell cellOri = row.createCell(counterCell++);
			cellOri.setCellStyle(styleCell);
			cellOri.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			HSSFCell cellMonete = row.createCell(counterCell++);
			cellMonete.setCellStyle(styleCell);
			cellMonete.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			HSSFCell cellAzioni = row.createCell(counterCell++);
			cellAzioni.setCellStyle(styleCell);
			cellAzioni.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

			Object[] datiAggiuntivi = partita.getDatiAggiuntiviTavolo().get(giocatore);
			if (datiAggiuntivi != null && datiAggiuntivi.length >=3){
				cellOri.setCellValue((Double)datiAggiuntivi[0]);
				cellMonete.setCellValue((Double) datiAggiuntivi[1]);
				cellAzioni.setCellValue((Double) datiAggiuntivi[2]);
			}
		
		}
	}
	
	public void scriviPartiteStoneAge(String nomeTurno, Partita partita){
		HSSFSheet schedaTurno = creaSchedaTurnoStoneAge(nomeTurno);
		int prossimaRiga = schedaTurno.getLastRowNum()+(schedaTurno.getLastRowNum()==0?0:1);
		/* TEST quinta scheda */
		//int prossimaRiga = schedaTurno.getLastRowNum()+1;
		HSSFRow rowIntestazione = schedaTurno.createRow(prossimaRiga);
		HSSFCell cellIntestazione = rowIntestazione.createCell((short)0);
		cellIntestazione.setCellStyle(styleIntestazione);
		cellIntestazione.setCellValue("Tavolo N°"+partita.getNumeroTavolo());
		Region region = new Region(prossimaRiga,(short)0,prossimaRiga,(short)(partita.getNumeroGiocatori()*7-1));
		try {
			HSSFRegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN,region,schedaTurno,foglioTorneo);
			HSSFRegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN,region,schedaTurno,foglioTorneo);
			HSSFRegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN,region,schedaTurno,foglioTorneo);
			HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN,region,schedaTurno,foglioTorneo);
		} catch (/*Nestable*/Exception e) {
			throw new MyException("Errore nell'utilizzo della classe HSSFRegionUtil: "+e.getMessage());
		}
		schedaTurno.addMergedRegion(region);
		HSSFRow row = schedaTurno.createRow(prossimaRiga+1);
		short counterCell = 0;
		for (GiocatoreDTO giocatore: partita.getGiocatori()){
			HSSFCell cellId = row.createCell(counterCell++);
			cellId.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cellId.setCellValue(giocatore.getId());
			cellId.setCellStyle(styleCellId);
			HSSFCell cellNominativo = row.createCell(counterCell++);
			cellNominativo.setCellStyle(styleCell);
			String nominativo = giocatore.getNome()+" "+giocatore.getCognome();
//			if (giocatore.getNick() != null){
//				nominativo +=" ("+giocatore.getNick()+")";
//			}
			if (giocatore.getClubProvenienza() != null){
				nominativo +="\n"+giocatore.getClubProvenienza();
			}
			cellNominativo.setCellValue(new HSSFRichTextString(nominativo));
			HSSFCell cellPunteggio = row.createCell(counterCell++);
			cellPunteggio.setCellStyle(styleCell);
			cellPunteggio.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cellPunteggio.setCellValue(partita.getTavolo().get(giocatore));
			
			HSSFCell cellPuntiOfferti = row.createCell(counterCell++);
			cellPuntiOfferti.setCellStyle(styleCell);
			cellPuntiOfferti.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			HSSFCell cellGrani = row.createCell(counterCell++);
			cellGrani.setCellStyle(styleCell);
			cellGrani.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			HSSFCell cellUtensili = row.createCell(counterCell++);
			cellUtensili.setCellStyle(styleCell);
			cellUtensili.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			HSSFCell cellOmini = row.createCell(counterCell++);
			cellOmini.setCellStyle(styleCell);
			cellOmini.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

			Object[] datiAggiuntivi = partita.getDatiAggiuntiviTavolo().get(giocatore);
			if (datiAggiuntivi != null && datiAggiuntivi.length >=4){
				cellPuntiOfferti.setCellValue((Double)datiAggiuntivi[0]);
				cellGrani.setCellValue((Double)datiAggiuntivi[0]);
				cellUtensili.setCellValue((Double) datiAggiuntivi[1]);
				cellOmini.setCellValue((Double) datiAggiuntivi[2]);
			}
		
		}
	}
	
	public void stampaPartite(String nomeTurno, Partita partita){
		int numeroPartita = partita.getNumeroTavolo();
		HSSFSheet stampaSheet = foglioTorneo.getSheet(nomeTurno);
		int numeroRigaIntestazione;
		if (numeroPartita%2 == 0){
			numeroRigaIntestazione = 57*((numeroPartita-2)/2)+29;
		}else{
			numeroRigaIntestazione = 57*(numeroPartita - 1)/2;
		}
		HSSFRow rowIntestazione = stampaSheet.getRow(numeroRigaIntestazione);
		if (rowIntestazione == null){
			throw new MyException("Non è presente la riga "+(numeroRigaIntestazione+1)+ " sulla scheda "+nomeTurno);
		}
		HSSFCell cellIntestazione = rowIntestazione.getCell((short)1);
		cellIntestazione.setCellValue(nomeTurno+" - Tavolo N°"+partita.getNumeroTavolo());
		short counterPlayer = 0;
		for (GiocatoreDTO giocatore: partita.getGiocatori()){
			String nominativo = giocatore.getNome()+" "+giocatore.getCognome();
			if (giocatore.getClubProvenienza() != null){
				nominativo += " - "+giocatore.getClubProvenienza().getDenominazione();
			}
			int numRow = 24+numeroRigaIntestazione+counterPlayer++;
			HSSFRow playerRow = stampaSheet.getRow(numRow);
			HSSFCell cellNominativoPlayer = playerRow.getCell((short)1);
			cellNominativoPlayer.setCellValue(new HSSFRichTextString(nominativo));
		}
	}
	
	public void stampaSchedaPartite(String nomeTurno, Partita[] partite){
		String nomeScheda = "Stampe "+nomeTurno;
		HSSFSheet stampaScheda = creaSchedaTurno(nomeScheda, true);
		HSSFPatriarch patriarch = stampaScheda.createDrawingPatriarch();
		for (Partita partita: partite){
			int numeroPartita = partita.getNumeroTavolo();
			HSSFClientAnchor anchor = new HSSFClientAnchor(0,0,0,0,(short)0,(short)(30*(numeroPartita-1)),(short)8,(short)(30*(numeroPartita-1)+24));
			anchor.setAnchorType(3);
			patriarch.createPicture(anchor,indexPicture);
		}
	}
	
	private Partita[] loadPartite(String nomeTurno, boolean withGhost, TipoTorneo tipoTorneo){
		List giocatori = getListaGiocatori(false);
		Collections.sort(giocatori);
		Partita[] partite = null;
		HSSFSheet schedaTurno = foglioTorneo.getSheet(nomeTurno);
		if (schedaTurno != null){
			/* Modificato dopo che nella quinta scheda misteriosamente la prima riga viene letta */
			//partite = new Partita[schedaTurno.getLastRowNum()/2];
			partite = new Partita[(schedaTurno.getLastRowNum()+1)/2];
			Iterator it = schedaTurno.rowIterator();
			//it.next(); //Si salta la prima riga

			for (int i=0; it.hasNext(); i++){
				try{
					HSSFRow row = (HSSFRow) it.next();
					if (i%2 == 0){
						partite[i/2] = new Partita();
						partite[i/2].setNumeroTavolo(i/2+1);
					}else{
						try{
							switch (tipoTorneo) {
							case Dominion:
								setPartitaDominionFromRow(partite[(i-1)/2], row, giocatori);
								break;
							case StoneAge:
								setPartitaStoneAgeFromRow(partite[(i-1)/2], row, giocatori);
								break;
							default:
								setPartitaFromRow(partite[(i-1)/2], row, giocatori, withGhost);
								break;
							}
						}catch(MyException me){
							throw new MyException(me,me.getMessage()+" Scheda "+nomeTurno+" riga "+(i+1));
						}
					}
				}catch(MyException me){
					throw me;
				}catch(Exception e){
					throw new MyException(e,"Scheda "+nomeTurno+" riga "+(i+1)+ " "+e.getClass());
				}
			}

		//}else{
			//throw new MyException("Non è stata trovata la scheda con il "+nomeTurno);
		}
		return partite;
	}
	
	public Partita[] loadPartite(int numeroTurno, boolean withGhost, TipoTorneo tipoTorneo){
		return loadPartite(getNomeTurno(numeroTurno), withGhost, tipoTorneo);
	}
	
	public static String getNomeTurno(int numeroTurno){
		return numeroTurno+"° Turno";
	}
	private void setPartitaFromRow(Partita partita, HSSFRow row, List giocatori, boolean withGhost){
		int numeroGiocatori = 0;
		int numeroTripletteCelle = 5; //row.getLastCellNum()/3;
		for (int j=0; j<numeroTripletteCelle;j++){
			HSSFCell cellId = row.getCell((short)(j*3));
			if (cellId != null){
				Short id = null;
				try{
					id 	= (short)cellId.getNumericCellValue();
				}catch(Exception nfe){
					throw new MyException(nfe,"Colonna ID ("+((j*3)+1)+"°) con valore non numerico");
				}
				if (id != null && id.intValue() != 0){
					HSSFCell cellPunteggio = row.getCell((short)((j*3)+2));
					Float punteggio = null;
					try{
						punteggio = (float) cellPunteggio.getNumericCellValue();
					}catch(Exception nfe){
						throw new MyException(nfe,"Colonna Punteggio ("+((j*3)+2)+"°) con valore non numerico");
					}
					GiocatoreDTO giocatore = new GiocatoreDTO();
					giocatore.setId(id.intValue());
					int position = Collections.binarySearch(giocatori,giocatore);
					if (position >=0 || (giocatore.equals(GiocatoreDTO.FITTIZIO) && withGhost)){
						if (giocatore.equals(GiocatoreDTO.FITTIZIO)){
							giocatore = GiocatoreDTO.FITTIZIO;
						}else{
							giocatore = (GiocatoreDTO)giocatori.get(position);
						}
						partita.addGiocatore(giocatore, punteggio!=null?punteggio:0);
						numeroGiocatori++;
						HSSFCell cellName = row.getCell((short)((j*3)+1));
						String name = cellName.getRichStringCellValue().toString();
						if (name.contains("*")){
							partita.setVincitore(giocatore);
						}
					}else if (!giocatore.equals(GiocatoreDTO.FITTIZIO)){
						throw new MyException("Nella lista dei giocatori non è presente quello con indice "+id);
					}
				}
			}
		}
		partita.setNumeroGiocatori(numeroGiocatori);
	}
	
	private void setPartitaDominionFromRow(Partita partita, HSSFRow row, List giocatori){
		int numeroInformazioniGiocatore = 6;
		int numeroGiocatori = 0;
		int numeroTripletteCelle = 5; //row.getLastCellNum()/3;
		for (int j=0; j<numeroTripletteCelle;j++){
			HSSFCell cellId = row.getCell((short)(j*numeroInformazioniGiocatore));
			if (cellId != null){
				Short id = null;
				try{
					id 	= (short)cellId.getNumericCellValue();
				}catch(NumberFormatException nfe){
					throw new MyException(nfe,"Colonna ID ("+((j*numeroInformazioniGiocatore)+1)+"°) con valore non numerico");
				}
				if (id != null && id.intValue() != 0){
					HSSFCell cellPunteggio = row.getCell((short)((j*numeroInformazioniGiocatore)+2));
					Float punteggio = null;
					try{
						punteggio = (float) cellPunteggio.getNumericCellValue();
					}catch(NumberFormatException nfe){
						throw new MyException(nfe,"Colonna Punteggio ("+((j*numeroInformazioniGiocatore)+2)+"°) con valore non numerico");
					}
					HSSFCell cellOri = row.getCell((short)((j*numeroInformazioniGiocatore)+3));
					Float numeroOri = null;
					try{
						numeroOri = (float) cellOri.getNumericCellValue();
					}catch(NumberFormatException nfe){
						throw new MyException(nfe,"Colonna Ori ("+((j*numeroInformazioniGiocatore)+3)+"°) con valore non numerico");
					}
					HSSFCell cellMonete = row.getCell((short)((j*numeroInformazioniGiocatore)+4));
					Float numeroMonete = null;
					try{
						numeroMonete = (float) cellMonete.getNumericCellValue();
					}catch(NumberFormatException nfe){
						throw new MyException(nfe,"Colonna Monete ("+((j*numeroInformazioniGiocatore)+4)+"°) con valore non numerico");
					}
					HSSFCell cellAzioni = row.getCell((short)((j*numeroInformazioniGiocatore)+5));
					Float numeroAzioni = null;
					try{
						numeroAzioni = (float) cellAzioni.getNumericCellValue();
					}catch(NumberFormatException nfe){
						throw new MyException(nfe,"Colonna Azioni ("+((j*numeroInformazioniGiocatore)+5)+"°) con valore non numerico");
					}
					GiocatoreDTO giocatore = new GiocatoreDTO();
					giocatore.setId(id.intValue());
					int position = Collections.binarySearch(giocatori,giocatore);
					if (position >=0){
						giocatore = (GiocatoreDTO)giocatori.get(position);
						partita.addGiocatore(giocatore, punteggio!=null?punteggio:0);
						partita.addDatiAggiuntiviGiocatore(giocatore, new Integer[]{numeroOri==null?0:numeroOri.intValue(),numeroMonete==null?0:numeroMonete.intValue(),numeroAzioni==null?0:numeroAzioni.intValue()});
						numeroGiocatori++;
						HSSFCell cellName = row.getCell((short)((j*numeroInformazioniGiocatore)+1));
						String name = cellName.getRichStringCellValue().toString();
						if (name.contains("*")){
							partita.setVincitore(giocatore);
						}
					}else {
						throw new MyException("Nella lista dei giocatori non è presente quello con indice "+id);
					}
				}
			}
		}
		partita.setNumeroGiocatori(numeroGiocatori);
	}
	
	private void setPartitaStoneAgeFromRow(Partita partita, HSSFRow row, List giocatori){
		int numeroInformazioniGiocatore = 7;
		int numeroGiocatori = 0;
		int numeroTripletteCelle = 5; //row.getLastCellNum()/3;
		for (int j=0; j<numeroTripletteCelle;j++){
			HSSFCell cellId = row.getCell((short)(j*numeroInformazioniGiocatore));
			if (cellId != null){
				Short id = null;
				try{
					id 	= (short)cellId.getNumericCellValue();
				}catch(NumberFormatException nfe){
					throw new MyException(nfe,"Colonna ID ("+((j*numeroInformazioniGiocatore)+1)+"°) con valore non numerico");
				}
				if (id != null && id.intValue() != 0){
					HSSFCell cellPunteggio = row.getCell((short)((j*numeroInformazioniGiocatore)+2));
					Float punteggio = null;
					try{
						punteggio = (float) cellPunteggio.getNumericCellValue();
					}catch(NumberFormatException nfe){
						throw new MyException(nfe,"Colonna Punteggio ("+((j*numeroInformazioniGiocatore)+2)+"°) con valore non numerico");
					}
					HSSFCell cellOfferto = row.getCell((short)((j*numeroInformazioniGiocatore)+3));
					Float numeroOfferti = null;
					try{
						numeroOfferti = (float) cellOfferto.getNumericCellValue();
					}catch(NumberFormatException nfe){
						throw new MyException(nfe,"Colonna Punteggio Offerto ("+((j*numeroInformazioniGiocatore)+3)+"°) con valore non numerico");
					}
					HSSFCell cellGrani = row.getCell((short)((j*numeroInformazioniGiocatore)+4));
					Float numeroGrani = null;
					try{
						numeroGrani = (float) cellGrani.getNumericCellValue();
					}catch(NumberFormatException nfe){
						throw new MyException(nfe,"Colonna Grani ("+((j*numeroInformazioniGiocatore)+4)+"°) con valore non numerico");
					}
					HSSFCell cellUtensili = row.getCell((short)((j*numeroInformazioniGiocatore)+5));
					Float numeroUtensili = null;
					try{
						numeroUtensili = (float) cellUtensili.getNumericCellValue();
					}catch(NumberFormatException nfe){
						throw new MyException(nfe,"Colonna Utensili ("+((j*numeroInformazioniGiocatore)+5)+"°) con valore non numerico");
					}
					HSSFCell cellOmini = row.getCell((short)((j*numeroInformazioniGiocatore)+6));
					Float numeroOmini = null;
					try{
						numeroOmini = (float) cellOmini.getNumericCellValue();
					}catch(NumberFormatException nfe){
						throw new MyException(nfe,"Colonna Omini ("+((j*numeroInformazioniGiocatore)+6)+"°) con valore non numerico");
					}
					GiocatoreDTO giocatore = new GiocatoreDTO();
					giocatore.setId(id.intValue());
					int position = Collections.binarySearch(giocatori,giocatore);
					if (position >=0){
						giocatore = (GiocatoreDTO)giocatori.get(position);
						partita.addGiocatore(giocatore, punteggio!=null?punteggio:0);
						partita.addDatiAggiuntiviGiocatore(giocatore, new Integer[]{numeroOfferti==null?0:numeroOfferti.intValue(),numeroGrani==null?0:numeroGrani.intValue(),numeroUtensili==null?0:numeroUtensili.intValue(),numeroOmini==null?0:numeroOmini.intValue()});
						numeroGiocatori++;
						HSSFCell cellName = row.getCell((short)((j*numeroInformazioniGiocatore)+1));
						String name = cellName.getRichStringCellValue().toString();
						if (name.contains("*")){
							partita.setVincitore(giocatore);
						}
					}else {
						throw new MyException("Nella lista dei giocatori non è presente quello con indice "+id);
					}
				}
			}
		}
		partita.setNumeroGiocatori(numeroGiocatori);
	}
	
	public void scriviLog(String log){
		HSSFSheet logSheet = foglioTorneo.getSheet(SCHEDA_LOG);
		if (logSheet == null){
			logSheet = foglioTorneo.createSheet(SCHEDA_LOG);
			logSheet.setColumnWidth((short)0,Short.MAX_VALUE);
		}
		HSSFRow rowLog = logSheet.createRow(logSheet.getLastRowNum()+1);
		HSSFCell cellLog = rowLog.createCell((short)0);
		cellLog.setCellStyle(styleCell);
		cellLog.setCellValue(new HSSFRichTextString(Calendar.getInstance().getTime()+" "+log));
	}
	

	public void scriviLog(List<String> log){
		HSSFSheet logSheet = foglioTorneo.getSheet(SCHEDA_LOG);
		if (logSheet == null){
			logSheet = foglioTorneo.createSheet(SCHEDA_LOG);
			logSheet.setColumnWidth((short)0,Short.MAX_VALUE);
		}
		if (log != null){
			HSSFRow rowLog = logSheet.createRow(logSheet.getLastRowNum()+1);
			HSSFCell cellLog = rowLog.createCell((short)0);
			cellLog.setCellStyle(styleCell);
			cellLog.setCellValue(new HSSFRichTextString(Calendar.getInstance().getTime()+""));
			for (String rigaLog: log){
				rowLog = logSheet.createRow(logSheet.getLastRowNum()+1);
				cellLog = rowLog.createCell((short)0);
				cellLog.setCellStyle(styleCell);
				cellLog.setCellValue(new HSSFRichTextString(rigaLog));
			}
		}
	}
	
	public void scriviClassifica_new(TipoTorneo tipoTorneo){
		scriviClassifica(tipoTorneo);
		String sheetClassificaName = "Classifica_vera";
		int index = foglioTorneo.getSheetIndex(sheetClassificaName);
		if (index >=0){foglioTorneo.removeSheetAt(index);}
		HSSFSheet schedaClassifica = foglioTorneo.cloneSheet(foglioTorneo.getSheetIndex(SCHEDA_CLASSIFICA));
		System.out.println("schedaClassifica clonata: "+schedaClassifica.getSheetName());
		int sheetIndex = foglioTorneo.getSheetIndex(schedaClassifica);
		foglioTorneo.setSheetName(sheetIndex, sheetClassificaName);
	}
	
	
	
	public void scriviClassifica(TipoTorneo tipoTorneo){
		int index = foglioTorneo.getSheetIndex(SCHEDA_CLASSIFICA);
		if (index >=0){
			foglioTorneo.removeSheetAt(index);
		}
		
		HSSFCellStyle styleCellClassODD  = foglioTorneo.createCellStyle();
		HSSFCellStyle styleCellClassEVEN = foglioTorneo.createCellStyle();
		//styleCellClassEVEN.setDataFormat((short)2);
		//styleCellClassODD.setDataFormat((short)2);
		styleCellClassODD.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleCellClassEVEN.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleCellClassODD.setFillForegroundColor(HSSFColor.YELLOW.index);
		styleCellClassEVEN.setFillForegroundColor(HSSFColor.LIGHT_ORANGE.index);
		HSSFFont font = foglioTorneo.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		styleCellClassEVEN.setFont(font);
		styleCellClassODD.setFont(font);

		HSSFCellStyle styleCellClassWinODD  = foglioTorneo.createCellStyle();
		HSSFCellStyle styleCellClassWinEVEN = foglioTorneo.createCellStyle();
		styleCellClassWinODD.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleCellClassWinEVEN.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleCellClassWinODD.setFillForegroundColor(HSSFColor.YELLOW.index);
		styleCellClassWinEVEN.setFillForegroundColor(HSSFColor.LIGHT_ORANGE.index);
		styleCellClassWinEVEN.setFont(font);
		styleCellClassWinODD.setFont(font);
		styleCellClassWinEVEN.setBorderTop((short)5);
		styleCellClassWinEVEN.setBorderBottom((short)5);
		styleCellClassWinEVEN.setBorderLeft((short)5);
		styleCellClassWinEVEN.setBorderRight((short)5);
		styleCellClassWinEVEN.setLeftBorderColor(HSSFColor.DARK_GREEN.index);
		styleCellClassWinEVEN.setRightBorderColor(HSSFColor.DARK_GREEN.index);
		styleCellClassWinEVEN.setTopBorderColor(HSSFColor.DARK_GREEN.index);
		styleCellClassWinEVEN.setBottomBorderColor(HSSFColor.DARK_GREEN.index);
		styleCellClassWinODD.setBorderTop((short)5);
		styleCellClassWinODD.setBorderBottom((short)5);
		styleCellClassWinODD.setBorderLeft((short)5);
		styleCellClassWinODD.setBorderRight((short)5);
		styleCellClassWinODD.setLeftBorderColor(HSSFColor.DARK_GREEN.index);
		styleCellClassWinODD.setRightBorderColor(HSSFColor.DARK_GREEN.index);
		styleCellClassWinODD.setTopBorderColor(HSSFColor.DARK_GREEN.index);
		styleCellClassWinODD.setBottomBorderColor(HSSFColor.DARK_GREEN.index);
		
		HSSFCellStyle styleCellClass, styleCellClassWin;
		
		HSSFSheet schedaClassifica = foglioTorneo.createSheet(SCHEDA_CLASSIFICA);
		HSSFRow intestazione = schedaClassifica.createRow(0);
		short indexCell = 0;
		HSSFCellUtil.createCell(intestazione,  indexCell++, "Pos.",styleIntestazione);
		HSSFCellUtil.createCell(intestazione,  indexCell++, "Nome",styleIntestazione);
		HSSFCellUtil.createCell(intestazione,  indexCell++, "Cognome",styleIntestazione);
		HSSFCellUtil.createCell(intestazione,  indexCell++, "Nick",styleIntestazione);
//		HSSFCellUtil.createCell(intestazione,  4, "Regione",styleIntestazione);
		HSSFCellUtil.createCell(intestazione,  indexCell++, "Club o Famiglia",styleIntestazione);
		if (tipoTorneo != TipoTorneo.MasterRisiko2015){
			for (int i = 1; ; i++){
				Partita[] partiteTurnoi = loadPartite(i,false,tipoTorneo);
				if (partiteTurnoi == null){break;}
				HSSFCellUtil.createCell(intestazione,  indexCell++, "pt"+i,styleIntestazione);
			}
		}else{
			for (int i = 1; i <= 3; i++){
				Partita[] partiteTurnoi = loadPartite(i,false,tipoTorneo);
				if (partiteTurnoi == null){break;}
				HSSFCellUtil.createCell(intestazione,  indexCell++, "pt"+i,styleIntestazione);
			}
		}
		HSSFCellUtil.createCell(intestazione,  indexCell++, "v_tot",styleIntestazione);
		if (tipoTorneo == TipoTorneo.Dominion){
			HSSFCellUtil.createCell(intestazione,  indexCell++, "2_tot",styleIntestazione);
			HSSFCellUtil.createCell(intestazione,  indexCell++, "3_tot",styleIntestazione);
		}
		HSSFCellUtil.createCell(intestazione,  indexCell++, "pt_tot",styleIntestazione);
		if (tipoTorneo == TipoTorneo.ColoniDiCatan){
			HSSFCellUtil.createCell(intestazione,  indexCell++, "%_vitt",styleIntestazione);			
		}
		if (tipoTorneo == TipoTorneo.Dominion){
			HSSFCellUtil.createCell(intestazione,  indexCell++, "ori_tot",styleIntestazione);
			HSSFCellUtil.createCell(intestazione,  indexCell++, "monete_tot",styleIntestazione);
			HSSFCellUtil.createCell(intestazione,  indexCell++, "azioni_tot",styleIntestazione);
		}
		
		List<ScorePlayer>scores = null;
		switch (tipoTorneo) {
		case TorneoGufo:
			scores = getClassificaTorneoGufo();
			break;
		case CampionatoGufo:
			scores = getClassificaCampionatoGufo();
			break;
		case RadunoNazionale:
			scores = getClassificaRaduno(false);
			break;
		case MasterRisiko2015:
			scores = getClassificaQualificazioniNazionale(false, true);
			break;
		case Open:
			scores = getClassifica();
			break;
		case ColoniDiCatan:
			scores = getClassificaTorneoColoni();
			break;
		case Dominion:
			scores = getClassificaTorneoDominion();
			break;
		case StoneAge:
			scores = getClassificaTorneoStoneAge();
			break;
		default:
			throw new MyException("Tipo Torneo non previsto: "+tipoTorneo);
		}
		
		int position = 1;
		for (ScorePlayer scorePlayer: scores){
			indexCell = 0;
			GiocatoreDTO giocatore = scorePlayer.getGiocatore();
			int numeroRiga = schedaClassifica.getLastRowNum()+1;
			if (numeroRiga%2==0){
				styleCellClass = styleCellClassEVEN;
				styleCellClassWin = styleCellClassWinEVEN;
			}else{
				styleCellClass = styleCellClassODD;
				styleCellClassWin = styleCellClassWinODD;
			}
			HSSFRow rowScore = schedaClassifica.createRow(numeroRiga);
			HSSFCellUtil.createCell(rowScore,  indexCell++, String.valueOf(position++), styleCellClass);
			HSSFCellUtil.createCell(rowScore,  indexCell++, giocatore.getNome(), styleCellClass);
			HSSFCellUtil.createCell(rowScore,  indexCell++, giocatore.getCognome(), styleCellClass);
			HSSFCellUtil.createCell(rowScore,  indexCell++, giocatore.getNick(), styleCellClass);
//			HSSFCellUtil.createCell(rowScore,  indexCell++, giocatore.getRegioneProvenienza().getDescrizione(), styleCellClass);
			HSSFCellUtil.createCell(rowScore,  indexCell++, giocatore.getClubProvenienza()!=null?giocatore.getClubProvenienza().getDenominazione():"", styleCellClass);
			for (Partita partita: scorePlayer.getPartite()){
//				HSSFCell vittoria 	= rowScore.createCell((short)indexCell++, HSSFCell.CELL_TYPE_NUMERIC);
//				vittoria.setCellStyle(styleCellClass);
				HSSFCell punti	 	= rowScore.createCell((short)indexCell++, HSSFCell.CELL_TYPE_NUMERIC);
				if (tipoTorneo == TipoTorneo.MasterRisiko2015 && indiceFormatTreDecimali != -1){
					HSSFCellStyle cs = foglioTorneo.createCellStyle();
					cs.cloneStyleFrom(styleCellClass);
					cs.setDataFormat(indiceFormatTreDecimali);
					punti.setCellStyle(cs);
				}else{
					punti.setCellStyle(styleCellClass);
				}
				if(partita != null){
					if(partita.isVincitore(giocatore) && tipoTorneo != TipoTorneo.StoneAge){
						//if (tipoTorneo != TipoTorneo.ColoniDiCatan || partita.getPunteggio(giocatore) >= ScorePlayerTorneoColoni.PUNTEGGIO_VITTORIA){
//						vittoria.setCellValue(1);
						if (tipoTorneo == TipoTorneo.MasterRisiko2015 && indiceFormatTreDecimali != -1){
							HSSFCellStyle cs = foglioTorneo.createCellStyle();
							cs.cloneStyleFrom(styleCellClassWin);
							cs.setDataFormat(indiceFormatTreDecimali);
							punti.setCellStyle(cs);
						}else{
							punti.setCellStyle(styleCellClassWin);
						}
						//punti.setCellStyle(styleCellClassWin);
						//}
					}
					double puntid = partita.getPunteggioTrascodificatoB(giocatore).doubleValue();
					punti.setCellValue(puntid);
					//punti.setCellValue(partita.getPunteggioTrascodificato(giocatore));
				}				
			}
			HSSFCell totVittorieCell 	= rowScore.createCell((short)indexCell++, HSSFCell.CELL_TYPE_NUMERIC);
			totVittorieCell.setCellStyle(styleCellClass);
			if (tipoTorneo != TipoTorneo.ColoniDiCatan){
				totVittorieCell.setCellValue(scorePlayer.getNumeroVittorie());
			}else{
				totVittorieCell.setCellValue(scorePlayer.getNumeroVittorie() / 10.0f);
			}
			
			if (tipoTorneo == TipoTorneo.Dominion){
				ScorePlayerTorneoDominion scp = (ScorePlayerTorneoDominion) scorePlayer;
				
				HSSFCell totSecondiCell = rowScore.createCell((short)indexCell++, HSSFCell.CELL_TYPE_NUMERIC);
				totSecondiCell.setCellStyle(styleCellClass);
				totSecondiCell.setCellValue(scp.getNumeroSecondiPosti());
				
				HSSFCell totTerziCell = rowScore.createCell((short)indexCell++, HSSFCell.CELL_TYPE_NUMERIC);
				totTerziCell.setCellStyle(styleCellClass);
				totTerziCell.setCellValue(scp.getNumeroTerziPosti());
			}
			
			HSSFCell punteggioCell 	= rowScore.createCell((short)indexCell++, HSSFCell.CELL_TYPE_NUMERIC);

			if (tipoTorneo == TipoTorneo.MasterRisiko2015 && indiceFormatTreDecimali != -1){
				HSSFCellStyle cs = foglioTorneo.createCellStyle();
				cs.cloneStyleFrom(styleCellClass);
				cs.setDataFormat(indiceFormatTreDecimali);
				punteggioCell.setCellStyle(cs);
			}else{
				punteggioCell.setCellStyle(styleCellClass);
			}
			//punteggioCell.setCellStyle(styleCellClass);
			punteggioCell.setCellValue(scorePlayer.getPunteggioB(false).doubleValue());
			//punteggioCell.setCellValue(scorePlayer.getPunteggio(false));
			if (tipoTorneo == TipoTorneo.ColoniDiCatan){
				HSSFCell percCell 	= rowScore.createCell((short)indexCell++, HSSFCell.CELL_TYPE_NUMERIC);
				percCell.setCellStyle(styleCellClass);
				percCell.setCellValue(((ScorePlayerTorneoColoni) scorePlayer).getPercentualePuntiVittoria());		
			}
			
			if (tipoTorneo == TipoTorneo.Dominion){
				ScorePlayerTorneoDominion scp = (ScorePlayerTorneoDominion) scorePlayer;
				
				HSSFCell totOriCell = rowScore.createCell((short)indexCell++, HSSFCell.CELL_TYPE_NUMERIC);
				totOriCell.setCellStyle(styleCellClass);
				totOriCell.setCellValue(scp.getNumeroOri());
				
				HSSFCell totMoneteCell = rowScore.createCell((short)indexCell++, HSSFCell.CELL_TYPE_NUMERIC);
				totMoneteCell.setCellStyle(styleCellClass);
				totMoneteCell.setCellValue(scp.getNumeroMonete());
				
				HSSFCell totAzioniCell = rowScore.createCell((short)indexCell++, HSSFCell.CELL_TYPE_NUMERIC);
				totAzioniCell.setCellStyle(styleCellClass);
				totAzioniCell.setCellValue(scp.getNumeroAzioni());
			}
//			HSSFCell vittoria1 	= rowScore.createCell((short)indexCell++, HSSFCell.CELL_TYPE_NUMERIC);
//			vittoria1.setCellStyle(styleCellClass);
//			HSSFCell punti1 	= rowScore.createCell((short)indexCell++, HSSFCell.CELL_TYPE_NUMERIC);
//			punti1.setCellStyle(styleCellClass);
//			if(partitaPrimoTurno != null){
//				if(partitaPrimoTurno.isVincitore(giocatore)){
//					vittoria1.setCellValue(1);
//				}
//				punti1.setCellValue(partitaPrimoTurno.getPunteggio(giocatore));
//			}
//			HSSFCell vittoria2 	= rowScore.createCell((short)indexCell++, HSSFCell.CELL_TYPE_NUMERIC);
//			vittoria2.setCellStyle(styleCellClass);
//			HSSFCell punti2 	= rowScore.createCell((short)indexCell++, HSSFCell.CELL_TYPE_NUMERIC);
//			punti2.setCellStyle(styleCellClass);
//			if(partitaSecondoTurno != null){
//				if(partitaSecondoTurno.isVincitore(giocatore)){
//					vittoria2.setCellValue(1);
//				}
//				punti2.setCellValue(partitaSecondoTurno.getPunteggio(giocatore));
//			}
//			HSSFCell vittoria3 	= rowScore.createCell((short)indexCell++, HSSFCell.CELL_TYPE_NUMERIC);
//			vittoria3.setCellStyle(styleCellClass);
//			HSSFCell punti3 	= rowScore.createCell((short)indexCell++, HSSFCell.CELL_TYPE_NUMERIC);
//			punti3.setCellStyle(styleCellClass);
//			if(partitaTerzoTurno != null){
//				if(partitaTerzoTurno.isVincitore(giocatore)){
//					vittoria3.setCellValue(1);
//				}
//				punti3.setCellValue(partitaTerzoTurno.getPunteggio(giocatore));
//			}
//			HSSFCell totVittorie = rowScore.createCell((short)indexCell++,HSSFCell.CELL_TYPE_FORMULA);
//			totVittorie.setCellStyle(styleCellClass);
//			totVittorie.setCellFormula("$G"+"$"+(numeroRiga+1)+"+"+"$I"+"$"+(numeroRiga+1));
//			HSSFCell totPunti = rowScore.createCell((short)indexCell++,HSSFCell.CELL_TYPE_FORMULA);
//			totPunti.setCellStyle(styleCellClass);
//			totPunti.setCellFormula("$H"+"$"+(numeroRiga+1)+"+"+"$J"+"$"+(numeroRiga+1));
			
			HSSFCell id = rowScore.createCell((short)indexCell, HSSFCell.CELL_TYPE_NUMERIC);
			id.setCellValue(giocatore.getId());
			schedaClassifica.setColumnHidden((short)indexCell++,true);
//			HSSFCell presenzaCell = rowScore.createCell((short)indexCell++);
//			presenzaCell.setCellValue("SI");
		}
	}
	
	public List<ScorePlayer> getClassificaRaduno(boolean partecipanti){
		List<ScorePlayer> scores = new ArrayList<ScorePlayer>();
		List<GiocatoreDTO>giocatori = getListaGiocatori(partecipanti);
		List<Partita[]> listaPartiteTotali = new ArrayList<Partita[]>();
		for (int i = 1; ; i++){
			Partita[] partiteTurnoi = loadPartite(i,false,TipoTorneo.RadunoNazionale);
			if (partiteTurnoi == null){break;}
			listaPartiteTotali.add(partiteTurnoi);
		}
		Partita[] partiteGiocatore = null;
		for (GiocatoreDTO giocatore: giocatori){
			partiteGiocatore = new Partita[listaPartiteTotali.size()];
			int indexTurno = 0;
			for (Partita[] partite: listaPartiteTotali){
				partiteGiocatore[indexTurno++] = inspectPartite(partite, giocatore);
			}
			ScorePlayer scorePlayer = new ScorePlayerRaduno(giocatore,partiteGiocatore);
			scores.add(scorePlayer);
		}
		Collections.sort(scores, new ScoreRadunoComparator());
		return scores;
	}
	
	public List<ScorePlayer> getClassificaRadunoAlSecondoTurno(boolean partecipanti){
		List<ScorePlayer> scores = new ArrayList<ScorePlayer>();
		List<GiocatoreDTO>giocatori = getListaGiocatori(partecipanti);
		List<Partita[]> listaPartiteTotali = new ArrayList<Partita[]>();
		for (int i = 1; i <=2; i++){
			Partita[] partiteTurnoi = loadPartite(i,false,TipoTorneo.RadunoNazionale);
			listaPartiteTotali.add(partiteTurnoi);
		}
		Partita[] partiteGiocatore = null;
		for (GiocatoreDTO giocatore: giocatori){
			partiteGiocatore = new Partita[listaPartiteTotali.size()];
			int indexTurno = 0;
			for (Partita[] partite: listaPartiteTotali){
				partiteGiocatore[indexTurno++] = inspectPartite(partite, giocatore);
			}
			ScorePlayer scorePlayer = new ScorePlayerRaduno(giocatore,partiteGiocatore);
			scores.add(scorePlayer);
		}
		Collections.sort(scores, new ScoreRadunoComparator());
		return scores;
	}
	
	public List<ScorePlayer> ordinaSemifinalistiRaduno(List<GiocatoreDTO> semifinalisti){
		List<ScorePlayer> scores = new ArrayList<ScorePlayer>();
		List<Partita[]> listaPartiteTotali = new ArrayList<Partita[]>();
		for (int i = 1; ; i++){
			Partita[] partiteTurnoi = loadPartite(i,false,TipoTorneo.RadunoNazionale);
			if (partiteTurnoi == null){break;}
			listaPartiteTotali.add(partiteTurnoi);
		}
		Partita[] partiteGiocatore = null;
		for (GiocatoreDTO giocatore: semifinalisti){
			partiteGiocatore = new Partita[listaPartiteTotali.size()];
			int indexTurno = 0;
			for (Partita[] partite: listaPartiteTotali){
				partiteGiocatore[indexTurno++] = inspectPartite(partite, giocatore);
			}
			ScorePlayer scorePlayer = new ScorePlayerRaduno(giocatore,partiteGiocatore);
			scores.add(scorePlayer);
		}
		Collections.sort(scores, new ScoreSemifinalistiRadunoComparator());
		return scores;
	}
	
	private List<ScorePlayer> getClassifica(){
		List<ScorePlayer> scores = new ArrayList<ScorePlayer>();
		List<GiocatoreDTO>giocatori = getListaGiocatori(false);
		List<Partita[]> listaPartiteTotali = new ArrayList<Partita[]>();
		for (int i = 1; ; i++){
			Partita[] partiteTurnoi = loadPartite(i,false,TipoTorneo.Open);
			if (partiteTurnoi == null){break;}
			listaPartiteTotali.add(partiteTurnoi);
		}
		Partita[] partiteGiocatore = null;
		for (GiocatoreDTO giocatore: giocatori){
			partiteGiocatore = new Partita[listaPartiteTotali.size()];
			int indexTurno = 0;
			for (Partita[] partite: listaPartiteTotali){
				partiteGiocatore[indexTurno++] = inspectPartite(partite, giocatore);
			}
			ScorePlayer scorePlayer = new ScorePlayerOpen(giocatore,partiteGiocatore);
			scores.add(scorePlayer);
		}
		Collections.sort(scores, new ScoreTorneoOpenComparator());
		return scores;
	}
	
	private List<ScorePlayer> getClassificaTorneoGufo(){
		List<ScorePlayer> scores = new ArrayList<ScorePlayer>();
		List<GiocatoreDTO>giocatori = getListaGiocatori(false);
		List<Partita[]> listaPartiteTotali = new ArrayList<Partita[]>();
		for (int i = 1; ; i++){
			Partita[] partiteTurnoi = loadPartite(i,false,TipoTorneo.TorneoGufo);
			if (partiteTurnoi == null){break;}
			listaPartiteTotali.add(partiteTurnoi);
		}
		Partita[] partiteGiocatore = null;
		for (GiocatoreDTO giocatore: giocatori){
			partiteGiocatore = new Partita[listaPartiteTotali.size()];
			int indexTurno = 0;
			for (Partita[] partite: listaPartiteTotali){
				partiteGiocatore[indexTurno++] = inspectPartite(partite, giocatore);
			}
			ScorePlayer scorePlayer = new ScorePlayerTorneoGufo(giocatore,partiteGiocatore);
			scores.add(scorePlayer);
		}
		Collections.sort(scores, new ScoreTorneoOpenComparator());
		return scores;
	}
	
	public List<ScorePlayer> getClassificaTorneoColoni(){
		List<ScorePlayer> scores = new ArrayList<ScorePlayer>();
		List<GiocatoreDTO>giocatori = getListaGiocatori(false);
		List<Partita[]> listaPartiteTotali = new ArrayList<Partita[]>();
		for (int i = 1; ; i++){
			Partita[] partiteTurnoi = loadPartite(i,false,TipoTorneo.ColoniDiCatan);
			if (partiteTurnoi == null){break;}
			listaPartiteTotali.add(partiteTurnoi);
		}
		Partita[] partiteGiocatore = null;
		for (GiocatoreDTO giocatore: giocatori){
			partiteGiocatore = new Partita[listaPartiteTotali.size()];
			int indexTurno = 0;
			for (Partita[] partite: listaPartiteTotali){
				partiteGiocatore[indexTurno++] = inspectPartite(partite, giocatore);
			}
			if (partiteGiocatore != null){
				ScorePlayer scorePlayer = new ScorePlayerTorneoColoni(giocatore,partiteGiocatore);
				scores.add(scorePlayer);
			}
		}
		Collections.sort(scores, new ScoreTorneoColoniComparator());
		return scores;
	}
	
	public List<ScorePlayer> getClassificaTorneoDominion(){
		List<ScorePlayer> scores = new ArrayList<ScorePlayer>();
		List<GiocatoreDTO>giocatori = getListaGiocatori(false);
		List<Partita[]> listaPartiteTotali = new ArrayList<Partita[]>();
		for (int i = 1; ; i++){
			Partita[] partiteTurnoi = loadPartite(i,false,TipoTorneo.Dominion);
			if (partiteTurnoi == null){break;}
			listaPartiteTotali.add(partiteTurnoi);
		}
		Partita[] partiteGiocatore = null;
		for (GiocatoreDTO giocatore: giocatori){
			partiteGiocatore = new Partita[listaPartiteTotali.size()];
			int indexTurno = 0;
			for (Partita[] partite: listaPartiteTotali){
				partiteGiocatore[indexTurno++] = inspectPartite(partite, giocatore);
			}
			if (partiteGiocatore != null){
				ScorePlayer scorePlayer = new ScorePlayerTorneoDominion(giocatore,partiteGiocatore);
				scores.add(scorePlayer);
			}
		}
		Collections.sort(scores, new ScoreTorneoDominionComparator());
		return scores;
	}
	
	public List<ScorePlayer> getClassificaTorneoStoneAge(){
		List<ScorePlayer> scores = new ArrayList<ScorePlayer>();
		List<GiocatoreDTO>giocatori = getListaGiocatori(false);
		List<Partita[]> listaPartiteTotali = new ArrayList<Partita[]>();
		for (int i = 1; ; i++){
			Partita[] partiteTurnoi = loadPartite(i,false,TipoTorneo.StoneAge);
			if (partiteTurnoi == null){break;}
			listaPartiteTotali.add(partiteTurnoi);
		}
		Partita[] partiteGiocatore = null;
		for (GiocatoreDTO giocatore: giocatori){
			partiteGiocatore = new Partita[listaPartiteTotali.size()];
			int indexTurno = 0;
			for (Partita[] partite: listaPartiteTotali){
				partiteGiocatore[indexTurno++] = inspectPartite(partite, giocatore);
			}
			if (partiteGiocatore != null){
				ScorePlayer scorePlayer = new ScorePlayerTorneoStoneAge(giocatore,partiteGiocatore);
				scores.add(scorePlayer);
			}
		}
		Collections.sort(scores, new ScoreTorneoStoneAgeComparator());
		return scores;
	}
	
	private List<ScorePlayer> getClassificaCampionatoGufo(){
		List<ScorePlayer> scores = new ArrayList<ScorePlayer>();
		List<GiocatoreDTO>giocatori = getListaGiocatori(false);
		List<Partita[]> listaPartiteTotali = new ArrayList<Partita[]>();
		for (int i = 1; ; i++){
			Partita[] partiteTurnoi = loadPartite(i,true,TipoTorneo.CampionatoGufo);
			if (partiteTurnoi == null){break;}
			listaPartiteTotali.add(partiteTurnoi);
		}
		Partita[] partiteGiocatore = null;
		for (GiocatoreDTO giocatore: giocatori){
			partiteGiocatore = new Partita[listaPartiteTotali.size()];
			int indexTurno = 0;
			for (Partita[] partite: listaPartiteTotali){
				partiteGiocatore[indexTurno++] = inspectPartite(partite, giocatore);
			}
			ScorePlayer scorePlayer = new ScorePlayerCampionatoGufo(giocatore,partiteGiocatore);
			scores.add(scorePlayer);
		}
		Collections.sort(scores, new ScoreCampionatoComparator());
		return scores;
	}
	
	public List<ScorePlayer> getClassificaQualificazioniNazionale(boolean partecipanti, boolean compreseSemifinali){
		List<ScorePlayer> scores = new ArrayList<ScorePlayer>();
		List<GiocatoreDTO>giocatori = getListaGiocatori(partecipanti);
		List<Partita[]> listaPartiteTotali = new ArrayList<Partita[]>();
		int numeroTurniDisputati = 0;
		int numeroTurniDaConsiderare = 2;
		if (compreseSemifinali) numeroTurniDaConsiderare++;
		for (int i = 1; i <=numeroTurniDaConsiderare ; i++){
			Partita[] partiteTurnoi = loadPartite(i,true,TipoTorneo.MasterRisiko2015);
			if (partiteTurnoi == null){break;}
			numeroTurniDisputati++;
			listaPartiteTotali.add(partiteTurnoi);
		}
		Partita[] partiteGiocatore = null;
		for (GiocatoreDTO giocatore: giocatori){
			//partiteGiocatore = new Partita[listaPartiteTotali.size()];
			partiteGiocatore = new Partita[numeroTurniDisputati];
			int indexTurno = 0;
			short numeroPartiteDisputate = 0;
			for (Partita[] partite: listaPartiteTotali){
				partiteGiocatore[indexTurno++] = inspectPartite(partite, giocatore);
				if (partiteGiocatore[indexTurno-1] != null){numeroPartiteDisputate++;}
			}
			if (compreseSemifinali || numeroTurniDisputati == 1 || numeroPartiteDisputate >=2){
				ScorePlayer scorePlayer = new ScorePlayerQualificazioniNazionale(giocatore,partiteGiocatore);
				scores.add(scorePlayer);
			}
		}
		Collections.sort(scores, new ScoreQualificazioniNazionaleComparator());
		Partita[] finale = loadPartite(4, false, TipoTorneo.MasterRisiko2015);
		if (finale != null){
			int index = 0;
			for (GiocatoreDTO finalista: finale[0].getGiocatoriOrdinatiPerPunteggio()){
				Iterator<ScorePlayer> iterator = scores.iterator();
				while (iterator.hasNext()){
					ScorePlayer scorePlayer = iterator.next(); 
					if (scorePlayer.getGiocatore().equals(finalista)){
						iterator.remove();
						scores.add(index++, scorePlayer);
						break;
					}
				}
			}
		}
		return scores;
	}
	
	public List<GiocatoreDTO> getSemifinalisti(){
		List<GiocatoreDTO> semifinalisti = new ArrayList();
		List giocatori = getListaGiocatori(false);
		Collections.sort(giocatori);
		HSSFSheet schedaClassifica = foglioTorneo.getSheet(SCHEDA_CLASSIFICA);
		GiocatoreDTO giocatoreDiRicerca = new GiocatoreDTO();
		for (int i=1; i<=schedaClassifica.getLastRowNum() && semifinalisti.size() < 16; i++){
			HSSFRow row = schedaClassifica.getRow(i);
			HSSFCell presenzaCell = row.getCell((short) 13);
			if (presenzaCell.getStringCellValue().equalsIgnoreCase("SI")){
				HSSFCell idCell = row.getCell((short) 12);
				Short id = (short)idCell.getNumericCellValue();
				giocatoreDiRicerca.setId(id.intValue());
				int position = Collections.binarySearch(giocatori,giocatoreDiRicerca);
				if (position >=0){
					semifinalisti.add((GiocatoreDTO)giocatori.get(position));
				}else{
					throw new MyException("Nella lista dei giocatori non è presente quello con indice "+id);
				}
			}
		}
		return semifinalisti;
	}
	
	public static Partita inspectPartite(Partita[] partite, GiocatoreDTO giocatore){
		Partita result = null;
		if (partite != null && giocatore != null){
			for (Partita partita: partite){
				if (partita != null && partita.eAlTavolo(giocatore)){
					result = partita;
					break;
				}
			}
		}
		return result;
	}
	
	public void closeFileExcel(){
		if (pathFileExcel != null){
			try{
				FileOutputStream fileOut = new FileOutputStream(pathFileExcel);
				foglioTorneo.write(fileOut);
				fileOut.close();
			}catch(IOException ioe){
				throw new MyException("Impossibile scrivere sul file Excel "+pathFileExcel+":\n "+ioe.getMessage());
			}
		}
	}
}
