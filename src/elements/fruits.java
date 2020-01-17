package elements;

import java.util.ArrayList;
import java.util.HashMap;

import dataStructure.Edge;
import dataStructure.Vertex;
import dataStructure.edge_data;
import dataStructure.node_data;
import gameClient.MyGameGUI;
import gameClient.game;
import utils.Point3D;

public interface fruits {
	
	public double getValue();
	
	public int getType();
	
	public Point3D getLocation();
	
	public void DrawFruit(game game,double minx,double miny,double maxx,double maxy);
	
	public void setType(int type);
	
	public void setValue(double value);

	public void setPos(String pos);

	public edge_data getEdge(HashMap<Integer, HashMap<Integer, Edge>> edge,ArrayList<Vertex> nodes);
	
	public int getTag();
	
	public void setTag(int flag);

}
