/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geotools.Converter.osm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Ban
 */
public class OsmMain extends DefaultHandler{
    
    
    //<editor-fold defaultstate="collapsed" desc="Variables and Objects">
    //required variables
    timeClass time;
    
    //objects for node, line and polygon
    public ArrayList<osmNode> osmNodes = new ArrayList<osmNode>();
    //public ArrayList<osmPolygon> osmPolygons = new ArrayList<osmPolygon>();
    public ArrayList<osmLine> osmLines = new ArrayList<osmLine>(),
    		osmPolygons = new ArrayList<osmLine>();
    		
    public OsmMain(){}
   
    // parse the file

    public void parseOsm(File osmfile, JProgressBar progress, JLabel lab) throws Exception {
       //Create a "parser factory" for creating SAX parsers
       SAXParserFactory spfac = SAXParserFactory.newInstance();

       //Now use the parser factory to create a SAXParser object
       SAXParser sp = spfac.newSAXParser();
       //counting lines
       BufferedReader reader = new BufferedReader(new FileReader(osmfile));
       int lines = 0;
       while (reader.readLine() != null) lines++;
       reader.close();
       totalLines=lines;
       bar = progress;
       bar.setMaximum(totalLines);
       bar.setValue(0);
       updateText = lab;
       //Finally, tell the parser to parse the input and notify the handler
       sp.parse(osmfile, this);
       cleanNodes(); //removes nodes that exist in ways
    }
    osmNode tempNode;
    osmLine tempLine;
    boolean withinNode = false;
    boolean withinLine = false;
    boolean foundFirst = false;
    boolean samePoint = false;
    double firstId;
    boolean deletedData = false;
    int totalLines;
    int linecount;
    JProgressBar bar;
    JLabel updateText;
    
    public void startElement(String uri, String localName,
            String qName, Attributes attributes) throws SAXException {
    	linecount++;
    	bar.setValue(linecount);
    	updateText.setText("Parsing Osm file "+linecount+"/"+totalLines);
    	if(attributes.getIndex("action")!=-1){
    		if(attributes.getValue("action").equalsIgnoreCase("delete")){
    			deletedData = true;
    		}
    	}
    	if(!deletedData){
	    	if (qName.equalsIgnoreCase("node")) {
	    		tempNode = new osmNode();
	    		tempNode.addId(attributes.getValue("id"));
	    		double lat = Double.parseDouble(attributes.getValue("lat")),
	    				lon =Double.parseDouble(attributes.getValue("lon"));
	    		tempNode.addNode(lat, lon);
	    		withinNode = true;
	    		
		    }
	    	else if(qName.equalsIgnoreCase("way")){
	    		tempLine = new osmLine();
	    		tempLine.addId(attributes.getValue("id"));
	    		withinLine = true;
	    	}
	    	else if(qName.equalsIgnoreCase("nd")){
	    		double refID = Double.parseDouble(attributes.getValue("ref"));
	    		if(foundFirst){
	    			if(refID == firstId) samePoint = true;
	    		}
	    		else if(!foundFirst) {
	    			firstId = refID;
	    			foundFirst = true;
	    		}
	    		if(!samePoint){
	    			for(int i=0; i<osmNodes.size();i++){
		    			if(osmNodes.get(i).getId()==refID){
		    				tempLine.addNode(osmNodes.get(i));
		    				osmNodes.get(i).setExistsInWay(true);
		    				break;
		    			}
		    		}		    		
	    		}
	    		else if(samePoint) tempLine.closePolygon();
	    	}
	    	else if(qName.equalsIgnoreCase("tag")){
	    		String Key = attributes.getValue("k");
	    		String Value = attributes.getValue("v");
	    		if(withinNode){ tempNode.addTag(Key, Value);}
	    		else if (withinLine) {tempLine.addTag(Key, Value);}
	    	}
    	}
	}
    
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
    	if(!deletedData){
	    	if(qName.equalsIgnoreCase("node")){
	    		osmNodes.add(tempNode);
	    		withinNode =false;
	    		System.out.print("exiting node");
	    		samePoint = false;
	    		foundFirst = false;
	    	}
	    	else if(qName.equalsIgnoreCase("way")){
	    		if(tempLine.coordinate.size()>1){
		    		if(!samePoint) {
		    			osmLines.add(tempLine);
		    		}
		    		else{ 
		    			if(tempLine.coordinate.size()>3){
		    				osmPolygons.add(tempLine);
		    			}
		    		}
	    		}
	    		samePoint = false;
	    		foundFirst = false;
	    		withinLine = false;
	    		firstId = 0;
	    	}
    	}
    	else{
    		if(qName.equalsIgnoreCase("node") || qName.equalsIgnoreCase("way"))
    			deletedData = false;   	
    	}
    }
    
    public void cleanNodes(){
    	for(int i = 0;i<osmNodes.size();i++){
    		if(osmNodes.get(i).existsInWay()){
    			osmNodes.remove(i);
    			i--;
    		}
    	}
    }
    
}