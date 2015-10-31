package backend;

import daten.D_Position;
import backend.karte.Feld;
import backend.karte.Karte;
import backend.spiel.Einheit;
import backend.spiel.Spiel;
import backend.spiel.Spieler;

public class Kampfsystem {
	private Regelwerk regelwerk;
	private Spiel spiel;
	
	public Kampfsystem(Regelwerk regelwerk, Spiel spiel) {
		this.regelwerk=regelwerk;
		this.spiel=spiel;
	}
	
	public D_Position nahkampf(int idSpieler,Karte karte,Feld feldAlt,Feld feldNeu){
		D_Position posNeu=new D_Position();
		Einheit angreiferEinheit=feldAlt.getEinheit();
		Einheit verteidigerEinheit=feldNeu.getEinheit();
		int punkte=regelwerk.getNoetigeBewegungspunkte(feldAlt,feldNeu);
		if (verteidigerEinheit.getIdSpieler()==idSpieler)
			throw new RuntimeException("Auf dem Zielfeld der Einheit steht bereits eine Einheit von Ihnen!");
		if (angreiferEinheit.getDaten().getInt("angriffAktuell")<=0)
			throw new RuntimeException("Ihre Einheit kann nicht angreifen!");
		
		// Nahkampfalgorithmus
		int temp=0;
		// alte Daten des Angreifers
		int angriffA=angreiferEinheit.getDaten().getInt("angriffAktuell");
		int verteidigungA=angreiferEinheit.getDaten().getInt("verteidigungAktuell");
		int lebenA=angreiferEinheit.getDaten().getInt("lebenAktuell");
		// alte Daten des Verteidigers
		int verteidigungV=verteidigerEinheit.getDaten().getInt("verteidigungAktuell");
		int lebenV=verteidigerEinheit.getDaten().getInt("lebenAktuell");
		
		// neue Daten des Angreifers
		temp=lebenA-verteidigungV/2-Regelwerk.getZufallszahl(0,verteidigungV/2);
		if (temp<lebenA){
			angreiferEinheit.getDaten().setInt("lebenAktuell",temp);
			if (temp<angreiferEinheit.getDaten().getInt("lebenMaximal")/2){
				// bei verwundeten Einheiten die Angriffs- und Verteidigungspunkte halbieren
				angreiferEinheit.getDaten().setInt("angriffAktuell",angriffA/2);
				angreiferEinheit.getDaten().setInt("verteidigungAktuell",verteidigungA/2);
			}
		}
		
		// neue Daten des Verteidigers
		temp=lebenV-angriffA+verteidigungV-1-Regelwerk.getZufallszahl(0,verteidigungV/2);
		if (temp<lebenV){
			verteidigerEinheit.getDaten().setInt("lebenAktuell",temp);
			if (temp<verteidigerEinheit.getDaten().getInt("lebenMaximal")/2){
				// bei verwundeten Einheiten die Angriffs- und Verteidigungspunkte halbieren
				verteidigerEinheit.getDaten().setInt("angriffAktuell",angriffA/2);
				verteidigerEinheit.getDaten().setInt("verteidigungAktuell",verteidigungA/2);
			}
		}
		
		// Lebenspunkte aktualisieren
		lebenA=angreiferEinheit.getDaten().getInt("lebenAktuell");
		lebenV=verteidigerEinheit.getDaten().getInt("lebenAktuell");

		Spieler angreiferSpieler=spiel.getSpieler(idSpieler);
		Spieler verteidigerSpieler=spiel.getSpieler(verteidigerEinheit.getIdSpieler());

		if (lebenA<=0) // Angreifer verliert und verschwindet
			angreiferSpieler.removeEinheit(angreiferEinheit,karte.getFeld(angreiferEinheit.getPosX(),angreiferEinheit.getPosY()));				

		if (lebenV<=0) // Verteidiger verliert und verschwindet
			verteidigerSpieler.removeEinheit(verteidigerEinheit,karte.getFeld(verteidigerEinheit.getPosX(),verteidigerEinheit.getPosY()));				
		if ((lebenA>0)&&(lebenV<=0)){
			// Angreifer gewinnt und nimmt neue Position ein
			posNeu=regelwerk.bewege(angreiferEinheit,feldAlt,feldNeu,feldNeu.getPosX(),feldNeu.getPosY(),punkte);
			
		}
		// Bewegungspunkte beim Angreifer auf 0 setzen
		angreiferEinheit.getDaten().setInt("bewegungAktuell",0);
		return posNeu;
	}
	
	public void fernkampf(int idSpieler,Karte karte,Feld feldAlt,Feld feldNeu){
		Einheit angreiferEinheit=feldAlt.getEinheit();
		Einheit verteidigerEinheit=feldNeu.getEinheit();

		// Daten des Angreifers
		int angriffA=angreiferEinheit.getDaten().getInt("angriffAktuell");
		int verteidigungA=angreiferEinheit.getDaten().getInt("verteidigungAktuell");
		int lebenA=angreiferEinheit.getDaten().getInt("lebenAktuell");
		// alte Daten des Verteidigers
		int verteidigungV=verteidigerEinheit.getDaten().getInt("verteidigungAktuell");
		int lebenV=verteidigerEinheit.getDaten().getInt("lebenAktuell");
		
		// Fernkampfalgorithmus
		int temp=0;
		// neue Daten des Verteidigers
		temp=lebenV-angriffA+verteidigungV-1-Regelwerk.getZufallszahl(0,verteidigungV/2);
		if (temp<lebenV){
			verteidigerEinheit.getDaten().setInt("lebenAktuell",temp);
			if (temp<verteidigerEinheit.getDaten().getInt("lebenMaximal")/2){
				// bei verwundeten Einheiten die Angriffs- und Verteidigungspunkte halbieren
				verteidigerEinheit.getDaten().setInt("angriffAktuell",angriffA/2);
				verteidigerEinheit.getDaten().setInt("verteidigungAktuell",verteidigungA/2);
			}
		}
		
		// Lebenspunkte aktualisieren
		lebenA=angreiferEinheit.getDaten().getInt("lebenAktuell");
		lebenV=verteidigerEinheit.getDaten().getInt("lebenAktuell");

		Spieler verteidigerSpieler=spiel.getSpieler(verteidigerEinheit.getIdSpieler());
		
		if (lebenV<=0) // Verteidiger verliert und verschwindet
			verteidigerSpieler.removeEinheit(verteidigerEinheit,karte.getFeld(verteidigerEinheit.getPosX(),verteidigerEinheit.getPosY()));				

		// Bewegungspunkte beim Angreifer auf 0 setzen
		angreiferEinheit.getDaten().setInt("bewegungAktuell",0);
	}
}
