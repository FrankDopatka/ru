package backend;

import java.util.ArrayList;

import daten.D_Position;
import backend.karte.Feld;
import backend.karte.Karte;
import backend.spiel.Einheit;
import backend.spiel.Spiel;
import backend.spiel.Spiel.Bewegungsrichtung;

public class Regelwerk {
	public static final ArrayList<String> landeinheiten=new ArrayList<String>();
	public static final ArrayList<String> wasserfelder=new ArrayList<String>();
	public Spiel spiel=null;
	
	static{
		landeinheiten.add("Siedler");
		landeinheiten.add("Krieger");
		wasserfelder.add("Meer");
		wasserfelder.add("Kueste");
	}
	
	public Regelwerk(Spiel spiel){
		this.spiel=spiel;
	}

	public boolean istLandeinheit(Einheit einheit){
		String art=einheit.getDaten().getString("einheitArt");
		return landeinheiten.contains(art);
	}

	public boolean istWasserfeld(Feld feld){
		String art=feld.getDaten().getString("feldArt");
		return wasserfelder.contains(art);
	}
	
	public D_Position bewegeEinheit(int idSpieler, int idKarte, int x, int y, int richtung) {
		Karte karte=spiel.getKarte(idKarte);
		Feld feldAlt=karte.getFeld(x,y);
		Einheit einheit=feldAlt.getEinheit();
		int neuX=x;
		int neuY=y;
		switch (Bewegungsrichtung.fromOrdinal(richtung)){
		case NORD:
			neuY--;
			break;
		case NORDOST:
			neuX++;
			neuY--;
			break;
		case OST:
			neuX++;
			break;
		case SUEDOST:
			neuX++;
			neuY++;
			break;
		case SUED:
			neuY++;
			break;
		case SUEDWEST:
			neuX--;
			neuY++;
			break;
		case WEST:
			neuX--;
			break;
		case NORDWEST:
			neuX--;
			neuY--;
			break;
		}
		if((neuY<1)||(neuY>karte.getGroesseY()))
			throw new RuntimeException("Man kann den Kartenrand nicht verlassen!");
		if (spiel.getSpielerAmZug()!=idSpieler)
			throw new RuntimeException("Sie sind nicht am Zug!");
		if (neuX<1) neuX=karte.getGroesseX();
		if (neuX>karte.getGroesseX()) neuX=1;
		Feld feldNeu=karte.getFeld(neuX,neuY);
		
		if (istLandeinheit(einheit)&&istWasserfeld(feldNeu))
			throw new RuntimeException("Landeinheiten koennen nicht auf dem Wasser bewegt werden!");

			
		
		
		feldAlt.setEinheit(null);
		feldNeu.setEinheit(einheit);
		D_Position posNeu=new D_Position();
		posNeu.setInt("x",neuX);
		posNeu.setInt("y",neuY);
		karte.setUpdate(feldAlt.toDatenArray(),einheit.getIdSpieler());
		karte.setUpdate(feldNeu.toDatenArray(),einheit.getIdSpieler());
		return posNeu;
	}
	
}
