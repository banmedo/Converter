/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geotools.Converter.osm;

import java.util.ArrayList;

import com.vividsolutions.jts.geom.Coordinate;

/**
 *
 * @author Ban
 */
public class osmNode {
    
    Coordinate coordinate;
    double id;
    ArrayList<Tag> tag = new ArrayList<Tag>();
    boolean inWay = false;
    
    public osmNode(){
        coordinate = new Coordinate(0,0);
    }
    
    public void addNode(double lat,double lon){
        //create a coordinate out of recieved arguments
        coordinate = new Coordinate(lon,lat);
        
    }
    
    public void setExistsInWay(boolean t){
    	inWay = t;
    }
    
    public boolean existsInWay(){
    	return inWay;
    }
    
    public void addTag(String Key, String Value){
        //generate tags
        Tag tempTag = new Tag();
        tempTag.key = Key;
        tempTag.value = Value;
        tag.add(tempTag);
    }
    
    public void addId(String argID){
        id=Double.parseDouble(argID);
    }
    public Coordinate returnCoordinate(){
        return coordinate;
    }
    
    public double returnId(){
        return id;
    }
    
    public ArrayList<Tag> getTags(){
        return tag;
    }

    void addCoordinate(Coordinate get) {
        coordinate = get;
    }
        
    public Coordinate getCoordinate(){
    	return coordinate;
    }
    public double getId(){
    	return id;
    	
    }
}
