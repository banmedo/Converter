package org.geotools.Converter;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import org.geotools.Converter.osm.OsmMain;


/**
 * Hello world!
 *
 */
public class App extends SwingWorker<Integer,String> implements ActionListener 
{
    static File osmfile;
    static String osmFileName;
    OsmMain osm = new OsmMain();
    shpMain shp = new shpMain();
    int programStages = 3;
    public App() throws Exception{}
    public static void main( String[] args ) throws Exception
    {
        App app = new App();
        app.generateInterface();
    }
    
	//generate interface
    static final JLabel signature = new JLabel("by - G10'E project group C");
    final JFrame window = new JFrame("OSM to SHP converter(custom tags)");
    static JPanel panel = new JPanel(),
            panel1 = new JPanel(),
            panel2 = new JPanel(),
            progPanel = new JPanel();
        
    static JLabel inLabel = new JLabel("Select input file  "),
                outLabel = new JLabel("Select output folder"),
                globLabel = new JLabel(),
                selectNote = new JLabel("Tick the types of features you would like to export");
        
    static JTextField inField = new JTextField("Select the input osm file                         "),
                outField = new JTextField("Select the output path                            ");
        
    static JButton inButton = new JButton("Browse"),
                outButton = new JButton("Browse"),
                startButton = new JButton("Convert"),
                nextButton = new JButton("Next->"),
                backButton = new JButton("<-Back");
    
    static JCheckBox pointCheck = new JCheckBox("point"),
    		lineCheck = new JCheckBox("line"),
    		polygonCheck = new JCheckBox("polygon");
    
