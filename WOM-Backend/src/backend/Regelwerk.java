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
	
	public D_Position bewegeEinheit(int idSpieler,int idKarte,int xAlt,int yAlt,int richtung) {
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
		
		if (istLandeinheit(einheit)&&istWasserfeld(feldNeu))
			throw new RuntimeException("Landeinheiten koennen nicht auf dem Wasser bewegt werden!");

		Einheit einheitFeldNeu=feldNeu.getEinheit();
		if (einheitFeldNeu!=null){ // AUF DEM ZIELFELD IST SCHON EINE EINHEIT
			if (einheitFeldNeu.getIdSpieler()==idSpieler)
				throw new RuntimeException("Auf dem Zielfeld der Einheit steht bereits eine Einheit von Ihnen!");
			if (einheit.getDaten().getInt("angriffAktuell")<=0)
				throw new RuntimeException("Ihre Einheit kann nicht angreifen!");
			
			//TODO Kampf
				
		}
		
		feldAlt.setEinheit(null);
		feldNeu.setEinheit(einheit);
		D_Position posNeu=new D_Position();
		posNeu.setInt("x",xNeu);
		posNeu.setInt("y",yNeu);
		karte.setUpdate(feldAlt.toDatenArray(),einheit.getIdSpieler());
		karte.setUpdate(feldNeu.toDatenArray(),einheit.getIdSpieler());
		return posNeu;
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
			xNeu++;
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
		}
		return new int[]{xNeu,yNeu};
	}
}
