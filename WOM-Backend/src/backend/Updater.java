package backend;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import daten.*;

public class Updater{
	@SuppressWarnings("rawtypes")
	private LinkedBlockingQueue[] listen=new LinkedBlockingQueue[100];

	public Updater(){
	}
	
	public void addSpieler(int idSpieler){
		listen[idSpieler]=new LinkedBlockingQueue<D>();
	}

	public void removeSpieler(int idSpieler){
		if (listen[idSpieler]!=null) listen[idSpieler].clear();
		listen[idSpieler]=null;
	}
	
	public boolean hatSpieler(int idSpieler){
		return listen[idSpieler]!=null;
	}
	
	// Auslesen durch den Client
	public ArrayList<D> get(int vonSpielerId){
		if (listen[vonSpielerId]==null) return null;
		if (listen[vonSpielerId].size()==0) return null;
		ArrayList<D> daten=new ArrayList<D>();
		D datenwert;
		while ((datenwert=(D)listen[vonSpielerId].poll())!=null){
			daten.add(datenwert);
		}
		return daten;
	}
	
	// vonSpielerId: der Spieler, der das Update selbst gesetzt hat, wird manuell aktualisiert
	// -> in dessen Liste muss das Update nicht reingeschrieben werden
	@SuppressWarnings("unchecked")
	public void put(ArrayList<D> felddaten,int vonSpielerId){
		for(int i=1;i<=99;i++){
			if ((listen[i]!=null)&&(i!=vonSpielerId)) listen[i].addAll(felddaten);
		}
	}
}