    static JProgressBar progress = new JProgressBar(),
    		ovPrg = new JProgressBar();
    //static Image icon = Toolkit.getDefaultToolkit().getImage("./icon.png");
    //new ImageIcon(getClass().getResource("/images/myHeader.jpg"))
    static Image icon;
    private void generateInterface(){
    	icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/res/icon.png"));
    	startButton.setToolTipText("Click to start Converting");
    	signature.setToolTipText("Nishanta Khanal, David Nhemaphuki, Arun Bhandari, Megha Shrestha");
    	
    	window.setIconImage(icon);
        window.setSize(500,450);				// set the size of windows
        window.setLocationRelativeTo(null);  	// center the window
        window.setResizable(false);				// set the windows cant be resized
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
        GridBagLayout winBag = new GridBagLayout(), 
        		gridBag1 = new GridBagLayout(),
                gridBag2 = new GridBagLayout();			// defining Layout for panels
        GridBagConstraints constraint = new GridBagConstraints();  //define constraints to be used for layout
        
        window.setLayout(winBag);		// set Layout of window
        
        //set Layout
        panel1.setLayout(gridBag1);					
        panel1.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        panel2.setLayout(gridBag2);
        panel2.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        
        //set constraint values
        constraint.gridx=0;
        constraint.gridy=0;
        constraint.insets = new Insets(10,10,10,10);
        gridBag1.setConstraints(inLabel, constraint);
        constraint.gridx=1;
        constraint.gridy=0;
        gridBag1.setConstraints(inField, constraint);
        inField.setPreferredSize(new Dimension(200,20));
        constraint.gridx=2;
        constraint.gridy=0;
        gridBag1.setConstraints(inButton, constraint);
        panel1.add(inLabel);
        panel1.add(inField);
        panel1.add(inButton);
        constraint.gridx=0;
        constraint.gridy=1;
        gridBag1.setConstraints(outLabel, constraint);
        constraint.gridx=1;
        constraint.gridy=1;
        gridBag1.setConstraints(outField, constraint);
        outField.setPreferredSize(new Dimension(200,20));
        constraint.gridx=2;
        constraint.gridy=1;
        gridBag1.setConstraints(outButton, constraint);
        panel1.add(outLabel);
        panel1.add(outField);
        panel1.add(outButton);
        
        // for panel 2
        constraint.gridx=1;
        constraint.gridy=0;
        gridBag2.setConstraints(selectNote, constraint);
        panel2.add(selectNote);
        constraint.gridx=0;
        constraint.gridy=1;
        gridBag2.setConstraints(pointCheck, constraint);
        panel2.add(pointCheck);
        constraint.gridx=1;
        constraint.gridy=1;
        gridBag2.setConstraints(lineCheck, constraint);
        panel2.add(lineCheck);
        constraint.gridx=2;
        constraint.gridy=1;
        gridBag2.setConstraints(polygonCheck, constraint);
        panel2.add(polygonCheck);
        constraint.gridx=1;
        constraint.gridy=2;
        gridBag2.setConstraints(startButton, constraint);
        panel2.add(startButton);
        constraint.gridx=1;
        constraint.gridy=3;
        gridBag2.setConstraints(globLabel, constraint);
        panel2.add(globLabel);
        constraint.gridx=0;
        constraint.gridy=4;
        constraint.gridwidth=5;
        progress.setPreferredSize(new Dimension(window.getWidth()-100, 20));
        gridBag2.setConstraints(progress, constraint);
        panel2.add(progress);
        constraint.gridx=0;
        constraint.gridy=5;
        constraint.gridwidth=5;
        ovPrg.setPreferredSize(new Dimension(window.getWidth()-100, 20));
        gridBag2.setConstraints(ovPrg, constraint);
        panel2.add(ovPrg);
        constraint.gridx=1;
        constraint.gridy=7;
        constraint.gridwidth=3;
        gridBag2.setConstraints(signature, constraint);
        panel2.add(signature);
        
        constraint.gridx=0;
        constraint.gridy=0;
        winBag.setConstraints(panel1, constraint);
        window.add(panel1);
        constraint.gridx=0;
        constraint.gridy=1;
        constraint.gridheight=2;
        winBag.setConstraints(panel2, constraint);
        window.add(panel2);
        //set the windows t viewabe
        window.setVisible(true);
        //add action listeners
        inField.addActionListener(this);
        inButton.addActionListener(this);
        outField.addActionListener(this);
        outButton.addActionListener(this);
        startButton.addActionListener(this);
        
        new FileDrop( window, new FileDrop.Listener()
        {   public void filesDropped( java.io.File[] files )
            {   
        		if(files.length==1){   
            		try
                    {   //inField.setText(files[0].toURI().toURL().toString().substring(6));
            			String inPath = new String(files[0].getPath());
            			int colon;
                    	do{
            	        	colon = inPath.indexOf("\\");
            	        	if(colon!=-1){
            	        		inPath = inPath.substring(0, colon)+"/"+inPath.substring(colon+1);
            	        	}
                    	}while(colon!=-1);
                    	inField.setText(inPath);
                    	int length = files[0].getName().length();	
                    	osmFileName = files[0].getName().substring(0,length-4);
                    	outField.setText(inPath.substring(0, inField.getText().length()-length));
                    }   // end try
                    catch( Exception e ) {}
                }
        		else if(files.length>1){
        			JOptionPane.showMessageDialog(null, "Please select a single file");
        		}
            }   // end filesDropped
        }); // end FileDrop.Listener
        
    }
    //implement events
    public void actionPerformed(ActionEvent event) {
        File newInFile,newOutFile;
        if(event.getSource()==inButton){
            try {
                final JFileChooser fc = new JFileChooser();
                int returnVal = fc.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    newInFile = fc.getSelectedFile();
                    String inPath = newInFile.getPath();
                    int colon;
                	do{
        	        	colon = inPath.indexOf("\\");
        	        	if(colon!=-1){
        	        		inPath = inPath.substring(0, colon)+"/"+inPath.substring(colon+1);
        	        	}
                	}while(colon!=-1);
                	
                    inField.setText(inPath);
                    String fileName = newInFile.getName();
                    int len = fileName.length();
                    osmFileName = fileName.substring(0, len-4);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Please Enter a valid file!!");
            }
        }
        else if(event.getSource()==outButton){
            try {
                final JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = fc.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    newOutFile = fc.getSelectedFile();
                    String outPath = new String(newOutFile.getPath());
                    int colon;
                	do{
        	        	colon = outPath.indexOf("\\");
        	        	if(colon!=-1){
        	        		outPath = outPath.substring(0, colon)+"/"+outPath.substring(colon+1);
        	        	}
                	}while(colon!=-1);
                	outField.setText(outPath+"/");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Please Enter a valid path!!");
            }
        }
        else if(event.getSource()==startButton){
        	if(pointCheck.isSelected()||lineCheck.isSelected()||polygonCheck.isSelected()){
            	startButton.setText("converting...");
	        	startButton.setEnabled(false);
	        	pointCheck.setEnabled(false);
	            lineCheck.setEnabled(false);
	            polygonCheck.setEnabled(false);
	            inField.setEditable(false);
	            outField.setEditable(false);
	            inButton.setEnabled(false);
	            outButton.setEnabled(false);
	            
	            programStages=3;
	            if(pointCheck.isSelected()){
	            	programStages++;
	            	if(lineCheck.isSelected()){
	            		programStages++;
	            		if(polygonCheck.isSelected()) programStages++;
	            	}
	            }
	            ovPrg.setMaximum(programStages);
	    		window.repaint();
	        	osmfile = new File(inField.getText());            
	            
	            try {
	            	new App().execute();
	            	//doInBackground();
	            } catch (Exception ex) {
	                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
	                JOptionPane.showMessageDialog(null,"Unexpected Error Occured!! Please check if the filename and path are valid!!");
	                startButton.setText("Convert");
	            	startButton.setEnabled(true);
	            	pointCheck.setEnabled(true);
		            lineCheck.setEnabled(true);
		            polygonCheck.setEnabled(true);
		            inField.setEditable(true);
		            outField.setEditable(true);
		            inButton.setEnabled(true);
		            outButton.setEnabled(true);
		            
	            }
        	}
        
        	else
        		JOptionPane.showMessageDialog(null, "Select at least one of the feature types");
    	}	
    }

    
    
