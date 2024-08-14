package it.desimone.replayrd3.ui.animation;

import it.desimone.utils.ResourceLoader;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class DicePanel extends JPanel{

	private JLabel dado1Attacco = new JLabel();
	private JLabel dado2Attacco = new JLabel();
	private JLabel dado3Attacco = new JLabel();
	private JLabel dado1Difesa = new JLabel();
	private JLabel dado2Difesa = new JLabel();
	private JLabel dado3Difesa = new JLabel();
	
	private Icon[] diceFaces;
	
	{
		diceFaces = new Icon[6];
		for (int i = 0; i < diceFaces.length; i++){
			diceFaces[i] = new ImageIcon(ResourceLoader.getPathDado(i+1));
		}
	}
	
	public DicePanel(){
		setLayout(new GridLayout(3,2,4,4));
		//setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		add(dado1Attacco);
		add(dado1Difesa);
		add(dado2Attacco);
		add(dado2Difesa);
		add(dado3Attacco);
		add(dado3Difesa);
		
		setOpaque(false);
	}

	public void setDadiAttaccoTxt(Byte[] dadiAttacco){
		if (dadiAttacco != null){
			if (dadiAttacco.length >= 1 && dadiAttacco[0] != null){
				dado1Attacco.setText(dadiAttacco[0].toString());
			}else{
				dado1Attacco.setText(null);
			}
			if (dadiAttacco.length >= 2 && dadiAttacco[1] != null){
				dado2Attacco.setText(dadiAttacco[1].toString());
			}else{
				dado2Attacco.setText(null);
			}
			if (dadiAttacco.length == 3 && dadiAttacco[2] != null){
				dado3Attacco.setText(dadiAttacco[2].toString());
			}else{
				dado3Attacco.setText(null);
			}
		}
	}
	
	public void setDadiDifesaTxt(Byte[] dadiDifesa){
		if (dadiDifesa != null){
			if (dadiDifesa.length >= 1 && dadiDifesa[0] != null){
				dado1Difesa.setText(dadiDifesa[0].toString());
			}else{
				dado1Difesa.setText(null);
			}
			if (dadiDifesa.length >= 2 && dadiDifesa[1] != null){
				dado2Difesa.setText(dadiDifesa[1].toString());
			}else{
				dado2Difesa.setText(null);
			}
			if (dadiDifesa.length == 3 && dadiDifesa[2] != null){
				dado3Difesa.setText(dadiDifesa[2].toString());
			}else{
				dado3Difesa.setText(null);
			}
		}
	}
	
	public void setDadiAttacco(Byte[] dadiAttacco){
		if (dadiAttacco != null){
			if (dadiAttacco.length >= 1 && dadiAttacco[0] != null){
				dado1Attacco.setIcon(diceFaces[dadiAttacco[0]-1]);
			}else{
				dado1Attacco.setIcon(null);
			}
			if (dadiAttacco.length >= 2 && dadiAttacco[1] != null){
				dado2Attacco.setIcon(diceFaces[dadiAttacco[1]-1]);
			}else{
				dado2Attacco.setIcon(null);
			}
			if (dadiAttacco.length == 3 && dadiAttacco[2] != null){
				dado3Attacco.setIcon(diceFaces[dadiAttacco[2]-1]);
			}else{
				dado3Attacco.setIcon(null);
			}
		}
	}
	
	public void setDadiDifesa(Byte[] dadiDifesa){
		if (dadiDifesa != null){
			if (dadiDifesa.length >= 1 && dadiDifesa[0] != null){
				dado1Difesa.setIcon(diceFaces[dadiDifesa[0]-1]);
			}else{
				dado1Difesa.setIcon(null);
			}
			if (dadiDifesa.length >= 2 && dadiDifesa[1] != null){
				dado2Difesa.setIcon(diceFaces[dadiDifesa[1]-1]);
			}else{
				dado2Difesa.setIcon(null);
			}
			if (dadiDifesa.length == 3 && dadiDifesa[2] != null){
				dado3Difesa.setIcon(diceFaces[dadiDifesa[2]-1]);
			}else{
				dado3Difesa.setIcon(null);
			}
		}
	}
	
	
	public void setDadiAttacco(Byte[] dadiAttacco, String colore){
		if (dadiAttacco != null){
			if (dadiAttacco.length >= 1 && dadiAttacco[0] != null){
				dado1Attacco.setIcon(new ImageIcon(ResourceLoader.getPathDado(dadiAttacco[0], colore)));
			}else{
				dado1Attacco.setIcon(null);
			}
			if (dadiAttacco.length >= 2 && dadiAttacco[1] != null){
				dado2Attacco.setIcon(new ImageIcon(ResourceLoader.getPathDado(dadiAttacco[1], colore)));
			}else{
				dado2Attacco.setIcon(null);
			}
			if (dadiAttacco.length == 3 && dadiAttacco[2] != null){
				dado3Attacco.setIcon(new ImageIcon(ResourceLoader.getPathDado(dadiAttacco[2], colore)));
			}else{
				dado3Attacco.setIcon(null);
			}
		}
	}
	
	public void setDadiDifesa(Byte[] dadiDifesa, String colore){
		if (dadiDifesa != null){
			if (dadiDifesa.length >= 1 && dadiDifesa[0] != null){
				dado1Difesa.setIcon(new ImageIcon(ResourceLoader.getPathDado(dadiDifesa[0], colore)));
			}else{
				dado1Difesa.setIcon(null);
			}
			if (dadiDifesa.length >= 2 && dadiDifesa[1] != null){
				dado2Difesa.setIcon(new ImageIcon(ResourceLoader.getPathDado(dadiDifesa[1], colore)));
			}else{
				dado2Difesa.setIcon(null);
			}
			if (dadiDifesa.length == 3 && dadiDifesa[2] != null){
				dado3Difesa.setIcon(new ImageIcon(ResourceLoader.getPathDado(dadiDifesa[2], colore)));
			}else{
				dado3Difesa.setIcon(null);
			}
		}
	}
	
	public JLabel getDado1Attacco() {
		return dado1Attacco;
	}

	public void setDado1Attacco(JLabel dado1Attacco) {
		this.dado1Attacco = dado1Attacco;
	}

	public JLabel getDado2Attacco() {
		return dado2Attacco;
	}

	public void setDado2Attacco(JLabel dado2Attacco) {
		this.dado2Attacco = dado2Attacco;
	}

	public JLabel getDado3Attacco() {
		return dado3Attacco;
	}

	public void setDado3Attacco(JLabel dado3Attacco) {
		this.dado3Attacco = dado3Attacco;
	}

	public JLabel getDado1Difesa() {
		return dado1Difesa;
	}

	public void setDado1Difesa(JLabel dado1Difesa) {
		this.dado1Difesa = dado1Difesa;
	}

	public JLabel getDado2Difesa() {
		return dado2Difesa;
	}

	public void setDado2Difesa(JLabel dado2Difesa) {
		this.dado2Difesa = dado2Difesa;
	}

	public JLabel getDado3Difesa() {
		return dado3Difesa;
	}

	public void setDado3Difesa(JLabel dado3Difesa) {
		this.dado3Difesa = dado3Difesa;
	}
	
	
	
}
