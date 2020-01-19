package Tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import gameClient.MyGameGUI;
import utils.Point3D;

public class TestGraph {
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
	public void jsonStrTest() {
		game_service game= Game_Server.getServer(2);
		String jsonGraph=game.getGraph();
		DGraph dg=new DGraph();
		dg.init(jsonGraph);
		assertEquals(22	, dg.edgeSize());
		assertEquals(11	, dg.nodeSize());

	}
	
	@Test
	public void MinMaxTest() {
		Point3D min=new Point3D(Integer.MAX_VALUE, Integer.MAX_VALUE);
		Point3D max=new Point3D(Integer.MIN_VALUE,Integer.MIN_VALUE );
		game_service game= Game_Server.getServer(2);
		String jsonGraph=game.getGraph();
		DGraph dg=new DGraph();
		dg.init(jsonGraph);
		gg.graph.graph=dg;

		gg.initMinMax(min, max);
		System.out.println(min);
		System.out.println(max);

		assertEquals(new Point3D(35.18753053591606,32.10154696638656), min);
		assertEquals(new Point3D(35.20792948668281,32.10785303529412), max);

		
	}
	
}
