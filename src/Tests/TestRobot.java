package Tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.json.JSONException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import elements.Robot;
import elements.robots;
import gameClient.MyGameGUI;
import utils.Point3D;

public class TestRobot {
	
	
	MyGameGUI gg=new MyGameGUI(new DGraph());
	static int i=1;

	@BeforeClass
	public static void bfrClass() {
		System.out.println("Robot Test");
	}

	@AfterClass
	public static void aftClass() {
		System.out.println("Finished Test");
	}

	@BeforeEach
	public void setUp() throws Exception {
		System.out.println("Test number "+i);
		i++;
	}
	
	@Test
	public void JsonStrTest() {
		game_service game= Game_Server.getServer(2);
		game.startGame();
		game.addRobot(0);
		game.addRobot(1);
		String robot=game.getRobots().toString();
		gg.initRobots(robot);
		assertEquals(1, gg.robots.size());
		game.stopGame();
	}
	
	@Test
	public void SetGetTest() {
		robots r=new Robot(0, 2, 4, 0, 1, new Point3D(1,1));
		assertEquals(0, r.getID());
		assertEquals(2, r.getSrc());
		assertEquals(4, r.getDest());
		assertEquals(0, r.getValue());
		assertEquals(1, r.getSpeed());
		assertEquals(new Point3D(1,1), r.getLocation());

		r.setPos(2, 3);
		r.setSrc(5);
		r.setValue(5);
		assertEquals(new Point3D(2,3), r.getLocation());
		assertEquals(5,r.getSrc());
		assertEquals(5,r.getValue());

	}
	
}
