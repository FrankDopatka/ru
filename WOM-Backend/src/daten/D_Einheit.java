package daten;

public class D_Einheit extends D {

	public D_Einheit(){
		addInt("id",0);
		addInt("idKarte",0);
		addInt("x",0);
		addInt("y",0);
		addInt("idSpieler",0);
		addString("art",""); // gleich der Name der Klasse
		addString("name",""); // gleich dem angezeigten Namen im Spiel

		addInt("bewegungMaximal",100);
		addInt("bewegungAktuell",100);
		addInt("lebenMaximal",1);
		addInt("lebenAktuell",1);
		addInt("angriffMaximal",1);
		addInt("angriffAktuell",1);
		addInt("verteidigungMaximal",1);
		addInt("verteidigungAktuell",1);
		
		addInt("kostenProduktion",0);
		addInt("kostenProduktionFertig",0);
		
		addBool("istLandeinheit",true);
	}
}