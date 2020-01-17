package gameClient;

import java.awt.Graphics;

import Server.game_service;

public interface game {

	public void setIsRun(boolean flag);

	public game_service getGame();
	
	public Graphics getGraphics();
	
	public double getTimeToEnd();
	
	public double scale(double pos, double minpos, double maxpos, int minf, int maxf);
}
