package backend.spiel.einheiten;

import backend.spiel.Einheit;

public class Krieger extends Einheit {
	public Krieger(){
		getDaten().setString("name","Krieger");

		getDaten().setInt("bewegungMaximal",100);
		getDaten().setInt("bewegungAktuell",100);
		getDaten().setInt("lebenMaximal",10);
		getDaten().setInt("lebenAktuell",10);
		getDaten().setInt("angriffMaximal",8);
		getDaten().setInt("angriffAktuell",8);
		getDaten().setInt("verteidigungMaximal",3);
		getDaten().setInt("verteidigungAktuell",3);
		
		getDaten().setInt("kostenProduktion",100);
	}
	
	public Krieger(int idSpieler){
		this();
		setSpieler(idSpieler);
	}
}
