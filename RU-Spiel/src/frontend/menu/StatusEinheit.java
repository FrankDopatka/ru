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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import daten.*;
import frontend.Frontend;

public class StatusEinheit extends JDialog implements ActionListener{
	private static final long serialVersionUID = 1L;
	private Frontend frontend;
	private D_Einheit einheit;
	private JButton abbrechen=new JButton("Abbrechen");
	private JButton[] buttons=new JButton[10];
	private static int reichweiteUmgebung=2;
	
	public StatusEinheit(Frontend frontend,D_Einheit einheit,D_Spieler spieler){
		super(frontend);
		this.frontend=frontend;
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
		if (einheit.getString("name").equals("Siedler")) buttons[0].setText("Stadt gruenden");			
		buttons[9].setText("Aufloesen");
		
		setTitle("Status der Einheit");
		abbrechen.addActionListener(this);

		int x=1;
		addEintrag(x++,jp,"Leben: ",einheit.getInt("lebenAktuell")+" von "+einheit.getInt("lebenMaximal"));
		addEintrag(x++,jp,"Angriff: ",einheit.getInt("angriffAktuell")+" von "+einheit.getInt("angriffMaximal"));
		addEintrag(x++,jp,"Verteidigung: ",einheit.getInt("verteidigungAktuell")+" von "+einheit.getInt("verteidigungMaximal"));
		addEintrag(x++,jp,"Bewegung: ",einheit.getInt("bewegungAktuell")+" von "+einheit.getInt("bewegungMaximal"));

		JPanel umgebungPanel=new JPanel();
		umgebungPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc=new GridBagConstraints();
		int xStart=einheit.getInt("x")-reichweiteUmgebung;
		int yStart=einheit.getInt("y")-reichweiteUmgebung;
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
			add(setLabelHeader(einheit.getString("name")+" von "+spieler.getString("name")+" aus "+spieler.getString("nation")),BorderLayout.NORTH);
		else
			add(setLabelHeader(einheit.getString("name")+" von dem "+spieler.getString("rasse")+" "+spieler.getString("name")),BorderLayout.NORTH);
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
			if (einheit.getString("name").equals("Siedler")){
				// Stadt gruenden
				iBackendSpiel backend=frontend.getBackend();
				JTextField name=new JTextField(20);
		  	Object[] eingaben={"Name der Stadt",name};
		  	JOptionPane eingabe=new JOptionPane(eingaben,JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION){
					private static final long serialVersionUID = 1L;
					 @Override
		        public void selectInitialValue() {
		          super.selectInitialValue();
		          Object[] felder=(Object[]) this.getMessage();
		          ((JTextField)felder[1]).requestFocusInWindow();
		        }
		  	};
		  	eingabe.createDialog(null, "Neue Stadt gruenden...").setVisible(true);
		  	if ((eingabe.getValue()!=null)&&(eingabe.getValue().equals(JOptionPane.OK_OPTION))){
		  		int idKarte=einheit.getInt("idKarte");
		  		int x=einheit.getInt("x");
		  		int y=einheit.getInt("y");
		  		backend.gruendeStadt(frontend.getIdSpieler(),idKarte,x,y,name.getText());
		  		ArrayList<D> daten=Xml.toArray(backend.getFeldDaten(idKarte,x,y));
		  		frontend.getKarte().updateFeld(x,y,daten);
					this.setVisible(false);
					this.dispose();
		  	}
			}
		}
		if (o==abbrechen){
			this.setVisible(false);
			this.dispose();
		}
	}
}
