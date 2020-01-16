package gameClient;

import java.awt.Label;

import javax.swing.JLabel;

import Server.game_service;

public class ClockThread extends Thread{
	long seconds;
	MyGameGUI g;
	JLabel clock;

	public ClockThread(MyGameGUI game,JLabel label) {
		this.seconds=game.game.timeToEnd()/1000;
		this.clock=label;
		this.g=game;
	}

	public void run() {
		for (int i = (int)this.seconds; i >=0; i--) {
			int sec=(int)i;
			clock.setText("TIME: "+sec);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		g.game.stopGame();
		g.isRun=false;
		g.b.setEnabled(false);

	}

}
