package elements;

import gameClient.MyGameGUI;
import utils.Point3D;

public interface fruits {
	
	public double getValue();
	
	public int getType();
	
	public Point3D getLocation();
	
	public void DrawFruit(MyGameGUI game,double minx,double miny,double maxx,double maxy);
	
}
