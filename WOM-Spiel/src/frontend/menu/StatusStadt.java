package frontend.menu;

import interfaces.iBackendSpiel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import daten.*;
import frontend.Frontend;

public class StatusStadt extends JDialog implements ActionListener{
	private static final long serialVersionUID = 1L;
	private Frontend frontend;
	private iBackendSpiel backend;
	private D_Stadt stadt;
	private D_Einheit einheit;
	private JButton abbrechen=new JButton("Abbrechen");
	private JButton[] buttons=new JButton[10];
	private static int reichweiteUmgebung=2;
	
	public StatusStadt(Frontend frontend,D_Stadt stadt,D_Einheit einheit,D_Spieler spieler){
		super(frontend);
		this.frontend=frontend;
		backend=frontend.getBackend();
		this.stadt=stadt;
		this.einheit=einheit;
		JPanel jp=new JPanel();
		JPanel jpButtons=new JPanel();
		setLayout(new BorderLayout());
		jp.setLayout(new GridBagLayout());
		jpButtons.setLayout(new GridLayout(buttons.length,1));
		
		for (int i=0;i<buttons.length;i++){
			buttons[i]=new JButton("");
			buttons[i].addActionListener(this);
			jpButtons.add(buttons[i]);
		}
		buttons[0].setText("Produktion");			
		buttons[9].setText("Aufloesen");
		setTitle("Status der Stadt");
		abbrechen.addActionListener(this);

		int x=1;
		String produktion=stadt.getString("produziere");
		if ((produktion!=null)&&(produktion.length()>1)){
			addEintrag(x++,jp,"Produktion: ",produktion+", "+stadt.getString("bereitsProduziert")+" von "+stadt.getString("kostenProduktion"));			
		}
		if (einheit!=null){
			addEintrag(x++,jp,"","");
			addEintrag(x++,jp,"Einheit in der Stadt:","");
			addEintrag(x++,jp,"Typ: ",einheit.getString("name"));
			addEintrag(x++,jp,"Leben: ",einheit.getInt("lebenAktuell")+" von "+einheit.getInt("lebenMaximal"));
			addEintrag(x++,jp,"Angriff: ",einheit.getInt("angriffAktuell")+" von "+einheit.getInt("angriffMaximal"));
			addEintrag(x++,jp,"Verteidigung: ",einheit.getInt("verteidigungAktuell")+" von "+einheit.getInt("verteidigungMaximal"));
			addEintrag(x++,jp,"Bewegung: ",einheit.getInt("bewegungAktuell")+" von "+einheit.getInt("bewegungMaximal"));			
		}

		JPanel umgebungPanel=new JPanel();
		umgebungPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc=new GridBagConstraints();
		int xStart=stadt.getInt("x")-reichweiteUmgebung;
		int yStart=stadt.getInt("y")-reichweiteUmgebung;
		for(int i=0;i<reichweiteUmgebung*2+1;i++){
			for(int j=0;j<reichweiteUmgebung*2+1;j++){
				JLabel bild=new JLabel();
				int xAktuellKarte=xStart+i;
				int yAktuellKarte=yStart+j;
				if ((xAktuellKarte>=1)&&(xAktuellKarte<=frontend.getKarte().getGroesseX())&&
						(yAktuellKarte>=1)&&(yAktuellKarte<=frontend.getKarte().getGroesseY())){
					bild.setIcon(new ImageIcon(frontend.getKarte().getFeld(xAktuellKarte,yAktuellKarte).getBild()));
				}
				else{
					bild.setIcon(new ImageIcon(frontend.getKarte().getBildFeldLeer()));
				}
				gbc.gridx=i;
				gbc.gridy=j;
				umgebungPanel.add(bild,gbc);
			}
		}
		add(umgebungPanel,BorderLayout.WEST);
		
		if (!spieler.getString("nation").equals("null"))
			add(setLabelHeader(stadt.getString("name")+" von "+spieler.getString("name")+" aus "+spieler.getString("nation")),BorderLayout.NORTH);
		else
			add(setLabelHeader(stadt.getString("name")+" von dem "+spieler.getString("rasse")+" "+spieler.getString("name")),BorderLayout.NORTH);
		add(jp,BorderLayout.CENTER);
		add(jpButtons,BorderLayout.EAST);
		add(abbrechen,BorderLayout.SOUTH);
		
		setSize(new Dimension(700,300));
    setLocationRelativeTo(frontend);
    setModal(true);
		setVisible(true);
	}

	private void addEintrag(int zeile,JPanel jp,String kopf,String daten){
		GridBagConstraints gbc;
		gbc=new GridBagConstraints();
		gbc.gridx=0;
		gbc.gridy=zeile;
		gbc.gridwidth=20;
		gbc.gridheight=1;
		gbc.fill=GridBagConstraints.HORIZONTAL;
		jp.add(setLabel(kopf),gbc);
		gbc=new GridBagConstraints();
		gbc.gridx=21;
		gbc.gridy=zeile;
		gbc.gridwidth=20;
		gbc.gridheight=1;
		gbc.fill=GridBagConstraints.HORIZONTAL;
		jp.add(setLabel(daten),gbc);
	}

	private JLabel setLabel(String text){
		JLabel jl=new JLabel();
		jl.setFont(new Font("Arial",Font.BOLD,12));
		jl.setText(text);
		return jl;
	}

	private JLabel setLabelHeader(String text){
		JLabel jl=new JLabel();
		jl.setFont(new Font("Arial",Font.BOLD,16));
		jl.setText(text);
		return jl;
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		Object o=ev.getSource();
		if (o==buttons[0]){
			ArrayList<D> daten=Xml.toArray(backend.getProduzierbareEinheiten(stadt.getInt("idSpieler"),stadt.getInt("id")));
			final JComboBox<String> jWahlStadtproduktion=new JComboBox<String>();
			for(D produzierbareEinheit:daten){
				String name=""+produzierbareEinheit.getString("name");
				String kosten=""+produzierbareEinheit.getString("kostenProduktion");
				jWahlStadtproduktion.addItem(name+" ("+kosten+")");
	  	}
	  	Object[] eingaben={"Produziere...",jWahlStadtproduktion};
	  	JOptionPane eingabe=new JOptionPane(eingaben,JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION){
				private static final long serialVersionUID = 1L;
				@SuppressWarnings("unchecked")
				@Override
        public void selectInitialValue() {
          super.selectInitialValue();
          Object[] felder=(Object[]) this.getMessage();
          ((JComboBox<String>)felder[1]).requestFocusInWindow();
        }
	  	};
	  	eingabe.createDialog(null, "Produktion der Stadt einstellen...").setVisible(true);
	  	if ((eingabe.getValue()!=null)&&(eingabe.getValue().equals(JOptionPane.OK_OPTION))){
	  		String wahl=""+jWahlStadtproduktion.getSelectedItem();
	  		wahl=wahl.split(" ")[0];
	  		backend.produziere(stadt.getInt("idSpieler"),stadt.getInt("id"),wahl);	  		
	  		this.setVisible(false);
				this.dispose();
	  	}
		}
		if (o==abbrechen){
			this.setVisible(false);
			this.dispose();
		}
	}
}
