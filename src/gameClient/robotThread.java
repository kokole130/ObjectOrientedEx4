package gameClient;

import java.awt.Graphics;
import java.awt.List;
import java.util.ArrayList;
import java.util.LinkedList;

import org.json.JSONException;
import org.json.JSONObject;

import dataStructure.edge_data;
import elements.Robot;
import elements.robots;
import sun.awt.RepaintArea;
import utils.Point3D;

public class robotThread extends Thread {

	robots robot;
	MyGameGUI g;

	public robotThread(MyGameGUI game,robots r) {
		this.robot=r;
		this.g=game;
	}

	public void run() {
		
		while(g.isRun) {
//				g.initRobots(g.game.getRobots().toString());
				g.initRobots(g.game.move().toString());
				g.repaint();
					
				
				LinkedList<String> log = (LinkedList<String>) g.game.move();
				if(log!=null) {
					long t = g.game.timeToEnd();
					for(int i=0;i<log.size();i++) {
						String robot_json = log.get(i);
						try {
							JSONObject line = new JSONObject(robot_json);
							JSONObject ttt = line.getJSONObject("Robot");
							int rid = ttt.getInt("id");
							int src = ttt.getInt("src");
							int dest = ttt.getInt("dest");
						
							if(dest==-1) {	
								dest = nextNode(gg, src);
								game.chooseNextEdge(rid, dest);
								System.out.println("Turn to node: "+dest+"  time to end:"+(t/1000));
								System.out.println(ttt);
							}
						} 
						catch (JSONException e) {e.printStackTrace();}
					}
				}
				
				
				
				
			}
//				g.repaint();
//				g.game.chooseNextEdge(robot.getID(), robot.getDest());
//				double destx = g.graph.graph.ver.get(robot.getDest()).getLocation().x();
//				double srcx = robot.getLocation().x();
//				double steps = (destx - srcx)/20;
//				Point3D min=new Point3D(Integer.MAX_VALUE, Integer.MAX_VALUE);
//				Point3D max=new Point3D(Integer.MIN_VALUE,Integer.MIN_VALUE );
//				g.initMinMax(min, max);
//				if(srcx<destx) {
//					for (double i = srcx; i < destx; i+=steps) {
//						double y=FX(i);
////						robot.setPos(i, y);
//						g.update(g.getGraphics());
//					}
//				}
//				else {
//					for (double i = srcx; i > destx; i+=steps) {
//						double y=FX(i);
////						robot.setPos(i, y);
//						g.update(g.getGraphics());
//
//					}
//				}
//				g.game.move();
//				robot.setSrc(robot.getDest());
//				robot.chooseNextNode(-1);
//				System.out.println(robot.getValue());
//			}
		
	}

	private double FX(double x) {
		double y;
		Point3D src = robot.getLocation();
		Point3D dest = g.graph.graph.ver.get(robot.getDest()).getLocation();

		double m=(src.y()-dest.y())/((src.x()-dest.x()));

		y=m*(x-dest.x())+dest.y();

		return y;
	}
}
