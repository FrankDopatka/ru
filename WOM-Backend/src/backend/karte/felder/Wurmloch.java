package backend.karte.felder;

import backend.karte.Feld;

public class Wurmloch extends Feld {
	
	public Wurmloch(){
	}	
	public Wurmloch(int idKarte,int x,int y) {
		super(idKarte,x,y,"Wurmloch");
		getDaten().addInt("xZiel",0); // X-Koordinate des Zielfeldes im Weltraum
		getDaten().addInt("yZiel",0); // y-Koordinate des Zielfeldes im Weltraum
	}

	@Override
	public void setErlaubteRessourcenArt(){
	}
}
