package dataStructure;

import java.io.Serializable;

public class Edge implements edge_data,Serializable{
	int src,dest;
	double weight;
	int tag;
	
	/**
	 * constructor that initializing a new edge with source key,destination key and weight.
	 * @param s - source key
	 * @param d - destination key
	 * @param w - weight of this edge
	 */
	
	public Edge(int s,int d,double w) {
		this.src=s;
		this.dest=d;
		this.weight=w;
		this.tag=0;
	}
	
	
	/**
	 * copy constructor that initializing a new edge with the same values of 'other' Edge.
	 * @param other - Edge to copy
	 */
	public Edge(Edge other) {
		this.dest=other.dest;
		this.src=other.src;
		this.weight=other.weight;
		this.tag=0;
	}
	
	/**
	 * The id of the source node of this edge.
	 * @return integer source key
	 */
	
	@Override
	public int getSrc() {
		return this.src;
	}
	
	/**
	 * The id of the destination node of this edge
	 * @return integer destination key
	 */
	
	@Override
	public int getDest() {
		return this.dest;
	}
	
	/**
	 * @return the weight of this edge (positive value).
	 */
	
	@Override
	public double getWeight() {
		return this.weight;
	}
	
	/**
	 * return the remark (meta data) associated with this edge.
	 * @return all details of this edge as string.
	 */
	
	@Override
	public String getInfo() {
		return "Source:"+this.src+"\nDestination:"+this.dest+"\nWeight:"+this.weight;
	}
	
	/**
	 * Allows changing the remark (meta data) associated with this edge.
	 * @param s - a string of edge files that want to set.
	 */
	
	@Override
	public void setInfo(String s) {
		for (int i = 0; i < s.length(); i++) {
			if(i+7<s.length()&&s.substring(i,i+7).equals("Source:")) {
				i=i+7;
				for (int j = i; j < s.length(); j++) {
					if(s.charAt(j)=='\n') {
						this.src=Integer.parseInt(s.substring(i, j));
						break;
					}
				}
			}
			if(i+12<s.length()&&s.substring(i,i+12).equals("Destination:")) {
				i=i+12;
				for (int j = i; j < s.length(); j++) {
					if(s.charAt(j)=='\n') {
						this.dest=Integer.parseInt(s.substring(i, j));
						break;
					}
				}
			}
			if(i+7<s.length()&&s.substring(i,i+7).equals("Weight:")) {
				i=i+7;
				this.weight=Double.parseDouble(s.substring(i));
				break;
			}
		}
	}
	
	/**
	 * Temporal data (aka color: e,g, white, gray, black) 
	 * which can be used be algorithms 
	 * @return integer that shows if we passed on this edge or not.
	 */
	
	@Override
	public int getTag() {
		return this.tag;
	}
	
	/** 
	 * Allow setting the "tag" value for temporal marking an edge - common 
	 * practice for marking by algorithms.
	 * @param t - the new value of the tag
	 */
	
	@Override
	public void setTag(int t) {
		this.tag=t;
	}
	
	/**
	 * Side function that compares this edge with another edge.
	 * @return a boolean
	 */
	
	public boolean equals(Object o) {
		if(o!=null) {
			Edge t=(Edge)o;
			if(this.src==t.src&&this.dest==t.dest)return true;
			return false;
		}
		return false;
	}
	
	/**
	 * Side function that returns all the details of this edge as a string.
	 */
	
	public String toString() {
		return "Source:"+this.src+" Destination:"+this.dest+" Weight:"+this.weight+"\n";
	}
	
	/**
	 * Side function that copy this edge to another.
	 * @return new edge with the same values of this edge.
	 */
	
	public Edge copy() {
		Edge tmp = new Edge(this);
		return tmp;
	}

}
