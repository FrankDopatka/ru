package frontend.menu;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JOptionPane;


public class MenuEingabe {
	private JOptionPane eingabeDialog;
	private String titel;
	private Component parent;
	
	public MenuEingabe(Component parent,String titel,ArrayList<String> felderBeschriftung,ArrayList<Object> felderEingabe){
		this.titel=titel;
		this.parent=parent;
		Object[] eingaben=new Object[felderBeschriftung.size()*2];
		for (int i=0;i<felderBeschriftung.size()*2;i+=2){
			eingaben[i]=felderBeschriftung.get(i/2);
			eingaben[i+1]=felderEingabe.get(i/2);
		}
		eingabeDialog=new JOptionPane(eingaben,JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
	}
	
	public boolean start(){
		eingabeDialog.createDialog(parent,titel).setVisible(true);			
		return (eingabeDialog.getValue()!=null)&&(eingabeDialog.getValue().equals(JOptionPane.OK_OPTION));
	}
}
