package dataStructure;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import utils.Point3D;


public class DGraph implements graph,Serializable{
	public int keyNum;
	public ArrayList<Vertex> ver;
	public HashMap<Integer,HashMap<Integer,Edge>> edge;
	private int edgesSize;
	private int nodesSize;
	public int mc;

	/**
	 * a constructor that initializing new DGraph and set default variables.
	 */

	public DGraph() {
		this.ver=new ArrayList<>();
		this.edge=new HashMap<Integer,HashMap<Integer,Edge>>();
		this.keyNum=0;
		this.mc=0;
		this.edgesSize=0;
	}

	/**
	 * return the node_data by the node_id,
	 * @param key - the node_id
	 * @return the node_data by the node_id, null if none.
	 */


	@Override
	public node_data getNode(int key) {
		if(key<this.ver.size()) {
			if(key==this.ver.get(key).key) {
				return this.ver.get(key);
			}
			else return null;
		}
		else {
			return null;
		}
	}

	/**
	 * return the data of the edge (src,dest), null if none.
	 * Note: this method should run in O(1) time.
	 * @param src
	 * @param dest
	 * @return edge with source - src and destination - dest.
	 */

	@Override
	public edge_data getEdge(int src, int dest) {
		if(src<this.edge.size()&&dest<this.edge.size()) {
			if(this.ver.get(src)!=null) {
				if(this.edge.get(src).containsKey(dest)) {
					return this.edge.get(src).get(dest);
				}
			}
		}
		return null;
	}

	/**
	 * add a new node to the graph with the given node_data.
	 * Note: this method should run in O(1) time.
	 * @param n - node to add
	 */

	@Override
	public void addNode(node_data n) {
		if(n!=null) {
			Vertex tmp=(Vertex)n;
			tmp.key=keyNum;
			this.ver.add(tmp);
			this.edge.put(keyNum,new HashMap<Integer,Edge>());
			nodesSize++;
			keyNum++;
			this.mc++;
		}
	}

	/**
	 * Connect an edge with weight w between node src to node dest in O(1) running time.
	 * @param src - the source of the edge.
	 * @param dest - the destination of the edge.
	 * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
	 */

	@Override
	public void connect(int src, int dest, double w) {
		if((src<0&&dest<0)||(dest>=this.nodeSize()&&src>=this.nodeSize())) {
			System.out.println("there is not such as src or dest");
			return ;
		}
		if(this.ver.get(src)==null) {
			System.out.println("there is not such as src: "+src);
			return ;
		}
		if(this.ver.get(dest)==null) {
			System.out.println("there is not such as dest : "+dest);
			return ;
		}
		if(src==dest) {
			System.out.println("you can't connect the same vertex");
			return;
		}
		if(src<this.edge.size()&&dest<this.edge.size()) {
			this.edge.get(src).put(dest, new Edge(src,dest,w));
			this.edgesSize++;
			this.mc++;
		}
	}

	/**
	 * This method return a pointer (shallow copy) for the
	 * collection representing all the nodes in the graph in O(1) running time. 
	 * @return Collection<node_data>
	 */

	@Override
	public Collection<node_data> getV() {
		if(this.nodeSize()>0) {
			ArrayList<Vertex> vertmp=new ArrayList<>(this.ver);
			vertmp.removeAll(Collections.singleton(null));
			return (Collection)vertmp;
		}
		else {
			return (Collection)new ArrayList<Vertex>();
		}
	}

	/**
	 * This method return a pointer (shallow copy) for the
	 * collection representing all the edges getting out of 
	 * the given node (all the edges starting (source) at the given node) in  O(1) running time. 
	 * @return Collection<edge_data>
	 */

	@Override
	public Collection<edge_data> getE(int node_id) {
		if(node_id<this.ver.size()) {
			if(this.edge.get(node_id)!=null) {
				return new LinkedList<edge_data>(this.edge.get(node_id).values());
			}
		}
		return null;
	}

	/**
	 * Delete the node (with the given ID) from the graph -
	 * and removes all edges which starts or ends at this node in O(n) running time,
	 * |V|=n, as all the edges should be removed.
	 * @return the data of the removed node (null if none). 
	 * @param key
	 */

	@Override
	public node_data removeNode(int key) {
		if(key>=this.ver.size()||key<0) {
			System.out.println("Invalid vertex key");
			return null;
		}
		Vertex temp=new Vertex(this.ver.get(key).getKey(),this.ver.get(key));
		this.ver.set(key, null);
		nodesSize--;
		updateEdge(key);
		this.mc++;
		return temp;
	}

