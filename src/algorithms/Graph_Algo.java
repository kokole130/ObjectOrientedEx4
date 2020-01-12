package algorithms;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

import dataStructure.DGraph;
import dataStructure.Edge;
import dataStructure.Vertex;
import dataStructure.graph;
import dataStructure.node_data;

/**
 * This empty class represents the set of graph-theory algorithms
 * which should be implemented as part of Ex2 - Do edit this class.
 * @author Ron Azu
 *
 */
public class Graph_Algo implements graph_algorithms{

	public DGraph graph;
	
	/**
	 * default constructor for initializing the graph.
	 */
	public Graph_Algo() {
		this.graph=new DGraph();
	}
	
	public Graph_Algo(graph g) {
		this.init(g);
	}
	
	@Override
	public void init(graph g) {
		this.graph=((DGraph)g).copy();
	}

	@Override
	public void init(String file_name) {	       
		try
		{    
			FileInputStream file = new FileInputStream(file_name); 
			ObjectInputStream in = new ObjectInputStream(file); 

			//converting from read object file to Dgraph into this
			this.graph = (DGraph)in.readObject(); 

			in.close(); 
			file.close(); 

			System.out.println("Object has been deserialized"); 
			System.out.println(this.graph);
		} 

		catch(IOException ex) 
		{ 
			System.out.println("IOException is caught"); 
		} 

		catch(ClassNotFoundException ex) 
		{ 
			System.out.println("ClassNotFoundException is caught"); 
		} 
	}

	@Override
	public void save(String file_name) {          
		try
		{    
			FileOutputStream file = new FileOutputStream(file_name); 
			ObjectOutputStream out = new ObjectOutputStream(file); 

			//converting the DGraph object into 
			out.writeObject(this.graph); 

			out.close(); 
			file.close(); 

			System.out.println("Object has been serialized"); 
		}   
		catch(IOException ex) 
		{ 
			System.out.println("IOException is caught"); 
		} 
	}

	@Override
	public boolean isConnected() {
		//running through all points
		for (int i = 0; i < this.graph.ver.size(); i++) {
			for (int j = 0; j < this.graph.ver.size(); j++) {
				//reseting the the tags for the next check
				tagReset();
				if(this.graph.ver.get(i)!=null&&this.graph.ver.get(j)!=null) {
					if(i!=j) {
						//checking if each point is connected to all other points
						if(!isConnected(i, j,i))return false;
					}
				}
			}
		}
		tagReset();
		return true;
	}

	
	/**
	 * This is a recursive function that checking if two point on the graph is connected
	 * @param src is the source point
	 * @param dest is the destination point
	 * @param originalSrc in every itration i want to save the ancestor source
	 * @return boolean, true if connected and false if they doesn't 
	 */
	public boolean isConnected(int src,int dest,int originalSrc) {
		//if it connnected with only one edge between them
		if(this.graph.edge.get(src).containsValue(new Edge(src, dest, 10))) return true;

		for (int i = 0; i < this.graph.edge.size();i++) {//running through all point the check all the path's
			if(this.graph.edge.get(src).containsKey(i)) {
				if(this.graph.edge.get(src).get(i).getDest()==originalSrc) continue;
				//^if we arrive to the source key we inserted in the first recursion
				if(this.graph.edge.get(src).get(i).getTag()==0) {//if the next node is not visited u can recurse
					this.graph.edge.get(src).get(i).setTag(1);
					if(isConnected(this.graph.edge.get(src).get(i).getDest(), dest,originalSrc))
						return true;//recurse on the next node the dest node
				}
			}
		}
		return false;
		//^if didn't return yet, the current nodes aren't connected
	}

	@Override
	public double shortestPathDist(int src, int dest) {
		double res=shortestPathDistSide(src, dest);//computing the shortest path
		tagReset();//reseting the tags in order to allow another algorithm
		LastKeyReset();//also reseting the last key variable
		return res;
	}

