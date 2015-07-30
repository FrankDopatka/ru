package backend.spiel.einheiten;

import backend.spiel.Einheit;

public class Panzer extends Einheit {
	public Panzer(){
		getDaten().setString("name","Panzer");

		getDaten().setInt("bewegungMaximal",300);
		getDaten().setInt("bewegungAktuell",300);
		getDaten().setInt("lebenMaximal",500);
		getDaten().setInt("lebenAktuell",500);
		getDaten().setInt("angriffMaximal",200);
		getDaten().setInt("angriffAktuell",200);
		getDaten().setInt("verteidigungMaximal",100);
		getDaten().setInt("verteidigungAktuell",100);
		
		getDaten().setInt("kostenProduktion",400);
	}
	
	public Panzer(int idSpieler){
		this();
		setSpieler(idSpieler);
	}
}
