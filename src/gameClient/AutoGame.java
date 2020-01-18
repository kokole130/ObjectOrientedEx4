package gameClient;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.Vertex;
import dataStructure.node_data;
import elements.Fruit;
import elements.Robot;
import elements.fruits;
import elements.robots;
import utils.Point3D;

public class AutoGame extends JFrame implements Runnable,game, ActionListener{

	Graph_Algo graph=new Graph_Algo();
	ArrayList<fruits> fruits= new ArrayList<>();
	ArrayList<robots> robots=new ArrayList<>();
	JLabel clock=new JLabel("Time:");
	JLabel value=new JLabel("Value: 0");
	JPanel p = new JPanel();
	JButton b = new JButton("Start Game!");
	game_service game;
	boolean isRun;
	int map;
	Image backGround;
	Graphics doubleG;
	
	/**
	 * constructor for the automatic game
	 */
	public AutoGame(){
		isRun=false;
		InitGUI();
		this.setVisible(true);
	}

	public static void main(String[] args) {
		AutoGame s = new AutoGame();
	}
	/**
	 * Side function that create all the window, the buttons and the game interface.
	 */
	private void InitGUI() {

		this.setSize(1400, 1000);
		this.setLocationRelativeTo(null);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		p.add(b);
		b.setVisible(true);
		b.setEnabled(false);
		b.addActionListener(this);
		p.add(clock);
		p.add(value);
		clock.setVisible(true);
		value.setVisible(true);

		this.add(p);
		p.setVisible(true);

		MenuBar menuBar = new MenuBar();
		Menu menu1 = new Menu("Menu");
		menuBar.add(menu1);

		MenuItem setgame=new MenuItem("Set Game");

		menu1.add(setgame);

		this.setMenuBar(menuBar);

		setgame.addActionListener(this);

		this.setVisible(true);		
	}
	/**
	 * Side function that set the background on another 'Graphics' after we draw the graph,
	 * for double buffering.
	 */
	@Override
	public void update(Graphics g) {
		if(backGround==null) {
			backGround=createImage(this.getSize().width, this.getSize().height);
			doubleG=backGround.getGraphics();
		}
		doubleG.setColor(getBackground());
		doubleG.fillRect(0, 0, this.getSize().width, this.getSize().height);
		doubleG.setColor(getForeground());
		paint(doubleG);

		g.drawImage(backGround,0,0,this);
		Point3D min=new Point3D(Integer.MAX_VALUE, Integer.MAX_VALUE);
		Point3D max=new Point3D(Integer.MIN_VALUE,Integer.MIN_VALUE );

		initMinMax(min, max);

		for (int i = 0; i < fruits.size(); i++) {//draw each fruit on the window
			this.fruits.get(i).DrawFruit(this,min.x(),min.y(),max.x(),max.y());
		}

		for (int i = 0; i < robots.size(); i++) {
			robots.get(i).DrawRobot(this, min.x(), min.y(), max.x(), max.y());;
		}
	}
	/**
	 * The main function that manage a dialog between the game to the user.
	 * first- the user should press set game , to initial a specific map ,
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String ans=e.getActionCommand();
		if(ans.equals("Set Game")) {

			update(getGraphics());

			JFrame frame = new JFrame();
			String choose;
			String []scenario= {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"
					,"16","17","18","19","20","21","22","23"};				
			choose = (String) JOptionPane.showInputDialog(frame,"Choose a map between 0 to 23",
					"Scenario",JOptionPane.QUESTION_MESSAGE,null,scenario,scenario[0]);
			this.map=Integer.parseInt(choose);
			game=Game_Server.getServer(this.map);
			this.graph.graph.init(game.getGraph());
			this.initFruits(game.getFruits().toString());
			robotsFirstLocating();
			this.clock.setText("Time: "+game.timeToEnd()/1000);
			this.b.setEnabled(true);

			repaint();
		}
		if(ans.equals("Start Game!")) {
			game.startGame();
			isRun=true;
			Thread t=new Thread(this);
			t.start();
			ClockThread t1=new ClockThread(this, clock);
			t1.start();
		}
		repaint();

	}
	
	/**
	 * function that takes from the server the number of robots for the specific map.
	 * @param scenario - the map
	 * @return the number of the robots
	 * @throws JSONException - if should a problem with the server
	 */
	public int getNumOfRobots(int scenario) throws JSONException {
		game_service temp=Game_Server.getServer(scenario);
		JSONObject object=new JSONObject(temp.toString());
		int num=object.getJSONObject("GameServer").getInt("robots");
		return num;
	}