	/**
	 * Dijkastra algorithm - that updating all the weights of each vertex as the shortest path from source vertex,
	 * and after every update, we using the current as the nearest vertex to the source that is not visited.
	 * @param src - Tha starting point of the shortest path.
	 * @param dest - The ending point of the shortest path.
	 * @return return the path weight.
	 */
	private double shortestPathDistSide(int src, int dest) {
		if((src<0&&dest<0)||(dest>=this.graph.ver.size()&&src>=this.graph.ver.size())) {
			System.out.println("there is not such as src or dest");
		}
		if(this.graph.ver.get(src)==null) {
			System.out.println("there is not such as src: "+src);
			return -1;
		}
		if(this.graph.ver.get(dest)==null) {
			System.out.println("there is not such as dest : "+dest);
			return -1;
		}

		//we pass the first node, so we updating his weight and his visited tag
		this.graph.ver.get(src).setWeight(0);
		this.graph.ver.get(src).setTag(1);
		int current=src;
		int i=0;
		while (!allVisited()) {
			if(this.graph.edge.get(current).containsKey(i)) {
				int tmp=this.graph.edge.get(current).get(i).getDest();
				//a specific dest edge in current list

				double verWeight=this.graph.ver.get(current).getWeight();
				//the current weight of current vertex

				double edgeWeight=this.graph.edge.get(current).get(i).getWeight();
				//the weight of the 'current to tmp' edge 

				//updating each vertex weight and mark his minimum last key as the current vertex 
				if(this.graph.ver.get(tmp).getWeight()>verWeight+edgeWeight) {
					this.graph.ver.get(tmp).setWeight(verWeight+edgeWeight);
					this.graph.ver.get(tmp).setLastKey(current);
				}
			}
			if(i==this.graph.edge.size()-1) {
				/* if we arrived to the last vertex that updated, 
				 * we changing the current to the nearest point to the source that not visited.
				 */
				current=minWeightDest(current);
				i=0;
				this.graph.ver.get(current).setTag(1);
				continue;
			}
			i++;

		}
		//returning the dest weight, because its the shortest path
		double res=this.graph.ver.get(dest).getWeight();
		//reseting the weights for the next algorithm
		weightReset();
		return res;
	}

	/**
	 * Side function for 'shortestPathDist' that checking what is nearest point to the source that isn't visited
	 * @param current - is the current point we standing on in the current iteration
	 * @return the key of the nearest point to the source thats not visited
	 */
	private int minWeightDest(int current) {
		int minindex=0;
		double minWeight=Integer.MAX_VALUE;

		for (int i = 0; i < this.graph.ver.size(); i++) {
			if(this.graph.ver.get(i)==null) {
				continue;
			}
			double temp=this.graph.ver.get(i).getWeight();
			//weight of the current edge in 'current' list

			int destTag=this.graph.ver.get(i).getTag();
			//the tag of the current dest vertex we check

			if((temp<=minWeight)&&(destTag!=1)) {
				minWeight=temp;
				minindex=i;
			}

		}
		return minindex;
	}

	@Override
	public List<node_data> shortestPath(int src, int dest) {
		List<node_data>tmp2=shortestPathSide(src, dest);
		tagReset();
		weightReset();
		LastKeyReset();
		return tmp2;
	}
	
	/**
	 * Dijkastra algorithm - that updating all the weights of each vertex as the shortest path from source vertex,
	 * and after every update, we using the current as the nearest vertex to the source that is not visited,
	 * and also running the last keys of the path in order to compute the path.
	 * @param src - Tha starting point of the shortest path.
	 * @param dest - The ending point of the shortest path.
	 * @return return the path.
	 */
	private List<node_data> shortestPathSide(int src, int dest) {
		if(this.graph.ver.get(src)==null||this.graph.ver.get(dest)==null) {
			return null;
		}
		this.shortestPathDistSide(src, dest);//computing the weights and last keys of all vertexes
		LinkedList<node_data> tmp=new LinkedList<node_data>();
		tmp.add(this.graph.ver.get(dest));//starting point from the end - dest
		Vertex v=this.graph.ver.get(dest);
		while(v.getKey()!=src||v.getLastKey()!=-1) {
			v = this.graph.ver.get(this.graph.ver.get(v.getKey()).getLastKey());//updating to the last key
			tmp.add(v);//adding to the list tha last keys
		}
		if(v.getLastKey()==-1&&v.getKey()!=src) return null;
		LinkedList<node_data> tmp2=new LinkedList<node_data>();
		for (int i = tmp.size()-1; i >= 0; i--) {
			//reversing the list the start from the beginning and not from the last
			tmp2.add(tmp.get(i));
		}
		return tmp2;
	}
	
	@Override
	public List<node_data> TSP(List<Integer> targets) {
		double min=Integer.MAX_VALUE;
		LinkedList<node_data> ans=new LinkedList<>();
		for (int i = 0; i < targets.size(); i++) {
			if(min>this.TSPOnedouble(targets, targets.get(i))) {
				min=this.TSPOnedouble(targets, targets.get(i));
				ans=(LinkedList<node_data>) this.TSPOneList(targets, targets.get(i));
			}
		}
		return ans;
	}

