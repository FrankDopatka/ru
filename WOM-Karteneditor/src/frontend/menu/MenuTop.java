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
	private Frontend fr;
  private JMenu karte;
  private JMenu hilfe;
  private TreeMap<String,JMenuItem> menus=new TreeMap<String,JMenuItem>();
  
  public MenuTop(Frontend fr){
  	this.fr=fr;
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
		if (o==menus.get("karte-neu")) new KarteNeu(fr);
		if (o==menus.get("karte-generieren")){

			// TODO Kartengenerator einbauen

		}
		if (o==menus.get("karte-laden")) new KarteLaden(fr);
		if (o==menus.get("karte-speichern")) new KarteSpeichern(fr);

    if (o==menus.get("hilfe-ueber")){
    	JOptionPane.showMessageDialog(
    			fr,fr.getTitle()+"\n\n"+"Copyright by Prof. Dr. Frank Dopatka",
    			"Ueber WorldOfMKI",JOptionPane.INFORMATION_MESSAGE);
    }
		if (o==menus.get("beenden")) System.exit(0);
	}
}
