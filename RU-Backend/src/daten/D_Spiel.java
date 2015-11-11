package daten;

public class D_Spiel extends D{

	public D_Spiel(){
		addInt("id",0);
		addInt("spielerMax",4); // 0: beliebig viele
		addInt("kartenMax",1); // 0: beliebig viele
		addBool("istGestartet",false);
		addInt("spielerAnzahl",0);
		addInt("kartenAnzahl",0);
		addInt("aktuelleRunde",0);
		addInt("spielerAmZug",0);
		addInt("counterEinheit",0);
	}
}