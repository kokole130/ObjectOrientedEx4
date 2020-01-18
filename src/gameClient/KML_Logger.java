package gameClient;

import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class KML_Logger {
	
	public static void writeToKML(double x,double y) {
		try {
			FileWriter fw=new FileWriter(fileName);
			PrintWriter out=new PrintWriter(fw);
			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			out.println("<kml xmlns=\"http://earth.google.com/kml/2.2\">\n");
			out.println("<Document>\n");
			out.println("<name>Points with TimeStamps</name>\n");
			out.println("<Placemark>\n");
			out.println("<TimeStamp>\n");
			out.println("<when>"+System.currentTimeMillis()+"</when>\n");
			out.println("</TimeStamp>\n");
			out.println("<styleUrl>#paddle-a</styleUrl>\n");
			out.println("<Point>\n");
			out.println("<coordinates>"+x+","+y+","+"0"+"</coordinates>\n");
			out.println("</Point>\n");
			out.println("</Placemark>\n");
			out.println("</Document>\n");
			out.println("</kml>\n");

			
			out.close();

		}
		catch(IOException e) {
			e.printStackTrace();
		}

	}
}
