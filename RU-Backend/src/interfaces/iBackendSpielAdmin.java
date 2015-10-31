package interfaces;

public interface iBackendSpielAdmin {
	String neuesSpiel(int id,int anzahlSpieler,int anzahlKarten);
	String hinzufuegenKarte(String pfad);
	String getRassen();
	String getNationsArten(String rasse);
	String hinzufuegenSpieler(int idSpieler,String name,String rasse,String nation);
	String starteSpiel();
	String speichernSpiel(String pfad);
	String ladenSpiel(String pfad);
}
