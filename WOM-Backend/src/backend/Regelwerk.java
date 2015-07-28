package backend;

import java.util.ArrayList;
import java.util.Random;

import daten.D;
import daten.D_Position;
import backend.karte.Feld;
import backend.karte.Karte;
import backend.spiel.Einheit;
import backend.spiel.Spiel;
import backend.spiel.Spiel.Bewegungsrichtung;
import backend.spiel.Spieler;
import backend.spiel.Stadt;
import backend.spiel.einheiten.Krieger;
import backend.spiel.einheiten.Siedler;

public class Regelwerk {
	public Spiel spiel=null;
	
	public static int getZufallszahl(int ug,int og){
		Random r=new Random();
		return r.nextInt(og-ug+1)+ug;
	}
	
	public Regelwerk(Spiel spiel){
		this.spiel=spiel;
	}
	
	public D_Position bewegeEinheit(int idSpieler,int idKarte,int xAlt,int yAlt,int richtung) {
		D_Position posNeu=new D_Position(); // Rueckgabe
		posNeu.setInt("x",0);
		posNeu.setInt("y",0);
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

		Einheit einheitFeldNeu=feldNeu.getEinheit();
		if (einheitFeldNeu!=null){ 
			// AUF DEM ZIELFELD IST SCHON EINE EINHEIT
			if (einheitFeldNeu.getIdSpieler()==idSpieler)
				throw new RuntimeException("Auf dem Zielfeld der Einheit steht bereits eine Einheit von Ihnen!");
			
			if (einheit.getDaten().getInt("angriffAktuell")<=0)
				throw new RuntimeException("Ihre Einheit kann nicht angreifen!");
			
			// Kampfalgorithmus
			int temp=0;
			// alte Daten des Angreifers
			int angriffA=einheit.getDaten().getInt("angriffAktuell");
			int verteidigungA=einheit.getDaten().getInt("verteidigungAktuell");
			int lebenA=einheit.getDaten().getInt("lebenAktuell");
			// alte Daten des Verteidigers
			int verteidigungV=einheitFeldNeu.getDaten().getInt("verteidigungAktuell");
			int lebenV=einheitFeldNeu.getDaten().getInt("lebenAktuell");
			
			// neue Daten des Angreifers
			temp=lebenA-verteidigungV/2-getZufallszahl(0,verteidigungV/2);
			if (temp<lebenA){
				einheit.getDaten().setInt("lebenAktuell",temp);
				if (temp<einheit.getDaten().getInt("lebenMaximal")/2){
					// bei verwundeten Einheiten die Angriffs- und Verteidigungspunkte halbieren
					einheit.getDaten().setInt("angriffAktuell",angriffA/2);
					einheit.getDaten().setInt("verteidigungAktuell",verteidigungA/2);
				}
			}
			
			// neue Daten des Verteidigers
			temp=lebenV-angriffA+verteidigungV-1-getZufallszahl(0,verteidigungV/2);
			if (temp<lebenV){
				einheitFeldNeu.getDaten().setInt("lebenAktuell",temp);
				if (temp<einheitFeldNeu.getDaten().getInt("lebenMaximal")/2){
					// bei verwundeten Einheiten die Angriffs- und Verteidigungspunkte halbieren
					einheitFeldNeu.getDaten().setInt("angriffAktuell",angriffA/2);
					einheitFeldNeu.getDaten().setInt("verteidigungAktuell",verteidigungA/2);
				}
			}
			
			// Lebenspunkte aktualisieren
			lebenA=einheit.getDaten().getInt("lebenAktuell");
			lebenV=einheitFeldNeu.getDaten().getInt("lebenAktuell");

			
			Spieler angreifer=spiel.getSpieler(idSpieler);
			Spieler verteidiger=spiel.getSpieler(einheitFeldNeu.getIdSpieler());

			if (lebenA<=0) // Angreifer verliert und verschwindet
				angreifer.removeEinheit(einheit,karte.getFeld(einheit.getPosX(),einheit.getPosY()));				

			if (lebenV<=0) // Verteidiger verliert und verschwindet
				verteidiger.removeEinheit(einheitFeldNeu,karte.getFeld(einheitFeldNeu.getPosX(),einheitFeldNeu.getPosY()));				
			if ((lebenA>0)&&(lebenV<=0)){
				// Angreifer gewinnt und nimmt neue Position ein
				posNeu=bewege(einheit,feldAlt,feldNeu,xNeu,yNeu,punkte);
			}
			if ((lebenA>0)&&(lebenV>0)){
				// beide ueberleben und behalten die Position bei
				// Bewegungspunkte beim Angreifer abziehen
				einheit.getDaten().setInt("bewegungAktuell",einheit.getDaten().getInt("bewegungAktuell")-punkte);
			}
	//		karte.setUpdate(feldAlt.toDatenArray(),einheitFeldNeu.getIdSpieler());
	//		karte.setUpdate(feldNeu.toDatenArray(),einheitFeldNeu.getIdSpieler());			
		}
		else{
			// AUF DEM ZIELFELD IST KEINE EINHEIT -> einfach dahin bewegen
			posNeu=bewege(einheit,feldAlt,feldNeu,xNeu,yNeu,punkte);
		}
		karte.setUpdate(feldAlt.toDatenArray(),einheit.getIdSpieler());
		karte.setUpdate(feldNeu.toDatenArray(),einheit.getIdSpieler());			
		return posNeu;
	}
	
	private D_Position bewege(Einheit einheit,Feld feldAlt,Feld feldNeu,int xNeu,int yNeu,int punkteVerbraucht){
		D_Position posNeu=new D_Position();
		einheit.getDaten().setInt("bewegungAktuell",einheit.getDaten().getInt("bewegungAktuell")-punkteVerbraucht);
		feldAlt.setEinheit(null);
		feldNeu.setEinheit(einheit);
		posNeu=new D_Position();
		posNeu.setInt("x",xNeu);
		posNeu.setInt("y",yNeu);
		return posNeu;
	}
	
	private int getNoetigeBewegungspunkte(Feld feldAlt,Feld feldNeu) {
		// TODO ggf. komplexere Berechnung noetig in Abhaengigkeit der Felduebergaenge und der Einheiten
		return feldNeu.getBewegungspunkte();
	}
	
	public ArrayList<D> getProduzierbareEinheiten(int idSpieler,int idStadt){
		ArrayList<D> ergebnis=new ArrayList<D>();
		Spieler spieler=spiel.getSpieler(idSpieler);
		Stadt stadt=spieler.getStadt(idStadt);
		
		// TODO Produktion abhaengig von Wissenschaft, Lage, Stadt
		
		Krieger krieger=new Krieger();
		krieger.setSpieler(spieler.getId());
		ergebnis.add(krieger.getDaten());
		Siedler siedler=new Siedler();
		siedler.setSpieler(spieler.getId());
		ergebnis.add(siedler.getDaten());
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
		case "Krieger":
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
}
