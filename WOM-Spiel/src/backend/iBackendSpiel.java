package backend;

public interface iBackendSpiel {
	String getAlleSpieler();
	String beendenRunde(int idSpieler);

	String getKarte(int id);
	String getKartenDaten(int id);
	String getFeldDaten(int idKarte,int x,int y);
	String getEinheitDaten(int idKarte,int x,int y);
	String getStadtDaten(int idKarte,int x,int y);
	String getSpielerDaten(int idSpieler);
	
	String bewegeEinheit(int idSpieler,int idKarte,int x,int y,int richtung);
	String gruendeStadt(int idSpieler,int idKarte,int x,int y,String name);
	String update(int idSpieler,int idKarte);	
}