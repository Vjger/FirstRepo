package it.desimone.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import it.desimone.replayrd3.ColoreGiocatore;
import it.desimone.replayrd3.Partita;

public class ResourceLoader {
	
	
	public static final String ROOT;

	
	static{
		ROOT = new File("").getAbsolutePath()+File.separator+"resources";
		MyLogger.getLogger().finer("ROOT: "+ROOT);	
	}

	
	public static String getPathCarro(ColoreGiocatore color){
		return getPathCarro(color.getColore().toLowerCase());
	}
	
	public static String getPathCarroBig(ColoreGiocatore color){
		return getPathCarroBig(color.getColore().toLowerCase());
	}
	
	public static String getPathCarro(String colore){
		String path = ROOT+File.separator+"images"+File.separator+"carri"+File.separator;
		path += colore+"_45px.png";
		return path;
	}
	
	public static String getPathCarroBig(String colore){
		String path = ROOT+File.separator+"images"+File.separator+"carri"+File.separator;
		path += colore+"_60px.png";
		return path;
	}
	
	public static String getPathFreccia(String colore){
		String path = ROOT+File.separator+"images"+File.separator+"frecce"+File.separator;
		path += "Freccia"+colore+".gif";
		return path;
	}
	
	public static String getPathPlancia(Configurator.PlanciaSize size){
		String path = ROOT+File.separator+"images"+File.separator;
		switch (size) {
		case BIG:
			path += "Plancia-Risiko-Challenge.jpg";
			break;
		case SMALL:
			path += "Plancia-Risiko-Challenge_rid.jpg";
			break;
		default:
			break;
		}
		return path;
	}
	
	public static String getPathPlanciaHDReady(){
		String path = ROOT+File.separator+"images"+File.separator;
		path += "Plancia-Risiko-Challenge HDR.jpg";
		return path;
	}
	
	public static String getPathPlanciaFullHD(){
		String path = ROOT+File.separator+"images"+File.separator;
		path += "Plancia-Risiko-Challenge FullHD.jpg";
		return path;
	}
	
	public static String getPathObiettivo(int numeroObiettivo){
		String path = ROOT+File.separator+"images"+File.separator+"obiettivi"+File.separator;
		path += "round_ob-"+numeroObiettivo+".png";
		return path;
	}
	
	
	public static void savePartita(Partita partita){
		String path = ROOT+File.separator+"partite"+File.separator;

		try {
			OutputStream file = new FileOutputStream(new File(path+partita.getIdPartita()+".ser"));
			OutputStream buffer = new BufferedOutputStream(file);
			ObjectOutput output = new ObjectOutputStream(buffer);

			output.writeObject(partita);
			
			output.close();
		}  catch(IOException ex){
			//MyLogger.getLogger().severe("Cannot perform output.");
		}
	}
	
	public static Partita readPartita(String idPartita){
		Partita partita = null;
		
		String path = ROOT+File.separator+"partite"+File.separator;

		try {
			InputStream  file = new FileInputStream(new File(path+idPartita+".ser"));
			InputStream  buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream(buffer);

			partita = (Partita)input.readObject();
			
			input.close();
		}catch(IOException ex){
			//MyLogger.getLogger().severe("Cannot perform output.");
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return partita;
	}
	
	public static String getPathDado(int dado){
		String path = ROOT+File.separator+"images"+File.separator+"dice"+File.separator;
		path += "Dice"+dado+"_rid.jpg";
		return path;
	}
	
	public static String getPathDado(int dado, String colore){
		String path = ROOT+File.separator+"images"+File.separator+"dice"+File.separator+"colorati"+File.separator;
		path += "dado_"+colore.toLowerCase()+"_"+dado+".png";
		return path;
	}
	
	public static String getPathPulsante(String pulsante){
		String path = ROOT+File.separator+"images"+File.separator+"pulsanti"+File.separator;
		path += "player_"+pulsante.toLowerCase()+"_rid.png";
		return path;
	}
}
