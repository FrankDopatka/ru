package frontend;

import frontend.menu.MenuRechts;
import frontend.menu.MenuTop;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import daten.D;
import daten.D_Karte;
import daten.Xml;
import backend.BackendKarteneditorStub;
import backend.iBackendKarteneditor;

public class Frontend extends JFrame{
	private static final long serialVersionUID = 1L;
	private iBackendKarteneditor backend;
	private MenuTop menuTop;
	private MenuRechts menuRechts=null;
	private JPanel panel=new JPanel();
	private JTextArea ta=new JTextArea(6,20);
	private JTextField st=new JTextField("");
	private JScrollPane scrollerKarte;
	private Karte karte;
	private int spielfeldGroesse=50;
	private int zoomfaktor=100;
	
	public Frontend(String url){
		super();
		backend=new BackendKarteneditorStub(url);
		setTitle("WOM Karteneditor, Version 0.80 REST");
		setLayout(new BorderLayout());
		panel.setLayout(new BorderLayout());
		menuTop=new MenuTop(this);
		// Menu:
		panel.add(menuTop,BorderLayout.NORTH);
    // Logger:
    JPanel logger=new JPanel();
    logger.setLayout(new BorderLayout());
		ta.setFont(new Font("Arial", Font.PLAIN, 11));
		ta.setOpaque(true);
		ta.setEditable(false);
		logger.add(new JScrollPane(ta),BorderLayout.CENTER);
		logger.add(st,BorderLayout.SOUTH);
		panel.add(logger,BorderLayout.SOUTH);
		// Fenster:
		add(panel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800,600);
		setVisible(true);
	}
	
	public void log(String text){
		if (ta.getText().length()==0)
			ta.setText(" "+text);
		else
			ta.setText(ta.getText()+"\n"+" "+text);
	}
	
	public void setStatus(String text){
		st.setText(" "+text);
	}

	public iBackendKarteneditor getBackend() {
		return backend;
	}

	public Karte neueKarte(String backendDaten){
		if (this.karte!=null){
			karte.terminate();
			panel.remove(scrollerKarte);
		}
		ArrayList<D> daten=Xml.toArray(backendDaten);
		int i;
		for (i=0;i<daten.size();i++){
			if (daten.get(i) instanceof D_Karte) break;
		}
		D_Karte kartenDaten=(D_Karte)daten.get(0);
		int x=kartenDaten.getInt("x");
		int y=kartenDaten.getInt("y");
		this.karte=new Karte(this,x,y);
		zeichneFelder(daten);
		scrollerKarte=new JScrollPane(karte,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		panel.add(scrollerKarte,BorderLayout.CENTER);
		panel.revalidate();
		panel.repaint();
		return karte;
	}

	public void zeichneFelder(ArrayList<D> daten) {
		if (daten==null){
			String backendDaten=backend.getKarte();
			daten=Xml.toArray(backendDaten);
		}	
		if (karte!=null) karte.zeichneFelder(daten);
	}

	public void zeichneFeld(int[] pos) {
		if (karte!=null) karte.zeichneFeld(pos);
	}
	
	public void setMenuRechts(MenuRechts mr){
		if (menuRechts!=null) panel.remove(menuRechts);
		menuRechts=mr;
		menuRechts.setEnabled(true);
		panel.add(menuRechts,BorderLayout.EAST);
		panel.revalidate();
		panel.repaint();
	}
	
	public MenuRechts getMenuRechts(){
		return menuRechts;
	}
	
	public Karte getKarte(){
		return karte;
	}
	
	public void setZoomfaktor(int x){
		zoomfaktor=x;
	}
	
	public int getZoomfaktor(){
		return zoomfaktor;
	}
	
	public void setSpielfeldGroesse(int x){
		spielfeldGroesse=x;
	}
	
	public int getSpielfeldGroesse(){
		return spielfeldGroesse;
	}
}
