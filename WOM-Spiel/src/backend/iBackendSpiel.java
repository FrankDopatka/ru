package backend;

public interface iBackendSpiel {
	String neuesSpiel(int id,int anzahlSpieler,int anzahlKarten);
	String hinzufuegenKarte(String pfad);
	String getRassen();
	String getNationsArten(String rasse);
	String hinzufuegenSpieler(int id,String name,String rasse,String nation);
	String getAlleSpieler();
	String starteSpiel();

	String getKarte(int id);
	String getKartenArten();
	String getErlaubteFeldArten(String kartenArt);
	String getErlaubteRessourcenArten(String feldArt);
	String getKartenDaten(int id);
	String getFeldDaten(int idKarte,int x,int y);
	String getEinheitDaten(int idKarte,int x,int y);
	String getStadtDaten(int idKarte,int x,int y);
	String getSpielerDaten(int idSpieler);
	
	String bewegeEinheit(int idSpieler,int idKarte,int x,int y,int richtung);
	String gruendeStadt(int idSpieler,int idKarte,int x,int y,String name);
	
	String speichernSpiel(String pfad);
	String ladenSpiel(String pfad);
	
}