package daten;

public class D_Feld extends D {

	public D_Feld(){
		addInt("id",0);
		addInt("idKarte",0);
		addInt("x",0);
		addInt("y",0);
		addString("feldArt","");
		
		addInt("spielerstart",0); // 0: von hier startet kein Spieler, >0: von hier startet der Spieler mit dieser ID
		addString("ressource","");
	}
}