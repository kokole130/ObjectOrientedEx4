package gameClient;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;

import Server.game_service;
import dataStructure.DGraph;
import dataStructure.node_data;
import elements.Fruit;
import elements.Robot;
import elements.fruits;
import elements.robots;

public class KML_Logger {
	
    private StringBuffer sb = new StringBuffer();
    
    
    /**
     * default constructor that initializing to StringBuffer KML format
     */
    public KML_Logger(){
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<kml xmlns=\"http://earth.google.com/kml/2.2\">\n");
        sb.append("  <Document>\n");
        sb.append("    <name>TimeStamps</name>\n");
        sb.append("    <Style id=\"paddle-ylw-circle\">\n");
        sb.append("      <IconStyle>\n");
        sb.append("        <Icon>\n");
        sb.append("          <href>http://maps.google.com/mapfiles/kml/paddle/ylw-circle.png</href>\n" );//banana
        sb.append("        </Icon>\n");
        sb.append("        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\n");
        sb.append("      </IconStyle>\n");
        sb.append("    </Style>\n");
        sb.append("    <Style id=\"paddle-red-circle\">\n");
        sb.append("      <IconStyle>\n");
        sb.append("        <Icon>\n");
        sb.append("          <href>http://maps.google.com/mapfiles/kml/paddle/red-circle.png</href>\n");//apple
        sb.append("        </Icon>\n");
        sb.append("        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\n");
        sb.append("      </IconStyle>\n");
        sb.append("    </Style>\n");
        sb.append("    <Style id=\"hiker-icon\">\n");
        sb.append("      <IconStyle>\n");
        sb.append("        <Icon>\n");
        sb.append("          <href>http://maps.google.com/mapfiles/kml/shapes/motorcycling.png</href>\n");//robot
        sb.append("        </Icon>\n");
        sb.append("        <hotSpot x=\"0\" y=\".5\" xunits=\"fraction\" yunits=\"fraction\"/>\n");
        sb.append("      </IconStyle>\n");
        sb.append("    </Style>\n");
        sb.append("    <Style id=\"find the fruits\">\n");
        sb.append("      <ListStyle>\n");
        sb.append("        <listItemType>find the fruits</listItemType>\n");
        sb.append("      </ListStyle>\n");
        sb.append("    </Style>\n");
        sb.append(" ");

    }
    
    public StringBuffer getSB() {
        return sb;
    }

    public void writeGraph(DGraph graph) {
    	for (int i = 0; i <graph.ver.size() ; i++) {
            sb.append("<Placemark>\n" + 
        "    <description>" + 
            		"vertex number:").append(graph.ver.get(i).getKey()).append("</description>\n")
            .append("    <Point>\n").append("      <coordinates>")
            .append(graph.ver.get(i).getLocation().x()).append(",").append(graph.ver.get(i).getLocation().y())
            .append(",0</coordinates>\n").append("    </Point>\n").append("  </Placemark>\n");
		}

    }


    public void writeRobot(ArrayList<robots> rob) {
        LocalDateTime current = LocalDateTime.now();
        
        for (int i=0;i<rob.size();i++){
        	sb.append("<Placemark>\n");
        	sb.append("   <TimeStamp>\n");
        	sb.append("      <when>"+current+"</when>\n");
        	sb.append("   </TimeStamp>\n");
        	sb.append("   <styleUrl>#hiker-icon</styleUrl>\n");
        	sb.append("   <Point>\n");
        	sb.append("      <coordinates>"+rob.get(i).getLocation().x()+","+rob.get(i).getLocation().y()+",0</coordinates>\n");
        	sb.append("   </Point>\n");
        	sb.append("</Placemark>");

        }
    }
    
    /**
     * function that gets a fruit list
     * @param fr
     * @param game
     */
    public void writeFruit(ArrayList<fruits> fr) {
        LocalDateTime current = LocalDateTime.now();	
        for (int i=0;i<fr.size();i++){
            String type = "#paddle-ylw-circle";//banana
            if (fr.get(i).getType()==1){
                type = "#paddle-red-circle";//apple
            }
            sb.append("<Placemark>\n");
            sb.append("   <TimeStamp>\n");
        	sb.append("      <when>"+current+"</when>\n");
            sb.append("   </TimeStamp>\n");
            sb.append("   <styleUrl>"+type+"</styleUrl>\n");
            sb.append("   <Point>\n");
            sb.append("      <coordinates>"+fr.get(i).getLocation().x()+","+fr.get(i).getLocation().y()+",0</coordinates>\n");
            sb.append("   </Point>\n");
            sb.append("</Placemark>");

        }
    }

    
    /**
     * function that save that string into a file.
     * @param fileName - the file name.
     */
    public void Save(String fileName){
        sb.append("  </Document>\n" +                "</kml>");
        File file = new File(fileName+".kml");
        try {
            FileWriter fw = new FileWriter(file);
			fw.write(String.valueOf(sb));
	        fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

}
