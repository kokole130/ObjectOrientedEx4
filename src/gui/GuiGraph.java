package gui;

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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import algorithms.Graph_Algo;
import utils.Point3D;
import dataStructure.DGraph;
import dataStructure.Edge;
import dataStructure.Vertex;
import dataStructure.node_data;

public class GuiGraph extends JFrame implements MouseListener,ActionListener{

	public Graph_Algo ga=new Graph_Algo();//for display the graph in a graphical window
	List<node_data> sp=new LinkedList<>();//for set the right shortest path every time the user execute this algorithm 
	List<node_data> tsp=new LinkedList<>();//for set the right TSP every time the user execute this algorithm
	Label connectedl=new Label();//for show the user if the graph is connected or not
	
	/**
	 *Default constructor that initializing this window 
	 */
	
	public GuiGraph() {
		InitGui();
	}
	
	/**
	 * constructor that gets Graph_Algo object and initializing this 'ga'
	 * @param g - Graph_Algo object
	 */
	
	public GuiGraph(Graph_Algo g) {
		ga.graph=(DGraph) g.copy();
		InitGui();
	}
	
	/**
	 * Function that initializing this window with all the menus and the menu bars and make them action listening/performed
	 */
	
	public void InitGui() {
		this.setSize(500, 500);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		MenuBar menuBar = new MenuBar();

		Menu menu1 = new Menu("File");
		menuBar.add(menu1);

		MenuItem save=new MenuItem("Save");
		MenuItem open=new MenuItem("Open");

		menu1.add(save);
		menu1.add(open);

		Menu menu2 = new Menu("Edit");
		menuBar.add(menu2);

		MenuItem connect=new MenuItem("Connect");
		MenuItem removeEdge=new MenuItem("Remove edge");
		MenuItem removeVertex=new MenuItem("Remove vertex");

		menu2.add(connect);
		menu2.add(removeEdge);
		menu2.add(removeVertex);

		Menu menu3 = new Menu("Algorithms");
		menuBar.add(menu3);

		MenuItem shortPathList=new MenuItem("Shortest path");
		MenuItem connected=new MenuItem("The graph is connected ?");
		MenuItem Tsp=new MenuItem("TSP");

		menu3.add(connected);
		menu3.add(shortPathList);
		menu3.add(Tsp);

		this.setMenuBar(menuBar);

		save.addActionListener(this);
		open.addActionListener(this);
		connect.addActionListener(this);
		removeEdge.addActionListener(this);
		removeVertex.addActionListener(this);
		shortPathList.addActionListener(this);
		connected.addActionListener(this);
		Tsp.addActionListener(this);

		this.addMouseListener(this);

		this.setVisible(true);

	}
	/**
	 * Function that gets an action pressed by the user and for each button - makes different actions in this window
	 */
	
	public void actionPerformed(ActionEvent e) 
	{
		int src=0,dest=0;
		double weight=0;


		String file=e.getActionCommand();

		if(file.equals("Save")) {//if the user press on save menuItem
			this.ga.save("graph.txt");
			System.out.println("the graph is saved in the project folder named 'graph.txt'");
		}
		if(file.equals("Open")) {//if the user press on open menuItem
			this.ga.init("graph.txt");
			System.out.println("the graph is opened and presented in the window");
		}
		if(file.equals("Connect")) {//if the user press on connect menuItem
			JFrame frame = new JFrame();
			String name = JOptionPane.showInputDialog(frame,"Source: ");//input from the user
			if(name!=null&&(!name.equals(""))) {//parse the source vertex key
				src=Integer.parseInt(name);
			}
			name = JOptionPane.showInputDialog(frame,"Destination: ");
			if(name!=null&&(!name.equals(""))) {//parse the destination vertex key
				dest=Integer.parseInt(name);
			}
			name = JOptionPane.showInputDialog(frame,"Weight: ");
			if(name!=null&&(!name.equals(""))) {//parse the weight of the new edge
				weight=Double.parseDouble(name);
			}
			this.ga.graph.connect(src, dest, weight);
		}
		if(file.equals("Remove edge")) {//if the user press on remove edge menuItem
			JFrame frame = new JFrame();
			String name = JOptionPane.showInputDialog(frame,"Source: ");//input from the user
			if(name!=null&&(!name.equals(""))) {//parse the source vertex key
				src=Integer.parseInt(name);
			}
			name = JOptionPane.showInputDialog(frame,"Destination: ");//input from the user
			if(name!=null&&(!name.equals(""))) {//parse the destination vertex key
				dest=Integer.parseInt(name);
			}
			this.ga.graph.removeEdge(src, dest);
		}
		if(file.equals("Remove vertex")) {//if the user press on remove vertex menuItem
			JFrame frame = new JFrame();
			String name = JOptionPane.showInputDialog(frame,"Key: ");//input from the user
			if(name!=null&&(!name.equals(""))) {//parse the vertex key
				src=Integer.parseInt(name);
			}
			this.ga.graph.removeNode(src);
		}
		if(file.equals("Shortest path")) {//if the user press on shortest path menuItem
			JFrame frame = new JFrame();
			String name = JOptionPane.showInputDialog(frame,"Source: ");//input from the user
			if(name!=null&&(!name.equals(""))) {//parse the source vertex key to beginning the traveling
				src=Integer.parseInt(name);
			}
			name = JOptionPane.showInputDialog(frame,"Destination: ");//input from the user
			if(name!=null&&(!name.equals(""))) {//parse the destination vertex key to finish the traveling
				dest=Integer.parseInt(name);
			}
			if(this.ga.isConnected(src,dest,src)) {//for the drawing the path on the window in different color. 
				this.ga.tagReset();
				this.sp=this.ga.shortestPath(src, dest);
			}
			else {//note when there is no path between source and destination
				connectedl.setText("There is not a path between "+src+" and "+dest);
				connectedl.setBounds(1, 1, 200, 30);
				add(connectedl);
			}
		}
		if(file.equals("The graph is connected ?")) {//if the user press on the graph is connected ? menuItem
			connectedl.setText("The graph is connected?: "+this.ga.isConnected());//set true/false on the window
			connectedl.setBounds(1, 1, 200, 30);
			add(connectedl);//draw it on the window
		}
		if(file.equals("TSP")) {//if the user press on TSP menuItem
			JFrame frame = new JFrame();
			String name = JOptionPane.showInputDialog(frame,"add number of keys with ',' between them: ");//input from the user
			LinkedList<Integer> s =new LinkedList<>();
			int j=0;
			if(name!=null) {//if the user puts a numbers
				for (int i = 0; i < name.length(); i++) {
					if(name.charAt(i)==',') {
						s.add(Integer.parseInt(name.substring(j,i)));//take the key number in each Integer.
						j=i+1;
					}
					if(i==name.length()-1) {
						s.add(Integer.parseInt(name.substring(i)));
						break;
					}
				}
				this.tsp=this.ga.TSP(s);
			}
		}
		repaint();//repaint the window after the changes

	}
	
