package daten;

public class D_Stadt extends D{
	
	public D_Stadt(){
		addInt("id",0);
		addInt("idKarte",0);
		addInt("x",0);
		addInt("y",0);
		addInt("idSpieler",0);
		addString("name","");
		
		addString("produziere","");
		addInt("bereitsProduziert",0);
		addInt("kostenProduktion",0);
		
		addInt("groesse",1);
		addInt("proRundeWissenschaft",10);
		addInt("proRundeProduktion",50);
		addInt("proRundeGeld",10);
		addInt("proRundeNahrung",10);
	}
}