	long start = System.currentTimeMillis();
	
	/**
	 * function that gets from the server the location of the robots and the fruits,
	 * in time the game is running, and draw them after the changes.
	 * @param game2
	 */
	public  void moveRobots(game_service game2) {
		if(game.isRunning()) {
			initRobots(this.game.getRobots().toString());
			ArrayList<String> log=(ArrayList<String>) game.move();
			if(log!=null) {
				for (int i = 0; i < log.size(); i++) {
					String robot_json=log.get(i);
					try {
						JSONObject ob=new JSONObject(robot_json);
						JSONObject r=ob.getJSONObject("Robot");
						int rid=r.getInt("id");
						int src=r.getInt("src");
						int dest=r.getInt("dest");
						String p=r.getString("pos");
						int val=r.getInt("value");
						Point3D pos=new Point3D(p);

						if(dest==-1) {
							if(robots.get(rid).getNextPath().size()==0) {
								robots.get(rid).setNextPath(robotNextDest(rid));
							}
							dest=this.robots.get(rid).getNextPath().remove(0).getKey();
							game2.chooseNextEdge(rid, dest);
						}
						//DrawRobot(this,pos);

					}
					catch(JSONException e) {
						e.printStackTrace();
					}

				}
			}
			update(getGraphics());
		}
	}

//SCLAES***************************SCALES INITIALIZE*******************************************************

	/**
	 * function that set the minimum x and y and the maximum x and y on 2 points. (for a good mapping on the window)
	 * @param min - minimum point
	 * @param max - maximum point
	 */
	public void initMinMax(Point3D min,Point3D max) {
		for (int i = 0; i < this.graph.graph.ver.size(); i++) {
			if(this.graph.graph.ver.get(i) != null) {
				Vertex tmp = this.graph.graph.ver.get(i);
				if(tmp.getLocation().x()>max.x()) {
					max.setX(tmp.getLocation().x());
				}
				if(tmp.getLocation().x()<min.x()) {
					min.setX(tmp.getLocation().x());
				}
				if(tmp.getLocation().y()>max.y()) {
					max.setY(tmp.getLocation().y());
				}
				if(tmp.getLocation().y()<min.y()) {
					min.setY(tmp.getLocation().y());
				}
			}
		}
	}

	/**
	 * 
	 * @param data denote some data to be scaled
	 * @param r_min the minimum of the range of your data
	 * @param r_max the maximum of the range of your data
	 * @param t_min the minimum of the range of your desired target scaling
	 * @param t_max the maximum of the range of your desired target scaling
	 * @return
	 */
	public double scale(double data, double r_min, double r_max, 
			double t_min, double t_max)
	{

		double res = ((data - r_min) / (r_max-r_min)) * (t_max - t_min) + t_min;
		return res;
	}

//INIT***************************INTIALIZE MAP OBJECTS******************************************************
	
