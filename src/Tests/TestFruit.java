package Tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import elements.Fruit;
import elements.fruits;
import gameClient.MyGameGUI;
import utils.Point3D;

public class TestFruit {
	MyGameGUI gg=new MyGameGUI(new DGraph());
	static int i=1;

	@BeforeClass
	public static void bfrClass() {
		System.out.println("Fruit Test");
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
	public void jsonStrTest() {
		game_service game= Game_Server.getServer(2);
		game.startGame();
		String fruits=game.getFruits().toString();
		gg.initFruits(fruits);
		assertEquals(3, gg.fruits.size());
		game.stopGame();
	}
	
	@Test
	public void SetGetTest() {
		fruits f=new Fruit(7, 1, new Point3D(5,1));
		assertEquals(7, f.getValue());
		assertEquals(1, f.getType());
		assertEquals(new Point3D(5,1), f.getLocation());

	}
}
