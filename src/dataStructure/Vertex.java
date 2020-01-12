package dataStructure;


import java.io.Serializable;

import utils.Point3D;

public class Vertex implements node_data,Serializable {
	double weight;
	int key;
	Point3D p;
	int tag;
	int lastKey;

	/**
	 * constructor that initializing a vertex with a graph location
	 * @param p location of this vertex
	 */
	public Vertex(Point3D p) {
		this.p=new Point3D(p);
		this.tag=0;
		this.weight=Integer.MAX_VALUE;
		this.lastKey=-1;
	}
	
	public Vertex(int key,Point3D p) {
		this.key=key;
		this.p=new Point3D(p);
		this.tag=0;
		this.weight=Integer.MAX_VALUE;
		this.lastKey=-1;
	}
	
	/**
	 * copy constructor that initializing a vertex with a graph location and with an unique key
	 * @param key - a key integer we want to copy
	 * @param other - vertex object we want to copy
	 */
	public Vertex(int key,Vertex other) {
		this.key=key;
		this.p=new Point3D(other.p);
		this.weight=Integer.MAX_VALUE;
		this.tag=0;
		this.lastKey=-1;
	}

	/**
	 * Return the key (id) associated with this node.
	 * @return the unique key of this vertex
	 */
	
	@Override
	public int getKey() {
		return this.key;
	}

	/** Return the location (of applicable) of this node, if
	 * none return null.
	 * @return the specific location of this vertex
	 */
	
	@Override
	public Point3D getLocation() {
		return this.p;
	}

	/** Allows changing this node's location.
	 * 
	 * @param p - new new location  (position) of this node.
	 */
	
	@Override
	public void setLocation(Point3D p) {
		Point3D tmp=new Point3D(p);
		this.p=tmp;
	}

	/**
	 * Return the weight associated with this node.
	 * @return the current weight of this vertex.
	 */
	
	@Override
	public double getWeight() {
		return this.weight;
	}

	/**
	 * Allows changing this node's weight.
	 * @param w - the new weight
	 */
	
	@Override
	public void setWeight(double w) {
		this.weight=w;
	}

	/**
	 * return the remark (meta data) associated with this node.
	 * @return this vertex details as string.
	 */
	
	@Override
	public String getInfo() {
		return "Vertex:"+key+"\nWeight:"+weight+"\nPoint:"+p;
	}

	/**
	 * Returns the last key associated with this node.
	 * @return unique vertex key
	 */
	
	public int getLastKey() {
		return this.lastKey;
	}
	
	/**
	 * Allows changing this node's last key.
	 * @param key - the new last key of this vertex.
	 */

	public void setLastKey(int key) {
		this.lastKey=key;
	}
	
	/**
	 * Allows changing the remark (meta data) associated with this node.
	 * @param s - all the details of this vertex to initial.
	 */
	
	@Override
	public void setInfo(String s) {
		for (int i = 0; i < s.length(); i++) {
			if(s.substring(i,i+7).equals("Vertex:")) {
				i=i+7;
				for (int j = i; j < s.length(); j++) {
					if(s.charAt(j)=='\n') {
						this.key=Integer.parseInt(s.substring(i, j));
						break;
					}
				}
			}
			if(s.substring(i,i+7).equals("Weight:")) {
				i=i+7;
				for (int j = i; j < s.length(); j++) {
					if(s.charAt(j)=='\n') {
						this.weight=Double.parseDouble(s.substring(i, j));
						break;
					}
				}
			}
			if(s.substring(i,i+6).equals("Point:")) {
				i=i+6;
				this.p=new Point3D(s.substring(i));
				break;
			}
		}
	}

	/**
	 * Temporal data (aka color: e,g, white, gray, black) 
	 * which can be used be algorithms 
	 * @return integer that shows if the algorithm pass of this node or not.
	 */
	
	@Override
	public int getTag() {
		return this.tag;
	}
	
	/** 
	 * Allow setting the "tag" value for temporal marking an node - common 
	 * practice for marking by algorithms.
	 * @param t - the new value of the tag
	 */
	
	@Override
	public void setTag(int t) {
		this.tag=t;
	}
	
	/**
	 * Side function that returns all details of this node as a string.
	 */
	
	public String toString() {
		return "Vertex:"+key+" Weight:"+weight+" Point:"+p+"\n";
	}
	
	/**
	 * Side function that copy this vertex to another.
	 * @return new vertex
	 */
	
	public Vertex copy() {
		Vertex tmp = new Vertex(this.key,this);
		return tmp;
	}
	
	/**
	 * Side function that compares this node with another.
	 * @return a boolean
	 */
	
	public boolean equals(Object ot) {
		if(ot instanceof Vertex) {
			ot=(Vertex)ot;		
			if(this.getKey()==((Vertex) ot).getKey()) {
				return true;
			}
		}
		return false;
	}

}
