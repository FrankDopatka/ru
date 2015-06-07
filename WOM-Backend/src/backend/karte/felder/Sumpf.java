package backend.karte.felder;

import backend.karte.Feld;

public class Sumpf extends Feld {
	
	public Sumpf(){
		init();
	}	
	public Sumpf(int idKarte,int x,int y) {
		super(idKarte,x,y,"Sumpf");
		init();
	}
	
	public void init(){
		setBewegungspunkte(100);
	}
	
	@Override
	public void setErlaubteRessourcenArt(){
		addErlaubteRessourcenArt("Oel");
		addErlaubteRessourcenArt("Gummi");
		addErlaubteRessourcenArt("Wild");
	}
}
