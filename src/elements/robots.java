package elements;

import gameClient.MyGameGUI;
import utils.Point3D;

public interface robots {
	
	public int getID();
	
	public int getSrc();

	public int getDest();

	public Point3D getLocation();
	
	public double getSpeed();
	
	public double getValue();
	
	public void DrawRobot(MyGameGUI game);

}
