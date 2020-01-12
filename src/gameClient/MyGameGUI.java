package gameClient;

import utils.StdDraw;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import elements.Fruit;
import elements.Robot;
import elements.fruits;
import elements.robots;
import utils.Point3D;
import algorithms.Graph_Algo;
import dataStructure.Vertex;

public class MyGameGUI extends JFrame implements ActionListener,MouseListener,KeyListener{

	Graph_Algo graph=new Graph_Algo();
	ArrayList<fruits> fruits= new ArrayList<>();
	ArrayList<robots> robots=new ArrayList<>();
	int map;
	Label clock;
	Game_Server game;

	public MyGameGUI() {
		InitGUI();
		this.setVisible(true);
	}

	public void InitGUI() {
		
		//StdDraw.setCanvasSize(1000, 1000);
		this.setSize(1000, 1000);
		
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		MenuBar menuBar = new MenuBar();
		//StdDraw.this.
		Menu menu1 = new Menu("Menu");
		menuBar.add(menu1);

		MenuItem setgame=new MenuItem("Set Game");
		MenuItem addRobot=new MenuItem("Add Robot");


		menu1.add(setgame);
		menu1.add(addRobot);

		this.setMenuBar(menuBar);

		setgame.addActionListener(this);
		addRobot.addActionListener(this);

		this.addMouseListener(this);

		this.setVisible(true);
	}

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



	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}


	public void paint(Graphics g) {

		super.paint(g);

		g=(Graphics2D)g;
		
		Point3D min=new Point3D(Integer.MAX_VALUE, Integer.MAX_VALUE);
		Point3D max=new Point3D(Integer.MIN_VALUE,Integer.MIN_VALUE );
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
		System.out.println(max.x()+","+max.y()+","+min.x()+","+min.y());
		
		DrawVertex(g,min,max);
		

		g.setColor(Color.RED);
		((Graphics2D) g).setStroke(new BasicStroke(3));//for bolding the pen radius.
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
						int tmp1=v1.getLocation().ix();
						int tmp2=v1.getLocation().iy();
						for (int k = 0; k < 3; k++) {//draw  the direction of the edge with midsegment formula.
							x=(tmp1+v2.getLocation().ix())/2;
							y=(tmp2+v2.getLocation().iy())/2;
							tmp1=x;
							tmp2=y;
						}

						g.setColor(Color.yellow);
						g.fillOval(x-5, y-5, 10, 10);
						g.setColor(Color.RED);

						g.drawString(this.graph.graph.edge.get(i).get(j).getWeight()+"",
								(int)((v1.getLocation().x()+v2.getLocation().x())/4),
								(int)((v1.getLocation().y()+v2.getLocation().y())/4));/*draw the edge weight in the middle
																						of the edge*/
					}
				}
			}
		}

		for (int i = 0; i < fruits.size(); i++) {//draw each fruit on the window
			this.fruits.get(i).DrawFruit(this,min.x(),min.y(),max.x(),max.y());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String ans=e.getActionCommand();
		if(ans.equals("Set Game")) {

			JFrame frame = new JFrame();
			String choose;
			String []scenario= {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"
					,"16","17","18","19","20","21","22","23"};				
			choose = (String) JOptionPane.showInputDialog(frame,"Choose a map between 0 to 23",
					"Scenario",JOptionPane.QUESTION_MESSAGE,null,scenario,scenario[0]);
			this.map=Integer.parseInt(choose);
			game_service temp=game.getServer(this.map);
			this.graph.graph.init(temp.getGraph());
			this.initFruits(temp.getFruits().toString());
			//System.out.println(graph.graph);

			repaint();
		}
		if(ans.equals("Add Robot")) {
			int num=0;
			try {
				num=getNumOfRobots(this.map);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			game_service temp=game.getServer(this.map);
			for (int i = 0; i < num; i++) {
				JFrame frame = new JFrame();
				String node_id=JOptionPane.showInputDialog(frame,"Choose ID to locate the robot");
				int id=Integer.parseInt(node_id);

				this.robots.add(new Robot(i,id,-1,0,1,graph.graph.ver.get(id).getLocation()));
				this.robots.get(i).DrawRobot(this);
				repaint();
			} 
		}
	}

	public int getNumOfRobots(int scenario) throws JSONException {
		game_service temp=Game_Server.getServer(scenario);
		JSONObject object=new JSONObject(temp.toString());
		int num=object.getJSONObject("GameServer").getInt("robots");
		System.out.println(num);
		return num;
	}
	
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
				System.out.println(x+","+y);
				g.fillOval((int)x,(int)y , 14, 14);//for locating the vertex in the window
				g.setFont(new Font("Monaco", Font.PLAIN, 22));
				g.drawString(""+p.getKey(), (int)(x+7),(int)(y+7-15));//for show the key of the vertex
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
	
	public static void main(String[] args) {
		MyGameGUI holut=new MyGameGUI();
		//holut.setBounds(50, 50, 1000, 1000);
		//holut.setVisible(true);
	}
}