	/**
	 * Computing Tsp algorithm from specific index.
	 * @param targets - the vertexes i most to pass on
	 * @param index - starting point of the path
	 * @return list of the TSP algorithm path
	 */
	private List<node_data> TSPOneList(List<Integer> targets,int index) {
		int currentIndex = index;//the variable that represent the vertexes we pass on
		List<node_data> ans=new LinkedList<>();
		int visited[]=new int[this.graph.ver.size()];
		while(!allTargetsVisited(targets,visited)) {//if we visit all the targets in the target list
			int minIndex=currentIndex;
			double min=Integer.MAX_VALUE;
			for (int i = 0; i < targets.size(); i++) {
				if(targets.get(i)!=currentIndex&&visited[targets.get(i)]!=1) {//finding the next closest vertex that not visited
					double tmp = this.shortestPathDist(currentIndex, targets.get(i));
					if(min>tmp) {//updating the minimum path from current
						min=tmp;
						minIndex=targets.get(i);
					}
				}
			}
			int removeindex = ans.size();
			if(currentIndex!=minIndex) {//if we got to the end so dont add vertex to the list
				ans.addAll(this.shortestPath(currentIndex, minIndex));
				if(ans.size()!=this.shortestPath(currentIndex, minIndex).size()) {
					//in order not create duplicating vertexes on ans list
					ans.remove(removeindex);

				}
			}
			visited[currentIndex]=1;
			currentIndex=minIndex;//updating the minimum vertex index to current
		}
		return ans;
	}

	private double TSPOnedouble(List<Integer> targets,int index) {
		int currentIndex = index;//the variable that represent the vertexes we pass on
		double ans=0;
		int visited[]=new int[this.graph.ver.size()];
		while(!allTargetsVisited(targets,visited)) {//if we visit all the targets in the target list
			int minIndex=currentIndex;
			double min=Integer.MAX_VALUE;
			for (int i = 0; i < targets.size(); i++) {
				if(targets.get(i)!=currentIndex&&visited[targets.get(i)]!=1) {//finding the next closest vertex that not visited
					double tmp = this.shortestPathDist(currentIndex, targets.get(i));
					if(min>tmp) {//updating the minimum path from current
						min=tmp;
						minIndex=targets.get(i);
					}
				}
			}
		
			ans+=this.shortestPathDist(currentIndex, minIndex);//updating the ans that returns as the shortest path
			visited[currentIndex]=1;
			currentIndex=minIndex;
		}
		return ans;
	}

	@Override
	public graph copy() {
		DGraph temp=new DGraph();
		temp=this.graph.copy();
		return temp;
	}
	
//------------------SIDE FUNCTIONS-----------------------------------------
	/**
	 * Function that passing through all vertexes and reseting the 'lastkey' variable
	 * This function is important after any algorithm run, 
	 * in order to let the next algorithm to run well fine.
	 */
	private void LastKeyReset() {
		for (int i = 0; i < this.graph.ver.size(); i++) {
			if(this.graph.ver.get(i)!=null) {
				this.graph.ver.get(i).setLastKey(-1);
			}
		}
	}

	/**
	 * Function that checking if all the point in this graph were visited
	 * @return boolean - true if all visited, false if not all visited
	 */
	private boolean allVisited() {
		for (int i = 0; i < this.graph.ver.size(); i++) {
			if(this.graph.ver.get(i)!=null&&this.graph.ver.get(i).getTag()==0)return false;
		}
		return true;
	}

	/**
	 * Function that reset all the weight of the vertexes to infinite(Integer.MAX_VALUE).
	 * This function is important after any algorithm run, 
	 * in order to let the next algorithm to run well fine. 
	 */
	private void weightReset() {
		for (int i = 0; i < this.graph.ver.size(); i++) {
			if(this.graph.ver.get(i)!=null) {
				this.graph.ver.get(i).setWeight(Integer.MAX_VALUE);
			}
		}
	}

	/**
	 * Function that reset all the tags in the vertex and Edges in the graph to 0(Not visited).
	 * This function is important after any algorithm run, 
	 * in order to let the next algorithm to run well fine.
	 */
	public void tagReset() {
		for (int i = 0; i < this.graph.ver.size(); i++) {
			if(this.graph.ver.get(i)!=null) {
				this.graph.ver.get(i).setTag(0);//reseting all vertexes
				for (int j = 0; j < this.graph.edge.size(); j++) {
					if(this.graph.edge.get(i)!=null&&this.graph.edge.get(i).containsKey(j)) {
						this.graph.edge.get(i).get(j).setTag(0);//reseting all edges
					}
				}
			}
		}
	}

	/**
	 * function that checking if all the targets vertex is visited
	 * @param targets - the list that we checking on
	 * @param visited - array of the visited tag
	 * @return
	 */
	private boolean allTargetsVisited(List<Integer> targets, int []visited) {
		for (int j = 0; j < targets.size(); j++) {
			for (int i = 0; i < this.graph.ver.size(); i++) {
				if(i==targets.get(j)) {
					if(visited[i]==0) {
						return false;
					}
					break;
				}
			}
		}
		return true;
	}
}