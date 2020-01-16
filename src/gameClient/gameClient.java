package gameClient;

import utils.Point3D;

public interface gameClient {
	
	public int getNumOfRobots(int scenario);
	
	public void initMinMax(Point3D min,Point3D max);
	
	public void initFruits(String jsonStr);
	
	public void updateFruits(String jsonStr);
	
	public void initRobots(String jsonStr);
	
	public void updateRobots(String jsonStr);
}
