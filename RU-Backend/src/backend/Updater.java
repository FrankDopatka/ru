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
	@SuppressWarnings("unchecked")
	public ArrayList<D> get(int vonIdSpieler){
		if (listen[vonIdSpieler]==null) return null;
		if (listen[vonIdSpieler].size()==0) return null;
		ArrayList<D> daten=new ArrayList<D>();
//		D datenwert;
//		while ((datenwert=(D)listen[vonIdSpieler].poll())!=null) daten.add(datenwert);
		listen[vonIdSpieler].drainTo(daten);
		return daten;
	}
	
	// Zuruecksetzen, z.B. beim Holen einer Karte
	public void reset(int vonIdSpieler){
		if (listen[vonIdSpieler]==null) return;
		if (listen[vonIdSpieler].size()==0) return;
		listen[vonIdSpieler].clear();
	}
	
	// vonSpielerId: der Spieler, der das Update selbst gesetzt hat, wird manuell aktualisiert
	// -> in dessen Liste muss das Update nicht reingeschrieben werden
	@SuppressWarnings("unchecked")
	public void putFelddaten(ArrayList<D> felddaten,int idSpieler){
		for(int i=1;i<=99;i++){
			if ((listen[i]!=null)&&(i!=idSpieler)) listen[i].addAll(felddaten);
		}
	}

	// Updates des Spiels selbst gehen immer an alle Spieler
	@SuppressWarnings("unchecked")
	public void putSpieldaten(D_Spiel spieldaten){
		for(int i=1;i<=99;i++){
			if (listen[i]!=null) listen[i].add(spieldaten);
		}
	}
}
