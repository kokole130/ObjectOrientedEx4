package gameClient;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.corba.se.spi.activation.Server;

import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.Vertex;
import elements.fruits;
import elements.robots;

public class AutoGame {
	
	Graph_Algo graph=new Graph_Algo();
	game_service game;
	boolean isRun;
	int map;
	ArrayList<fruits> fruits= new ArrayList<>();
	ArrayList<robots> robots=new ArrayList<>();
	
	public ArrayList<robots> initStartRobots(Graph_Algo g,ArrayList<robots> r,ArrayList<fruits> f,int map){
		fruits=f;
		robots=r;
		game=Game_Server.getServer(map);
		int num=0;
		try {
			num=getNumOfRobots(map);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		double minDis=Double.MAX_VALUE;
		
		for (int i = 0; i < num; i++) {
			for (int j = 0; j < g.graph.ver.size(); j++) {
				Vertex temp=g.graph.ver.get(j);
				for (int j2 = 0; j2 < fruits.size(); j2++) {
					if(temp.getLocation().distance2D(fruits.get(j2).getLocation())<minDis) {
						double x=temp.getLocation().x();
						double y=temp.getLocation().y();
						r.get(i).setPos(x, y);
					}
				}
			}
		}
		
		
		
	}
	
	public int getNumOfRobots(int scenario) throws JSONException {
		game_service temp=Game_Server.getServer(scenario);
		JSONObject object=new JSONObject(temp.toString());
		int num=object.getJSONObject("GameServer").getInt("robots");
		return num;
	}
	
	
}
