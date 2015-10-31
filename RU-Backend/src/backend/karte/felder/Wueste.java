package backend.karte.felder;

import backend.karte.Feld;

public class Wueste extends Feld {
	
	public Wueste(){
	}	
	public Wueste(int idKarte,int x,int y) {
		super(idKarte,x,y,"Wueste");
	}
	
	public void init(){
		setBewegungspunkte(50);
	}
	
	@Override
	public void setErlaubteRessourcenArt(){
		addErlaubteRessourcenArt("Oel");
		addErlaubteRessourcenArt("Oase");
		addErlaubteRessourcenArt("Salpeter");
		addErlaubteRessourcenArt("Weihrauch");
	}
}
