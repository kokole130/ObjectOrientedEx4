package gameClient;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    public KML_Logger(){
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<kml xmlns=\"http://earth.google.com/kml/2.2\">\n");
        sb.append("  <Document>\n");
        sb.append("    <name>Points with TimeStamps</name>\n");
        sb.append("    <Style id=\"paddle-a\">\n");
        sb.append("      <IconStyle>\n");
        sb.append("        <Icon>\n");
        sb.append("          <href>http://maps.google.com/mapfiles/kml/paddle/A.png</href>\n" );//A
        sb.append("        </Icon>\n");
        sb.append("        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\n");
        sb.append("      </IconStyle>\n");
        sb.append("    </Style>\n");
        sb.append("    <Style id=\"paddle-b\">\n");
        sb.append("      <IconStyle>\n");
        sb.append("        <Icon>\n");
        sb.append("          <href>http://maps.google.com/mapfiles/kml/paddle/B.png</href>\n");//B
        sb.append("        </Icon>\n");
        sb.append("        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\n");
        sb.append("      </IconStyle>\n");
        sb.append("    </Style>\n");
        sb.append("    <Style id=\"hiker-icon\">\n");
        sb.append("      <IconStyle>\n");
        sb.append("        <Icon>\n");
        sb.append("          <href>http://maps.google.com/mapfiles/ms/icons/hiker.png</href>\n");//anashim
        sb.append("        </Icon>\n");
        sb.append("        <hotSpot x=\"0\" y=\".5\" xunits=\"fraction\" yunits=\"fraction\"/>\n");
        sb.append("      </IconStyle>\n");
        sb.append("    </Style>\n");
        sb.append("    <Style id=\"check-hide-children\">\n");
        sb.append("      <ListStyle>\n");
        sb.append("        <listItemType>checkHideChildren</listItemType>\n");
        sb.append("      </ListStyle>\n");
        sb.append("    </Style>\n");
        sb.append(" ");

    }
    
    public StringBuffer getSB() {
        return sb;
    }

    public void writeGraph(DGraph gg) {
        for (node_data n : gg.ver) {
            sb.append("<Placemark>\n" + 
        "    <description>" + 
            		"place num:").append(n.getKey()).append("</description>\n")
            .append("    <Point>\n").append("      <coordinates>")
            .append(n.getLocation().x()).append(",").append(n.getLocation().y())
            .append(",0</coordinates>\n").append("    </Point>\n").append("  </Placemark>\n");
        }
    }

    public void writeRobot(ArrayList<robots> rob, game_service game) {
        DateFormat d1 = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat d2 = new SimpleDateFormat("HH:mm:ss");
        String d3 = d1.format(new Date(2323223232L));
        String d4 = d2.format(new Date(2323223232L));
        String d5 = d3+"T"+d4+"Z";
        
        for (int i=0;i<rob.size();i++){
            sb.append("<Placemark>\n" + 
        "      <TimeStamp>\n" + "        <when>")
            .append(d5).append("</when>\n").append("      </TimeStamp>\n")
            .append("      <styleUrl>#hiker-icon</styleUrl>\n").append("      <Point>\n")
            .append("        <coordinates>").
            append(rob.get(i).getLocation().x()).append(",").
            append(rob.get(i).getLocation().y()).append(",0</coordinates>\n").
            append("      </Point>\n").append("    </Placemark>");
        }
    }
    
    public void writeFruit(ArrayList<fruits> fr, game_service game) {
        DateFormat d1 = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat d2 = new SimpleDateFormat("HH:mm:ss");
        String d3 = d1.format(new Date(2323223232L));
        String d4 = d2.format(new Date(2323223232L));
        String d5 = d3+"T"+d4+"Z";
    	
        for (int i=0;i<fr.size();i++){
            String type = "#paddle-a";//banana
            if (fr.get(i).getType()==1){
                type = "#paddle-b";//apple
            }
            sb.append("<Placemark>\n" + 
            "      <TimeStamp>\n" +"        <when>").append(d5)
            .append("</when>\n").append("      </TimeStamp>\n")
            .append("      <styleUrl>").append(type).append("</styleUrl>\n")
            .append("      <Point>\n").append("        <coordinates>")
            .append(fr.get(i).getLocation().x()).append(",")
            .append(fr.get(i).getLocation().y()).append(",0</coordinates>\n")
            .append("      </Point>\n").append("    </Placemark>");

        }
    }
    
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
