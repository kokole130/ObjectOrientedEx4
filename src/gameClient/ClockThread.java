package gameClient;

import java.awt.Label;

import javax.swing.JLabel;

import Server.game_service;

public class ClockThread extends Thread{
	double seconds;
	game g;
	JLabel clock;

	public ClockThread(game game,JLabel label) {
		System.out.println(game.getTimeToEnd());
		this.seconds=game.getTimeToEnd()/1000;
		this.clock=label;
		this.g=game;
	}

	public void run() {
		for (int i =(int)this.seconds; i >=0; i--) {
			int sec=(int)i;
			clock.setText("TIME: "+sec);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		//g.getGame().stopGame();
		g.setIsRun(false);
	}

}
