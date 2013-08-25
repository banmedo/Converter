/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geotools.Converter;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JProgressBar;

import org.geotools.Converter.osm.Tag;
import org.geotools.Converter.osm.osmLine;
import org.geotools.Converter.osm.osmNode;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
/**
 *
 * @author Ban
 */
public class shpMain {
    //<editor-fold defaultstate="collapsed" desc="Variables and objects">
    
    // basic variables 
    File shpPointFile;
    File shpLineFile;
    File shpPolygonFile;
   
    //shp file handler variables
    SimpleFeatureType pointType, lineType, polygonType;
    SimpleFeatureCollection pointCollection = FeatureCollections.newCollection(),
            lineCollection = FeatureCollections.newCollection(),
            polygonCollection = FeatureCollections.newCollection();
    SimpleFeatureBuilder pointBuilder, lineBuilder, polygonBuilder;
    SimpleFeature pointFeature, lineFeature, polygonFeature;
    Transaction transaction = new DefaultTransaction("create");
    SimpleFeatureStore pointFeatureStore, lineFeatureStore, polygonFeatureStore;

    //</editor-fold>
    
    /*
     * ------------------------------------
     * public Methods
     * ------------------------------------
     */
    public shpMain(){
    }
    
    public void setFilePath(String filePath){
        shpPointFile = new File(filePath+"_point.shp");
        shpLineFile = new File(filePath+"_line.shp");
        shpPolygonFile = new File(filePath+"_polygon.shp");
        
    }
    
    public void drawShpPoints(JProgressBar pBar,ArrayList<osmNode> osmNodes) throws IOException{
        // draw one point at a time
        GeometryFactory geomFactory = JTSFactoryFinder.getGeometryFactory(null);
        pointFeatureStore.setTransaction(transaction);
        int i = 0;
        pBar.setMaximum(osmNodes.size());
        for(osmNode onePoint:osmNodes){
        	pBar.setValue(i);i++;
            Point point = geomFactory.createPoint(onePoint.getCoordinate());
            pointBuilder.add(point);
            pointBuilder.add(onePoint.getId());
            for(String attribute:uniquePointTagKeys){
            	boolean tagFound=false;
            	for(Tag oneTag:onePoint.getTags()){
            		if (attribute.equalsIgnoreCase(oneTag.key)){
            			tagFound=!tagFound;
            			pointBuilder.add(oneTag.value);
            			break;
            		}
            	}
            	if(!tagFound){
            		pointBuilder.add(null);
            	}
            }
            pointFeature = pointBuilder.buildFeature(null);
            pointCollection.add(pointFeature);
        }
        pointFeatureStore.addFeatures(pointCollection);
        transaction.commit();
    }
    
    public void drawShpLines(JProgressBar pBar,ArrayList<osmLine> osmLines) throws IOException{
        GeometryFactory geomFactory = JTSFactoryFinder.getGeometryFactory(null);
        lineFeatureStore.setTransaction(transaction);
        int i = 0;
        pBar.setMaximum(osmLines.size());
        for(osmLine oneLine:osmLines){
        	pBar.setValue(i);i++;
            LineString line = geomFactory.createLineString(oneLine.coordinateInArray());
            lineBuilder.add(line);
            lineBuilder.add(oneLine.getId());
            for(String attribute:uniqueLineTagKeys){
            	boolean tagFound=false;
            	for(Tag oneTag:oneLine.getTags()){
            		if (attribute.equalsIgnoreCase(oneTag.key)){
            			tagFound=!tagFound;
            			lineBuilder.add(oneTag.value);
            			break;
            		}
            	}
            	if(!tagFound){
            		lineBuilder.add(null);
            	}
            }
            lineFeature = lineBuilder.buildFeature(null);
            lineCollection.add(lineFeature);
        }
        lineFeatureStore.addFeatures(lineCollection);
        transaction.commit();
    
    }
    
