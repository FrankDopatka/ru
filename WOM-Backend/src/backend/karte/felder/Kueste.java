package backend.karte.felder;

import backend.karte.Feld;

public class Kueste extends Feld {
	
	public Kueste(){
	}	
	public Kueste(int idKarte,int x,int y) {
		super(idKarte,x,y,"Kueste");
	}

	@Override
	public void setErlaubteRessourcenArt(){
		addErlaubteRessourcenArt("Fisch");
		addErlaubteRessourcenArt("Oel");
	}
}
