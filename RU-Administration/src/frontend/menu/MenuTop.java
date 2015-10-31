package frontend.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import frontend.Frontend;

public class MenuTop extends JMenuBar implements ActionListener{
	private static final long serialVersionUID = 1L;
	private Frontend frontend;
	private FunktionenAdmin funktionenAdmin;
	private FunktionenKarteneditor funktionenKarteneditor;
  private JMenu server;
  private JMenu karte;
  private JMenu hilfe;
  private TreeMap<String,JMenuItem> menus=new TreeMap<String,JMenuItem>();
  
  public MenuTop(Frontend frontend){
  	this.frontend=frontend;
  	this.funktionenAdmin=frontend.getFunktionenAdmin();
  	this.funktionenKarteneditor=frontend.getFunktionenKarteneditor();
  	server=new JMenu("Server");
    add(server);
    addMenuItem("spiel-neu","neues Spiel auf dem Server erzeugen",server);
    addMenuItem("spiel-karte-neu","dem Spiel eine Karte hinzufuegen",server);
    addMenuItem("spiel-spieler-neu","dem Spiel einen Spieler hinzufuegen",server);
    addMenuItem("spiel-starten","neues Spiel auf dem Server starten",server);
    addMenuItem("spiel-laden","Spiel serverseitig laden",server);
    addMenuItem("spiel-speichern","Spiel serverseitig speichern",server);
  	karte=new JMenu("Karte");
    add(karte);
    addMenuItem("karte-neu","neue Karte erzeugen",karte);
    addMenuItem("karte-generieren","neue Welt generieren",karte);
    addMenuItem("karte-laden","Karte laden",karte);
    addMenuItem("karte-speichern","Karte speichern",karte);
    addMenuItem("beenden","Programm beenden",karte);
    hilfe=new JMenu("Hilfe");
    add(hilfe);
    addMenuItem("hilfe-ueber","Ueber",hilfe);
  }
  
  private void addMenuItem(String name,String titel,JMenu hauptmenu){
  	JMenuItem it=new JMenuItem(titel);
    it.addActionListener(this);
    hauptmenu.add(it);
    menus.put(name,it);
  }

	@Override
	public void actionPerformed(ActionEvent ev) {
		Object o=ev.getSource();
		
		if (o==menus.get("spiel-neu")) funktionenAdmin.neuesSpiel();
		if (o==menus.get("spiel-karte-neu")) funktionenAdmin.hinzufuegenKarte();
		if (o==menus.get("spiel-spieler-neu")) funktionenAdmin.hinzufuegenSpieler();
		if (o==menus.get("spiel-starten")) funktionenAdmin.startenSpiel();
		if (o==menus.get("spiel-laden")) funktionenAdmin.ladenSpiel();
		if (o==menus.get("spiel-speichern")) funktionenAdmin.speichernSpiel();
		
		if (o==menus.get("karte-neu")) funktionenKarteneditor.neueKarte();
		if (o==menus.get("karte-generieren")){
			// TODO Kartengenerator einbauen
		}
		if (o==menus.get("karte-laden")) funktionenKarteneditor.ladenKarte();
		if (o==menus.get("karte-speichern")) funktionenKarteneditor.speichernKarte();

    if (o==menus.get("hilfe-ueber")){
    	JOptionPane.showMessageDialog(
    			frontend,frontend.getTitle()+"\n\n"+"Copyright by Prof. Dr. Frank Dopatka",
    			"Ueber Rising Universe",JOptionPane.INFORMATION_MESSAGE);
    }
		if (o==menus.get("beenden")) System.exit(0);
	}
}
