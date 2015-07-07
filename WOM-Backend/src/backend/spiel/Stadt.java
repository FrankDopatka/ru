package backend.spiel;

import java.util.ArrayList;

import backend.Parameter;
import backend.karte.Feld;
import daten.D;
import daten.D_Stadt;
import daten.Xml;

public class Stadt {
	private Spiel spiel;
	private D_Stadt d_stadt=new D_Stadt();
	
	public Stadt(Spiel spiel){
		this.spiel=spiel;
	}

	public Stadt(Spiel spiel,D_Stadt daten){
		this(spiel);
		d_stadt=daten;
	}

	public D_Stadt getDaten(){
		return d_stadt;
	}
	
	public Object toXml() {
		StringBuffer xml=new StringBuffer();
		xml.append(Xml.fromD(d_stadt));
		return xml;
	}

	public void setProduktion(String zuProduzieren) {
		try{
			if (spiel.getRegelwerk().istEinheit(zuProduzieren)){
				d_stadt.setString("produziere",zuProduzieren);
				d_stadt.setInt("bereitsProduziert",0);
				@SuppressWarnings("unchecked")
				Class<Einheit> c=(Class<Einheit>)Class.forName(Parameter.pfadKlassenEinheiten+zuProduzieren);
				Einheit einheit=(Einheit)c.newInstance();
				d_stadt.setInt("kostenProduktion",einheit.getProduktionskosten());
			}
			else
				throw new RuntimeException("Stadtproduktion von "+zuProduzieren+" ist nicht erlaubt!");			
		}
		catch (Exception e){
			throw new RuntimeException(e.getMessage());			
		}		
	}

	public void updateProduktion() {
		String produziere=d_stadt.getString("produziere");
		if ((produziere!=null)&&(produziere.length()>1)){
			int wert=d_stadt.getInt("bereitsProduziert");
			wert+=d_stadt.getInt("proRundeProduktion");
			d_stadt.setInt("bereitsProduziert",wert);
			if (wert>=d_stadt.getInt("kostenProduktion")){
				if (spiel.getRegelwerk().istEinheit(produziere)){
					int idSpieler=d_stadt.getInt("idSpieler");
					Feld feld=spiel.getKarte(d_stadt.getInt("idKarte")).getFeld(d_stadt.getInt("x"),d_stadt.getInt("y"));
					Einheit einheit=spiel.getSpieler(idSpieler).addEinheit(produziere, feld);
					ArrayList<D> daten=new ArrayList<D>();
					daten.add(feld.getDaten());
					daten.add(this.getDaten());
					daten.add(einheit.getDaten());
					spiel.setUpdate(daten,0);
				}
				else{

					// TODO Stadtverbesserung abschliessen
					
				}
				d_stadt.setString("produziere","");
				d_stadt.setInt("bereitsProduziert",0);
				d_stadt.setInt("kostenProduktion",0);
			}
		}
	}
}
