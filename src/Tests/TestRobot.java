package Tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import gameClient.MyGameGUI;

public class TestRobot {
	
	MyGameGUI gg;
	
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
	void setUp() throws Exception {
		System.out.println("Test number "+i);
		i++;
	}
	
	@Test
	public void JsonStrTest() {
		game_service game= Game_Server.getServer(2);
		String robot=game.getRobots().toString();
		gg.initRobots(robot);
		assertEquals(1, gg.robots.size());	
	}
}
