package backend.karte.felder;

import backend.karte.Feld;

public class Eis extends Feld {
	
	public Eis(){
		init();
	}	
	public Eis(int idKarte,int x,int y) {
		super(idKarte,x,y,"Eis");
		init();
	}
	
	public void init(){
		setBewegungspunkte(50);
	}

	@Override
	public void setErlaubteRessourcenArt(){
		addErlaubteRessourcenArt("Aluminium");
		addErlaubteRessourcenArt("Oel");
		addErlaubteRessourcenArt("Pelz");
		addErlaubteRessourcenArt("Wild");
	}
}