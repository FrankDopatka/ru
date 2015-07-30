package backend.spiel.einheiten;

import backend.spiel.Einheit;

public class Schwertkaempfer extends Einheit {
	public Schwertkaempfer(){
		getDaten().setString("name","Schwertkaempfer");

		getDaten().setInt("bewegungMaximal",100);
		getDaten().setInt("bewegungAktuell",100);
		getDaten().setInt("lebenMaximal",12);
		getDaten().setInt("lebenAktuell",12);
		getDaten().setInt("angriffMaximal",15);
		getDaten().setInt("angriffAktuell",15);
		getDaten().setInt("verteidigungMaximal",6);
		getDaten().setInt("verteidigungAktuell",6);
		
		getDaten().setInt("kostenProduktion",200);
	}
	
	public Schwertkaempfer(int idSpieler){
		this();
		setSpieler(idSpieler);
	}
}
