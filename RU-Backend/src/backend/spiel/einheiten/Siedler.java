package backend.spiel.einheiten;

import backend.spiel.Einheit;

public class Siedler extends Einheit {
	public Siedler(){
		getDaten().setString("name","Siedler");

		getDaten().setInt("bewegungMaximal",100);
		getDaten().setInt("bewegungAktuell",100);
		getDaten().setInt("lebenMaximal",1);
		getDaten().setInt("lebenAktuell",1);
		getDaten().setInt("angriffMaximal",0);
		getDaten().setInt("angriffAktuell",0);
		getDaten().setInt("verteidigungMaximal",0);
		getDaten().setInt("verteidigungAktuell",0);
		
		getDaten().setInt("kostenProduktion",150);
	}
	
	public Siedler(int idSpieler){
		this();
		setSpieler(idSpieler);
	}
}
