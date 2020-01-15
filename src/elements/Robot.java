package elements;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


import gameClient.MyGameGUI;
import utils.Point3D;

public class Robot implements robots{

	int id;
	int src;
	int dest;
	double value;
	double speed;
	Point3D pos;
	BufferedImage img;
	public boolean clicked;
	
	
	
	public Robot(int id,int src,int dest,double value,double speed,Point3D pos) {
		this.id=id;
		this.src=src;
		this.dest=dest;
		this.value=value;
		this.pos=new Point3D(pos);
		this.speed=speed;
		this.clicked=false;
	}

	@Override
	public int getID() {
		return this.id;
	}

	@Override
	public int getSrc() {
		return this.src;
	}

	@Override
	public int getDest() {
		return this.dest;
	}

	@Override
	public Point3D getLocation() {
		return this.pos;
	}

	public double getSpeed() {
		return this.speed;
	}

	public double getValue() {
		return this.value;
	}

	public String toString() {
		return "robot- id:"+this.id+" ,value:"+this.value+ " ,src:"+this.src+
				" ,dest:"+this.dest+" ,speed:"+this.speed+" ,pos:"+this.pos;
	}

	public void DrawRobot(MyGameGUI game,double minx,double miny,double maxx,double maxy) {
		String path="robot.png";
		File file=new File(path);
		try {
			img=ImageIO.read(file);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		Graphics g = game.getGraphics();
		
		double xs=game.scale(this.pos.x(), minx, maxx, 100, 950);
		double ys=game.scale(this.pos.y(), miny, maxy, 100, 950);
		g.drawImage(img,(int)xs,(int)ys, null);


	}
	
	public void setClicked(boolean p) {
		this.clicked=p;
	}
	
	public boolean getClicked() {
		return this.clicked;
	}
	
	public void chooseNextNode(int dest) {
		this.dest=dest;
	}
	
	public void setSrc(int src) {
		this.src=src;
	}
	
	public void setPos(double x, double y) {
		this.pos.setX(x);
		this.pos.setY(y);
	}

	public void setValue(double value) {
		this.value=value;	
	}
	
}
