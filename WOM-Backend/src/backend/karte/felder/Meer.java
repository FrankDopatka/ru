package backend.karte.felder;

import backend.karte.Feld;

public class Meer extends Feld {
	
	public Meer(){
		init();
	}	
	public Meer(int idKarte,int x,int y) {
		super(idKarte,x,y,"Meer");
		init();
	}
	
	public void init(){
		getDaten().setBool("istWasserfeld",true);
		setBewegungspunkte(100);
	}

	@Override
	public void setErlaubteRessourcenArt(){
		addErlaubteRessourcenArt("Fisch");
		addErlaubteRessourcenArt("Oel");
		addErlaubteRessourcenArt("Wal");
	}
}
