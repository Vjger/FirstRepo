package it.desimone.risiko.torneo.utils;

import it.desimone.risiko.torneo.dto.GiocatoreDTO;
import it.desimone.risiko.torneo.dto.Partita;
import it.desimone.utils.MyLogger;
import it.desimone.utils.ResourceLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfUtils {
	
	//private InputStream immagineRisikoStream = this.getClass().getResourceAsStream("/image004_rid.png");
	//private URL immagineRisikoURL =  this.getClass().getResource("/image004_rid.png");
	//private static byte[] immagineRisikoByte;
	private Document document;
	private PdfWriter writer;
	
	private Image immagineTestata;
	
	private static final Phrase EMPTY_PHRASE = new Phrase("");
	
	public PdfUtils(){
		
		ResourceLoader rl = new ResourceLoader();
        //URL immagineRisikoURL = rl.getImmagineRisiko();
		//String immagineRisikoURL = rl.getImmagineRisiko();
		byte[] immagineRisikoURL = rl.getImmagineRisiko();
        try {
        	if (immagineRisikoURL != null){
            	MyLogger.getLogger().finer("Ottengo l'istanza dell'immagine della testata");
        		immagineTestata = Image.getInstance(immagineRisikoURL);
            	MyLogger.getLogger().finer("Scalo l'immagine della testata");
	        	immagineTestata.scalePercent(20.0f);
        	}
		} catch (Exception e) {
			MyLogger.getLogger().severe("Eccezione nella creazione dell'immagine per il pdf");
		} 
//		List lista = new ArrayList();
//		byte [] buff = new byte [1024];
//		int n;
//		try {
//			while( (n = immagineRisikoStream.read(buff, 0, buff.length))!= -1){
//				for (byte b: buff){
//					lista.add(b);
//				}
//			}
//			immagineRisikoByte = new byte[lista.size()];
//			Iterator iterator = lista.iterator();
//			int index = 0;
//			while(iterator.hasNext()){
//				immagineRisikoByte[index++] = (Byte)iterator.next();
//			}
//		} catch (IOException e) {
//			MyLogger.getLogger().severe("Eccezione: "+e.getMessage());
//		}
	}
	
	public void openDocument(String fileName){
		try{
			document = new Document(PageSize.A4, 50, 50, 50, 50);
			writer = PdfWriter.getInstance(document, new FileOutputStream(new File(fileName)));
	        document.open();
		} catch (IOException e) {
			MyLogger.getLogger().severe("Eccezione sul file "+fileName+" :"+e.getMessage());
		} catch (DocumentException e) {
			MyLogger.getLogger().severe("Eccezione sul file "+fileName+" :"+e.getMessage());
		}
	}
	
	public void closeDocument(){
		document.close();
	}
	
	public void stampaPartiteRisiko(Partita[] partite, String nomeTurno){
		try{	
            for (Partita partita: partite){
            	stampaRefertoRisiko(document, partita, nomeTurno);
            	document.newPage();
            }
		}catch (IOException e) {
			MyLogger.getLogger().severe("IOException"+ " :"+e.getMessage());
		}catch (DocumentException e) {
			MyLogger.getLogger().severe("DocumentException"+ " :"+e.getMessage());
		}	
	}
	
	private void stampaRefertoRisiko(Document document, Partita partita, String numeroTurno) throws DocumentException, IOException{
		
		MyLogger.getLogger().entering("PdfUtils", "stampaRefertoRisiko(document, "+partita+" "+numeroTurno+")");
		
        int numeroColonne = 5;
        PdfPTable table = new PdfPTable(new float[]{0.4f,0.12f,0.12f,0.12f,0.24f});//(numeroColonne);
               
        PdfPCell riga1 = new PdfPCell (new Paragraph ("Turno "+numeroTurno+" - Stampa Tavolo N°"+partita.getNumeroTavolo()));
        riga1.setColspan (numeroColonne);
        riga1.setHorizontalAlignment (Element.ALIGN_CENTER);
        riga1.setBackgroundColor (new BaseColor(128, 200, 128));
        riga1.setPadding (10.0f);
        table.addCell(riga1);
         
        PdfPCell riga2;
        if (immagineTestata != null){
        	MyLogger.getLogger().finer("Setto l'immagine della testata");
            riga2 = new PdfPCell(immagineTestata, true);        	
        }else{
        	riga2 = new PdfPCell(new Paragraph (""));
        }
        riga2.setColspan(numeroColonne);
        riga2.setHorizontalAlignment (Element.ALIGN_CENTER);
        riga2.setBackgroundColor (new BaseColor(128, 200, 128));
        riga2.setPadding (10.0f);
        table.addCell(riga2);
        
        table.addCell("Nominativo");
        table.addCell("Punti");
        table.addCell("Pti fuori obb.");
        table.addCell("Punti Torneo");
        table.addCell("Firma");
               
    	MyLogger.getLogger().finest("Ciclo partite");
		for (GiocatoreDTO giocatore: partita.getGiocatori()){
			String nominativo = giocatore.getNome()+" "+giocatore.getCognome();
			if (giocatore.getClubProvenienza() != null){
				nominativo += " - "+giocatore.getClubProvenienza().getDenominazione();
			}
			Phrase nome_club = new Phrase(nominativo, new Font(Font.FontFamily.HELVETICA, 10)); 
	        //table.addCell(nominativo);
			PdfPCell cellNome = new PdfPCell(nome_club);
			cellNome.setFixedHeight(25);
			table.addCell(cellNome);
			//table.addCell(nome_club);
			Float punteggio = partita.getPunteggio(giocatore);
			if (punteggio != null  && punteggio > 0f){ 
				BigDecimal punteggioB = new BigDecimal(punteggio);
				punteggioB = punteggioB.setScale(0,BigDecimal.ROUND_DOWN);
				PdfPCell cellPunteggio = new PdfPCell(new Phrase(punteggioB.toString()));
				cellPunteggio.setHorizontalAlignment(Element.ALIGN_RIGHT);
				table.addCell(cellPunteggio);
			}else{
				table.addCell(EMPTY_PHRASE);
			}
	        table.addCell(EMPTY_PHRASE);
		    table.addCell(EMPTY_PHRASE);
	        table.addCell(EMPTY_PHRASE);
		}
        
        PdfPCell rigaN1 = new PdfPCell (new Paragraph("\n\n\nNote: ______________________________________________________"));
        rigaN1.setColspan (numeroColonne);
        rigaN1.setHorizontalAlignment (Element.ALIGN_LEFT);
        rigaN1.setPadding (1.0f);
        rigaN1.setBorder(0);
        table.addCell(rigaN1);
        
        PdfPCell rigaN2 = new PdfPCell (new Paragraph("\n___________________________________________________________"));
        rigaN2.setColspan (numeroColonne);
        rigaN2.setHorizontalAlignment (Element.ALIGN_LEFT);
        rigaN2.setPadding (1.0f);
        rigaN2.setBorder(0);
        table.addCell(rigaN2);	
        
        PdfPCell rigaN3 = new PdfPCell (new Paragraph("\n___________________________________________________________"));
        rigaN3.setColspan (numeroColonne);
        rigaN3.setHorizontalAlignment (Element.ALIGN_LEFT);
        rigaN3.setPadding (1.0f);
        rigaN3.setBorder(0);
        table.addCell(rigaN3);
        
    	MyLogger.getLogger().finest("Aggiungo la tabella al documento");
        document.add(table);
        
		MyLogger.getLogger().exiting("PdfUtils", "stampaRefertoRisiko");
	}

	public static void main (String[] args){
		Partita partita = new Partita();
		partita.setNumeroGiocatori(5);
		partita.setNumeroTavolo(1);
		GiocatoreDTO giocatore1 = new GiocatoreDTO();
		giocatore1.setNome("pippo");
		giocatore1.setCognome("Baudoooooooooooooooooo");
		giocatore1.setClubProvenienza(ClubLoader.IL_GUFO);
		giocatore1.setId(1);
		partita.addGiocatore(giocatore1, 0f);
		GiocatoreDTO giocatore2 = new GiocatoreDTO();
		giocatore2.setNome("pluto");
		giocatore2.setCognome("Canisssssssssssssssssss");
		giocatore2.setClubProvenienza(ClubLoader.GRIFONE);
		giocatore2.setId(2);
		partita.addGiocatore(giocatore2, 0f);
		GiocatoreDTO giocatore3 = new GiocatoreDTO();
		giocatore3.setNome("paperino");
		giocatore3.setCognome("paolinoooooooooooooo");
		giocatore3.setClubProvenienza(ClubLoader.IL_GUISCARDO);
		giocatore3.setId(3);
		partita.addGiocatore(giocatore3, 0f);
		GiocatoreDTO giocatore4 = new GiocatoreDTO();
		giocatore4.setNome("rockerduck");
		giocatore4.setCognome("anatra");
		giocatore4.setClubProvenienza(ClubLoader.ASINELLI);
		giocatore4.setId(4);
		partita.addGiocatore(giocatore4, 0f);
		GiocatoreDTO giocatore5 = new GiocatoreDTO();
		giocatore5.setNome("commissario");
		giocatore5.setCognome("basettoni");
		giocatore5.setClubProvenienza(ClubLoader.GIMAGIOKE);
		giocatore5.setId(5);
		partita.addGiocatore(giocatore5, 0f);
		
		Document document = new Document(PageSize.A4, 50, 50, 25, 25);
		document.addAuthor("Gestione Raduno"); 
		document.addSubject("Questo è il Subject");
		document.addCreationDate();
		document.addCreator("Questo è il Creator");
		document.addHeader("name", "content");
		document.addTitle("Questo è il Title");
		
		PdfWriter writer = null;
		String fileName = "C:\\Documents and Settings\\Vjger\\Desktop\\test.pdf";
		PdfUtils pdfUtils = new PdfUtils();
		pdfUtils.openDocument(fileName);
		try{
			pdfUtils.stampaPartiteRisiko(new Partita[]{partita}, "1");
            pdfUtils.closeDocument();
		} catch (Exception e) {
			MyLogger.getLogger().severe("Eccezione sul file "+fileName+" :"+e.getMessage());
		} 
		System.out.println("End");
	}
}
