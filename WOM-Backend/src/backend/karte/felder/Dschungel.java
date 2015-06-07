package backend.karte.felder;

import backend.karte.Feld;

public class Dschungel extends Feld {
	
	public Dschungel(){
		init();
	}	
	public Dschungel(int idKarte,int x,int y) {
		super(idKarte,x,y,"Dschungel");
		init();
	}
	
	public void init(){
		setBewegungspunkte(100);
	}
	
	@Override
	public void setErlaubteRessourcenArt(){
		addErlaubteRessourcenArt("Edelsteine");
		addErlaubteRessourcenArt("Faerbemittel");
		addErlaubteRessourcenArt("Gewuertz");
		addErlaubteRessourcenArt("Gummi");
		addErlaubteRessourcenArt("Kohle");
		addErlaubteRessourcenArt("Obst");
		addErlaubteRessourcenArt("Seide");
	}
}