	/**
	 * Side function that initial all the fruits from json string. 
	 * @param jsonStr - json string 
	 */
	public void initFruits(String jsonStr) {
		try {
			jsonStr="{"+'"'+"Fruits"+'"'+":"+jsonStr+"}";
			JSONObject object = new JSONObject(jsonStr);
			JSONArray fruits = object.getJSONArray("Fruits");

			int type;
			double value;
			String pos;
			for (int i = 0; i < fruits.length(); ++i) {				
				type = fruits.getJSONObject(i).getJSONObject("Fruit").getInt("type");
				pos = fruits.getJSONObject(i).getJSONObject("Fruit").getString("pos");
				value=fruits.getJSONObject(i).getJSONObject("Fruit").getDouble("value");
				this.fruits.add(new Fruit(value,type,new Point3D(pos)));
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * function that update the location of all the fruits when the game is running.
	 * @param jsonStr - json string.
	 */
	public void updateFruits(String jsonStr) {
		try {
			jsonStr="{"+'"'+"Fruits"+'"'+":"+jsonStr+"}";
			JSONObject object = new JSONObject(jsonStr);
			JSONArray fruits = object.getJSONArray("Fruits");

			int type;
			double value;
			String pos;
			for (int i = 0; i < fruits.length(); ++i) {				
				type = fruits.getJSONObject(i).getJSONObject("Fruit").getInt("type");
				pos = fruits.getJSONObject(i).getJSONObject("Fruit").getString("pos");
				value=fruits.getJSONObject(i).getJSONObject("Fruit").getDouble("value");

				this.fruits.get(i).setType(type);
				this.fruits.get(i).setValue(value);
				this.fruits.get(i).setPos(pos);
			}


		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * function that initial the location of all the robots from a json stirng.
	 * @param jsonStr - json string
	 */
	public void initRobots(String jsonStr) {
		try {
			jsonStr="{"+'"'+"Robots"+'"'+":"+jsonStr+"}";
			JSONObject object = new JSONObject(jsonStr);
			JSONArray robot = object.getJSONArray("Robots");

			int robot_id,src,dest;
			double value,speed;
			String pos;
			for (int i = 0; i < robot.length(); ++i) {				
				robot_id = robot.getJSONObject(i).getJSONObject("Robot").getInt("id");
				pos = robot.getJSONObject(i).getJSONObject("Robot").getString("pos");
				value=robot.getJSONObject(i).getJSONObject("Robot").getDouble("value");
				src=robot.getJSONObject(i).getJSONObject("Robot").getInt("src");
				dest=robot.getJSONObject(i).getJSONObject("Robot").getInt("dest");
				speed=robot.getJSONObject(i).getJSONObject("Robot").getDouble("speed");
				this.robots.add(new Robot(robot_id,src,dest,value,speed,new Point3D(pos)));
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * function that update the location of all the robots at the game is running.
	 * @param jsonStr - json string
	 */
	public void updateRobots(String jsonStr) {
		try {

			jsonStr="{"+'"'+"Robots"+'"'+":"+jsonStr+"}";
			JSONObject object = new JSONObject(jsonStr);
			JSONArray robot = object.getJSONArray("Robots");

			int robot_id,src,dest;
			double value,speed;
			String pos;
			for (int i = 0; i < robot.length(); ++i) {				
				robot_id = robot.getJSONObject(i).getJSONObject("Robot").getInt("id");
				pos = robot.getJSONObject(i).getJSONObject("Robot").getString("pos");
				value=robot.getJSONObject(i).getJSONObject("Robot").getDouble("value");
				src=robot.getJSONObject(i).getJSONObject("Robot").getInt("src");
				dest=robot.getJSONObject(i).getJSONObject("Robot").getInt("dest");
				speed=robot.getJSONObject(i).getJSONObject("Robot").getDouble("speed");

				Point3D tmp = new Point3D(pos);
				this.robots.get(i).setSrc(src);
				this.robots.get(i).setPos(tmp.x(), tmp.y());
				this.robots.get(i).setValue(value);
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}

//DRAW***************************DRAW FUNCTIONS*************************************************************

	/**
	 * function that draw the graph, fruits and the robots on the window in all stages of the game.
	 */
	public void paint(Graphics g) {

		super.paint(g);

		g=(Graphics2D)g;

		Point3D min=new Point3D(Integer.MAX_VALUE, Integer.MAX_VALUE);
		Point3D max=new Point3D(Integer.MIN_VALUE,Integer.MIN_VALUE );

		initMinMax(min, max);

		DrawVertex(g,min,max);

		g.setColor(Color.RED);

		((Graphics2D) g).setStroke(new BasicStroke(3));//for bolding the pen radius.

		DrawEdges(g, min, max);

		for (int i = 0; i < fruits.size(); i++) {//draw each fruit on the window
			this.fruits.get(i).DrawFruit(this,min.x(),min.y(),max.x(),max.y());
		}

		for (int i = 0; i < robots.size(); i++) {
			robots.get(i).DrawRobot(this, min.x(), min.y(), max.x(), max.y());;
		}

	}

	/**
	 * function that draw a robot on the window according to the point we get.
	 * @param game - this game
	 * @param pos - the location to draw
	 */
	public void DrawRobot(game game,Point3D pos) {
		Point3D min=new Point3D(Integer.MAX_VALUE, Integer.MAX_VALUE);
		Point3D max=new Point3D(Integer.MIN_VALUE,Integer.MIN_VALUE );
		initMinMax(min, max);		
		String path="robot.png";
		File file=new File(path);
		Image img=null;
		try {
			img=ImageIO.read(file);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		Graphics g = game.getGraphics();

		double xs=game.scale(pos.x(), min.x(), max.x(), 100, 950);
		double ys=game.scale(pos.y(), min.y(), max.y(), 100, 950);
		g.drawImage(img,(int)xs,(int)ys, null);
	}

	/**
	 * function that draw all the vertex on the window
	 * @param g - this graphics
	 * @param min - minimum point
	 * @param max - maximum point
	 */
	private void DrawVertex(Graphics g, Point3D min, Point3D max) {
		for (int i = 0; i < this.graph.graph.ver.size(); i++) {//draw all the vertex in the window

			if(this.graph.graph.ver.get(i) != null)
			{//only when the vertex is exist (and not removed before)
				Vertex p = this.graph.graph.ver.get(i).copy();//copy this vertex to p
				g.setColor(Color.BLUE);
				double x = p.getLocation().x();
				x=scale(x,min.x(), max.x(),100,950);
				double y = p.getLocation().y();
				y=scale(y, min.y(), max.y(),100,950);
				g.fillOval((int)x-7,(int)y-7 , 14, 14);//for locating the vertex in the window
				g.setFont(new Font("Monaco", Font.PLAIN, 22));
				g.drawString(""+p.getKey(), (int)(x+7),(int)(y+7-15));//for show the key of the vertex
			}
		}
	}

	/**
	 * function that draw all the edges on the window
	 * @param g - this graphics
	 * @param min - minimum point
	 * @param max - maximum point
	 */
	private void DrawEdges(Graphics g, Point3D min, Point3D max) {
		for (int i = 0; i < this.graph.graph.ver.size(); i++) {//draw all the edges in the window
			if(this.graph.graph.edge.get(i)!=null) {//get in only if the source HashMap is exist(and dont removed by remove source vertex)
				for (int j = 0; j < this.graph.graph.ver.size(); j++) {
					if(this.graph.graph.edge.get(i).containsKey(j)) {//just when the specific HashMap contains the right edge
						Vertex v1=this.graph.graph.ver.get(this.graph.graph.edge.get(i).get(j).getSrc()).copy();
						Vertex v2=this.graph.graph.ver.get(this.graph.graph.edge.get(i).get(j).getDest()).copy();
						int xs1=(int)scale(v1.getLocation().x(), min.x(), max.x(), 100, 950);
						int ys1=(int)scale(v1.getLocation().y(), min.y(), max.y(), 100, 950);
						int xs2=(int)scale(v2.getLocation().x(), min.x(), max.x(), 100, 950);
						int ys2=(int)scale(v2.getLocation().y(), min.y(), max.y(), 100, 950);

						g.drawLine(xs1,ys1,xs2,ys2);//draw an edge between v1 and v2
						int x=0;
						int y=0;
						int tmp1=xs1;
						int tmp2=ys1;
						for (int k = 0; k < 3; k++) {//draw  the direction of the edge with midsegment formula.

							x=(tmp1+xs2)/2;
							y=(tmp2+ys2)/2;
							tmp1=x;
							tmp2=y;
						}

						g.setColor(Color.yellow);
						g.fillOval(x-5, y-5, 10, 10);
						g.setColor(Color.RED);
						//
						//							g.drawString(this.graph.graph.edge.get(i).get(j).getWeight()+"",
						//									(int)((xs1+xs2)/2),
						//									(int)((ys1+ys2)/2));													
					}
				}
			}
		}
	}

	/**
	 * function that returns the status of the game.
	 */
	@Override
	public void setIsRun(boolean flag) {
		this.isRun=flag;
	}

	/**
	 * function that returns the server game.
	 */
	@Override
	public game_service getGame() {
		return this.game;
	}

	/**
	 * the function that invoked at the moment the user press on start game.
	 * the function gets the location of the robots and fruits , draw them at the correct location,
	 * calculates at the same time the value of the user , and when the game is over - present the score to the user.
	 */
	@Override
	public void run() {
		double sum=0;
		while(game.isRunning()) {
			updateFruits(game.getFruits().toString());
			updateRobots(game.getRobots().toString());
			moveRobots(game);
			if(System.currentTimeMillis() - start> 1000/10){
				repaint();
				start = System.currentTimeMillis();
			}
			
			sum=0;
			for (int j = 0; j < robots.size(); j++) {
				sum+=robots.get(j).getValue();
			}
			value.setText("Value: "+sum);
		}


	}

	/**
	 * @param pos denote some data to be scaled
	 * @param minpos the minimum of the range of your data
	 * @param maxpos the maximum of the range of your data
	 * @param minf the minimum of the range of your desired target scaling
	 * @param maxf the maximum of the range of your desired target scaling
	 * @return
	 */
	@Override
	public double scale(double pos, double minpos, double maxpos, int minf, int maxf) {

		double res = ((pos - minpos) / (maxpos-minpos)) * (maxf - minf) + minf;
		return res;
	}

	/**
	 * 
	 */
	private void robotsFirstLocating() {
		initFruits(game.getFruits().toString());
		Point3D min=new Point3D(Integer.MAX_VALUE, Integer.MAX_VALUE);
		Point3D max=new Point3D(Integer.MIN_VALUE,Integer.MIN_VALUE );
		initMinMax(min, max);
		for (int i = 0; i < fruits.size(); i++) {//draw each fruit on the window
			this.fruits.get(i).DrawFruit(this,min.x(),min.y(),max.x(),max.y());
		}
		int num=0;
		try {
			num=getNumOfRobots(this.map);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		for (int i = 0; i <num; i++) {
			Vertex temp = graph.graph.ver.get(this.fruits.get(i).getEdge(graph.graph.edge,graph.graph.ver).getSrc());
			game.addRobot(temp.getKey());
			DrawRobot(this, temp.getLocation());
		}
		initRobots(game.getRobots().toString());
	}
	
	/**
	 * Function that computing the full next path of any robot to get to the fruit.
	 * @param robot id(not vertex)
	 * @return the path for the next closest fruit
	 */
	public List <node_data> robotNextDest(int robot) {
		int index=-1;
		double minDistance=Integer.MAX_VALUE;
		List <node_data> s = new ArrayList<>();
		initFruits(game.getFruits().toString());
		for (int i = 0; i < fruits.size(); i++) {
			Vertex v = this.graph.graph.ver.get(fruits.get(i).getEdge(graph.graph.edge, graph.graph.ver).getSrc());
			double tmp = graph.shortestPathDist(this.robots.get(robot).getSrc(),v.getKey());
			if((tmp<minDistance)&&(fruits.get(i).getTag()==-1)) {
				index=i;
				minDistance = tmp;
				s=graph.shortestPath(this.robots.get(robot).getSrc(),v.getKey());
			}
		}
		if(index == -1) {
			return null;
		}
		//adding the vertex after the fruit, in order to take the fruit
		Vertex last = this.graph.graph.ver.get(fruits.get(index).getEdge(graph.graph.edge, graph.graph.ver).getDest());
		fruits.get(index).setTag(robot);
		s.add(last);
		return s;
	}
	
	/**
	 * function that returns the time left to the game.
	 */
	@Override
	public double getTimeToEnd() {
		return game.timeToEnd();
	}


}
