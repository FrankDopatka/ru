package backend;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import interfaces.iBackendSpiel;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import daten.D;
import daten.Xml;

public class BackendSpielStub implements iBackendSpiel{
	private static final boolean log=true;
	private String url;
	private Client client=ClientBuilder.newClient();
	
	public BackendSpielStub(String url){
		if (url.endsWith("/"))
			this.url=url+"ru/spiel/";
		else
			this.url=url+"/ru/spiel/";
	}
	
	private String getXmlvonRest(String pfad){
		String anfrage=url+pfad;
		if ((log)&&(!anfrage.contains("/update/"))) System.out.println("CLIENT ANFRAGE: "+anfrage);
		String s=client.target(anfrage).request().accept("application/xml").get(String.class);
		if ((log)&&(!anfrage.contains("/update/"))){
			ArrayList<D> daten=Xml.toArray(s);
			System.out.println(daten);
		}
		return s;
	}
	
	@Override
	public String getAlleSpieler() {
		return getXmlvonRest("getAlleSpieler");
	}

	@Override
	public String beendenRunde(int idSpieler) {
		return getXmlvonRest("beendenRunde"+"/"+idSpieler);
	}

	@Override
	public String getKarte(int idKarte,int idSpieler) {
		return getXmlvonRest("getKarte"+"/"+idKarte+"/"+idSpieler);
	}

	@Override
	public String getKartenUmgebung(int idKarte,int idSpieler,int x,int y,int reichweite) {
		return getXmlvonRest("getKartenUmgebung"+"/"+idKarte+"/"+idSpieler+"/"+x+"/"+y+"/"+reichweite);
	}

	@Override
	public String getKartenDaten(int idKarte) {
		return getXmlvonRest("getKartenDaten"+"/"+idKarte);
	}

	@Override
	public String getFeldDaten(int idKarte, int x, int y) {
		return getXmlvonRest("getFeldDaten"+"/"+idKarte+"/"+x+"/"+y);
	}
	
	@Override
	public String getEinheitDaten(int idKarte, int x, int y) {
		return getXmlvonRest("getEinheitDaten"+"/"+idKarte+"/"+x+"/"+y);
	}

	@Override
	public String getStadtDaten(int idKarte, int x, int y) {
		return getXmlvonRest("getStadtDaten"+"/"+idKarte+"/"+x+"/"+y);
	}

	@Override
	public String getSpielerDaten(int idSpieler) {
		return getXmlvonRest("getSpielerDaten"+"/"+idSpieler);
	}
	
	@Override
	public String getProduzierbareEinheiten(int idSpieler, int idStadt) {
		return getXmlvonRest("getProduzierbareEinheiten"+"/"+idSpieler+"/"+idStadt);
	}
	
	@Override
	public String bewegeEinheit(int idSpieler, int idKarte, int x, int y, int richtung) {
		return getXmlvonRest("bewegeEinheit"+"/"+idSpieler+"/"+idKarte+"/"+x+"/"+y+"/"+richtung);
	}
	
	@Override
	public String gruendeStadt(int idSpieler, int idKarte, int x, int y, String name) {
		try {
			return getXmlvonRest("gruendeStadt"+"/"+idSpieler+"/"+idKarte+"/"+x+"/"+y+"/"+URLEncoder.encode(""+name,"ISO-8859-1"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	@Override
	public String update(int idSpieler,int idKarte) {
		return getXmlvonRest("update"+"/"+idSpieler+"/"+idKarte);
	}

	
	
	
	
	@Override
	public String produziere(int idSpieler,int idStadt,String zuProduzieren) {
		try {
			return getXmlvonRest("produziere"+"/"+idSpieler+"/"+idStadt+"/"+URLEncoder.encode(""+zuProduzieren,"ISO-8859-1"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	@Override
	public String getAngriffsRadius(int idSpieler,int idKarte,int x,int y) {
		return getXmlvonRest("getAngriffsRadius"+"/"+idSpieler+"/"+idKarte+"/"+x+"/"+y);
	}

	@Override
	public String fernangriff(int idSpieler,int idKarte,int xAngreifer,int yAngreifer,int xVerteidiger,int yVerteidiger) {
		return getXmlvonRest("fernangriff"+"/"+idSpieler+"/"+idKarte+"/"+xAngreifer+"/"+yAngreifer+"/"+xVerteidiger+"/"+yVerteidiger);
	}

	@Override
	public String getSpieldaten() {
		return getXmlvonRest("getSpieldaten");
	}
}
