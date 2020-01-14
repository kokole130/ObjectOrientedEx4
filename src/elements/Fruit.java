package elements;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.xml.stream.util.EventReaderDelegate;

import org.w3c.dom.css.RGBColor;

import com.sun.prism.Image;

import dataStructure.edge_data;
import dataStructure.node_data;
import gameClient.MyGameGUI;
import utils.Point3D;

public class Fruit implements fruits {
	public final double EPS=0.0001;
	public Point3D pos;
	public double value;
	public int type;
	BufferedImage img;

	public Fruit(double v,int t,Point3D p) {
		this.value=v;
		this.type=t;
		this.pos=new Point3D(p);
	}

	@Override
	public double getValue() {
		return this.value;
	}

	@Override
	public int getType() {
		return this.type;
	}

	@Override
	public Point3D getLocation() {
		return this.pos;
	}


	public String toString() {
		return "fruit- value:"+this.value+" ,type:"+this.type+" ,pos:"+this.pos;
	}

	public edge_data getEdge(HashMap<Integer,HashMap<Integer,edge_data>> edges,ArrayList<node_data> nodes) {
		for (int i = 0; i < edges.size(); i++) {
			for(edge_data e:edges.get(i).values()) {
				int src=e.getSrc();
				int dest=e.getDest();
				double dist=nodes.get(src).getLocation().distance2D(nodes.get(dest).getLocation());
				double d1=this.pos.distance2D(nodes.get(src).getLocation());
				double d2=this.pos.distance2D(nodes.get(dest).getLocation());

				if(d1+d2==dist||(d1+d2+EPS>dist&&d1+d2-EPS<dist)) {
					if(nodes.get(e.getSrc()).getLocation().y()>nodes.get(e.getDest()).getLocation().y()&&this.type==-1) {	
						return e;
					}
					else if(nodes.get(e.getSrc()).getLocation().y()<nodes.get(e.getDest()).getLocation().y()&&this.type==1) {
						return e;
					}
				}
			}
		}
		return null;
	}

	public void DrawFruit(MyGameGUI game,double minx,double miny,double maxx,double maxy) {
		String banana="banana.png";
		String apple="apple.png";
		File file;
		try {
			if(this.type==-1) {
				img =  ImageIO.read(new File(banana));
			}
			else {
				img =  ImageIO.read(new File(apple));
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		Graphics g = game.getGraphics();

		double xs=game.scale(this.pos.x(), minx, maxx, 100, 950);
		double ys=game.scale(this.pos.y(), miny, maxy, 100, 950);

		g.drawImage(img,(int)xs,(int)ys, null);	

	}
	
	public void setType(int type) {
		this.type=type;
	}
	
	public void setValue(double value) {
		this.value=value;
	}
	
	public void setPos(String pos) {
		this.pos=new Point3D(pos);
	}
}
