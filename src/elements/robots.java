package elements;

import java.util.ArrayList;
import java.util.List;

import dataStructure.node_data;
import gameClient.MyGameGUI;
import gameClient.game;
import utils.Point3D;

public interface robots {
	
	public int getID();
	
	public int getSrc();

	public int getDest();

	public Point3D getLocation();
	
	public double getSpeed();
	
	public double getValue();
	
	public void DrawRobot(game game,double minx,double miny,double maxx,double maxy);
	
	public void setClicked(boolean p);
	
	public boolean getClicked();
	
	public void chooseNextNode(int dest);

	public void setSrc(int src);
	
	public void setPos(double x, double y);

	public void setValue(double value);
	
	public List<node_data> getNextPath();

	public void setNextPath(List<node_data> robotNextDest);
	
	public int getTag();
	
	public void setTag(int tag);
	
}
