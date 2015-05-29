package frontend;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Scrollable;

import daten.*;

public class Karte extends JPanel implements Scrollable{
	private static final long serialVersionUID = 1L;
	private Frontend frontend;
	private Feld[][] felder;
	private D_Karte daten;
	private HashMap<String,BufferedImage> bildFeld=new HashMap<String,BufferedImage>();
	private HashMap<String,BufferedImage> bildRessource=new HashMap<String,BufferedImage>();
	private HashMap<String,BufferedImage> bildEinheit=new HashMap<String,BufferedImage>();
	private HashMap<String,BufferedImage> bildStadt=new HashMap<String,BufferedImage>();
	private BufferedImage bildFeldGewaehlt;
	
	private void dateienEinlesen(String pfad,HashMap<String,BufferedImage> container){
		File[] dateien;
		try {
			dateien=(new File(pfad)).listFiles();
			for (File datei:dateien){
				String name=datei.getName();
				container.put(name.substring(0,name.lastIndexOf('.')),ImageIO.read(datei));
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public Karte(Frontend frontend,D_Karte daten) {
		this.frontend=frontend;
		this.daten=daten;
		String pfadBild="daten//felder";
		String pfadRessource="daten//ressourcen";
		String pfadEinheit="daten//einheiten";
		String pfadStadt="daten//stadt";
		try {
			bildFeldGewaehlt=ImageIO.read(new File(pfadBild,"gewaehlt.png"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		dateienEinlesen(pfadBild,bildFeld);
		dateienEinlesen(pfadRessource,bildRessource);
		dateienEinlesen(pfadEinheit,bildEinheit);
		dateienEinlesen(pfadStadt,bildStadt);
		
		int spielfeldGroesse=frontend.getSpielfeldGroesse();
		int zoomfaktor=frontend.getZoomfaktor();
		int offset=spielfeldGroesse*zoomfaktor/100;
		setLayout(null);
		setPreferredSize(new Dimension(this.daten.getInt("x")*offset,this.daten.getInt("y")*offset));
		felder=new Feld[this.daten.getInt("x")+1][this.daten.getInt("y")+1];
		for (int i=1;i<=this.daten.getInt("x");i++){
			for (int j=1;j<=this.daten.getInt("y");j++){
				Feld f=new Feld(frontend,i,j);
				felder[i][j]=f;
				add(f);
			}
		}
	}
	
	public int getId(){
		return this.daten.getInt("id");
	}
	
	public BufferedImage getBildFeld(String feldArt){
		return bildFeld.get(feldArt);
	}
	public BufferedImage getBildRessource(String ressorcenArt){
		return bildRessource.get(ressorcenArt);
	}
	public BufferedImage getBildEinheit(String einheitArt){
		return bildEinheit.get(einheitArt);
	}
	public BufferedImage getBildStadt(String nummer){
		return bildStadt.get(nummer);
	}

	public void setEventhandler(KarteEventHandler eventHandler){
		for (int i=1;i<=this.daten.getInt("x");i++){
			for (int j=1;j<=this.daten.getInt("y");j++){
				felder[i][j].setEventhandler(eventHandler);
			}
		}
	}

	public void zeichneFelder(ArrayList<D> daten){
		if (daten==null) return;
		int spielfeldGroesse=frontend.getSpielfeldGroesse();
		int zoomfaktor=frontend.getZoomfaktor();
		int offset=spielfeldGroesse*zoomfaktor/100;
		setPreferredSize(new Dimension(this.daten.getInt("y")*offset,this.daten.getInt("y")*offset));
		for(D datenwert:daten){
			if (datenwert instanceof D_Feld){
				D_Feld datenFeld=(D_Feld)datenwert;
				int x=datenFeld.getInt("x");
				int y=datenFeld.getInt("y");
				Feld f=felder[x][y];
				f.setBounds(new Rectangle(offset,offset));
				f.setLocation((f.getPosX()-1)*offset,(f.getPosY()-1)*offset);
				f.setDaten(datenFeld);
			}
			else if (datenwert instanceof D_Einheit){
				D_Einheit datenEinheit=(D_Einheit)datenwert;
				int x=datenEinheit.getInt("x");
				int y=datenEinheit.getInt("y");
				Feld f=felder[x][y];
				f.setEinheit(datenEinheit);
			}
			else if (datenwert instanceof D_Stadt){
				D_Stadt datenStadt=(D_Stadt)datenwert;
				int x=datenStadt.getInt("x");
				int y=datenStadt.getInt("y");
				Feld f=felder[x][y];
				f.setStadt(datenStadt);
			}
		}
		for (int i=1;i<=this.daten.getInt("x");i++){
			for (int j=1;j<=this.daten.getInt("y");j++){
				felder[i][j].zeichnen();
			}
		}
	}

	public Feld getFeld(int x,int y){
		return felder[x][y];
	}
	
	public void zeichneFeld(int x,int y) {
		getFeld(x,y).zeichnen();
	}
	public void zeichneFeld(int[] pos) {
		zeichneFeld(pos[0],pos[1]);
	}
	
	public void updateFeld(int x,int y,ArrayList<D> daten) {
		for(D datenwert:daten){
			if (datenwert instanceof D_Feld) updateFeldBasis(x,y,(D_Feld)datenwert);
			if (datenwert instanceof D_Einheit) updateFeldEinheit(x,y,(D_Einheit)datenwert);
			if (datenwert instanceof D_Stadt) updateFeldStadt(x,y,(D_Stadt)datenwert);
		}
	}
	
	public void updateFeldBasis(int x,int y,D_Feld datenwert) {
		Feld f=felder[x][y];
		f.setEinheit(null);
		f.setStadt(null);
		f.setDaten(datenwert);
		zeichneFeld(x,y);
	}
	public void updateFeldEinheit(int x,int y,D_Einheit datenwert) {
		Feld f=felder[x][y];
		f.setEinheit(datenwert);
		zeichneFeld(x,y);
	}
	public void updateFeldStadt(int x,int y,D_Stadt datenwert) {
		Feld f=felder[x][y];
		f.setStadt(datenwert);
		zeichneFeld(x,y);
	}

	public BufferedImage getBildFeldGewaehlt() {
		return bildFeldGewaehlt;
	}
	
	public void terminate() {
		for (int i=1;i<=this.daten.getInt("x");i++){
			for (int j=1;j<=this.daten.getInt("y");j++){
				felder[i][j].terminate();
				felder[i][j]=null;
			}
		}
		felder=null;
	}
	
	@Override
	public Dimension getPreferredScrollableViewportSize() {
		 return getPreferredSize();
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}
	
	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 10;
	}
	
	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 10;
	}

}
