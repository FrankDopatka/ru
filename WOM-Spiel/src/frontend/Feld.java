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
	private boolean istMarkiert=false;
	private D_Feld d_Feld=null;
	private D_Einheit d_Einheit=null;
	private D_Stadt d_Stadt=null;
	
	public Feld(Frontend frontend,int x,int y) {
		this.frontend=frontend;
		this.x=x;
		this.y=y;
		setEnabled(true);
		setOpaque(true);
		setFocusable(false);
		setVisible(true);
	}
	
	public void markiere(boolean istMarkiert){
		this.istMarkiert=istMarkiert;
	}
	
	public boolean istMarkiert(){
		return istMarkiert;
	}
	
	public void setDaten(D_Feld daten){
		d_Feld=daten;
	}
	public D_Feld getDaten(){
		return d_Feld;
	}
	public void setEinheit(D_Einheit einheit){
		d_Einheit=einheit;
	}
	public D_Einheit getEinheit(){
		return d_Einheit;
	}
	public void setStadt(D_Stadt stadt){
		d_Stadt=stadt;
	}
	public D_Stadt getStadt(){
		return d_Stadt;
	}
	
	public void setEventhandler(KarteEventHandler eventHandler){
		this.eventHandler=eventHandler;
		if (eventHandler!=null){
			addMouseListener(eventHandler);
			addMouseMotionListener(eventHandler);
		}
	}
	
	public int getKartenId(){
		return d_Feld.getInt("idKarte");
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
		Image im=getBild();
		if (im!=null){
				Icon ic=new ImageIcon(im);
				setIcon(ic);			
			}
			else
				setIcon(null);
	}
	
	public Image getBild(){
		Image im;
		Karte karte=frontend.getKarte();
		BufferedImage bildIcon=karte.getBildFeld(d_Feld.getString("feldArt").toLowerCase());
		BufferedImage bild=new BufferedImage(frontend.getSpielfeldGroesse(),frontend.getSpielfeldGroesse(),BufferedImage.TYPE_INT_ARGB);
		Graphics g=bild.getGraphics();
		g.drawImage(bildIcon,0,0,null);
		if (d_Feld.getString("ressource").length()>0){
			BufferedImage bildRessource=karte.getBildRessource(d_Feld.getString("ressource").toLowerCase());
			g.drawImage(bildRessource,bildIcon.getWidth()/2-bildRessource.getWidth()/2,bildIcon.getHeight()/2-bildRessource.getWidth()/2,null);
		}
		if (d_Feld.getInt("spielerstart")!=0){
			g.setFont(new Font("Arial",Font.BOLD,24));
			g.setColor(new Color(0,0,0));
			g.drawString(d_Feld.getString("spielerstart"),5,24);
		}
		if (istMarkiert()){
			BufferedImage bildMarkiert=karte.getBildFeldMarkiert();
			g.drawImage(bildMarkiert,bildIcon.getWidth()/2-bildMarkiert.getWidth()/2,bildIcon.getHeight()/2-bildMarkiert.getWidth()/2,null);
		}
		if (this.equals(frontend.getFeldGewaehlt())){
			BufferedImage bildGewaehlt=karte.getBildFeldGewaehlt();
			g.drawImage(bildGewaehlt,bildIcon.getWidth()/2-bildGewaehlt.getWidth()/2,bildIcon.getHeight()/2-bildGewaehlt.getWidth()/2,null);
		}
		if ((d_Einheit!=null)&&(d_Stadt==null)){
			BufferedImage bildEinheit=karte.getBildEinheit(d_Einheit.getString("name").toLowerCase());
			g.drawImage(bildEinheit,bildIcon.getWidth()/2-bildEinheit.getWidth()/2,bildIcon.getHeight()/2-bildEinheit.getWidth()/2,null);
		}
		if (d_Stadt!=null){
			BufferedImage bildStadt=karte.getBildStadt("1");
			g.drawImage(bildStadt,bildIcon.getWidth()/2-bildStadt.getWidth()/2,bildIcon.getHeight()/2-bildStadt.getWidth()/2,null);
			if (d_Einheit!=null){
				BufferedImage bildFlagge=karte.getBildStadt("flagge");
				g.drawImage(bildFlagge,bildIcon.getWidth()/2-bildStadt.getWidth()/2,bildIcon.getHeight()/2-bildStadt.getWidth()/2,null);
			}
		}
		g.dispose();
		int zoomfaktor=frontend.getZoomfaktor();
		if (zoomfaktor==100)
			im=bild;
		else
			im=bild.getScaledInstance(bild.getWidth()*zoomfaktor/100,bild.getHeight()*zoomfaktor/100,Image.SCALE_FAST);
		return im;
	}
	
	public void terminate(){
		removeMouseListener(eventHandler);
		removeMouseMotionListener(eventHandler);
		setIcon(null);
		setVisible(false);
	}
	
	@Override
	public boolean equals(Object o){
		if (!(o instanceof Feld)) return false;
		Feld f=(Feld)o;
		return ((this.x==f.x)&&(f.y==this.y));
	}
	
	@Override
	public String toString(){
		String idString="["+frontend.getIdSpieler()+"] ";
		String s=idString+"Feld: "+d_Feld.toString();
		if (d_Einheit!=null) s+="\n"+idString+"Einheit: "+d_Einheit.toString();
		if (d_Stadt!=null) s+="\n"+idString+"Stadt: "+d_Stadt.toString();
		return s;
	}
}
