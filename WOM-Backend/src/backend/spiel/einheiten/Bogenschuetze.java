package backend.spiel.einheiten;

import backend.spiel.Einheit;

public class Bogenschuetze extends Einheit {
	public Bogenschuetze(){
		getDaten().setString("name","Bogenschuetze");

		getDaten().setInt("bewegungMaximal",100);
		getDaten().setInt("bewegungAktuell",100);
		getDaten().setInt("lebenMaximal",12);
		getDaten().setInt("lebenAktuell",12);
		getDaten().setInt("angriffMaximal",10);
		getDaten().setInt("angriffAktuell",10);
		getDaten().setInt("verteidigungMaximal",3);
		getDaten().setInt("verteidigungAktuell",3);
		
		getDaten().setInt("kostenProduktion",200);
		
		getDaten().setBool("istFernkampfeinheit",true);
		getDaten().setInt("reichweiteFernkampf",2);
	}
	
	public Bogenschuetze(int idSpieler){
		this();
		setSpieler(idSpieler);
	}
}
