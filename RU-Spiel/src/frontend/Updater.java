package frontend;

import java.util.ArrayList;

import daten.*;

public class Updater extends Thread{
	private Frontend frontend;
	private boolean terminate=false;
	private int idKarte;
	private int timer;
	
	public Updater(Frontend frontend,int idKarte,int timer){
		this.frontend=frontend;
		this.idKarte=idKarte;
		this.timer=timer;
		this.start();
	}
	
	@Override
	public void run(){
		Karte karte=frontend.getKarte();
		do{
			try{
				Thread.sleep(timer*1000);
				ArrayList<D> daten=Xml.toArray((frontend.getBackend().update(frontend.getIdSpieler(),idKarte)));
				if ((daten==null)||(daten.size()==0)||((daten.size()==1)&&(daten.get(0) instanceof D_Fehler))) continue;
				if ((daten.size()==1)&&(daten.get(0) instanceof D_OK)) continue;
				int i=0;
				while (i<daten.size()){
					D d=daten.get(i);
					if (d instanceof D_Feld) {
						int x=d.getInt("x"); int y=d.getInt("y");
						karte.updateFeldBasis(x,y,(D_Feld)d);
					}
					if (d instanceof D_Einheit){
						int x=d.getInt("x"); int y=d.getInt("y");
						karte.updateFeldEinheit(x,y,(D_Einheit)d);
					}
					if (d instanceof D_Stadt){
						int x=d.getInt("x"); int y=d.getInt("y");
						karte.updateFeldStadt(x,y,(D_Stadt)d);
					}
					if (d instanceof D_Spiel){
						frontend.log("neue Spieldaten erhalten... Runde: "+d.getString("aktuelleRunde")+", Spieler am Zug: "+d.getString("spielerAmZug"));
					}
					i++;
					Thread.sleep(100);
				}
			}
			catch (Exception e){
				System.err.println("Fehler im Updater: "+e.getMessage());
			}
		} while (!terminate);
	}

	public void terminate(){
		terminate=true;
	}
}
