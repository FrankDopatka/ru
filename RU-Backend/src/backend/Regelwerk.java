package backend;

import java.util.ArrayList;
import java.util.Random;

import daten.D;
import daten.D_Position;
import backend.karte.Feld;
import backend.karte.Karte;
import backend.spiel.*;
import backend.spiel.Spiel.Bewegungsrichtung;
import backend.spiel.einheiten.*;

public class Regelwerk {
	public Spiel spiel=null;
	public Kampfsystem kampfsystem=null;
	
	public static int getZufallszahl(int ug,int og){
		Random r=new Random();
		return r.nextInt(og-ug+1)+ug;
	}
	
	public Regelwerk(Spiel spiel){
		this.spiel=spiel;
		kampfsystem=new Kampfsystem(this,spiel);
	}
	
	public D_Position bewegeEinheit(int idSpieler,int idKarte,int xAlt,int yAlt,int richtung) {
		D_Position posNeu=new D_Position(); // Rueckgabe
		Karte karte=spiel.getKarte(idKarte);
		Feld feldAlt=karte.getFeld(xAlt,yAlt);
		Einheit einheit=feldAlt.getEinheit();
		int[] neu=getNeueKoordinaten(xAlt,yAlt,richtung);
		int xNeu=neu[0]; 
		int yNeu=neu[1];
		if((yNeu<1)||(yNeu>karte.getGroesseY()))
			throw new RuntimeException("Man kann den Kartenrand nicht verlassen!");
		if (spiel.getSpielerAmZug()!=idSpieler)
			throw new RuntimeException("Sie sind nicht am Zug!");
		if (xNeu<1) xNeu=karte.getGroesseX();
		if (xNeu>karte.getGroesseX()) xNeu=1;
		Feld feldNeu=karte.getFeld(xNeu,yNeu);
		if (einheit.istLandeinheit()&&feldNeu.istWasserfeld())
			throw new RuntimeException("Landeinheiten koennen nicht auf Wasser bewegt werden!");

		int punkte=getNoetigeBewegungspunkte(feldAlt,feldNeu);	
		if (einheit.getDaten().getInt("bewegungAktuell")<punkte)
			throw new RuntimeException("Diese Einheit kann sich in dieser Runde nicht mehr auf dieses Feld bewegen!");

		if (feldNeu.getEinheit()!=null){ 
			// AUF DEM ZIELFELD IST SCHON EINE EINHEIT -> Nahkampf!
			posNeu=kampfsystem.nahkampf(idSpieler,karte,feldAlt,feldNeu);
		}
		else{
			// AUF DEM ZIELFELD IST KEINE EINHEIT -> einfach dahin bewegen
			posNeu=bewege(einheit,feldAlt,feldNeu,xNeu,yNeu,punkte);
		}
		karte.setUpdate(feldAlt.toDatenArray(),einheit.getIdSpieler());
		karte.setUpdate(feldNeu.toDatenArray(),einheit.getIdSpieler());			
		return posNeu;
	}
	
	public D_Position bewege(Einheit einheit,Feld feldAlt,Feld feldNeu,int xNeu,int yNeu,int punkteVerbraucht){
		D_Position posNeu=new D_Position();
		einheit.getDaten().setInt("bewegungAktuell",einheit.getDaten().getInt("bewegungAktuell")-punkteVerbraucht);
		feldAlt.setEinheit(null);
		feldNeu.setEinheit(einheit);
		posNeu=new D_Position();
		posNeu.setInt("x",xNeu);
		posNeu.setInt("y",yNeu);
		return posNeu;
	}
	
	public int getNoetigeBewegungspunkte(Feld feldAlt,Feld feldNeu) {
		// TODO ggf. komplexere Berechnung noetig in Abhaengigkeit der Felduebergaenge und der Einheiten
		return feldNeu.getBewegungspunkte();
	}
	
	public ArrayList<D> getProduzierbareEinheiten(int idSpieler,int idStadt){
		ArrayList<D> ergebnis=new ArrayList<D>();
		Spieler spieler=spiel.getSpieler(idSpieler);
		Stadt stadt=spieler.getStadt(idStadt);
		
		// TODO Produktion abhaengig von Wissenschaft, Lage, Stadt
		
		ergebnis.add((new Siedler(idSpieler)).getDaten());
		ergebnis.add((new Krieger(idSpieler)).getDaten());
		ergebnis.add((new Bogenschuetze(idSpieler)).getDaten());
		ergebnis.add((new Schwertkaempfer(idSpieler)).getDaten());
		ergebnis.add((new Panzer(idSpieler)).getDaten());

		return ergebnis;
	}

	private int[] getNeueKoordinaten(int xAlt,int yAlt,int richtung){
		int xNeu=xAlt;
		int yNeu=yAlt;
		switch (Bewegungsrichtung.fromOrdinal(richtung)){
		case NORD:
			yNeu--;
			break;
		case NORDOST:
			xNeu++;
			yNeu--;
			break;
		case OST:
			xNeu++;
			break;
		case SUEDOST:
			xNeu++;
			yNeu++;
			break;
		case SUED:
			yNeu++;
			break;
		case SUEDWEST:
			xNeu--;
			yNeu++;
			break;
		case WEST:
			xNeu--;
			break;
		case NORDWEST:
			xNeu--;
			yNeu--;
			break;
		case AKTION:
			break;
		}
		return new int[]{xNeu,yNeu};
	}

	public boolean istEinheit(String zuProduzieren) {
		switch (zuProduzieren){
		case "Siedler":
		case "Bogenschuetze":
		case "Schwertkaempfer":
		case "Krieger":
		case "Panzer":
			return true;
		}
		return false;
	}

	public void neueRunde() {
		Spieler spieler=spiel.getSpieler(spiel.getSpielerAmZug());
		for (Stadt stadt:spieler.getStaedte()){
			stadt.updateProduktion();
		}
	}

	public void fernkampf(int idSpieler,int idKarte,int xAngreifer,int yAngreifer,int xVerteidiger,int yVerteidiger) {
		if (spiel.getSpielerAmZug()!=idSpieler)
			throw new RuntimeException("Sie sind nicht am Zug!");
		Karte karte=spiel.getKarte(idKarte);
		Feld angreiferFeld=karte.getFeld(xAngreifer,yAngreifer);
		Feld verteidigerFeld=karte.getFeld(xVerteidiger,yVerteidiger);
		Einheit angreiferEinheit=angreiferFeld.getEinheit();
		Einheit verteidigerEinheit=verteidigerFeld.getEinheit();
		if (angreiferEinheit==null)
			throw new RuntimeException("Es gibt keinen Angreifer!");
		if (angreiferEinheit.getIdSpieler()!=spiel.getSpielerAmZug())
			throw new RuntimeException("Sie duerfen nur mit Ihren eigenen Einheiten angreifen!");
		if (verteidigerEinheit==null)
			throw new RuntimeException("Es gibt keine Einheit auf dem Feld des Angriffs!");
		if (!angreiferEinheit.getDaten().getBool("istFernkampfeinheit"))
			throw new RuntimeException("Der Angreifer ist keine!");
		kampfsystem.fernkampf(idSpieler,karte,angreiferFeld,verteidigerFeld);
		karte.setUpdate(angreiferFeld.toDatenArray(),0);
		karte.setUpdate(verteidigerFeld.toDatenArray(),0);			
	}
}
