package elements;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sun.prism.Image;

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
	
	
	
	public Robot(int id,int src,int dest,double value,double speed,Point3D pos) {
		this.id=id;
		this.src=src;
		this.dest=dest;
		this.value=value;
		this.pos=new Point3D(pos);
		this.speed=speed;
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

	public void DrawRobot(MyGameGUI game) {
		String path="robot.png";
		File file=new File(path);
		try {
			img=ImageIO.read(file);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		game.getGraphics().drawImage(img, (int)this.pos.x(),(int)this.pos.y(),null );

	}

}
