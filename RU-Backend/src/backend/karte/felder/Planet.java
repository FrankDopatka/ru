package backend.karte.felder;

import backend.karte.Feld;

public class Planet extends Feld {
	
	public Planet(){
	}	
	public Planet(int idKarte,int x,int y) {
		super(idKarte,x,y,"Planet");
	}

	@Override
	public void setErlaubteRessourcenArt(){
	}
}
