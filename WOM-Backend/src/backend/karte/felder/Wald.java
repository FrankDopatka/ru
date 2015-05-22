package backend.karte.felder;

import backend.karte.Feld;

public class Wald extends Feld {
	
	public Wald(){
	}	
	public Wald(int idKarte,int x,int y) {
		super(idKarte,x,y,"Wald");
	}

	@Override
	public void setErlaubteRessourcenArt(){
		addErlaubteRessourcenArt("Faerbemittel");
		addErlaubteRessourcenArt("Gewuertz");
		addErlaubteRessourcenArt("Gummi");
		addErlaubteRessourcenArt("Pelz");
		addErlaubteRessourcenArt("Seide");
		addErlaubteRessourcenArt("Wild");
		addErlaubteRessourcenArt("Uran");
	}
}
