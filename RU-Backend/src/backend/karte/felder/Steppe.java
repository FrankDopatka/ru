package backend.karte.felder;

import backend.karte.Feld;

public class Steppe extends Feld {
	
	public Steppe(){
		init();
	}	
	public Steppe(int idKarte,int x,int y) {
		super(idKarte,x,y,"Steppe");
		init();
	}
	
	public void init(){
		setBewegungspunkte(50);
	}
	
	@Override
	public void setErlaubteRessourcenArt(){
		addErlaubteRessourcenArt("Elfenbein");
		addErlaubteRessourcenArt("Pferde");
		addErlaubteRessourcenArt("Rinder");
		addErlaubteRessourcenArt("Wein");
		addErlaubteRessourcenArt("Weizen");
		addErlaubteRessourcenArt("Zucker");
	}
}
