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
public class osmLine {
    ArrayList<Coordinate> coordinate = new ArrayList<Coordinate>();
    ArrayList<Tag> tag = new ArrayList<Tag>();
    double id;
 
    public void addNode(double lat,double lon){
        //create a coordinate out of recieved arguments
        Coordinate coord = new Coordinate(lon,lat);
        coordinate.add(coord);
    }
    
    public void addNode(osmNode nod){
    	coordinate.add(nod.returnCoordinate());
    }
    public void addTag(String Key, String Value){
        //generate tags
        Tag tempTag = new Tag();
        tempTag.key = Key;
        tempTag.value = Value;
        tag.add(tempTag);
    }
    public double getId(){
    	return id;
    }
    
    void addId(String Id) {
        id = Double.parseDouble(Id);
    }
    public ArrayList<Coordinate> returnCoordinate(){
        return coordinate;
    }
    
    public double returnId(){
        return id;
    }
    
    public ArrayList<Tag> getTags(){
        return tag;
    }
    
    public Coordinate[] coordinateInArray(){
        int coordinateNumber = coordinate.size();
        Coordinate[] coordinateArray = new Coordinate[coordinateNumber];
        for(int i = 0;i<coordinateNumber;i++){
            coordinateArray[i]=coordinate.get(i);
        }
        return coordinateArray;
    }
    static int i;
	public void closePolygon() {
		if(coordinate.size()>0) 
			coordinate.add(coordinate.get(0));
	}
}