    private void showCompletionNote() {
    	globLabel.setText("File conversion completed!!!");
    	progress.setValue(progress.getMaximum());
    	startButton.setText("Convert");
    	//returning variables to initial states
    	startButton.setEnabled(true);
    	pointCheck.setEnabled(true);
        lineCheck.setEnabled(true);
        polygonCheck.setEnabled(true);
        inField.setEditable(true);
        outField.setEditable(true);
        inButton.setEnabled(true);
        outButton.setEnabled(true);
        
        
    }
    private void showCancellationNote() {
    	globLabel.setText("File conversion cancelled!!!");
    	progress.setValue(progress.getMinimum());
    	ovPrg.setValue(0);
    	startButton.setText("Convert");
    	//returning variables to initial states
    	startButton.setEnabled(true);
    	pointCheck.setEnabled(true);
        lineCheck.setEnabled(true);
        polygonCheck.setEnabled(true);
        inField.setEditable(true);
        outField.setEditable(true);
        inButton.setEnabled(true);
        outButton.setEnabled(true);
        
        
    }
    
    public boolean checkExistance(){
    	boolean exists = false;
    	if (pointCheck.isSelected()){
    		if(shp.shpPointFile.exists()) exists=true;
    	}
    	else if(lineCheck.isSelected()){
    		if(shp.shpLineFile.exists()) exists = true;
    	}
    	else if(polygonCheck.isSelected()){
    		if(shp.shpPolygonFile.exists()) exists = true;
    	}
		return exists;
    }
	
    @Override
	protected Integer doInBackground() throws Exception {
		int curValue = 1; // to update index of progressBar
		ovPrg.setMinimum(0);
		ovPrg.setValue(curValue);
		// TODO Auto-generated method stub
		shp.setFilePath(outField.getText()+osmFileName);
		boolean fileExists = checkExistance();
		if(fileExists){
			int ans = JOptionPane.showConfirmDialog(null, "Output file exists!! overwrite current file?");
			System.out.print(ans);
			if((ans==1)||(ans==2)){
				showCancellationNote();
				return null;
			}
		}
		curValue++;
    	ovPrg.setValue(curValue);
    	osm.parseOsm(osmfile,progress,globLabel);          // parse the osm file
		curValue++;
		ovPrg.setValue(curValue);
		if(pointCheck.isSelected()){
        	globLabel.setText("Creating point file");
            shp.preparePointFile(osm.osmNodes);	// sets up writing space
		    shp.drawShpPoints(progress,osm.osmNodes);
		    curValue++;
		    ovPrg.setValue(curValue);
        }
        if(lineCheck.isSelected()){
        	globLabel.setText("Creating line file");
            shp.prepareLineFile(osm.osmLines);
	        shp.drawShpLines(progress,osm.osmLines);
	        curValue++;
		    ovPrg.setValue(curValue);
        }
		if(polygonCheck.isSelected()){
			globLabel.setText("Creating polygon file");
	        shp.preparePolygonFIle(osm.osmPolygons);
        	shp.drawShpPolygons(progress,osm.osmPolygons);
        	curValue++;
		    ovPrg.setValue(curValue);
        }
        showCompletionNote();
        return null;
	}
	
	protected void done(){
		this.cancel(true);
	}
}
