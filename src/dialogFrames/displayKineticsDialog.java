package dialogFrames;

//Imports are listed in full to show what's being used could just import javax.swing.* and java.awt.* etc..
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import kinetics.rate;

@SuppressWarnings("serial")
//	Layout: 
//  |  - displayScrollPane(displayPanel)                                             
//  |    + kineticsPanel (for each kinetics)                                       
//  |      * kineticsLabel                        

public class displayKineticsDialog extends JDialog{
	private JPanel displayPanel;
	private JScrollPane displayScrollPane;
	private ArrayList<String> kineticIndexList;
	private ArrayList<rate> myKineticLib;
	
	private String COMMA=",";
	
	public displayKineticsDialog(ArrayList<String> indexList, ArrayList<rate> kineticLibrary)
	{
		kineticIndexList=new ArrayList<String>();
		myKineticLib=new ArrayList<rate>();
		kineticIndexList=indexList;
		myKineticLib=kineticLibrary;
		displayPanel=new JPanel();
		displayScrollPane=new JScrollPane(displayPanel);
		displayScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		//set JDialog properties
		setTitle("Kinetics Information");
		setSize(500,400);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setResizable(false);
		setModal(true);

		formatDisplay();
		add(displayScrollPane);
	}
	
	private String getRateLabelString(int size, int rateRxnID, int lumpedRxnID, String organism, String cellLine, String source)
	{
		if(size>1)
			return "Lumped reaction "+(rateRxnID+1)+"["+(lumpedRxnID+1)+"] ("+organism+", "+cellLine+", "+source+")";
		else
			return "Rate reaction "+(rateRxnID+1)+" ("+organism+", "+cellLine+", "+source+")";
	}
	
	private String processMultipleParamURL(String url, String parameters, String id)
	{
		String formattedURL="";
		ArrayList<String> placementString=new ArrayList<String>();
		ArrayList<String> toReplaceWith=new ArrayList<String>();
		
		String paraString=parameters, idString=id;
		int index;
		
		//retrieve placementString
		index=paraString.indexOf(">");
		while(index!=-1)
		{
			placementString.add(paraString.substring(0,index+1));
			paraString=paraString.substring(index+1);
			index=paraString.indexOf(">");
		}
		
		//retrieve toReplaceWith
		index=idString.indexOf(">");
		while(index!=-1)
		{
			toReplaceWith.add(idString.substring(1,index));
			idString=idString.substring(index+1);
			index=idString.indexOf(">");
		}

		formattedURL=url;
		
		for(int i=0; i<placementString.size(); i++)
		{
			index=formattedURL.indexOf(placementString.get(i));
			if(index!=-1)
			{
				String frontString=formattedURL.substring(0, index);
				String backString=formattedURL.substring(index+placementString.get(i).length());
				formattedURL=frontString+toReplaceWith.get(i)+backString;
			}
		}
		
		System.out.println("formattedURL: ".concat(formattedURL));
			
		return formattedURL;
	}
	
	private void formatDisplay()
	{
		displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));
		
		System.out.println("kineticIndexList: "+kineticIndexList.size());
		
		for(int i=0; i<kineticIndexList.size(); i++)
		{
			JPanel kineticPanel=new JPanel();
			kineticPanel.setLayout(new BoxLayout(kineticPanel, BoxLayout.Y_AXIS));
			kineticPanel.setOpaque(true);
			if ((i&1)==0)
				kineticPanel.setBackground(new Color(0xF0FFF0));
			else 
				kineticPanel.setBackground(new Color(0xFFE4C4));
			String kineticString=kineticIndexList.get(i);
			ArrayList<Integer> indexList=new ArrayList<Integer>();
			int commaIndex=kineticString.indexOf(COMMA);
			while(commaIndex!=-1)
			{
				indexList.add(Integer.parseInt(kineticString.substring(0, commaIndex)));
				kineticString=kineticString.substring(commaIndex+1);
				commaIndex=kineticString.indexOf(COMMA);
			}
			
			System.out.println("indexList: "+indexList.size());
			
			//note that for myKineticLib indexes, it starts with 1 whereas indexList start with zero.
			for(int j=0; j<indexList.size(); j++)
			{
				JPanel ratePanel=new JPanel();
				ratePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
				JLabel rateLabel=new JLabel();
				int kineticLibIndex=indexList.get(j)-1;
				final String URIString=processMultipleParamURL(myKineticLib.get(kineticLibIndex).getURL(), 
						myKineticLib.get(kineticLibIndex).getParameter(), myKineticLib.get(kineticLibIndex).getParamVal());
				rateLabel.setText(getRateLabelString(indexList.size(), i, j, myKineticLib.get(kineticLibIndex).getOrganism(),
						myKineticLib.get(kineticLibIndex).getCellLine(), myKineticLib.get(kineticLibIndex).getSource()));
				rateLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				rateLabel.addMouseListener(new MouseAdapter() {
					@Override 
					public void mouseClicked(MouseEvent e){
						if (e.getClickCount() > 0) {
							if (Desktop.isDesktopSupported()) {
								Desktop desktop = Desktop.getDesktop();
								try {
									URI rateURI=new URI(URIString);
									desktop.browse(rateURI);
								} catch (IOException ex) {
									ex.printStackTrace();
								} catch (URISyntaxException ex) {
									ex.printStackTrace();
								}
							}
						}
					}
				});
				//ratePanel.add(rateLabel);
				kineticPanel.add(rateLabel);
			}
			displayPanel.add(kineticPanel);
		}
	}
}
