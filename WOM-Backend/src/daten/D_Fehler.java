package daten;

public class D_Fehler extends D {

	public D_Fehler(){
		addString("meldung","");
	}
	
	public D_Fehler(String meldung){
		addString("meldung",meldung);
	}
}