package frontend;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import daten.*;

public class Feld extends JLabel{
	private static final long serialVersionUID = 1L;
	private Frontend frontend;
	private KarteEventHandler eventHandler;
	private int x;
	private int y;
	
	public Feld(Frontend frontend,int x,int y) {
		this.frontend=frontend;
		this.x=x;
		this.y=y;
		setEnabled(true);
		setOpaque(true);
		setFocusable(false);
		setVisible(true);
	}
	
	public void setEventhandler(KarteEventHandler eventHandler){
		this.eventHandler=eventHandler;
		if (eventHandler!=null){
			addMouseListener(eventHandler);
			addMouseMotionListener(eventHandler);
		}
	}

	public int getPosX(){
		return x;
	}
	public int getPosY(){
		return y;
	}
	public int[] getPos(){
		int[] ausgabe={getPosX(),getPosY()};
		return ausgabe;
	}

	public void zeichnen(){
		D_Feld daten=(D_Feld)Xml.toD(frontend.getBackendKarteneditor().getFeldDaten(x,y));
		zeichnen(daten);
	}
	
	public void zeichnen(D_Feld daten){
		Image im;
		BufferedImage bildIcon=frontend.getKarte().getBildFeld(daten.getString("feldArt"));
		BufferedImage bild=new BufferedImage(frontend.getSpielfeldGroesse(),frontend.getSpielfeldGroesse(),BufferedImage.TYPE_INT_ARGB);
		Graphics g=bild.getGraphics();
		g.drawImage(bildIcon,0,0,null);
		if (daten.getString("ressource").length()>0){
			BufferedImage bildRessource=frontend.getKarte().getBildRessource(daten.getString("ressource"));
			g.drawImage(bildRessource,bildIcon.getWidth()/2-bildRessource.getWidth()/2,bildIcon.getHeight()/2-bildRessource.getWidth()/2,null);
		}			
		if (daten.getInt("spielerstart")!=0){
			g.setFont(new Font("Arial",Font.BOLD,24));
			g.setColor(new Color(0,0,0));
			g.drawString(daten.getString("spielerstart"),5,24);
		}
		g.dispose();
		if (frontend.getZoomfaktor()==100)
			im=bild;
		else
			im=bild.getScaledInstance(bild.getWidth()*frontend.getZoomfaktor()/100,bild.getHeight()*frontend.getZoomfaktor()/100,Image.SCALE_FAST);
		if (im!=null){
			Icon ic=new ImageIcon(im);
			setIcon(ic);			
		}
		else{
			setIcon(null);
		}
	}
	
	public void terminate(){
		removeMouseListener(eventHandler);
		removeMouseMotionListener(eventHandler);
		setIcon(null);
		setVisible(false);
	}
	
	@Override
	public String toString(){
		D_Feld d=(D_Feld)Xml.toD(frontend.getBackendKarteneditor().getFeldDaten(x,y));
		String s="Feld "+d.getInt("x")+"/"+d.getInt("y")+" vom Typ "+d.getString("feldArt");
		if (d.getString("ressource").length()>0) s+=" und Ressource "+d.getString("ressource");
		if (d.getInt("spielerstart")>0) s+=" und Start von Spieler Nummer "+d.getInt("spielerstart");
		return s;
	}
}
