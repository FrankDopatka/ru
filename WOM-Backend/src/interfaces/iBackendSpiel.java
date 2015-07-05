package interfaces;

public interface iBackendSpiel {
	String getAlleSpieler();
	String beendenRunde(int idSpieler);

	String getKarte(int idKarte,int idSpieler);
	String getKartenUmgebung(int idKarte,int idSpieler,int x,int y,int reichweite);
	String getKartenDaten(int idKarte);
	String getFeldDaten(int idKarte,int x,int y);
	String getEinheitDaten(int idKarte,int x,int y);
	String getStadtDaten(int idKarte,int x,int y);
	String getSpielerDaten(int idSpieler);
	
	String getProduzierbareEinheiten(int idSpieler,int idStadt);
	
	String bewegeEinheit(int idSpieler,int idKarte,int x,int y,int richtung);
	String gruendeStadt(int idSpieler,int idKarte,int x,int y,String name);
	String update(int idSpieler,int idKarte);
}