	/**
	 * Side function that removes all the edges associated with key - source and destination.
	 * @param key - the vertex that removed.
	 */
	private void updateEdge(int key) {
		if(key<this.edgeSize()) {
			for (int i = 0; i < this.edge.size(); i++) {
				if(this.edge.get(i)!=null) {
					if(i!=key) {
						if(this.edge.get(i).containsKey(key)) {
							this.edge.get(i).remove(key);
							edgesSize--;
						}
					}
					else if(i==key) {
						edgesSize=edgesSize-this.edge.get(i).size();
						this.edge.replace(i, null);
					}
				}

			}
		}
	}

	/**
	 * Delete the edge from the graph,in O(1) running time.
	 * @param src - key source
	 * @param dest - key destination
	 * @return the data of the removed edge (null if none).
	 */

	@Override
	public edge_data removeEdge(int src, int dest) {
		if(src<this.edge.size()&&dest<this.edge.size()) {
			if(this.ver.get(src)!=null) {
				if(this.edge.get(src).containsKey(dest)) {
					Edge temp2=new Edge(this.edge.get(src).get(dest));
					this.edge.get(src).remove(dest);
					edgesSize--;
					this.mc++;
					return temp2;
				}
			}
		}
		return null;
	}

	/**
	 * Side function that returns all the details of this graph as a string.
	 */
	public String toString() {
		System.out.println("Vertex List:");
		for (int i = 0; i < this.nodeSize(); i++) {
			if(this.ver.get(i)!=null) {
				System.out.println(this.ver.get(i).getInfo());
			}
		}
		System.out.println();
		System.out.println("Edges");
		for (int i = 0; i < this.edge.size(); i++) {
			System.out.print("list number "+i+":");
			System.out.println();
			System.out.println();
			for (int j = 0;this.edge.get(i)!=null&&j<this.edge.size(); j++) {
				if(this.edge.get(i).containsKey(j)) {
					System.out.println(this.edge.get(i).get(j).getInfo()+" ,");
					System.out.println();
				}
			}
		}
		return"";
	}

	/**
	 * Function that do deep copy to this graph
	 * @return the graph that we copy
	 */
	public DGraph copy() {
		DGraph tmp = new DGraph();
		tmp.keyNum=this.keyNum;

		for (int i = 0; i < this.ver.size(); i++) {
			if(this.ver.get(i)!=null) {
				tmp.ver.add(this.ver.get(i).copy());
				tmp.edge.put(i,new HashMap<Integer,Edge>());
			}
			else {
				tmp.ver.add(null);
				tmp.edge.put(i,null);

			}
		}

		for (int i = 0; i < this.edge.size(); i++) {
			for (int j = 0; j < this.edge.size(); j++) {
				if(this.ver.get(i)!=null) {
					if(this.edge.get(i).containsKey(j)) {
						tmp.edge.get(i).put(this.edge.get(i).get(j).getDest(),this.edge.get(i).get(j).copy());
					}
				}
			}
		}

		tmp.edgesSize=this.edgesSize;
		tmp.nodesSize=this.nodesSize;
		tmp.mc=this.mc;

		return tmp;
	}

	@Override

	/** return the number of vertices (nodes) in the graph in O(1) running time.
	 */

	public int nodeSize() {
		return this.nodesSize;
	}

	/** 
	 * return the number of edges (assume directional graph) in O(1) running time.
	 */

	@Override
	public int edgeSize() {
		return this.edgesSize;
	}

	/**
	 * return the Mode Count - for testing changes in the graph.
	 */

	@Override
	public int getMC() {
		return this.mc;
	}

	/**
	 * return the current key number.
	 * @return specific integer key.
	 */

	public int getkeyNum() {
		return this.keyNum;
	}

	
    public void init(String jsonStr) {
        try {
            this.init();
            JSONObject graph = new JSONObject(jsonStr);
            JSONArray nodes = graph.getJSONArray("Nodes");
            JSONArray edges = graph.getJSONArray("Edges");
            
            
            int node_id,src,dest;
            double w;
            String pos;
            for (int i = 0; i < nodes.length(); ++i) {
                node_id = nodes.getJSONObject(i).getInt("id");
                pos = nodes.getJSONObject(i).getString("pos");
                this.addNode(new Vertex(node_id, new Point3D(pos)));
            }
            for (int i = 0; i < edges.length(); ++i) {
                src = edges.getJSONObject(i).getInt("src");
                dest = edges.getJSONObject(i).getInt("dest");
                w = edges.getJSONObject(i).getDouble("w");
                this.connect(src, dest, w);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	private void init() {
		this.ver = new ArrayList<Vertex>();
		this.edge = new HashMap<Integer, HashMap<Integer, Edge>>();
	}

}
