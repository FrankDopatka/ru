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
		do{
			try{
				Thread.sleep(timer*1000);
				ArrayList<D> daten=Xml.toArray((frontend.getBackend().update(frontend.getIdSpieler(),idKarte)));
				if ((daten==null)||(daten.size()==0)||((daten.size()==1)&&(daten.get(0) instanceof D_Fehler))) continue;
				if ((daten.size()==1)&&(daten.get(0) instanceof D_OK)) continue;
				int i=0;
				while (i<daten.size()-1){
					ArrayList<D> datensatz=new ArrayList<D>();
					D d=null;
					d=daten.get(i);
					datensatz.add(d);
					int x=d.getInt("x");
					int y=d.getInt("y");
					if (i<daten.size()-1) i++;
					d=daten.get(i);
					if (d instanceof D_Einheit){
						datensatz.add(d);
						if (i<daten.size()-1) i++;
					}
					d=daten.get(i);
					if (d instanceof D_Stadt){
						datensatz.add(d);
						if (i<daten.size()-1) i++;
					}
					frontend.getKarte().updateFeld(x,y,datensatz);
				}
			}
			catch (Exception e){
				e.printStackTrace();
			}
		} while (!terminate);			
	}

	public void terminate(){
		terminate=true;
	}
}
