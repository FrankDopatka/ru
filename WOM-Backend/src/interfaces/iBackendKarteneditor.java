package interfaces;

public interface iBackendKarteneditor {
	String neueKarte(int id,int x,int y,String kartenArt,String feldArt);
	String ladenKarte(String pfad);
	String speichernKarte(String pfad);
	String getKarte();
	
	String getKartenArten();
	String getErlaubteFeldArten(String kartenArt);
	String getErlaubteRessourcenArten(String feldArt);
	String getKartenDaten();
	String getFeldDaten(int x,int y);
	
	String setFeldArt(int x,int y,String feldArtNeu);
	String setSpielerstart(int x,int y,int spielerstart);
	String getSpielerstart(int spielerstart);
	String setRessource(int x,int y,String ressorce);
	String delRessource(int x,int y);
}
