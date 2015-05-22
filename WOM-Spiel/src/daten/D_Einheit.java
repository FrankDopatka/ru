package daten;

public class D_Einheit extends D {

	public D_Einheit(){
		addInt("id",0);
		addInt("idKarte",0);
		addInt("x",0);
		addInt("y",0);
		addInt("idSpieler",0);
		addString("einheitArt","Krieger"); // gleich der Name der Klasse
		addString("einheitName","Krieger"); // gleich dem angezeigten Namen im Spiel

		addInt("bewegungMaximal",1);
		addInt("bewegungAktuell",1);
		addInt("lebenMaximal",1);
		addInt("lebenAktuell",1);
		addInt("angriffMaximal",1);
		addInt("angriffAktuell",1);
		addInt("verteidigungMaximal",1);
		addInt("verteidigungAktuell",1);
	}
}