    public void drawShpPolygons(JProgressBar pBar,ArrayList<osmLine> osmPolygons) throws IOException{
        GeometryFactory geomFactory = JTSFactoryFinder.getGeometryFactory(null);
        polygonFeatureStore.setTransaction(transaction);
        int i = 0;
        pBar.setMaximum(osmPolygons.size());
        for(osmLine onePolygon:osmPolygons){
        	pBar.setValue(i);i++;
            LinearRing border = geomFactory.createLinearRing(onePolygon.coordinateInArray());
            Polygon polygon = geomFactory.createPolygon(border, null);
            polygonBuilder.add(polygon);
            polygonBuilder.add(onePolygon.getId());
            for(String attribute:uniquePolygonTagKeys){
            	boolean tagFound=false;
            	for(Tag oneTag:onePolygon.getTags()){
            		if (attribute.equalsIgnoreCase(oneTag.key)){
            			tagFound=!tagFound;
            			polygonBuilder.add(oneTag.value);
            			break;
            		}
            	}
            	if(!tagFound){
            		polygonBuilder.add(null);
            	}
            }
            polygonFeature = polygonBuilder.buildFeature(null);
            polygonCollection.add(polygonFeature);
        }
        polygonFeatureStore.addFeatures(polygonCollection);
        transaction.commit();
    
    }
    public void preparePointFile(ArrayList<osmNode> osmNodes) throws IOException, SchemaException {
    	uniquePointTagKeys = getUniquePointTagKeys(osmNodes);
        String passPointString = new String("location:Point:srid=4326,"+"id:Double");
        for(String attribute:uniquePointTagKeys){
        	int colon;
        	do{
	        	colon = attribute.indexOf(":");
	        	if(colon!=-1){
	        		attribute = attribute.substring(0, colon)+"_"+attribute.substring(colon+1);
	        	}
        	}while(colon!=-1);
        	passPointString=passPointString+","+attribute+":String";
        }
        
        pointType = DataUtilities.createType("Location", passPointString);
        
        pointCollection = FeatureCollections.newCollection();
        
        pointBuilder = new SimpleFeatureBuilder(pointType);
        
        pointFeature = pointBuilder.buildFeature(null);
        
        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
        
        Map <String,Serializable> parameters = new HashMap<String, Serializable>();
        
        parameters.put("url",shpPointFile.toURI().toURL());
        parameters.put("create spatial index",Boolean.TRUE);
        
        ShapefileDataStore newdatastore = (ShapefileDataStore)dataStoreFactory.createNewDataStore(parameters);
        newdatastore.createSchema(pointType);
        
        newdatastore.forceSchemaCRS(DefaultGeographicCRS.WGS84);  
        
        String typename = newdatastore.getTypeNames()[0];
        
        SimpleFeatureSource simpfeatsource = newdatastore.getFeatureSource(typename);
        
        pointFeatureStore = (SimpleFeatureStore)simpfeatsource;
    }
    
    public void prepareLineFile(ArrayList<osmLine> osmLines) throws SchemaException, MalformedURLException, IOException {
    	uniqueLineTagKeys = getUniqueTagKeys(osmLines,"lines");
        String passLineString = new String("location:LineString:srid=4326,"+"id:Double");
        for(String attribute:uniqueLineTagKeys){
        	int colon;
        	do{
	        	colon = attribute.indexOf(":");
	        	if(colon!=-1){
	        		attribute = attribute.substring(0, colon)+"_"+attribute.substring(colon+1);
	        	}
        	}while(colon!=-1);
        	passLineString=passLineString+","+attribute+":String";
        }
        
    	lineType = DataUtilities.createType("Location", passLineString);
        
        lineCollection = FeatureCollections.newCollection();
        
        lineBuilder = new SimpleFeatureBuilder(lineType);
        
        lineFeature = lineBuilder.buildFeature(null);
        
        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
        
        Map <String,Serializable> parameters = new HashMap<String, Serializable>();
        
        parameters.put("url",shpLineFile.toURI().toURL());
        parameters.put("create spatial index",Boolean.TRUE);
        
        ShapefileDataStore newdatastore = (ShapefileDataStore)dataStoreFactory.createNewDataStore(parameters);
        newdatastore.createSchema(lineType);
        
        newdatastore.forceSchemaCRS(DefaultGeographicCRS.WGS84);  
        
        String typename = newdatastore.getTypeNames()[0];
        
        SimpleFeatureSource simpfeatsource = newdatastore.getFeatureSource(typename);
        
        lineFeatureStore = (SimpleFeatureStore)simpfeatsource;
    }

