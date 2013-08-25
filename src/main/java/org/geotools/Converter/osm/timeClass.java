/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geotools.Converter.osm;

/**
 *
 * @author Ban
 */
public class timeClass {
    
    int yearA, monthA, dateA;
    int yearB, monthB, dateB;
    int hourA, minuteA, secondA;
    int hourB, minuteB, secondB;
    
    public boolean isANewerThanB(String A, String B){
        if (yearA<=yearB)
            if (monthA<=monthB)
                if(dateA<=dateB)
                    if(hourA<=hourB)
                        if(minuteA<=minuteB)
                            if(secondA<secondB)
                                return false;
        return true;
        
    }
    
    /*
     * -------------------------------
     * Private Classes
     * -------------------------------
     */
    
    private void getTime(String A, String B){
        int indexDash = 0,
                indexColon = 0,
                indexT = 0,
                indexZ = 0;
        String partCut = "",
                partLeft = "";
        //for A
        {
            indexDash = A.indexOf("-");
            partCut = A.substring(0, indexDash);
            yearA = Integer.parseInt(partCut);
            partLeft = A.substring(indexDash+1);
            A = partLeft;
            
            indexDash = A.indexOf("-");
            partCut = A.substring(0, indexDash);
            monthA = Integer.parseInt(partCut);
            partLeft = A.substring(indexDash+1);
            A = partLeft;
    
            indexT = A.indexOf("T");
            partCut = A.substring(0, indexT);
            dateA = Integer.parseInt(partCut);
            partLeft = A.substring(indexT+1);
            A = partLeft;
            
            indexColon = A.indexOf(":");
            partCut = A.substring(0, indexColon);
            hourA = Integer.parseInt(partCut);
            partLeft = A.substring(indexColon+1);
            A = partLeft;
            
            indexColon = A.indexOf(":");
            partCut = A.substring(0, indexColon);
            minuteA = Integer.parseInt(partCut);
            partLeft = A.substring(indexColon+1);
            A = partLeft;
            
            indexZ = A.indexOf("Z");
            partCut = A.substring(0, indexZ);
            secondA = Integer.parseInt(partCut);
            partLeft = A.substring(indexZ+1);
            A = partLeft;
            
        }
        
        {
            indexDash = B.indexOf("-");
            partCut = B.substring(0, indexDash);
            yearB = Integer.parseInt(partCut);
            partLeft = B.substring(indexDash+1);
            B = partLeft;
            
            indexDash = B.indexOf("-");
            partCut = B.substring(0, indexDash);
            monthB = Integer.parseInt(partCut);
            partLeft = B.substring(indexDash+1);
            B = partLeft;
    
            indexT = B.indexOf("T");
            partCut = B.substring(0, indexT);
            dateB = Integer.parseInt(partCut);
            partLeft = B.substring(indexT+1);
            B = partLeft;
            
            indexColon = B.indexOf(":");
            partCut = B.substring(0, indexColon);
            hourB = Integer.parseInt(partCut);
            partLeft = B.substring(indexColon+1);
            B = partLeft;
            
            indexColon = B.indexOf(":");
            partCut = B.substring(0, indexColon);
            minuteB = Integer.parseInt(partCut);
            partLeft = B.substring(indexColon+1);
            B = partLeft;
            
            indexZ = B.indexOf("Z");
            partCut = B.substring(0, indexZ);
            secondB = Integer.parseInt(partCut);
            partLeft = B.substring(indexZ+1);
            B = partLeft;
            
        }
    
    }
    
    
}