	/**
	 * Function that draw this graph in this window with all the vertex and the edges.
	 */
	
	public void paint(Graphics g)
	{
		super.paint(g);

		g=(Graphics2D)g;
		for (int i = 0; i < this.ga.graph.ver.size(); i++) {//draw all the vertex in the window


			if(this.ga.graph.ver.get(i) != null)
			{//only when the vertex is exist (and not removed before)
				Vertex p = this.ga.graph.ver.get(i).copy();//copy this vertex to p
				g.setColor(Color.BLUE);
				g.fillOval(p.getLocation().ix()-7, p.getLocation().iy()-7, 14, 14);//for locating the vertex in the window
				g.setFont(new Font("Monaco", Font.PLAIN, 22));
				g.drawString(""+p.getKey(), ((p.getLocation().ix())),((p.getLocation().iy()-15)));//for show the key of the vertex
			}
		}
		g.setColor(Color.RED);
		((Graphics2D) g).setStroke(new BasicStroke(3));//for bolding the pen radius.
		for (int i = 0; i < this.ga.graph.ver.size(); i++) {//draw all the edges in the window
			if(this.ga.graph.edge.get(i)!=null) {//get in only if the source HashMap is exist(and dont removed by remove source vertex)
				for (int j = 0; j < this.ga.graph.ver.size(); j++) {
					if(this.ga.graph.edge.get(i).containsKey(j)) {//just when the specific HashMap contains the right edge
						Vertex v1=this.ga.graph.ver.get(this.ga.graph.edge.get(i).get(j).getSrc()).copy();
						Vertex v2=this.ga.graph.ver.get(this.ga.graph.edge.get(i).get(j).getDest()).copy();

						g.drawLine((int)v1.getLocation().x(),(int)v1.getLocation().y(),
								(int)v2.getLocation().x(),(int)v2.getLocation().y());//draw an edge between v1 and v2
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

						g.drawString(this.ga.graph.edge.get(i).get(j).getWeight()+"",
								(int)((v1.getLocation().x()+v2.getLocation().x())/2),
								(int)((v1.getLocation().y()+v2.getLocation().y())/2));/*draw the edge weight in the middle
																						of the edge*/
					}
				}
			}
		}

		for (int i = 1; i < sp.size(); i++) {//marked the shortest path from src to dest.
			g.setColor(Color.CYAN);
			g.drawLine(sp.get(i-1).getLocation().ix(),sp.get(i-1).getLocation().iy(),
					sp.get(i).getLocation().ix(),sp.get(i).getLocation().iy());	/*draw in different color all the shortest path*/	
		}
		if(tsp!=null) {
			for (int i = 1; i < tsp.size(); i++) {//marked the TSP from src to dest.
				g.setColor(Color.CYAN);
				g.drawLine(tsp.get(i-1).getLocation().ix(),tsp.get(i-1).getLocation().iy(),
						tsp.get(i).getLocation().ix(),tsp.get(i).getLocation().iy());/*draw in different color all the TSP path*/
			}
		}
	}



	/**
	 * Function that every click from the user - add a new Vertex to this graph and repaint this window.
	 */
	
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		this.ga.graph.addNode(new Vertex(new Point3D(x,y,0)));
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

}