    public void preparePolygonFIle(ArrayList <osmLine> osmPolygons) throws SchemaException, IOException {
    	uniquePolygonTagKeys = getUniqueTagKeys(osmPolygons,"polygons");
        Collections.sort(uniquePolygonTagKeys);
        String passPolygonString = new String("location:Polygon:srid=4326,"+"id:Double");
        for(String attribute:uniquePolygonTagKeys){
        	int colon;
        	do{
	        	colon = attribute.indexOf(":");
	        	if(colon!=-1){
	        		attribute = attribute.substring(0, colon)+"_"+attribute.substring(colon+1);
	        	}
        	}while(colon!=-1);
        	passPolygonString=passPolygonString+","+attribute+":String";
        }

        pointType = DataUtilities.createType("Location", passPolygonString);
        
        polygonCollection = FeatureCollections.newCollection();
        
        polygonBuilder = new SimpleFeatureBuilder(pointType);
        
        polygonFeature = polygonBuilder.buildFeature(null);
        
        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
        
        Map <String,Serializable> parameters = new HashMap<String, Serializable>();
        
        parameters.put("url",shpPolygonFile.toURI().toURL());
        parameters.put("create spatial index",Boolean.TRUE);
        
        ShapefileDataStore newdatastore = (ShapefileDataStore)dataStoreFactory.createNewDataStore(parameters);
        newdatastore.createSchema(pointType);
        
        newdatastore.forceSchemaCRS(DefaultGeographicCRS.WGS84);  
        
        String typename = newdatastore.getTypeNames()[0];
        
        SimpleFeatureSource simpfeatsource = newdatastore.getFeatureSource(typename);
        
        polygonFeatureStore = (SimpleFeatureStore)simpfeatsource;
    }

    
    /*
     * --------------------------------------------
     * Private Classes
     * --------------------------------------------
     */
    
    private ArrayList<String> uniquePointTagKeys = new ArrayList<String>(),
    		uniqueLineTagKeys = new ArrayList<String>(),
    		uniquePolygonTagKeys = new ArrayList<String>();
    
    private ArrayList<String> getUniquePointTagKeys(ArrayList<osmNode> osmNodes) {
        int totalPoints = osmNodes.size();
        ArrayList<String> uniquekeys = new ArrayList<String>();
        for(int pointIndex=0;pointIndex<totalPoints;pointIndex++){
            int totalTags = osmNodes.get(pointIndex).getTags().size();
            for(int tagIndex=0;tagIndex<totalTags;tagIndex++){
                String key = osmNodes.get(pointIndex).getTags().get(tagIndex).key;
                if(uniquekeys.isEmpty()) uniquekeys.add(key);
                else{
                    boolean keyExists = false;
                    for(int arrayIndex = 0;arrayIndex<uniquekeys.size();arrayIndex++){
                        if(key.equalsIgnoreCase(uniquekeys.get(arrayIndex))){
                                keyExists=true;
                                break;
                        }
                    }
                    if(!keyExists) uniquekeys.add(key);
                }
            }
        }
        Collections.sort(uniquekeys);
        if (uniquekeys.size()>1){
            Selector newObj = new Selector();
            uniquekeys = newObj.getSelection(uniquekeys, "point");
        }
        return uniquekeys;
    }
    
    private ArrayList<String> getUniqueTagKeys(ArrayList<osmLine> osmLines,String s) {
        int totalLines = osmLines.size();
        ArrayList<String> uniquekeys = new ArrayList<String>();
        for(int lineIndex=0;lineIndex<totalLines;lineIndex++){
            int totalTags = osmLines.get(lineIndex).getTags().size();
            for(int tagIndex=0;tagIndex<totalTags;tagIndex++){
                String key = osmLines.get(lineIndex).getTags().get(tagIndex).key;
                if(uniquekeys.isEmpty()) uniquekeys.add(key);
                else{
                    boolean keyExists = false;
                    for(int arrayIndex = 0;arrayIndex<uniquekeys.size();arrayIndex++){
                        if(key.equalsIgnoreCase(uniquekeys.get(arrayIndex))){
                                keyExists=true;
                                break;
                        }
                    }
                    if(!keyExists) uniquekeys.add(key);
                }
            }
        }
        Collections.sort(uniqueLineTagKeys);
        if (uniquekeys.size()>1){
        	Selector newObj = new Selector();
        	uniquekeys = newObj.getSelection(uniquekeys, s);
        }
        return uniquekeys;
    }

}
