package backend.karte.felder;

import backend.karte.Feld;

public class Meer extends Feld {
	
	public Meer(){
	}	
	public Meer(int idKarte,int x,int y) {
		super(idKarte,x,y,"Meer");
	}

	@Override
	public void setErlaubteRessourcenArt(){
		addErlaubteRessourcenArt("Fisch");
		addErlaubteRessourcenArt("Oel");
		addErlaubteRessourcenArt("Wal");
	}
}
