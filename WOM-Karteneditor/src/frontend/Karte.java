package frontend;

import interfaces.iBackendKarteneditor;

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

import daten.D;
import daten.D_Feld;
import daten.D_FeldArt;
import daten.D_RessourcenArt;
import daten.Xml;

public class Karte extends JPanel implements Scrollable{
	private static final long serialVersionUID = 1L;
	private Frontend frontend;
	private iBackendKarteneditor backendKarteneditor;
	private Feld[][] felder;
	private int groesseX;
	private int groesseY;
	private HashMap<String,BufferedImage> bildFeld=new HashMap<String,BufferedImage>();
	private HashMap<String,BufferedImage> bildRessource=new HashMap<String,BufferedImage>();
	
	public Karte(Frontend frontend,int groesseX,int groesseY) {
		this.frontend=frontend;
		this.groesseX=groesseX;
		this.groesseY=groesseY;
		backendKarteneditor=frontend.getBackendKarteneditor();
		String kartenArt=Xml.toD(backendKarteneditor.getKartenDaten()).getString("kartenArt");
		D_FeldArt feldArten=(D_FeldArt)Xml.toD(backendKarteneditor.getErlaubteFeldArten(kartenArt));
		String pfadBild="daten//felder";
		String pfadRessource="daten//ressourcen";
		for (String feldArt:feldArten.getListe()){
  		try {
				bildFeld.put(feldArt, ImageIO.read(new File(pfadBild,feldArt.toLowerCase()+".jpg")));
				D_RessourcenArt feldRessourcen=(D_RessourcenArt)Xml.toD(backendKarteneditor.getErlaubteRessourcenArten(feldArt));
				if (feldRessourcen!=null){
					for (String resourcenArt:feldRessourcen.getListe()){
						if (!bildRessource.containsKey(resourcenArt)){
							bildRessource.put(resourcenArt,ImageIO.read(new File(pfadRessource,resourcenArt.toLowerCase()+".png")));
						}
					}					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
  	}

		int spielfeldGroesse=frontend.getSpielfeldGroesse();
		int zoomfaktor=frontend.getZoomfaktor();
		int offset=spielfeldGroesse*zoomfaktor/100;
		setLayout(null);
		setPreferredSize(new Dimension(groesseX*offset,groesseY*offset));
		felder=new Feld[groesseX+1][groesseY+1];
		for (int i=1;i<=groesseX;i++){
			for (int j=1;j<=groesseY;j++){
				Feld f=new Feld(frontend,i,j);
				felder[i][j]=f;
				add(f);
			}
		}
	}
	
	public BufferedImage getBildFeld(String feldArt){
		return bildFeld.get(feldArt);
	}

	public BufferedImage getBildRessource(String ressorcenArt){
		return bildRessource.get(ressorcenArt);
	}

	public void setEventhandler(KarteEventHandler eventHandler){
		for (int i=1;i<=groesseX;i++){
			for (int j=1;j<=groesseY;j++){
				felder[i][j].setEventhandler(eventHandler);
			}
		}
	}

	public void zeichneFelder(ArrayList<D> daten){
		if (daten==null) return;
		int spielfeldGroesse=frontend.getSpielfeldGroesse();
		int zoomfaktor=frontend.getZoomfaktor();
		int offset=spielfeldGroesse*zoomfaktor/100;
		setPreferredSize(new Dimension(groesseX*offset,groesseY*offset));
		for(D datenwert:daten){
			if (datenwert instanceof D_Feld){
				D_Feld datenFeld=(D_Feld)datenwert;
				int x=datenFeld.getInt("x");
				int y=datenFeld.getInt("y");
				Feld f=felder[x][y];
				f.setBounds(new Rectangle(offset,offset));
				f.setLocation((f.getPosX()-1)*offset,(f.getPosY()-1)*offset);
				f.zeichnen(datenFeld);
			}
		}
	}
	
	public void zeichneFeld(int[] pos) {
		felder[pos[0]][pos[1]].zeichnen();
	}
	
	public void terminate() {
		for (int i=1;i<=groesseX;i++){
			for (int j=1;j<=groesseY;j++){
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
